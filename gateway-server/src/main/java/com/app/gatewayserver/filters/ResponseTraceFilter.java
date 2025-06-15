package com.app.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseTraceFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    private FilterUtility filterUtility;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) ->
                chain.filter(exchange).then(
                        Mono.fromRunnable(() -> {
                            HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
                            String correlationId = filterUtility.getCorrelationId(httpHeaders);

                                    logger.info("Outgoing response [{}] {} with Correlation ID: {}",
                                    exchange.getRequest().getMethod(),
                                    exchange.getRequest().getURI(),
                                    correlationId);

                            exchange.getResponse().getHeaders().add(filterUtility.CORRELATION_ID, correlationId);

                        })
                );
    }
}