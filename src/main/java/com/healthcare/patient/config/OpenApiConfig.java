
package com.healthcare.patient.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI patientServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Healthcare Patient Service API")
                        .description(
                                "REST APIs for patient registration, " +
                                        "profile management, addresses, " +
                                        "emergency contacts and patient search."
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Healthcare Development Team")
                                .email("support@healthcare.com")
                        )
                );
    }
}