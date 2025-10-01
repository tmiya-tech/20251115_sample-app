package com.example.sample.webflux.cpubound.dto;

import java.util.List;

public record PrimeNumberResponse(
    Integer start,
    Integer end,
    Integer count,
    Long calculationTimeMs,
    List<Integer> primes
) {
}
