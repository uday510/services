package com.app.accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
/*@ComponentScans({ @ComponentScan("com.app.accounts.controller") })
@EnableJpaRepositories("com.app.accounts.repository")
@EntityScan("com.app.accounts.model")*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
        info = @Info(
                title = "Accounts microservice REST API Documentation",
                description = "Accounts microservice REST API Description",
                version = "v1",
                contact = @Contact(

                        name = "Uday Teja",
                        email = "dev@udayteja.com",
                        url = "https://udayteja.com"
                ),
                license = @License(
                        name = "Apache2.0",
                        url = "https://udayteja.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Accounts microservice REST API Documentation",
                url = "https://udayteja.com"
        )
)
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

}
