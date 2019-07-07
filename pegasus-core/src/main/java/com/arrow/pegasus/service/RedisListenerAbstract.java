package com.arrow.pegasus.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public abstract class RedisListenerAbstract extends QueueListenerAbstract {

    @Autowired
    private RedisConnectionFactory connectionFactory;

    private RedisMessageListenerContainer container;

    @Override
    public void start() {
        super.start();
        String method = "RedisListenerAbstract.start";
        if (!isStarted()) {
            List<PatternTopic> topics = new ArrayList<>();
            for (String queue : getQueues()) {
                topics.add(new PatternTopic(queue));
                logInfo(method, "adding queue: %s", queue);
            }
            MessageListenerAdapter adapter = new MessageListenerAdapter(this, "receiveMessage");
            adapter.afterPropertiesSet();
            container = new RedisMessageListenerContainer();
            container.addMessageListener(adapter, topics);
            container.setConnectionFactory(connectionFactory);
            container.afterPropertiesSet();
            container.start();
            setStarted(true);
            logInfo(method, "started");
        } else {
            logInfo(method, "already started");
        }
    }

    @PreDestroy
    public void preDestroy() {
        super.preDestroy();
        String method = "RedisListenerAbstract.preDestroy";
        if (container != null) {
            try {
                logInfo(method, "stop container ...");
                container.destroy();
            } catch (Throwable t) {
            }
            container = null;
        }
        setStarted(false);
        logInfo(method, "stopped");
    }

    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
}
