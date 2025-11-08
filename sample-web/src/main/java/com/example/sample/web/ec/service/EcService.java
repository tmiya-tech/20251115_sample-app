package com.example.sample.web.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.sample.web.ec.config.EcBackendClient;
import com.example.sample.web.ec.dto.BuyItemRequest;

@Service
public class EcService {

  private final RestTemplate backendClient;

  public EcService(@EcBackendClient RestTemplate backendClient) {
    this.backendClient = backendClient;
  }

  public void buyItem(BuyItemRequest request) {
    backendClient.postForEntity("/api/sleep", request, Void.class);
  }
}
