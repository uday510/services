package com.app.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.authorizeExchange(exchanges -> exchanges.pathMatchers("/actuator/**").permitAll()
        .pathMatchers(HttpMethod.GET, "/app/**").permitAll()
        .pathMatchers("/app/accounts-service/**").hasRole("ACCOUNTS")
        .pathMatchers("/app/cards-service/**").hasRole("CARDS")
        .pathMatchers("/app/loans-service/**").hasRole("LOANS").anyExchange().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));

    http.csrf(ServerHttpSecurity.CsrfSpec::disable);

    return http.build();
  }

  private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
    return new ReactiveJwtAuthenticationConverterAdapter(converter);
  }
}
