package com.arrow.kronos.api;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.KeyValuePair;
import com.arrow.kronos.KronosApiConstants;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.service.KronosServiceAbstract;

@Component
public class DeviceTelemetryProcessor extends KronosServiceAbstract {

	@Autowired
	private ConnectionFactory connectionFactory;

	private RabbitAdmin rabbitAdmin;
	private ConcurrentHashMap<String, Long> devices = new ConcurrentHashMap<>();

	@Override
	protected void postConstruct() {
		super.postConstruct();
		rabbitAdmin = new RabbitAdmin(connectionFactory);
		TopicExchange exchange = new TopicExchange(KronosApiConstants.DEVICE_TELEMETRY_EXCHANGE, true, false);
		rabbitAdmin.declareExchange(exchange);
	}

	public void process(Map<String, String> model) {
		String method = "process";
		final List<TelemetryItem> itemList = new ArrayList<>();
		if (model == null) {
			return;
		}
		long timestamp = Instant.now().toEpochMilli();
		String hid = null;
		for (String name : model.keySet()) {
			String value = model.get(name);
			if (StringUtils.isEmpty(value)) {
				logInfo(method, "ignored empty value for %s", name);
			} else {
				KeyValuePair<TelemetryItemType, String> pair = TelemetryItemType.parse(name);
				if (pair == null) {
					logInfo(method, "ignored invalid name/value: %s/%s", name, value);
				} else {
					String sysKey = pair.getValue();
					if (pair.getKey().isSystem()) {
						switch (sysKey) {
						case KronosConstants.Telemetry.DEVICE_HID:
							if (StringUtils.isEmpty(hid) && devices.containsKey(value)
							        && devices.get(value).intValue() > 0) {
								hid = value;
							}
							break;
						case KronosConstants.Telemetry.TIMESTAMP:
							try {
								timestamp = Long.parseLong(value);
							} catch (Exception e) {
								logError(method, "Invalid timestamp value found: %s", value);
							}
							break;
						default:
							logDebug(method, "ignored unknown system parameter: %s / %s", name, value);
						}
					} else {
						TelemetryItem item = convert(pair.getKey(), value);
						if (item != null) {
							item.setName(sysKey);
							itemList.add(item);
						}
					}
				}
			}
		}
		if (itemList.size() > 0) {
			if (StringUtils.isEmpty(hid)) {
				logInfo(method, "WARN: deviceHid is missing, telemetry is ignored");
			} else {
				Device device = getKronosCache().findDeviceByHid(hid);
				if (device == null) {
					logInfo(method, "ERROR: invalid deviceHid %s", hid);
				} else if (!device.isEnabled()) {
					logInfo(method, "ERROR: device is not active");
				} else {
					for (TelemetryItem item : itemList) {
						item.setTimestamp(timestamp);
						item.setHid(device.getHid());
						rabbitAdmin.getRabbitTemplate().convertAndSend(KronosApiConstants.DEVICE_TELEMETRY_EXCHANGE,
						        KronosApiConstants.deviceTelemetryRouting(device.getHid(), item.getName()),
						        MessageBuilder.withBody(JsonUtils.toJsonBytes(item)).build());
					}
				}
			}
		}
	}

	public void addDevice(String deviceHid) {
		if (deviceHid == null) {
			return;
		}
		devices.computeIfAbsent(deviceHid, v -> new Long(0));
		devices.computeIfPresent(deviceHid, (k, v) -> {
			v++;
			return v;
		});
		logDebug("addDevice", "%s %s", deviceHid, devices);
	}

	public void removeDevice(String deviceHid) {
		if (deviceHid == null) {
			return;
		}
		devices.computeIfPresent(deviceHid, (k, v) -> {
			if (v > 0) {
				v--;
			}
			return v;
		});
		devices.remove(deviceHid, new Long(0));
		logDebug("removeDevice", "%s %s", deviceHid, devices);
	}

	private TelemetryItem convert(TelemetryItemType type, String value) {
		String method = "convert";
		TelemetryItem result = new TelemetryItem();
		result.setType(type);
		try {
			switch (type) {
			case Boolean:
				result.setBoolValue(Boolean.parseBoolean(value));
				break;
			case Date:
				result.setDateValue(LocalDate.parse(value));
				break;
			case DateTime:
				result.setDatetimeValue(LocalDateTime.parse(value));
				break;
			case Float:
				result.setFloatValue(Double.parseDouble(value));
				break;
			case FloatCube:
				result.setFloatCubeValue(value);
				break;
			case FloatSquare:
				result.setFloatSqrValue(value);
				break;
			case Integer:
				result.setIntValue(Long.parseLong(value));
				break;
			case IntegerCube:
				result.setIntCubeValue(value);
				break;
			case IntegerSquare:
				result.setIntSqrValue(value);
				break;
			case String:
				result.setStrValue(value);
				break;
			case System:
				result.setStrValue(value);
				break;
			case Binary:
				result.setBinaryValue(value);
				break;
			default:
				throw new AcsLogicalException("unsupported type: " + type);
			}
		} catch (Exception e) {
			logError(method, "ERROR converting type: %s, value: %s, error: %s", type, value, e.getMessage());
			result = null;
		}
		return result;
	}
}
