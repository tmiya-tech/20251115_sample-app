package com.example.sample.webflux.ec.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import io.micrometer.observation.ObservationRegistry;

@Configuration
@EnableConfigurationProperties(EcProperties.class)
public class EcConfiguration {
    @Bean
    @EcBackendClient
    WebClient ecBackendClient(WebClient.Builder builder, EcProperties ecProperties,
            ObservationRegistry observationRegistry) {
        return builder
                .baseUrl(ecProperties.getBackendBaseUrl())
                .observationRegistry(observationRegistry)
                .build();
    }
}
