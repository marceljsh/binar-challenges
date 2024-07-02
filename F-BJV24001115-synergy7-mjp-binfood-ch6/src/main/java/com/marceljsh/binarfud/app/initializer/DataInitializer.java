package com.marceljsh.binarfud.app.initializer;

import com.marceljsh.binarfud.auth.service.AuthService;
import com.marceljsh.binarfud.security.model.Role;
import com.marceljsh.binarfud.security.repository.RoleRepository;
import com.marceljsh.binarfud.security.model.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final RoleRepository roleRepo;

  private final AuthService authService;

  @Override
  public void run(String... args) {
    for (Roles role : Roles.values())
      if (!roleRepo.existsByName(role.toString()))
        roleRepo.save(Role.builder()
            .name(role.toString())
            .build());

    authService.addGod();
  }

}
