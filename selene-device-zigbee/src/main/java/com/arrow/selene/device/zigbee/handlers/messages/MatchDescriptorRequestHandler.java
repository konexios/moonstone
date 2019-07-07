package com.arrow.selene.device.zigbee.handlers.messages;

import java.util.Collections;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zcl.ApplicationProfiles;
import com.arrow.selene.device.xbee.zcl.ZclClusters;
import com.arrow.selene.device.xbee.zcl.ZclDataType;
import com.arrow.selene.device.xbee.zcl.ZclStatus;
import com.arrow.selene.device.xbee.zcl.data.AttributeRecord;
import com.arrow.selene.device.xbee.zcl.domain.security.zone.attributes.SecurityZoneClusterAttributes;
import com.arrow.selene.device.xbee.zcl.domain.security.zone.commands.ZoneEnrollResponse;
import com.arrow.selene.device.xbee.zcl.general.HaProfileCommands;
import com.arrow.selene.device.xbee.zcl.general.WriteAttributes;
import com.arrow.selene.device.xbee.zdo.MatchDescriptorRequest;
import com.arrow.selene.device.xbee.zdo.MatchDescriptorResponse;
import com.arrow.selene.device.xbee.zdo.ZdoCommands;
import com.arrow.selene.device.zigbee.MessageInfo;
import com.arrow.selene.device.zigbee.MessageType;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.digi.xbee.api.models.ExplicitXBeeMessage;
import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class MatchDescriptorRequestHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule coordinator) {
		String method = "MatchDescriptorRequestHandler.handle";
		MatchDescriptorRequest request = MatchDescriptorRequest.fromPayload(message.getData());

		StringBuilder sb = new StringBuilder();
		int[] inputClusters = request.getInputClusters();
		if (inputClusters != null && inputClusters.length > 0) {
			for (int i = 0; i < inputClusters.length; i++) {
				if (i > 0)
					sb.append(", ");
				sb.append(buildClusterName(inputClusters[i]));
			}
		}
		if (sb.length() > 0) {
			logInfo(method, "device: %s, request input clusters: %s", message.getDevice().get64BitAddress().toString(),
			        sb.toString());
		}

		sb.setLength(0);
		boolean hasIasZone = false;
		int[] outputClusters = request.getOutputClusters();
		if (outputClusters != null && outputClusters.length > 0) {
			for (int i = 0; i < outputClusters.length; i++) {
				if (i > 0)
					sb.append(", ");
				sb.append(buildClusterName(outputClusters[i]));
				hasIasZone = true;
			}
		}
		if (sb.length() > 0) {
			logInfo(method, "device: %s, request output clusters: %s", message.getDevice().get64BitAddress().toString(),
			        sb.toString());
		}

		// auto accept
		coordinator.sendZdoMessage(message.getDevice(), ZdoCommands.MATCH_DESCRIPTOR_RSP, MatchDescriptorResponse
		        .toPayload(request.getSequence(), ZclStatus.SUCCESS, XBee16BitAddress.COORDINATOR_ADDRESS, 0));

		// additional logic for IAS_ZONE
		if (hasIasZone) {
			logInfo(method, "allowing IAS_ZONE enrollment ...");
			coordinator.sendMessage(message.getDevice().get64BitAddress(), message.getSourceEndpoint(),
			        ZclClusters.IAS_ZONE, ApplicationProfiles.HOME_AUTOMATION_PROFILE,
			        new ZoneEnrollResponse().withEnrollResponseCode(ZclStatus.SUCCESS).withZoneId(0)
			                .build(coordinator.nextSequence(), ZclClusters.IAS_ZONE));
			AttributeRecord record = new AttributeRecord();
			record.setAttributeDataType(ZclDataType.DATA_IEEE_ADDR);
			record.setAttributeId(SecurityZoneClusterAttributes.IAS_CIE_ADDRESS_ATTRIBUTE_ID);
			try {
				record.setRawValue(
				        ByteUtils.swapByteArray(Hex.decodeHex(coordinator.getInfo().getAddress().toCharArray())));
				logInfo(method, "writing coordinator address as CIE");
				byte sequence = coordinator.nextSequence();
				coordinator.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE,
				        coordinator.getInfo().getAddress(), message.getSourceEndpoint(), ZclClusters.IAS_ZONE,
				        new WriteAttributes(coordinator.getInfo().getManufacturerCode(),
				                Collections.singletonList(record)).build(sequence, ZclClusters.IAS_ZONE),
				        "ZoneEnrollRequestHandler", HaProfileCommands.WRITE_ATTRIBUTES_RSP));
			} catch (DecoderException ignore) {
				logError(method, "coordinator address is incorrect", ignore);
			}
		}
	}

	private static String buildClusterName(int cluster) {
		return String.format("0x%04x (%s)", cluster, ZclClusters.getName(cluster));
	}
}
