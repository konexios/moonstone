package com.arrow.selene.databus;

public interface Databus {
    String REDIS = "redis";
    String RABBITMQ = "rabbitmq";
    String FILE = "file";
    String MQTT = "mqtt";

    void start();

    void stop();

    void registerListener(DatabusListener listener, String... queues);

    void send(String queue, byte[] message);

    long getMaxBuffer();

    void setMaxBuffer(long maxBuffer);
}
