package com.arrow.kronos.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.arrow.acn.MqttConstants;
import com.arrow.acn.client.model.TelemetryItemModel;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.KeyValuePair;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.converter.TelemetryItemConverter;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.kafka.KafkaSender;
import com.arrow.pegasus.security.Crypto;
import com.arrow.pegasus.util.TtlMessagePostProcessor;

public class TelemetryProcessor extends KronosServiceAbstract {
	@Autowired
	private KafkaSender kafkaSender;
	@Autowired
	private RabbitTemplate rabbit;
	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private Crypto crypto;

	@Override
	protected void postConstruct() {
		super.postConstruct();
		String method = "postConstruct";

		RabbitAdmin admin = new RabbitAdmin(connectionFactory);
		logDebug(method, "declaring direct exchange %s", MqttConstants.APPLICATION_TELEMETRY_EXCHANGE);
		admin.declareExchange(new DirectExchange(MqttConstants.APPLICATION_TELEMETRY_EXCHANGE, true, false));
	}

	public List<String> process(Map<String, String> model) {
		String method = "process";
		List<String> errors = new ArrayList<>();
		List<TelemetryItem> itemList = new ArrayList<>();
		String hid = null;
		long timestamp = Instant.now().toEpochMilli();
		try {
			for (String name : model.keySet()) {
				String value = model.get(name);
				if (StringUtils.isNotEmpty(value)) {
					KeyValuePair<TelemetryItemType, String> pair = TelemetryItemType.parse(name);
					if (pair != null) {
						String sysKey = pair.getValue();
						if (pair.getKey().isSystem()) {
							if (StringUtils.equals(sysKey, KronosConstants.Telemetry.DEVICE_HID)) {
								if (StringUtils.isEmpty(hid)) {
									hid = value;
								} else {
									logInfo(method, "ignored multiple values of deviceHid");
								}
							} else if (StringUtils.equals(sysKey, KronosConstants.Telemetry.TIMESTAMP)) {
								try {
									timestamp = Long.parseLong(value);
								} catch (Exception e) {
									logError(method, "Invalid timestamp value found: %s", value);
								}
							} else {
								logInfo(method, "ignored unknown system parameter: %s / %s", name, value);
							}
						} else {
							TelemetryItem item = TelemetryItemConverter.toData(pair.getKey(), value);
							if (item != null) {
								item.setName(sysKey);
								itemList.add(item);
							}
						}
					} else {
						logInfo(method, "ignored invalid name/value: %s/%s", name, value);
					}
				} else {
					logInfo(method, "ignored empty value for %s", name);
				}
			}
			if (itemList.size() > 0) {
				String error = null;
				if (StringUtils.isEmpty(hid)) {
					error = "ERROR: deviceHid is missing, telemetry is ignored";
				} else {
					Device device = getKronosCache().findDeviceByHid(hid);
					if (device == null) {
						error = "ERROR: invalid deviceHid " + hid;
					} else if (!device.isEnabled()) {
						error = "ERROR: device is not active";
					} else {
						for (TelemetryItem item : itemList) {
							item.setDeviceId(device.getId());
							item.setTimestamp(timestamp);
						}

						Application app = getCoreCacheService().findApplicationById(device.getApplicationId());
						Gateway gateway = getKronosCache().findGatewayById(device.getGatewayId());
						checkEnabled(app, "application");
						String kafkaTopic = KronosConstants.Telemetry.kafkaTopic(app.getApplicationEngineId());
						String payload = JsonUtils.toJson(itemList);
						String key = crypto.randomToken();
						logInfo(method, "sending %d items to kafkaTopic: %s with key: %s", itemList.size(), kafkaTopic,
								key);
						logInfo(method, "applicationHid: %s, gatewayHid: %s, deviceHid: %s", app.getHid(),
								gateway.getHid(), device.getHid());
						kafkaSender.send(kafkaTopic, key, payload);

						// live telemetry streaming
						KronosApplication ka = getKronosCache()
								.findKronosApplicationByApplicationId(device.getApplicationId());
						if (ka != null && ka.isLiveTelemetryStreamingEnabled()) {
							List<TelemetryItemModel> models = new ArrayList<>();
							itemList.forEach(item -> {
								models.add(TelemetryItemConverter.toModel(item, device));
							});
							String routingKey = MqttConstants.applicationTelemetryRouting(app.getHid());
							logInfo(method, "streaming telemetry to routingKey: %s", routingKey);
							logInfo(method, "applicationHid: %s, gatewayHid: %s, deviceHid: %s", app.getHid(),
									gateway.getHid(), device.getHid());
							rabbit.convertAndSend(MqttConstants.APPLICATION_TELEMETRY_EXCHANGE, routingKey,
									JsonUtils.toJsonBytes(models),
									new TtlMessagePostProcessor(ka.getLiveTelemetryStreamingRetentionSecs()));
						}
					}
				}
				if (StringUtils.isNotEmpty(error)) {
					logError(method, error);
					errors.add(error);
				}
			} else {
				logInfo(method, "ignored empty dataset");
			}
		} catch (Exception e) {
		}
		return errors;
	}
}
