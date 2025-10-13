package com.example.sample.web.cpubound.service;

import org.springframework.stereotype.Service;

import com.example.sample.web.cpubound.dto.PrimeNumberRequest;
import com.example.sample.web.cpubound.dto.PrimeNumberResponse;

@Service
public class CpuBoundService {

    /**
     * Calculate prime numbers in the specified range.
     * This is a CPU-intensive operation.
     */
    public PrimeNumberResponse calculatePrimes(PrimeNumberRequest request) {
        long startTime = System.currentTimeMillis();

        int primeCount = 0;

        for (int num = request.start(); num <= request.end(); num++) {
            if (isPrime(num)) {
                primeCount++;
            }
        }

        long calculationTime = System.currentTimeMillis() - startTime;

        return new PrimeNumberResponse(
            request.start(),
            request.end(),
            primeCount,
            calculationTime
        );
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
