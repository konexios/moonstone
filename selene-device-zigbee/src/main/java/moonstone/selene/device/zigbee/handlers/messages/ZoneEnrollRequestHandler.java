package moonstone.selene.device.zigbee.handlers.messages;

import java.util.Collections;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.digi.xbee.api.models.ExplicitXBeeMessage;
import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zcl.ApplicationProfiles;
import moonstone.selene.device.xbee.zcl.ZclClusters;
import moonstone.selene.device.xbee.zcl.ZclDataType;
import moonstone.selene.device.xbee.zcl.ZclStatus;
import moonstone.selene.device.xbee.zcl.data.AttributeRecord;
import moonstone.selene.device.xbee.zcl.domain.security.zone.attributes.SecurityZoneClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.security.zone.attributes.ZoneType;
import moonstone.selene.device.xbee.zcl.domain.security.zone.commands.ZoneEnrollRequest;
import moonstone.selene.device.xbee.zcl.domain.security.zone.commands.ZoneEnrollResponse;
import moonstone.selene.device.xbee.zcl.general.HaProfileCommands;
import moonstone.selene.device.xbee.zcl.general.WriteAttributes;
import moonstone.selene.device.zigbee.MessageInfo;
import moonstone.selene.device.zigbee.MessageType;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;
import moonstone.selene.device.zigbee.ZigBeeEndDeviceModule;

public class ZoneEnrollRequestHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule coordinator) {
		String method = "handle";
		ZoneEnrollRequest request = new ZoneEnrollRequest().extract(message.getData());
		ZigBeeEndDeviceModule module = coordinator.getModule(message.getDevice().get64BitAddress().toString());
		logInfo(method, "device: %s requests enroll for zone type: %s", module.getInfo().getAddress(),
		        ZoneType.getByValue(request.getZoneType()));
		logInfo(method, "allowing enroll");
		coordinator.sendMessage(message.getDevice().get64BitAddress(), message.getSourceEndpoint(),
		        ZclClusters.IAS_ZONE, ApplicationProfiles.HOME_AUTOMATION_PROFILE,
		        new ZoneEnrollResponse().withEnrollResponseCode(ZclStatus.SUCCESS).withZoneId(0)
		                .build(request.getHeader().getSequence(), ZclClusters.IAS_ZONE));
		AttributeRecord record = new AttributeRecord();
		record.setAttributeDataType(ZclDataType.DATA_IEEE_ADDR);
		record.setAttributeId(SecurityZoneClusterAttributes.IAS_CIE_ADDRESS_ATTRIBUTE_ID);
		try {
			record.setRawValue(
			        ByteUtils.swapByteArray(Hex.decodeHex(coordinator.getInfo().getAddress().toCharArray())));
			logInfo(method, "writing coordinator address as CIE");
			byte sequence = coordinator.nextSequence();
			coordinator.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE,
			        module.getInfo().getAddress(), message.getSourceEndpoint(), ZclClusters.IAS_ZONE,
			        new WriteAttributes(request.getManufacturerCode(), Collections.singletonList(record))
			                .build(sequence, ZclClusters.IAS_ZONE),
			        "ZoneEnrollRequestHandler", HaProfileCommands.WRITE_ATTRIBUTES_RSP));
		} catch (DecoderException ignore) {
			logError(method, "coordinator address is incorrect", ignore);
		}
	}
}
