package com.example.ticketservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api(@Value("${openapi.service.url}") String url) {
        return new OpenAPI()
                .servers(List.of(new Server().url(url)))
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("TICKET-SERVICE API DOCS")
                .description("ticket-service rest api")
                .version("1.0.0");
    }
}