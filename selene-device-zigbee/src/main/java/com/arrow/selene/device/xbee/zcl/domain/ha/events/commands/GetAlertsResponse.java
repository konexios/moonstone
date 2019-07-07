package com.arrow.selene.device.xbee.zcl.domain.ha.events.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.ha.events.data.AlertStructure;
import com.arrow.selene.device.xbee.zcl.domain.ha.events.data.AlertType;
import com.arrow.selene.device.xbee.zcl.domain.ha.events.data.Category;
import com.arrow.selene.device.xbee.zcl.domain.ha.events.data.PresenceRecovery;

public class GetAlertsResponse extends ClusterSpecificCommand<GetAlertsResponse> {
	private byte numberOfAlerts;
	private AlertType alertType;
	private AlertStructure[] alertStructures;

	public byte getNumberOfAlerts() {
		return numberOfAlerts;
	}

	public GetAlertsResponse withNumberOfAlerts(byte numberOfAlerts) {
		this.numberOfAlerts = numberOfAlerts;
		return this;
	}

	public AlertType getAlertType() {
		return alertType;
	}

	public GetAlertsResponse withAlertType(AlertType alertType) {
		this.alertType = alertType;
		return this;
	}

	public AlertStructure[] getAlertStructures() {
		return alertStructures;
	}

	public GetAlertsResponse withAlertStructures(AlertStructure[] alertStructures) {
		this.alertStructures = alertStructures;
		return this;
	}

	@Override
	protected int getId() {
		return ApplianceEventsAndAlertClusterCommands.GET_ALERTS_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(1 + alertStructures.length * 3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) (numberOfAlerts & 0x0f | alertType.ordinal() << 4));
		for (AlertStructure item : alertStructures) {
			buffer.put((byte) item.getAlertId());
			buffer.put((byte) (item.getCategory().ordinal() & 0x0f | item.getPresenceRecovery().ordinal() << 4));
			buffer.put(item.getProprietary());
		}
		return buffer.array();
	}

	@Override
	public GetAlertsResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 1, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		byte value = buffer.get();
		numberOfAlerts = (byte) (value & 0x0f);
		alertType = AlertType.values()[value >> 4 & 0x0f];
		alertStructures = new AlertStructure[buffer.remaining() / 3];
		for (int i = 0; i < alertStructures.length; i++) {
			int alertId = buffer.get();
			value = buffer.get();
			Category category = Category.values()[value & 0x0f];
			PresenceRecovery presenceRecovery = PresenceRecovery.values()[value >> 4 & 0x03];
			byte proprietary = buffer.get();
			alertStructures[i] = new AlertStructure().withAlertId(alertId).withCategory(category).withPresenceRecovery(
					presenceRecovery).withProprietary(proprietary);

		}
		return this;
	}
}
