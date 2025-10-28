package com.example.sample.web.ec.config;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading;
import org.springframework.boot.autoconfigure.thread.Threading;
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
  @ConditionalOnThreading(Threading.PLATFORM)
  @EcBackendClient
  RestClient ecBackendClientPT(EcProperties ecProperties, ObservationRegistry observationRegistry) {
    return RestClient.builder()
        .requestFactory(new JdkClientHttpRequestFactory(HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build()))
        .baseUrl(ecProperties.getBackendBaseUrl())
        .observationRegistry(observationRegistry)
        .build();
  }

  @Bean
  @ConditionalOnThreading(Threading.VIRTUAL)
  @EcBackendClient
  RestClient ecBackendClientVT(EcProperties ecProperties, ObservationRegistry observationRegistry) {
    return RestClient.builder()
        .requestFactory(new JdkClientHttpRequestFactory(HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .executor(Executors.newVirtualThreadPerTaskExecutor()) // バーチャルスレッドを使用
            .build()))
        .baseUrl(ecProperties.getBackendBaseUrl())
        .observationRegistry(observationRegistry)
        .build();
  }

}
