package com.theplutushome.optimus.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OPTIMUS API")
                        .description("API information for The Plutus Home.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Malick")
                                .url("www.theplutushome.com")
                                .email("malick@theplutushome.com"))
                        .license(new License()
                                .name("License of API")
                                .url("API license URL")))
                .addSecurityItem(new SecurityRequirement().addList("X-API-KEY"))
                .components(new Components()
                        .addSecuritySchemes("X-API-KEY",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .name("X-API-KEY")
                                        .in(SecurityScheme.In.HEADER)));
    }
}
