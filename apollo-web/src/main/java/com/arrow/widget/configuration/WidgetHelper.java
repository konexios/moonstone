package com.arrow.widget.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.arrow.kronos.data.DeviceTelemetry;
import com.arrow.kronos.data.DeviceType;

public class WidgetHelper {

	public static Map<String, String> populateDeviceTelemetryNameDescriptionMap(Map<String, String> map,
	        DeviceType deviceType) {
		Assert.notNull(deviceType, "deviceType is null");

		if (map == null)
			map = new HashMap<>();

		for (DeviceTelemetry deviceTelemetry : deviceType.getTelemetries()) {
			if (!StringUtils.isEmpty(deviceTelemetry.getDescription()))
				map.put(deviceTelemetry.getName(), deviceTelemetry.getDescription());
		}

		return map;
	}

	public static String telemetryNameToDescription(Map<String, String> map, String name) {
		if (map == null)
			return name;

		if (map.containsKey(name))
			name = map.get(name);

		return name;
	}
}
