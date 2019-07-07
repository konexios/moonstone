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
package com.arrow.acn;

public interface MqttConstants {
	// topic exchange
	final static String DEFAULT_RABBITMQ_EXCHANGE = "amq.topic";

	// durable incoming queue
	final static String DEFAULT_RABBITMQ_TELEMETRY_QUEUE = "kronos.telemetry";
	final static String DEFAULT_RABBITMQ_TELEMETRY_BATCH_QUEUE = "kronos.telemetry.batch";
	final static String DEFAULT_RABBITMQ_TELEMTRY_GZIP_BATCH_QUEUE = "kronos.telemetry.gzip.batch";
	final static String DEFAULT_RABBITMQ_COMMAND_QUEUE = "kronos.command";
	final static String DEFAULT_RABBITMQ_MQTT_API_QUEUE = "kronos.api";

	// application telemetry direct exchange
	final static String APPLICATION_TELEMETRY_EXCHANGE = "kronos.application.telemetry";

	static String applicationTelemetryRouting(String applicationHid) {
		return String.format("app.%s", applicationHid);
	}

	static String gatewayToServerTelemetryRouting(String gatewayHid) {
		return String.format("krs.tel.gts.%s", gatewayHid);
	}

	static boolean isGatewayToServerTelemetryRouting(String queueName) {
		return queueName.startsWith("krs.tel.gts.");
	}

	static String gatewayToServerTelemetryBatchRouting(String gatewayHid) {
		return String.format("krs.tel.bat.gts.%s", gatewayHid);
	}

	static boolean isGatewayToServerTelemetryBatchRouting(String queueName) {
		return queueName.startsWith("krs.tel.bat.gts.");
	}

	static String gatewayToServerTelemetryGzipBatchRouting(String gatewayHid) {
		return String.format("krs.tel.gzb.gts.%s", gatewayHid);
	}

	static boolean isGatewayToServerTelemetryGzipBatchRouting(String queueName) {
		return queueName.startsWith("krs.tel.gzb.gts.");
	}

	static String gatewayToServerCommandRouting(String gatewayHid) {
		return String.format("krs.cmd.gts.%s", gatewayHid);
	}

	static String serverToGatewayTelemetryRouting(String gatewayHid) {
		return String.format("krs.tel.stg.%s", gatewayHid);
	}

	static String serverToGatewayCommandRouting(String gatewayHid) {
		return String.format("krs.cmd.stg.%s", gatewayHid);
	}

	static String gatewayToServerMqttApiRouting(String gatewayHid) {
		return String.format("krs.api.gts.%s", gatewayHid);
	}

	static String serverToGatewayMqttApiRouting(String gatewayHid) {
		return String.format("krs.api.stg.%s", gatewayHid);
	}
}