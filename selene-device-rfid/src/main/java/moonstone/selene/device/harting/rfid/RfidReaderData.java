package moonstone.selene.device.harting.rfid;

import java.util.ArrayList;
import java.util.List;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;
import moonstone.selene.device.harting.rfid.command.ReaderDiagnosticResponse;
import moonstone.selene.device.harting.rfid.command.ReaderDiagnosticResponse.Mode1;
import moonstone.selene.device.harting.rfid.command.ReaderDiagnosticResponse.Mode4;
import moonstone.selene.engine.DeviceDataAbstract;

public class RfidReaderData extends DeviceDataAbstract {
	private Mode1 mode1;
	private Mode4 mode4;
	private String message;

	public RfidReaderData(ReaderDiagnosticResponse response) {
		mode1 = response.getMode1();
		mode4 = response.getMode4();
		message = response.getMessage();
	}

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters result = new IotParameters();
		if (mode1 != null) {
			result.setBoolean("temperatureAlarm", mode1.getFlagsA().isTemperatureAlarm());
			result.setBoolean("temperatureWarning", mode1.getFlagsA().isTemperatureWarning());
			result.setBoolean("rfPowerOutOfRange", mode1.getFlagsA().isRfPowerOutOfRange());
			result.setBoolean("noise", mode1.getFlagsA().isNoise());
			result.setBoolean("checkAntenna1", mode1.getFlagsB().isCheckAntenna1());
			result.setBoolean("checkAntenna2", mode1.getFlagsB().isCheckAntenna2());
			result.setBoolean("checkAntenna3", mode1.getFlagsB().isCheckAntenna3());
			result.setBoolean("checkAntenna4", mode1.getFlagsB().isCheckAntenna4());
		}
		if (mode4 != null) {
			result.setBoolean("usbError", mode4.isUsbError());
			result.setBoolean("dcOutError", mode4.isDcOutError());
			result.setBoolean("ioExpanderError", mode4.isIoExpanderError());
			result.setBoolean("adcError", mode4.isAdcError());
			result.setBoolean("rtcError", mode4.isRtcError());
			result.setBoolean("rfDecoderError", mode4.isRfDecodercError());
			result.setBoolean("eepromError", mode4.isEepromError());
		}
		if (message != null) {
			result.setString("message", message);
		}
		return result;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		List<Telemetry> result = new ArrayList<>();
		if (mode1 != null) {
			result.add(writeBooleanTelemetry("temperatureAlarm", mode1.getFlagsA().isTemperatureAlarm()));
			result.add(writeBooleanTelemetry("temperatureWarning", mode1.getFlagsA().isTemperatureWarning()));
			result.add(writeBooleanTelemetry("rfPowerOutOfRange", mode1.getFlagsA().isRfPowerOutOfRange()));
			result.add(writeBooleanTelemetry("noise", mode1.getFlagsA().isNoise()));
			result.add(writeBooleanTelemetry("checkAntenna1", mode1.getFlagsB().isCheckAntenna1()));
			result.add(writeBooleanTelemetry("checkAntenna2", mode1.getFlagsB().isCheckAntenna2()));
			result.add(writeBooleanTelemetry("checkAntenna3", mode1.getFlagsB().isCheckAntenna3()));
			result.add(writeBooleanTelemetry("checkAntenna4", mode1.getFlagsB().isCheckAntenna4()));
		}
		if (mode4 != null) {
			result.add(writeBooleanTelemetry("usbError", mode4.isUsbError()));
			result.add(writeBooleanTelemetry("dcOutError", mode4.isDcOutError()));
			result.add(writeBooleanTelemetry("ioExpanderError", mode4.isIoExpanderError()));
			result.add(writeBooleanTelemetry("adcError", mode4.isAdcError()));
			result.add(writeBooleanTelemetry("rtcError", mode4.isRtcError()));
			result.add(writeBooleanTelemetry("rfDecoderError", mode4.isRfDecodercError()));
			result.add(writeBooleanTelemetry("eepromError", mode4.isEepromError()));
		}
		if (message != null) {
			result.add(writeStringTelemetry(TelemetryItemType.String, "message", message));
		}
		return result;
	}
}
