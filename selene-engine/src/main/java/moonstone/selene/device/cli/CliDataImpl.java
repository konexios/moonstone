package moonstone.selene.device.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;
import moonstone.selene.engine.DeviceDataAbstract;
import moonstone.selene.engine.EngineConstants;

public class CliDataImpl extends DeviceDataAbstract implements CliData {

	private String strData;
	private Long longData;
	private Double doubleData;

	private String[] strArrayData;
	private Long[] longArrayData;
	private Double[] doubleArrayData;

	private Map<String, String> rawTelemetries;

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters result = new IotParameters();
		if (strData != null) {
			result.setString("strVal", strData);
		} else if (longData != null) {
			result.setLong("intVal", longData);
		} else if (doubleData != null) {
			result.setDouble("floatVal", doubleData, EngineConstants.FORMAT_DECIMAL_2);
		} else if (strArrayData != null) {
			result.setLong("size", (long) strArrayData.length);
			for (int i = 0; i < strArrayData.length; i++) {
				result.setString("strVal-" + i, strArrayData[i]);
			}
		} else if (longArrayData != null) {
			result.setLong("size", (long) longArrayData.length);
			for (int i = 0; i < longArrayData.length; i++) {
				result.setLong("intVal-" + i, longArrayData[i]);
			}
		} else if (doubleArrayData != null) {
			result.setLong("size", (long) doubleArrayData.length);
			for (int i = 0; i < doubleArrayData.length; i++) {
				result.setDouble("floatVal-" + i, doubleArrayData[i], EngineConstants.FORMAT_DECIMAL_2);
			}
		} else if (rawTelemetries != null) {
			result.setDirty(true);
			result.putAll(rawTelemetries);
		}
		return result;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		List<Telemetry> result = new ArrayList<>();
		if (strData != null) {
			result.add(writeStringTelemetry(TelemetryItemType.String, "strVal", strData));
		} else if (longData != null) {
			result.add(writeIntTelemetry("intVal", longData));
		} else if (doubleData != null) {
			result.add(writeFloatTelemetry("floatVal", doubleData));
		} else if (strArrayData != null) {
			result.add(writeIntTelemetry("size", (long) strArrayData.length));
			for (int i = 0; i < strArrayData.length; i++) {
				result.add(writeStringTelemetry(TelemetryItemType.String, "strVal-" + i, strArrayData[i]));
			}
		} else if (longArrayData != null) {
			result.add(writeIntTelemetry("size", (long) longArrayData.length));
			for (int i = 0; i < longArrayData.length; i++) {
				result.add(writeIntTelemetry("intVal-" + i, longArrayData[i]));
			}
		} else if (doubleArrayData != null) {
			result.add(writeIntTelemetry("size", (long) doubleArrayData.length));
			for (int i = 0; i < doubleArrayData.length; i++) {
				result.add(writeFloatTelemetry("floatVal-" + i, doubleArrayData[i]));
			}
		} else if (rawTelemetries != null) {
			for (Entry<String, String> entry : rawTelemetries.entrySet()) {
				result.add(writeRawTelemetry(entry.getKey(), entry.getValue()));
			}
		}
		return result;
	}

	public String getStrData() {
		return strData;
	}

	public void setStrData(String strData) {
		this.strData = strData;
	}

	public Long getLongData() {
		return longData;
	}

	public void setLongData(Long longData) {
		this.longData = longData;
	}

	public Double getDoubleData() {
		return doubleData;
	}

	public void setDoubleData(Double doubleData) {
		this.doubleData = doubleData;
	}

	public String[] getStrArrayData() {
		return strArrayData;
	}

	public void setStrArrayData(String[] strArrayData) {
		this.strArrayData = strArrayData;
	}

	public Long[] getLongArrayData() {
		return longArrayData;
	}

	public void setLongArrayData(Long[] longArrayData) {
		this.longArrayData = longArrayData;
	}

	public Double[] getDoubleArrayData() {
		return doubleArrayData;
	}

	public void setDoubleArrayData(Double[] doubleArrayData) {
		this.doubleArrayData = doubleArrayData;
	}

	public Map<String, String> getRawTelemetries() {
		return rawTelemetries;
	}

	public void setRawTelemetries(Map<String, String> rawTelemetries) {
		this.rawTelemetries = rawTelemetries;
	}
}
