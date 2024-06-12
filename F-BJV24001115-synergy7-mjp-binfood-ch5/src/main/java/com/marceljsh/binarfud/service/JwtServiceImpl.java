package com.marceljsh.binarfud.service;

import com.marceljsh.binarfud.service.spec.BlacklistService;
import com.marceljsh.binarfud.service.spec.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtServiceImpl implements JwtService {

  private final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

  private final String secretKey;

  private final BlacklistService blacklistService;

  @Autowired
  public JwtServiceImpl(@Value("${jwt.secret}") String secretKey, BlacklistService blacklistService) {
    this.secretKey = secretKey;
    this.blacklistService = blacklistService;
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);

    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(Map.of(), userDetails);
  }

  private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
      .setClaims(extraClaims)
      .setSubject(userDetails.getUsername())
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
      .signWith(getSignInKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      final String username = extractUsername(token);
      boolean isTokenExpired = isTokenExpired(token);
      boolean isUsernameValid = username.equals(userDetails.getUsername());

      if (isTokenExpired) {
        log.error("token is expired");
        blacklistService.addToBlacklist(token);
        return false;
      }

      if (!isUsernameValid) {
        log.error("username is not valid");
        return false;
      }

      return true;

    } catch (MalformedJwtException e) {
      log.error("invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty");
    }

    return false;
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSignInKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);

    return Keys.hmacShaKeyFor(keyBytes);
  }
}
