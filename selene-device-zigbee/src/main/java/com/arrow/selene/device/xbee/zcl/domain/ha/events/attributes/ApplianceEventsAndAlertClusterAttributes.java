package com.arrow.selene.device.xbee.zcl.domain.ha.events.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class ApplianceEventsAndAlertClusterAttributes {
	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();
}
