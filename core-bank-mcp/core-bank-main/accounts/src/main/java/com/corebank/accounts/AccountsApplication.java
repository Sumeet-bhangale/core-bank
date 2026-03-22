package com.corebank.accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@OpenAPIDefinition(
        info = @Info(title = "Accounts Microservice REST API Documentation",
                version = "v1",
                description = "Core Bank Accounts Microservice REST API Documentation",
                contact = @Contact(
                        name = "Core Bank", email = "bZ2e5@example.com", url = "https://www.corebank.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.corebank.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Core Bank Documentation",
                url = "https://www.corebank.com"
        )
)
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

}
