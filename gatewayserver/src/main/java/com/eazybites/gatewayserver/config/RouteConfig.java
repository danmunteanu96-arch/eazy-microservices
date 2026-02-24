package com.eazybites.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {


    @Bean
    public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("accounts_route",
                        p -> p
                                .path("/eazybank/accounts/**")
                                .filters(f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
                                        .circuitBreaker(config -> config.setName("accountsCircuitBreaker").setFallbackUri("forward:/contactSupport"))

                                )
                                .uri("lb://accounts"))
                .route("loans_route",
                        p -> p
                                .path("/eazybank/loans/**")
                                .filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}"))
                                .uri("lb://loans"))
                .route("cards_route",
                        p -> p
                                .path("/eazybank/cards/**")
                                .filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}"))
                                .uri("lb://cards"))

                .build();
    }
}
