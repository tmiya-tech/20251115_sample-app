package com.example.sample.webflux.cpubound.dto;

public record PrimeNumberResponse(
    Integer start,
    Integer end,
    Integer count,
    Long calculationTimeMs
) {
}
