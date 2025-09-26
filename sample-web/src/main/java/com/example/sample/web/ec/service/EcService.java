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
    // 在庫確保APIを呼び出す
    backendClient.post()
      .uri("/api/inventory/reserve")
      .body(request)
      .retrieve()
      .toBodilessEntity();

    // 決済APIを呼び出す
    backendClient.post()
      .uri("/api/payment/pay")
      .body(request)
      .retrieve()
      .toBodilessEntity();

    // 配送手配APIを呼び出す
    backendClient.post()
      .uri("/api/shipping/setup")
      .body(request)
      .retrieve()
      .toBodilessEntity();
  }
}
