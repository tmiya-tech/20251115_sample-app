package com.example.sample.webflux.ec.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sample.ec")
public class EcProperties {

    private String backendBaseUrl;
    private int maxConnections = 500;
    private int pendingAcquireMaxCount = 1000;

    public String getBackendBaseUrl() {
        return backendBaseUrl;
    }

    public void setBackendBaseUrl(String backendBaseUrl) {
        this.backendBaseUrl = backendBaseUrl;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getPendingAcquireMaxCount() {
        return pendingAcquireMaxCount;
    }

    public void setPendingAcquireMaxCount(int pendingAcquireMaxCount) {
        this.pendingAcquireMaxCount = pendingAcquireMaxCount;
    }

}
