package com.example.sample.web.cpubound.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sample.web.cpubound.dto.PrimeNumberRequest;
import com.example.sample.web.cpubound.dto.PrimeNumberResponse;
import com.example.sample.web.cpubound.service.CpuBoundService;

@RestController
@RequestMapping("/api/cpubound")
public class CpuBoundController {

    private final CpuBoundService cpuBoundService;

    public CpuBoundController(CpuBoundService cpuBoundService) {
        this.cpuBoundService = cpuBoundService;
    }

    /**
     * Calculate prime numbers in the specified range.
     * This is a CPU-intensive operation designed for performance testing.
     *
     * @param request start and end range for prime number calculation
     * @return list of prime numbers and calculation metadata
     */
    @PostMapping("/primes")
    public ResponseEntity<PrimeNumberResponse> calculatePrimes(@RequestBody PrimeNumberRequest request) {
        PrimeNumberResponse response = cpuBoundService.calculatePrimes(request);
        return ResponseEntity.ok(response);
    }
}
