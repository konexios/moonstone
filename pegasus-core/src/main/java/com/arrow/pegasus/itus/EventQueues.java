package com.arrow.pegasus.itus;

public interface EventQueues {
    public final static String CLIENT_TO_GATEWAY = "itus.client.to.gateway.";

    public static String clientToGateway(String applicationHid) {
        return CLIENT_TO_GATEWAY + applicationHid;
    }
}
