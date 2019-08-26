package com.arrow.pegasus;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import moonstone.acs.Loggable;

public class CoreApplicationListener extends Loggable implements ApplicationListener<ApplicationEvent> {

    public CoreApplicationListener() {
        logInfo(getClass().getSimpleName(), "...");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        String method = "onApplicationEvent";
        logTrace(method, event.getClass().getName());
    }
}
