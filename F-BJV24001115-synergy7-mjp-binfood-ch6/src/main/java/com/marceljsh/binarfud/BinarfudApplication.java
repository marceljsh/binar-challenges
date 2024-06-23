package com.marceljsh.binarfud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BinarfudApplication {

  public static void main(String[] args) {
    SpringApplication.run(BinarfudApplication.class, args);
  }

}
