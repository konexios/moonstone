package com.arrow.selene.device.zigbee.handlers.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.Loggable;
import com.arrow.selene.SeleneException;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.sensor.SensorDataImpl;
import com.arrow.selene.device.xbee.zcl.ZclClusterInfo;
import com.arrow.selene.device.xbee.zcl.ZclClusters;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.arrow.selene.device.xbee.zcl.data.AttributeRecord;
import com.arrow.selene.device.xbee.zcl.general.ReportAttributes;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceModule;
import com.digi.xbee.api.models.ExplicitXBeeMessage;

public class ReportAttributesHandler extends Loggable implements MessageHandler {
	private static final Pattern SPACE = Pattern.compile(" ", Pattern.LITERAL);

	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "ReportAttributesHandler.handle";
		ZclClusterInfo clusterInfo = ZclClusters.getCluster(message.getClusterID());
		if (clusterInfo == null) {
			logError(method, "unknown cluster Id: 0x%04x", message.getClusterID());
			return;
		}
		List<SensorData<?>> list = new ArrayList<>();
		for (AttributeRecord record : new ReportAttributes().extract(message.getData()).getAttributes()) {
			Integer attributeId = record.getAttributeId();
			ImmutablePair<String, Attribute<? extends SensorData<?>>> pair = clusterInfo.getAttributes()
			        .get(attributeId);
			if (pair == null) {
				logError(method, "attribute: 0x%04x not found in cluster: %s", attributeId, clusterInfo);
			} else {
				String name = SPACE.matcher(clusterInfo.getName() + '_' + pair.getLeft())
				        .replaceAll(Matcher.quoteReplacement(""));
				logInfo(method, "name: %s, value: %s", name, record.getValue());
				Attribute<? extends SensorData<?>> attribute = pair.getRight();
				if (attribute == null) {
					list.add(record.toData(name));
				} else {
					try {
						logInfo(method, "Attribute class: %s", attribute.getClass().getName());
						SensorData<?> data = attribute.toData(name, record.getRawValue());
						logInfo(method, "SensorData class: %s", data.getClass().getName());
						logInfo(method, "rawValue HEX: %s", Hex.encodeHexString(record.getRawValue()));
						list.add(data);
					} catch (SeleneException e) {
						logWarn(method, "invalid value");
					}
				}
			}
		}
		ZigBeeEndDeviceModule endDeviceModule = module.getModule(message.getDevice().get64BitAddress().toString());
		if (endDeviceModule == null) {
			logInfo(method, "message from unknown device: %s", message.getDevice().get64BitAddress());
		} else {
			endDeviceModule.publishTelemetry(new SensorDataImpl<>(list));
		}
	}
}
