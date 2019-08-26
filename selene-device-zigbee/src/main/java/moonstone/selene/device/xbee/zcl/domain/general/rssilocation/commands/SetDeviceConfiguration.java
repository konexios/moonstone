package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class SetDeviceConfiguration extends ClusterSpecificCommand<SetDeviceConfiguration> {
	private short power;
	private short pathLossExponent;
	private short calcPeriod;
	private byte numberRssiMeasurements;
	private short reportingPeriod;

	public short getPower() {
		return power;
	}

	public SetDeviceConfiguration withPower(short power) {
		this.power = power;
		return this;
	}

	public short getPathLossExponent() {
		return pathLossExponent;
	}

	public SetDeviceConfiguration withPathLossExponent(short pathLossExponent) {
		this.pathLossExponent = pathLossExponent;
		return this;
	}

	public short getCalcPeriod() {
		return calcPeriod;
	}

	public SetDeviceConfiguration withCalcPeriod(short calcPeriod) {
		this.calcPeriod = calcPeriod;
		return this;
	}

	public byte getNumberRssiMeasurements() {
		return numberRssiMeasurements;
	}

	public SetDeviceConfiguration withNumberRssiMeasurements(byte numberRssiMeasurements) {
		this.numberRssiMeasurements = numberRssiMeasurements;
		return this;
	}

	public short getReportingPeriod() {
		return reportingPeriod;
	}

	public SetDeviceConfiguration withReportingPeriod(short reportingPeriod) {
		this.reportingPeriod = reportingPeriod;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.SET_DEVICE_CONFIGURATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(9);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort(power);
		buffer.putShort(pathLossExponent);
		buffer.putShort(calcPeriod);
		buffer.put(numberRssiMeasurements);
		buffer.putShort(reportingPeriod);
		return buffer.array();
	}

	@Override
	public SetDeviceConfiguration fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		power = buffer.getShort();
		pathLossExponent = buffer.getShort();
		calcPeriod = buffer.getShort();
		numberRssiMeasurements = buffer.get();
		reportingPeriod = buffer.getShort();
		return this;
	}
}
