package moonstone.selene.device.xbee.zcl.domain.se.demand.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.demand.data.CriticallyLevel;
import moonstone.selene.device.xbee.zcl.domain.se.demand.data.EventControl;
import moonstone.selene.device.xbee.zcl.domain.se.demand.data.EventStatus;
import moonstone.selene.device.xbee.zcl.domain.se.demand.data.SignatureType;

public class ReportEventStatus extends ClusterSpecificCommand<ReportEventStatus> {
	private long issuerEventId;
	private EventStatus eventStatus;
	private long eventStatusTime;
	private CriticallyLevel criticallyLevelApplied;
	private int coolingTemperatureSetpointApplied;
	private int heatingTemperatureSetpointApplied;
	private int averageLoadAdjustmentPercentage;
	private int dutyCycleApplied;
	private Set<EventControl> eventControls;
	private SignatureType signatureType;
	private byte[] signature;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public ReportEventStatus withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public ReportEventStatus withEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
		return this;
	}

	public long getEventStatusTime() {
		return eventStatusTime;
	}

	public ReportEventStatus withEventStatusTime(long eventStatusTime) {
		this.eventStatusTime = eventStatusTime;
		return this;
	}

	public CriticallyLevel getCriticallyLevelApplied() {
		return criticallyLevelApplied;
	}

	public ReportEventStatus withCriticallyLevelApplied(CriticallyLevel criticallyLevelApplied) {
		this.criticallyLevelApplied = criticallyLevelApplied;
		return this;
	}

	public int getCoolingTemperatureSetpointApplied() {
		return coolingTemperatureSetpointApplied;
	}

	public ReportEventStatus withCoolingTemperatureSetpointApplied(int coolingTemperatureSetpointApplied) {
		this.coolingTemperatureSetpointApplied = coolingTemperatureSetpointApplied;
		return this;
	}

	public int getHeatingTemperatureSetpointApplied() {
		return heatingTemperatureSetpointApplied;
	}

	public ReportEventStatus withHeatingTemperatureSetpointApplied(int heatingTemperatureSetpointApplied) {
		this.heatingTemperatureSetpointApplied = heatingTemperatureSetpointApplied;
		return this;
	}

	public int getAverageLoadAdjustmentPercentage() {
		return averageLoadAdjustmentPercentage;
	}

	public ReportEventStatus withAverageLoadAdjustmentPercentage(int averageLoadAdjustmentPercentage) {
		this.averageLoadAdjustmentPercentage = averageLoadAdjustmentPercentage;
		return this;
	}

	public int getDutyCycleApplied() {
		return dutyCycleApplied;
	}

	public ReportEventStatus withDutyCycleApplied(int dutyCycleApplied) {
		this.dutyCycleApplied = dutyCycleApplied;
		return this;
	}

	public Set<EventControl> getEventControls() {
		return eventControls;
	}

	public ReportEventStatus withEventControls(Set<EventControl> eventControls) {
		this.eventControls = eventControls;
		return this;
	}

	public SignatureType getSignatureType() {
		return signatureType;
	}

	public ReportEventStatus withSignatureType(SignatureType signatureType) {
		this.signatureType = signatureType;
		return this;
	}

	public byte[] getSignature() {
		return signature;
	}

	public ReportEventStatus withSignature(byte[] signature) {
		this.signature = signature;
		return this;
	}

	@Override
	protected int getId() {
		return DemandResponseAndLoadControlClusterCommands.REPORT_EVENT_STATUS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(60);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.put((byte) eventStatus.getValue());
		buffer.putInt((int) eventStatusTime);
		buffer.put((byte) criticallyLevelApplied.ordinal());
		buffer.putShort((short) coolingTemperatureSetpointApplied);
		buffer.putShort((short) heatingTemperatureSetpointApplied);
		buffer.put((byte) averageLoadAdjustmentPercentage);
		buffer.put((byte) dutyCycleApplied);
		buffer.put((byte) EventControl.getValue(eventControls));
		buffer.put((byte) signatureType.ordinal());
		buffer.put(signature);
		return buffer.array();
	}

	@Override
	public ReportEventStatus fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 60, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		eventStatus = EventStatus.getByValue(Byte.toUnsignedInt(buffer.get()));
		eventStatusTime = Integer.toUnsignedLong(buffer.getInt());
		criticallyLevelApplied = CriticallyLevel.values()[Byte.toUnsignedInt(buffer.get())];
		coolingTemperatureSetpointApplied = Short.toUnsignedInt(buffer.getShort());
		heatingTemperatureSetpointApplied = Short.toUnsignedInt(buffer.getShort());
		averageLoadAdjustmentPercentage = Byte.toUnsignedInt(buffer.get());
		dutyCycleApplied = Byte.toUnsignedInt(buffer.get());
		eventControls = EventControl.getByValue(buffer.get());
		signatureType = SignatureType.values()[Byte.toUnsignedInt(buffer.get())];
		signature = new byte[42];
		buffer.get(signature);
		return this;
	}
}
