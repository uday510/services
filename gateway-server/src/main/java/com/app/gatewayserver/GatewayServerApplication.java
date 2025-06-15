package com.app.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class GatewayServerApplication {

	private static final String ACCOUNTS = "accounts-service";
	private static final String CARDS = "cards-service";
	private static final String LOANS = "loans-service";

	private static final String BASE_PATH_PREFIX = "/app/";

	private static final String URI_ACCOUNTS = "lb://ACCOUNTS";
	private static final String URI_CARDS = "lb://CARDS";
	private static final String URI_LOANS = "lb://LOANS";

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	/**
	 * Configures custom routes with circuit breakers and response headers.
	 */
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()

				.route(ACCOUNTS, r -> r
						.path(BASE_PATH_PREFIX + ACCOUNTS + "/**")
						.filters(f -> f
								.rewritePath(BASE_PATH_PREFIX + ACCOUNTS + "/(?<segment>.*)", "/${segment}")
								.filter(addCommonHeaders("ACCOUNTS"))
								.circuitBreaker(c -> c.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/fallback/accounts"))
						)
						.uri(URI_ACCOUNTS))

				.route(CARDS, r -> r
						.path(BASE_PATH_PREFIX + CARDS + "/**")
						.filters(f -> f
								.rewritePath(BASE_PATH_PREFIX + CARDS + "/(?<segment>.*)", "/${segment}")
								.filter(addCommonHeaders("CARDS"))
								.circuitBreaker(c -> c.setName("cardsCircuitBreaker")
										.setFallbackUri("forward:/fallback/cards"))
						)
						.uri(URI_CARDS))

				.route(LOANS, r -> r
						.path(BASE_PATH_PREFIX + LOANS + "/**")
						.filters(f -> f
								.rewritePath(BASE_PATH_PREFIX + LOANS + "/(?<segment>.*)", "/${segment}")
								.filter(addCommonHeaders("LOANS"))
								.circuitBreaker(c -> c.setName("loansCircuitBreaker")
										.setFallbackUri("forward:/fallback/loans"))
						)
						.uri(URI_LOANS))

				.build();
	}

	/**
	 * Adds common response headers to all requests.
	 */
	private GatewayFilter addCommonHeaders(String serviceId) {
		return (exchange, chain) -> {
			var headers = exchange.getResponse().getHeaders();
			headers.add("X-Response-Time", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
			headers.add("X-Service-Id", serviceId);
			headers.add("X-Gateway-Instance", "GATEWAY-1");
			headers.add("Cache-Control", "no-store");
			return chain.filter(exchange);
		};
	}
}