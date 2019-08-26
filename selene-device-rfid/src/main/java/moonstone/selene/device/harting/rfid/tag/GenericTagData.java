package moonstone.selene.device.harting.rfid.tag;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.acs.JsonUtils;
import moonstone.selene.data.Telemetry;
import moonstone.selene.device.harting.rfid.command.ReadBufferResponse.Dataset;
import moonstone.selene.device.harting.rfid.command.ReadBufferResponse.Dataset.AntennaExtended;
import moonstone.selene.engine.DeviceDataAbstract;
import moonstone.selene.engine.EngineConstants;

public class GenericTagData extends DeviceDataAbstract implements TagData {
	Dataset dataset;

	public GenericTagData(Dataset dataset) {
		this.dataset = dataset;
	}

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters parameters = new IotParameters();
		parameters.setInteger("trType", dataset.getTrType());
		parameters.setInteger("iddt", dataset.getIddt());
		parameters.setString("id", Hex.encodeHexString(dataset.getId()));
		LocalTime time = dataset.getTime();
		parameters.setDateTime("time", LocalDateTime.now().withHour(time.getHour()).withMinute(time.getMinute())
				.withSecond(time.getSecond()).withNano(time.getNano()));
		parameters.setInteger("input", dataset.getInput());
		parameters.setInteger("state", dataset.getState());
		for (AntennaExtended item : dataset.getAntennas()) {
			String prefix = item.getAntennaSelector().name();
			parameters.setInteger(prefix + "_rssi", item.getRssi());
			parameters.setDouble(prefix + "_phaseAngle", item.getPhaseAngle(), EngineConstants.FORMAT_DECIMAL_2);
		}
		return parameters;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		List<Telemetry> telemetry = new ArrayList<>();
		telemetry.add(writeStringTelemetry(TelemetryItemType.String, "data", JsonUtils.toJson(dataset)));
		telemetry.add(writeIntTelemetry("trType", (long) dataset.getTrType()));
		telemetry.add(writeIntTelemetry("iddt", (long) dataset.getIddt()));
		telemetry.add(writeStringTelemetry(TelemetryItemType.String, "id", Hex.encodeHexString(dataset.getId())));
		LocalTime time = dataset.getTime();
		telemetry.add(writeDateTimeTelemetry(TelemetryItemType.DateTime, "time", LocalDateTime.now().withHour(
				time.getHour()).withMinute(time.getMinute()).withSecond(time.getSecond()).withNano(time.getNano())));
		telemetry.add(writeIntTelemetry("input", (long) dataset.getInput()));
		telemetry.add(writeIntTelemetry("state", (long) dataset.getState()));
		for (AntennaExtended item : dataset.getAntennas()) {
			String prefix = item.getAntennaSelector().name();
			telemetry.add(writeIntTelemetry(prefix + "_rssi", (long) item.getRssi()));
			telemetry.add(writeFloatTelemetry(prefix + "_phaseAngle", item.getPhaseAngle()));
		}
		telemetry.add(writeStringTelemetry(TelemetryItemType.String, "data", JsonUtils.toJson(dataset)));
		return telemetry;
	}
}
