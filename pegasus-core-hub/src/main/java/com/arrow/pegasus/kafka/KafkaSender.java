package com.arrow.pegasus.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.Assert;

import moonstone.acs.Loggable;

public class KafkaSender extends Loggable {

    @Autowired
    private KafkaTemplate<String, String> template;

    public void send(String topic, String key, String message) {
        String method = "topic";
        Assert.hasText(topic, "empty topic");
        Assert.hasText(message, "empty message");
        logDebug(method, "%s --> %s", topic, message.length() <= 100 ? message : message.substring(0, 100) + "...");
        template.send(topic, key, message);
    }
}
