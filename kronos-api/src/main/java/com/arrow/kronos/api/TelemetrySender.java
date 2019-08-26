package com.arrow.kronos.api;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.arrow.kronos.KronosApiConstants;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.service.KronosCache;
import com.arrow.pegasus.service.ProcessorAbstract;

import moonstone.acn.MqttConstants;
import moonstone.acs.JsonUtils;

@Component
public class TelemetrySender extends ProcessorAbstract implements CommandLineRunner {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private KronosCache kronosCache;

	@Override
	protected int getServiceNumThreads() {
		return KronosApiConstants.DEFAULT_PROCESSOR_NUM_THREADS;
	}

	@Override
	public void run(String... args) throws Exception {
		start();
	}

	public void process(final Map<String, String> model) {
		String method = "process";
		if (model == null) {
			logInfo(method, "ignore null model");
			return;
		}
		try {
			if (!isTerminating()) {
				getService().submit(new RabbitWorker(model));
			}
		} catch (Throwable t) {
			logError(method, t);
		}
	}

	class RabbitWorker implements Runnable {

		private Map<String, String> model;

		RabbitWorker(Map<String, String> model) {
			this.model = model;
		}

		@Override
		public void run() {
			String method = "RabbitWorker.run";
			try {
				String deviceHid = model.get("_|deviceHid");
				if (StringUtils.isEmpty(deviceHid)) {
					logError(method, "deviceHid not found in payload");
					return;
				}
				Device device = kronosCache.findDeviceByHid(deviceHid);
				if (device == null) {
					logError(method, "device not found for hid: %s", deviceHid);
					return;
				}
				if (!device.isEnabled()) {
					logError(method, "device is disabled: %s", deviceHid);
					return;
				}
				String gatewayId = device.getGatewayId();
				Gateway gateway = kronosCache.findGatewayById(gatewayId);
				if (gateway == null) {
					logError(method, "gateway not found with id: %s", gatewayId);
					return;
				}
				if (!gateway.isEnabled()) {
					logError(method, "gateway is disabled: %s", gatewayId);
					return;
				}
				rabbitTemplate.convertAndSend(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE,
				        MqttConstants.gatewayToServerTelemetryRouting(gateway.getHid()),
				        MessageBuilder.withBody(JsonUtils.toJsonBytes(model)).build());
				logInfo(method, "message sent for deviceHid: %s", deviceHid);
			} catch (Throwable t) {
				logError(method, "error processing message", t);
			}
		}
	}
}
