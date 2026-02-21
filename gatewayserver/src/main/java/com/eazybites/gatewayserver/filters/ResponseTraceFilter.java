package com.eazybites.gatewayserver.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;

@Configuration
public class ResponseTraceFilter {

    @Bean
    public GlobalFilter postGlobalFilter() {
        return ((exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            headers.add("X-Response-Time", LocalDateTime.now().toString());
            exchange.getResponse().getHeaders().addAll(headers);
            return chain.filter(exchange);
        });
    }
}
