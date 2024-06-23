package com.marceljsh.binarfud.common;

import com.marceljsh.binarfud.security.Role;
import com.marceljsh.binarfud.security.RoleRepository;
import com.marceljsh.binarfud.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private RoleRepository roleRepo;

  @Override
  public void run(String ...args) {
    List<Role> roles = Arrays.asList(
      Role.builder().name(Roles.ROLE_USER.toString()).build(),
      Role.builder().name(Roles.ROLE_MERCHANT.toString()).build()
    );

    roles.forEach(role -> {
      if (!roleRepo.existsByName(role.getName())) {
        roleRepo.save(role);
      }
    });
  }

}
