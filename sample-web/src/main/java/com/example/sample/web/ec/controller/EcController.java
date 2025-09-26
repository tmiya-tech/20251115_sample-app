package com.example.sample.web.ec.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sample.web.ec.dto.BuyItemRequest;
import com.example.sample.web.ec.service.EcService;

@RestController
@RequestMapping("/api/ec")
public class EcController {

    private final EcService ecService;

    public EcController(EcService ecService) {
        this.ecService = ecService;
    }

    @PostMapping("/buy")
    public Map<String, String> buyItem(@RequestBody BuyItemRequest request) {
        ecService.buyItem(request);
        return Map.of("status", "ok");
    }
}
