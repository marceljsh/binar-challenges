package com.marceljsh.binfood.resolver;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

  UserRepo userRepo;

  @Autowired
  UserArgumentResolver(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return User.class.equals(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();

    String rawToken = servletRequest.getHeader("Authorization");
    if (rawToken == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.name());
    }

    String token = rawToken.replace("Bearer ", "");
    if (token.isEmpty()) {
      return null;
    }

    User user = userRepo.findFirstByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.name()));
    if (user.getTokenExpiredAt() < System.currentTimeMillis()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.name());
    }

    return user;
  }
}
