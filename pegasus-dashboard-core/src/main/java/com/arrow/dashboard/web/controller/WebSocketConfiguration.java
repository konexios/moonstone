package com.arrow.dashboard.web.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.arrow.dashboard.DashboardConstants;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(DashboardConstants.MESSAGE_BROKER);
        registry.setApplicationDestinationPrefixes(DashboardConstants.APPLICATION_PREFIX);
    }

    @Override
    public void configureStompEndpoints(StompEndpointRegistry registry) {
        // @formatter:off
		registry.addEndpoint(DashboardConstants.MESSAGE_ENDPOINT)
			.addInterceptors(handshakeInterceptor())	
			.withSockJS();
		// @formatter:on
    }

    // TODO should make these configurable

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(1024000);

        // sendTimeLimit is 10s default
        // set sendBufferSizeLimit to 0 (zero) to disable buffering
        // registration.setSendTimeLimit(15 * 1000).setSendBufferSizeLimit(512 *
        // 1024);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // super.configureClientInboundChannel(registration);
        registration.taskExecutor().corePoolSize(Runtime.getRuntime().availableProcessors() * 16);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        // super.configureClientOutboundChannel(registration);
        registration.taskExecutor().corePoolSize(Runtime.getRuntime().availableProcessors() * 16);
    };

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
                        attributes.put(DashboardConstants.SessionAttribute.HTTP_SESSION_ID_ATTRIBUTE, session.getId());

                        // String applicationId = (String)
                        // session.getAttribute(CoreConstant.CURRENT_APPLICATION_ID);
                        // Assert.hasText(applicationId, "applicationId is
                        // empty");
                        // attributes.put(CoreConstant.CURRENT_APPLICATION_ID,
                        // applicationId);
                    }
                }
                return super.beforeHandshake(request, response, wsHandler, attributes);
            }
        };
    }
}