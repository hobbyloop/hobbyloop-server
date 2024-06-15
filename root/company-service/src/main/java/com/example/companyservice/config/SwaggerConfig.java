package com.example.companyservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    //도메인으로 그룹화
    @Bean
    public GroupedOpenApi companyApi() {
        return GroupedOpenApi.builder()
                .group("company-service")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("어드민 페이지")
                .pathsToMatch("/api/v1/admin/**")
                .build();
    }

    private Info apiInfo() {
        return new Info()
                .title("COMPANY-SERVICE API DOCS")
                .description("company-service rest api")
                .version("1.0.0");
    }
}
