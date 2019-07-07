package com.arrow.kronos.web;

public interface KronosWebConstants {

    public static final String DEVICE_TELEMETRY_DESTINATION_FORMAT = "/topic/device/%s/telemetry/%s";
    public static final String DEVICE_DESTINATION_FORMAT = "/topic/device/%s";
    public static final String DEVICE_TELEMETRY_RABBITMQ_QUEUE_NAME = "kronos.telemetry.web";
    public static final int DEVICE_TELEMETRY_LISTENER_NUM_THREADS = 10;
    
    
    // product configurations
    public static final String CONFIGURATION_NAME_LOGIN_SCREEN_DEV_REG_DESCRIPTION = "loginScreenDevRegDescription";
    public static final String CONFIGURATION_NAME_LOGIN_SCREEN_DEV_REG_METRICS = "loginScreenDevRegMetrics";
    public static final String CONFIGURATION_NAME_LOGIN_SCREEN_DEV_REG_EULA = "loginScreenDevRegEULA";
}
