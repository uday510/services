package com.app.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);
    private static final String CORRELATION_ID = FilterUtility.CORRELATION_ID;

    @Autowired
    private FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();

        String correlationId = filterUtility.getCorrelationId(headers);
        if (correlationId != null) {
            logger.debug("Correlation ID found in request: {}", correlationId);
        } else {
            correlationId = generateCorrelationID();
            exchange = filterUtility.setCorrelationId(exchange, correlationId);
            logger.debug("Generated new Correlation ID: {}", correlationId);
        }

        logger.info("Incoming request [{}] {} with Correlation ID: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                correlationId
        );

        return chain.filter(exchange);
    }

    private String generateCorrelationID() {
        return UUID.randomUUID().toString();
    }
}