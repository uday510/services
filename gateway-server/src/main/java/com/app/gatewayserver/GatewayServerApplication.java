package com.app.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()

			.route("accounts-service", r -> r.path("/app/accounts-service/**")
				.filters(f -> f.rewritePath("/app/accounts-service/(?<segment>.*)", "/${segment}"))
				.uri("lb://ACCOUNTS"))

			.route("cards-service", r -> r.path("/app/cards-service/**")
				.filters(f -> f.rewritePath("/app/cards-service/(?<segment>.*)", "/${segment}"))
				.uri("lb://CARDS"))

			.route("loans-service", r -> r.path("/app/loans-service/**")
				.filters(f -> f.rewritePath("/app/loans-service/(?<segment>.*)", "/${segment}"))
				.uri("lb://LOANS"))

			.build();
	}
}