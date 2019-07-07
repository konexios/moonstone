package com.arrow.kronos;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServerEndpointRegistration;

import com.arrow.kronos.api.DeviceTelemetryEndpoint;
import com.arrow.kronos.api.NodeTelemetryEndpoint;

// @Configuration
public class WebSocketConfiguration {
	@Bean
	public ServerEndpointExporter endpointExporter() {
		return new ServerEndpointExporter();
	}

	@Bean
	public ServerEndpointRegistration deviceTelemetryEndpointRegistration() {
		return new ServerEndpointRegistration("/api/v1/kronos/devices/{hid}/telemetry/{telemetryName}",
				deviceTelemetryEndpoint());
	}

	@Bean
	public DeviceTelemetryEndpoint deviceTelemetryEndpoint() {
		return new DeviceTelemetryEndpoint();
	}

	@Bean
	public ServerEndpointRegistration nodeTelemetryEndpointRegistration() {
		return new ServerEndpointRegistration("/api/v1/kronos/nodes/{hid}/telemetry/{telemetryName}",
				nodeTelemetryEndpoint());
	}

	@Bean
	public NodeTelemetryEndpoint nodeTelemetryEndpoint() {
		return new NodeTelemetryEndpoint();
	}
}
