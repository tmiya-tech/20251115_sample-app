package com.example.sample.web.ec.config;

import java.net.http.HttpClient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import io.micrometer.observation.ObservationRegistry;

@Configuration
@EnableConfigurationProperties(EcProperties.class)
public class EcConfiguration {

  @Bean
  @EcBackendClient
  RestClient ecBackendClient(EcProperties ecProperties, ObservationRegistry observationRegistry) {
    return RestClient.builder()
        .requestFactory(new JdkClientHttpRequestFactory(HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build()))
        .baseUrl(ecProperties.getBackendBaseUrl())
        .observationRegistry(observationRegistry)
        .build();
  }

}
