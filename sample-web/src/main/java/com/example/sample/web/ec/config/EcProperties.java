package com.example.sample.web.ec.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sample.ec")
public class EcProperties {
  private String backendBaseUrl;

  public String getBackendBaseUrl() {
    return backendBaseUrl;
  }

  public void setBackendBaseUrl(String backendBaseUrl) {
    this.backendBaseUrl = backendBaseUrl;
  }

}
