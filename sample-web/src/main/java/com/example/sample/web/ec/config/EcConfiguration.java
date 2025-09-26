package com.example.sample.web.ec.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(EcProperties.class)
public class EcConfiguration {

  @Bean
  @EcBackendClient
  RestClient ecBackendClient(EcProperties ecProperties) {
    return RestClient.builder()
      .baseUrl(ecProperties.getBackendBaseUrl())
      .build();
  }

}
