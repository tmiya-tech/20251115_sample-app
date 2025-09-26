package com.example.sample.webflux.ec.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sample.webflux.ec.dto.BuyItemRequest;
import com.example.sample.webflux.ec.service.EcService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ec")
public class EcController {

    private final EcService ecService;

    public EcController(EcService ecService) {
        this.ecService = ecService;
    }

    @PostMapping("/buy")
    public Mono<Map<String, String>> buyItem(@RequestBody Mono<BuyItemRequest> request) {
        return request
                .flatMap(ecService::buyItem)
                .thenReturn(Map.of("status", "ok"));
    }
}
