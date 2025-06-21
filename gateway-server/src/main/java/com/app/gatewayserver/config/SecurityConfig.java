package com.app.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
    serverHttpSecurity
        .authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET, "/app/**")
            .permitAll().pathMatchers("/app/accounts-service/**").hasRole("ACCOUNTS")
            .pathMatchers("/app/cards-service/**").hasRole("CARDS")
            .pathMatchers("/app/loans-service/**").hasRole("LOANS").anyExchange().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

    serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);

    return serverHttpSecurity.build();
  }

}
