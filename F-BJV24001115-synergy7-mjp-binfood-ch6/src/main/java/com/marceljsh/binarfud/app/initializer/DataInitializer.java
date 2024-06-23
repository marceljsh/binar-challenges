package com.marceljsh.binarfud.app.initializer;

import com.marceljsh.binarfud.security.model.Role;
import com.marceljsh.binarfud.security.repository.RoleRepository;
import com.marceljsh.binarfud.security.model.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private RoleRepository roleRepo;

  @Override
  public void run(String... args) {
    for (Roles role : Roles.values())
      if (!roleRepo.existsByName(role.toString()))
        roleRepo.save(Role.builder()
            .name(role.toString())
            .build());
  }

}
