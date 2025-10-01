package com.example.sample.webflux.cpubound.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sample.webflux.cpubound.dto.PrimeNumberRequest;
import com.example.sample.webflux.cpubound.dto.PrimeNumberResponse;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class CpuBoundService {

    /**
     * Calculate prime numbers in the specified range reactively.
     * This is a CPU-intensive operation that runs on a bounded elastic scheduler
     * to avoid blocking the event loop.
     */
    public Mono<PrimeNumberResponse> calculatePrimes(PrimeNumberRequest request) {
        return Mono.fromCallable(() -> {
            long startTime = System.currentTimeMillis();

            List<Integer> primes = new ArrayList<>();

            for (int num = request.start(); num <= request.end(); num++) {
                if (isPrime(num)) {
                    primes.add(num);
                }
            }

            long calculationTime = System.currentTimeMillis() - startTime;

            return new PrimeNumberResponse(
                request.start(),
                request.end(),
                primes.size(),
                calculationTime,
                primes
            );
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Check if a number is prime using trial division.
     * This is intentionally not optimized to create CPU load.
     */
    private boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }

        // Trial division - intentionally using less efficient algorithm
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }
}
