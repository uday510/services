package com.app.gatewayserver.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class FilterUtility {

    public static final String CORRELATION_ID = "app-correlation-id";

    /**
     * Extracts the correlation ID from HTTP headers if present.
     *
     * @param httpHeaders the HTTP headers
     * @return correlation ID or null if not present
     */
    public String getCorrelationId(HttpHeaders httpHeaders) {
        return httpHeaders.getFirst(CORRELATION_ID);
    }

    /**
     * Sets a custom header on the incoming request.
     *
     * @param exchange   the current server web exchange
     * @param headerName the header name
     * @param headerValue the header value
     * @return mutated ServerWebExchange with the new header
     */
    public ServerWebExchange setHttpRequestHeader(ServerWebExchange exchange, String headerName, String headerValue) {
        return exchange.mutate()
                .request(exchange.getRequest().mutate().header(headerName, headerValue).build())
                .build();
    }

    /**
     * Adds or overrides the correlation ID header.
     *
     * @param exchange the current web exchange
     * @param correlationId the correlation ID to set
     * @return mutated ServerWebExchange with correlation ID
     */
    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return setHttpRequestHeader(exchange, CORRELATION_ID, correlationId);
    }
}