package com.example.sample.webflux.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.sample.webflux.ec.config.EcBackendClient;
import com.example.sample.webflux.ec.dto.BuyItemRequest;

import reactor.core.publisher.Mono;

@Service
public class EcService {

    private final WebClient webClient;

    public EcService(@EcBackendClient WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Void> buyItem(BuyItemRequest request) {
        return callBackend("/api/inventory/reserve", request)
                .then(callBackend("/api/payment/pay", request))
                .then(callBackend("/api/shipping/setup", request));
    }

    private Mono<Void> callBackend(String path, BuyItemRequest request) {
        return webClient.post()
                .uri(path)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
