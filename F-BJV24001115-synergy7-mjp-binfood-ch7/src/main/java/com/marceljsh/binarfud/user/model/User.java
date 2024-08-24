package com.marceljsh.binarfud.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marceljsh.binarfud.common.model.Auditable;
import com.marceljsh.binarfud.security.model.Role;
import com.marceljsh.binarfud.order.model.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_users")
public class User extends Auditable implements UserDetails {

  @Column(
    unique = true,
    nullable = false,
    length = 32
  )
  private String username;

  @Column(
    unique = true,
    nullable = false,
    updatable = false
  )
  private String email;

  @JsonProperty(
    access = JsonProperty.Access.WRITE_ONLY
  )
  @Column(length = 60)
  private String password;

  @ManyToMany(
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL
  )
  @JoinTable(
    name = "tbl_user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles;

  @OneToMany(
    mappedBy = "owner",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL
  )
  private List<Order> orders;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    User user = (User) o;
    return Objects.equals(username, user.username) ||
        Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, email);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toSet());
  }

}
