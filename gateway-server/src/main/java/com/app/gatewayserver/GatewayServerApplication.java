package com.app.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import java.time.Duration;
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
										.setFallbackUri(
												"forward:/fallback/accounts")))
						.uri(URI_ACCOUNTS))

				.route(CARDS, r -> r
						.path(BASE_PATH_PREFIX + CARDS + "/**")
						.filters(f -> f
								.rewritePath(BASE_PATH_PREFIX + CARDS + "/(?<segment>.*)", "/${segment}")
								.retry(
										retryConfig -> retryConfig.setRetries(3)
												.setMethods(org.springframework.http.HttpMethod.GET)
												.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
						// .filter(addCommonHeaders("CARDS"))
						// .circuitBreaker(c -> c.setName("cardsCircuitBreaker")
						// .setFallbackUri(
						// "forward:/fallback/cards")
						// )
						)
						.uri(URI_CARDS))

				.route(LOANS, r -> r
						.path(BASE_PATH_PREFIX + LOANS + "/**")
						.filters(f -> f
								.rewritePath(BASE_PATH_PREFIX + LOANS + "/(?<segment>.*)", "/${segment}")
								.filter(addCommonHeaders("LOANS"))
								.circuitBreaker(c -> c.setName("loansCircuitBreaker")
										.setFallbackUri("forward:/fallback/loans"))
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)))
						.uri(URI_LOANS))
				.build();
	}

	/**
	 * Adds common response headers to all requests.a
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

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory ->
				factory.configureDefault(
				id -> new Resilience4JConfigBuilder(id).circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

}