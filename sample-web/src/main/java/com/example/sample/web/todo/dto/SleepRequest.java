package com.example.sample.web.todo.dto;

public class SleepRequest {
    private long sleepMs;

    public SleepRequest() {}

    public SleepRequest(long sleepMs) {
        this.sleepMs = sleepMs;
    }

    public long getSleepMs() {
        return sleepMs;
    }

    public void setSleepMs(long sleepMs) {
        this.sleepMs = sleepMs;
    }
}
