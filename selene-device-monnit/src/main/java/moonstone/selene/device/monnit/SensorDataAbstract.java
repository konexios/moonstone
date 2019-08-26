package moonstone.selene.device.monnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.monnit.mine.BaseApplication.Datum;
import com.monnit.mine.MonnitMineAPI.SensorMessage;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;
import moonstone.selene.engine.DeviceDataAbstract;
import moonstone.selene.engine.EngineConstants;

public abstract class SensorDataAbstract extends DeviceDataAbstract {
    private SensorMessage sensorMessage;

    public SensorDataAbstract(SensorMessage sensorMessage) {
        this.sensorMessage = sensorMessage;
        setTimestamp(sensorMessage.getMessageDate().toInstant().toEpochMilli());
    }

    @Override
    public IotParameters writeIoTParameters() {
        String method = "writeIoTParameters";
        IotParameters result = new IotParameters();
        result.put(TelemetryItemType.System.buildName("timestamp"),
                Long.toString(sensorMessage.getMessageDate().toInstant().toEpochMilli()));
        result.setInteger("signalStrength", sensorMessage.getSignalStrength());
        result.setDouble("voltage", sensorMessage.getVoltage(), EngineConstants.FORMAT_DECIMAL_2);
        for (Datum entry : sensorMessage.getData()) {
            if (entry.Data instanceof Byte) {
                result.setInteger(entry.Description.toLowerCase(Locale.getDefault()),
                        Integer.valueOf((Byte) entry.Data));
            } else if (entry.Data instanceof Integer) {
                result.setInteger(entry.Description.toLowerCase(Locale.getDefault()), (Integer) entry.Data);
            } else if (entry.Data instanceof Double) {
                result.setDouble(entry.Description.toLowerCase(Locale.getDefault()), (Double) entry.Data,
                        EngineConstants.FORMAT_DECIMAL_2);
            } else if (entry.Data instanceof Boolean) {
                result.setBoolean(entry.Description.toLowerCase(Locale.getDefault()), (Boolean) entry.Data);
            } else {
                logWarn(method, "data type %s is not supported, reporting as string", entry.Data.getClass());
                result.setString(entry.Description.toLowerCase(Locale.getDefault()), entry.Data.toString());
            }
        }
        return result;
    }

    @Override
    public List<Telemetry> writeTelemetries() {
        String method = "writeTelemetries";
        List<Telemetry> result = new ArrayList<>(sensorMessage.getData().size());
        result.add(writeIntTelemetry("signalStrength", Long.valueOf(sensorMessage.getSignalStrength())));
        result.add(writeFloatTelemetry("voltage", sensorMessage.getVoltage()));
        for (Datum entry : sensorMessage.getData()) {
            if (entry.Data instanceof Byte) {
                result.add(writeIntTelemetry(entry.Description.toLowerCase(Locale.getDefault()),
                        Long.valueOf((Byte) entry.Data)));
            } else if (entry.Data instanceof Integer) {
                result.add(writeIntTelemetry(entry.Description.toLowerCase(Locale.getDefault()),
                        Long.valueOf((Integer) entry.Data)));
            } else if (entry.Data instanceof Double) {
                result.add(
                        writeFloatTelemetry(entry.Description.toLowerCase(Locale.getDefault()), (Double) entry.Data));
            } else if (entry.Data instanceof Boolean) {
                result.add(writeBooleanTelemetry(entry.Description.toLowerCase(Locale.getDefault()),
                        (Boolean) entry.Data));
            } else {
                logWarn(method, "data type %s is not supported", entry.Data.getClass());
                result.add(writeStringTelemetry(TelemetryItemType.String,
                        entry.Description.toLowerCase(Locale.getDefault()), entry.Data.toString()));
            }
        }
        return result;
    }

	@Override
	public String toString() {
		return "SensorDataAbstract [sensorMessage=" + sensorMessage + "]";
	}
}
