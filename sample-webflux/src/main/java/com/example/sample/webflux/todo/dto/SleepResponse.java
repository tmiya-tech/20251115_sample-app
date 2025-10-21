package com.example.sample.webflux.todo.dto;

import java.time.LocalDateTime;

public class SleepResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long elapsedMs;

    public SleepResponse() {}

    public SleepResponse(LocalDateTime startTime, LocalDateTime endTime, long elapsedMs) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.elapsedMs = elapsedMs;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public long getElapsedMs() {
        return elapsedMs;
    }

    public void setElapsedMs(long elapsedMs) {
        this.elapsedMs = elapsedMs;
    }
}
