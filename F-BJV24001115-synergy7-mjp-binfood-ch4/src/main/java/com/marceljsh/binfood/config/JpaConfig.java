package com.marceljsh.binfood.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * using @EntityListeners and AuditingEntityListener
 * for @CreatedDate and @LastModifiedDate to be automatically populated
 * by @EnableJpaAuditing
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
