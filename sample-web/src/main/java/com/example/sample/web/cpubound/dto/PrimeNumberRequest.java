package com.example.sample.web.cpubound.dto;

public record PrimeNumberRequest(
    Integer start,
    Integer end
) {
    public PrimeNumberRequest {
        if (start == null || end == null) {
            throw new IllegalArgumentException("start and end must not be null");
        }
        if (start < 2) {
            throw new IllegalArgumentException("start must be greater than or equal to 2");
        }
        if (end < start) {
            throw new IllegalArgumentException("end must be greater than or equal to start");
        }
        if (end - start > 1_000_000) {
            throw new IllegalArgumentException("range must not exceed 1,000,000");
        }
    }
}
