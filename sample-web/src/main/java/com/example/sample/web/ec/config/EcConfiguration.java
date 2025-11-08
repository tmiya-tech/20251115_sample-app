package com.example.sample.web.ec.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.micrometer.observation.ObservationRegistry;

@Configuration
@EnableConfigurationProperties(EcProperties.class)
public class EcConfiguration {

  @Bean
  @EcBackendClient
  RestTemplate ecBackendClient(RestTemplateBuilder builder, EcProperties ecProperties,
      ObservationRegistry observationRegistry) {
    RestTemplate restTemplate = builder
        .rootUri(ecProperties.getBackendBaseUrl())
        .build();
    restTemplate.setObservationRegistry(observationRegistry);
    return restTemplate;
  }

}
