package com.marceljsh.binarfud.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

  @Value("${openapi.dev-url}")
  private String devUrl;

  @Value("${openapi.prod-url}")
  private String prodUrl;

  @Bean
  GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("public-apis")
        .pathsToMatch("/**")
        .build();
  }

  @Bean
  OpenAPI customOpenApi() {
    Contact contact = new Contact()
        .email("god@binarfud.com")
        .name("Binarfud")
        .url("https://binarfud.com");

    License license = new License()
        .name("MIT License")
        .url("https://choosealicense.com/licenses/mit/");

    Server dev = new Server()
        .url(devUrl)
        .description("Development server");

    Server prod = new Server()
        .url(prodUrl)
        .description("Production server");

    Info info = new Info()
        .title("Binarfud")
        .version("1.0")
        .contact(contact)
        .description("API for Binarfud ordering system")
        .termsOfService("https://www.binarfud.com/terms")
        .license(license);

    SecurityRequirement securityRequirement = new SecurityRequirement()
        .addList("bearerAuth");

    Components components = new Components()
        .addSecuritySchemes("bearerAuth", new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT"));

    return new OpenAPI()
        .info(info)
        .servers(List.of(dev, prod))
        .addSecurityItem(securityRequirement)
        .components(components);
  }

}
