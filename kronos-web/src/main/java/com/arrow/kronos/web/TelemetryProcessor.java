package com.arrow.kronos.web;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.KeyValuePair;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.service.KronosServiceAbstract;
import com.arrow.kronos.web.model.TelemetryItemModels;

@Component
public class TelemetryProcessor extends KronosServiceAbstract {

	@Autowired
	private SimpMessageSendingOperations messageTemplate;

	public void process(Map<String, String> model, Set<String> devices) {
		String method = "process";
		final List<TelemetryItem> itemList = new ArrayList<>();
		if (model == null || devices == null) {
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
							if (StringUtils.isEmpty(hid) && devices.contains(value)) {
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
					String destination = String.format(KronosWebConstants.DEVICE_DESTINATION_FORMAT, device.getId());
					messageTemplate.convertAndSend(destination, model);
					for (TelemetryItem item : itemList) {
						item.setTimestamp(timestamp);
						item.setDeviceId(device.getId());
						destination = String.format(KronosWebConstants.DEVICE_TELEMETRY_DESTINATION_FORMAT,
								device.getId(), item.getName());
						messageTemplate.convertAndSend(destination,
								new TelemetryItemModels.TelemetryItemChartModel(item));
					}
				}
			}
		}
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
