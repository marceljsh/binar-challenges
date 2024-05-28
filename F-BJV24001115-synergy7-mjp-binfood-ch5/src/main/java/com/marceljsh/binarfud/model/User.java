package com.marceljsh.binarfud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_users")
public class User extends AuditableBase {

  @Column(unique = true, nullable = false, length = 32)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;

  @Column(name = "access_token", length = 128)
  private String token;

  private Long tokenExpiredAt;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    User user = (User) o;
    return Objects.equals(username, user.username) || Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, email);
  }
}
