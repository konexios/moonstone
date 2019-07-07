package com.arrow.kronos.web;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.arrow.pegasus.CoreConstant;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocket extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app", "");
        config.enableSimpleBroker("/queue", "/topic");
    }

    @Override
    protected void configureStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/kronos/ws/device").addInterceptors(handshakeInterceptor()).withSockJS();
        registry.addEndpoint("/api/kronos/ws/home").addInterceptors(handshakeInterceptor()).withSockJS();
    }

    @Bean
    public HandshakeInterceptor handshakeInterceptor() {
        return new HttpSessionHandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                    WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                    HttpSession session = servletRequest.getServletRequest().getSession(false);
                    if (session != null) {
                        String applicationId = (String) session.getAttribute(CoreConstant.CURRENT_APPLICATION_ID);
                        Assert.hasText(applicationId, "applicationId is empty");

                        attributes.put(CoreConstant.CURRENT_APPLICATION_ID, applicationId);
                    }
                }
                return super.beforeHandshake(request, response, wsHandler, attributes);
            }
        };
    }

    @Bean
    public ChannelInterceptor telemetrySubscriptionInterceptor() {
        return new TelemetrySubscriptionInterceptor();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(telemetrySubscriptionInterceptor());
        super.configureClientInboundChannel(registration);
    }
}