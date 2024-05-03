package com.marceljsh.binfood.config;

import com.marceljsh.binfood.resolver.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class JwtResolverConfig implements WebMvcConfigurer {

  private final UserArgumentResolver userArgumentResolver;

  @Autowired
  public JwtResolverConfig(UserArgumentResolver userArgumentResolver) {
    this.userArgumentResolver = userArgumentResolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    resolvers.add(userArgumentResolver);
  }
}
