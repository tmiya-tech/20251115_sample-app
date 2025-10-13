package com.example.sample.web.cpubound.dto;

public record PrimeNumberResponse(
    Integer start,
    Integer end,
    Integer count,
    Long calculationTimeMs
) {
}
