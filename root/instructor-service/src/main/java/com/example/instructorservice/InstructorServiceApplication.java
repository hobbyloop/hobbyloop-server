package com.example.instructorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"com.example.instructorservice"})
public class InstructorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstructorServiceApplication.class, args);
    }
}
