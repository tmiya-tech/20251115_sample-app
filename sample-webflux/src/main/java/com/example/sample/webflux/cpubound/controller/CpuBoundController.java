package com.example.sample.webflux.cpubound.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sample.webflux.cpubound.dto.PrimeNumberRequest;
import com.example.sample.webflux.cpubound.dto.PrimeNumberResponse;
import com.example.sample.webflux.cpubound.service.CpuBoundService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cpubound")
public class CpuBoundController {

    private final CpuBoundService cpuBoundService;

    public CpuBoundController(CpuBoundService cpuBoundService) {
        this.cpuBoundService = cpuBoundService;
    }

    /**
     * Calculate prime numbers in the specified range reactively.
     * This is a CPU-intensive operation designed for performance testing.
     *
     * @param request start and end range for prime number calculation
     * @return Mono of prime numbers list and calculation metadata
     */
    @PostMapping("/primes")
    public Mono<PrimeNumberResponse> calculatePrimes(@RequestBody PrimeNumberRequest request) {
        return cpuBoundService.calculatePrimes(request);
    }
}
