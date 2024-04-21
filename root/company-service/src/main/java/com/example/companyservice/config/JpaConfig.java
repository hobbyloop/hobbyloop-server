package com.example.companyservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.persistence.Id;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.companyservice")
@EnableJpaAuditing
public class JpaConfig {
}
