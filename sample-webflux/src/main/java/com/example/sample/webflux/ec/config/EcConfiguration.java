package com.example.sample.webflux.ec.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.micrometer.observation.ObservationRegistry;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
@EnableConfigurationProperties(EcProperties.class)
public class EcConfiguration {
    @Bean
    @EcBackendClient
    WebClient ecBackendClient(WebClient.Builder builder, EcProperties ecProperties,
            ObservationRegistry observationRegistry) {
        var connectionProvider = ConnectionProvider.builder("myConnectionPool")
                .maxConnections(ecProperties.getMaxConnections())
                .pendingAcquireMaxCount(ecProperties.getPendingAcquireMaxCount())
                .build();

        var clientHttpConnector = new ReactorClientHttpConnector(HttpClient.create(connectionProvider));

        return builder
                .clientConnector(clientHttpConnector)
                .baseUrl(ecProperties.getBackendBaseUrl())
                .observationRegistry(observationRegistry)
                .build();
    }
}
