package com.example.sample.web.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.sample.web.ec.config.EcBackendClient;
import com.example.sample.web.ec.dto.BuyItemRequest;

@Service
public class EcService {

  private final RestClient backendClient;

  public EcService(@EcBackendClient RestClient backendClient) {
    this.backendClient = backendClient;
  }

  public void buyItem(BuyItemRequest request) {
    backendClient.post()
      .uri("/api/sleep")
      .body(request)
      .retrieve()
      .toBodilessEntity();
  }
}
