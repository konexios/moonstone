package com.arrow.kronos;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.arrow.kronos.engine")
public class KronosEngineProperties implements Serializable {
    private static final long serialVersionUID = 7217502744852487649L;

    private String applicationEngineId;

    public String getApplicationEngineId() {
        return applicationEngineId;
    }

    public void setApplicationEngineId(String applicationEngineId) {
        this.applicationEngineId = applicationEngineId;
    }
}
