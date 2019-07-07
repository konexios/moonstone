package com.arrow.pegasus.util;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

public class TtlMessagePostProcessor implements MessagePostProcessor {

    private final String timeToLive;

    public TtlMessagePostProcessor(long timeToLive) {
    	 this.timeToLive = String.valueOf(timeToLive * 1000);
	}
    

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties().setExpiration(timeToLive);
        return message;
    }
}