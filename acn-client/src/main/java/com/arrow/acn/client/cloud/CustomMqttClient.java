/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acn.client.cloud;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.arrow.acn.client.AcnClientException;
import com.arrow.acn.client.ClientConstants;
import com.arrow.acn.client.ClientConstants.Mqtt;
import com.arrow.acn.client.utils.Utils;
import com.arrow.acs.AcsUtils;
import com.arrow.acs.Loggable;

public class CustomMqttClient extends Loggable {
    private String url;
    private String clientId;
    private MqttClient client;
    private String[] topics;
    private MqttConnectOptions options;
    private boolean cleanSession;

    private Thread connectThread;
    private boolean disconnected = false;

    private SubscriberCallback subscriberCallback = new SubscriberCallback();

    public CustomMqttClient(String url) {
        this(url, MqttClient.generateClientId(), true);
    }

    public CustomMqttClient(String url, String clientId) {
        this(url, clientId, false);
    }

    private CustomMqttClient(String url, String clientId, boolean cleanSession) {
        AcsUtils.notEmpty(url, "url is not set");
        this.url = url;
        this.clientId = clientId;
        this.cleanSession = cleanSession;
        this.options = new MqttConnectOptions();
    }

    public MqttConnectOptions getOptions() {
        return options;
    }

    public void setOptions(MqttConnectOptions options) {
        this.options = options;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String... topics) {
        this.topics = topics;
    }

    public void setListener(MessageListener listener) {
        subscriberCallback.setListener(listener);
    }

    public synchronized void connect(boolean reconnect) {
        String method = "connect";
        if (reconnect && client != null && client.isConnected()) {
            logInfo(method, "connectionLost is FALSE, possible already recovered!!!!!!");
            return;
        }

        if (connectThread != null && connectThread.isAlive()) {
            logInfo(method, "connectThread is still running!");
            return;
        }

        connectThread = new Thread() {
            @Override
            public void run() {
                if (reconnect) {
                    logInfo(method, "pausing a bit before trying to reconnect ...");
                    Utils.sleep(Mqtt.DEFAULT_PAUSE_BEFORE_RECONNECT_MS);
                }
                client = initClient(clientId, options);
                if (client != null && topics != null) {
                    logInfo(method, "subscribing to: %d topics", topics.length);
                    subscribe(topics);
                } else {
                    logWarn(method, "there's no topics to subscribe");
                }
            }
        };
        logInfo(method, "starting connectThread ...");
        connectThread.start();
    }

    public void publish(String topic, byte[] data, int qos) {
        String method = "publish";
        checkConnection();
        try {
            MqttMessage msg = new MqttMessage(data);
            msg.setQos(qos);
            logDebug(method, "sending message to topic: %s", topic);
            client.publish(topic, msg);
            logInfo(method, "sent message to topic: %s", topic);
        } catch (Exception e) {
            throw new AcnClientException("unable to send mqtt message", e);
        }
    }

    public void subscribe(String... topics) {
        String method = "createNewSubscriber";
        checkConnection();
        while (true) {
            try {
                client.subscribe(topics);
                logInfo(method, "subscribed to topic: %s", Arrays.deepToString(topics));
                break;
            } catch (Throwable t) {
                logError(method, "unable to subscribe to MQTT topic, retrying in "
                        + ClientConstants.DEFAULT_CLOUD_CONNECTION_RETRY_INTERVAL_MS, t);
                Utils.sleep(ClientConstants.DEFAULT_CLOUD_CONNECTION_RETRY_INTERVAL_MS);
            }
        }
    }

    public void checkConnection() {
        String method = "checkConnection";
        while (client == null || !client.isConnected()) {
            logError(method, "client is not ready: %s, check back in %d ...", this.url,
                    Mqtt.DEFAULT_CHECK_CONNECTION_RETRY_INTERVAL_MS);
            Utils.sleep(Mqtt.DEFAULT_CHECK_CONNECTION_RETRY_INTERVAL_MS);
        }
    }

    public void disconnect() {
        String method = "disconnectClient";
        if (client != null && client.isConnected()) {
            try {
                if (topics != null) {
                    client.unsubscribe(topics);
                }
                logInfo(method, "disconnecting client ...");
                client.disconnect();
                logInfo(method, "client disconnected");
            } catch (MqttException e) {
                try {
                    logInfo(method, "cannot disconnect client, disconnecting client forcibly...");
                    client.disconnectForcibly();
                    logInfo(method, "client disconnected forcibly");
                } catch (MqttException e1) {
                    logError(method, "cannot disconnect client");
                }
            }
        }

        if (connectThread != null && connectThread.isAlive()) {
            try {
                disconnected = true;
                logInfo(method, "waiting for connectThread to stop ...");
                connectThread.join();
                logInfo(method, "connectThread terminated");
            } catch (Throwable t) {
            }
        }
    }

    private MqttClient initClient(String clientId, MqttConnectOptions options) {
        String method = "initClient";
        logInfo(method, "creating client %s to %s", clientId, url);
        while (!disconnected) {
            try {
                MqttClient client = new MqttClient(url, clientId, null);
                client.setCallback(subscriberCallback);
                options.setCleanSession(cleanSession);
                client.connect(options);
                logInfo(method, "client connected: %s", clientId);
                disconnected = false;
                return client;
            } catch (Throwable t) {
                long retry = ClientConstants.DEFAULT_CLOUD_CONNECTION_RETRY_INTERVAL_MS;
                logError(method, "unable to connect to MQTT server, retrying in " + retry, t);
                Utils.sleep(retry);
            }
        }
        logInfo(method, "done");
        return null;
    }

    private class SubscriberCallback implements MqttCallbackExtended {
        private MessageListener listener;

        SubscriberCallback() {
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String method = "messageArrived";
            byte[] payload = message.getPayload();
            logDebug(method, "topic: %s, payload: %s", topic, new String(payload, StandardCharsets.UTF_8));
            if (listener != null) {
                try {
                    listener.processMessage(topic, payload);
                } catch (Throwable t) {
                    logError(method, t);
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            String method = "deliveryComplete";
            logDebug(method, "messageId: %s", token.getMessageId());
        }

        @Override
        public void connectionLost(Throwable cause) {
            String method = "connectionLost";
            logInfo(method, "cause: %s", cause.getMessage());
            connect(true);
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            String method = "connectComplete";
            logInfo(method, "serverURI: %s", serverURI);
        }

        void setListener(MessageListener listener) {
            this.listener = listener;
        }
    }
}
