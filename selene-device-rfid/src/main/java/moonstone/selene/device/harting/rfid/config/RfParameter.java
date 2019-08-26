package moonstone.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import moonstone.selene.device.harting.rfid.config.RfInterface.Level;

public class RfParameter implements ConfigParameter<RfParameter> {
	private static final long serialVersionUID = 3893743960601042522L;

	private static final int ID = 20;

	private static final String AIR_INTERFACE_ANTENNA_UHF_NO1_RSSI_FILTER = "AirInterface_Antenna_UHF_No1_RSSIFilter";
	private static final String AIR_INTERFACE_ANTENNA_UHF_NO2_OUTPUT_POWER =
			"AirInterface_Antenna_UHF_No2_OutputPower";
	private static final String AIR_INTERFACE_ANTENNA_UHF_NO2_RSSI_FILTER = "AirInterface_Antenna_UHF_No2_RSSIFilter";
	private static final String AIR_INTERFACE_ANTENNA_UHF_NO3_OUTPUT_POWER =
			"AirInterface_Antenna_UHF_No3_OutputPower";
	private static final String AIR_INTERFACE_ANTENNA_UHF_NO3_RSSI_FILTER = "AirInterface_Antenna_UHF_No3_RSSIFilter";
	private static final String AIR_INTERFACE_ANTENNA_UHF_NO4_OUTPUT_POWER =
			"AirInterface_Antenna_UHF_No4_OutputPower";
	private static final String AIR_INTERFACE_ANTENNA_UHF_NO4_RSSI_FILTER = "AirInterface_Antenna_UHF_No4_RSSIFilter";

	private int rssiFilter1;
	private int rssiFilter2;
	private int rssiFilter3;
	private int rssiFilter4;
	private Level rfPowerAntenna2;
	private Level rfPowerAntenna3;
	private Level rfPowerAntenna4;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(AIR_INTERFACE_ANTENNA_UHF_NO1_RSSI_FILTER);
		ALL.add(AIR_INTERFACE_ANTENNA_UHF_NO2_OUTPUT_POWER);
		ALL.add(AIR_INTERFACE_ANTENNA_UHF_NO2_RSSI_FILTER);
		ALL.add(AIR_INTERFACE_ANTENNA_UHF_NO3_OUTPUT_POWER);
		ALL.add(AIR_INTERFACE_ANTENNA_UHF_NO3_RSSI_FILTER);
		ALL.add(AIR_INTERFACE_ANTENNA_UHF_NO4_OUTPUT_POWER);
		ALL.add(AIR_INTERFACE_ANTENNA_UHF_NO4_RSSI_FILTER);
	}

	public RfParameter() {
	}

	public RfParameter(int rssiFilter1, int rssiFilter2, int rssiFilter3, int rssiFilter4, Level rfPowerAntenna2,
	                   Level rfPowerAntenna3, Level rfPowerAntenna4) {
		this.rssiFilter1 = rssiFilter1;
		this.rssiFilter2 = rssiFilter2;
		this.rssiFilter3 = rssiFilter3;
		this.rssiFilter4 = rssiFilter4;
		this.rfPowerAntenna2 = rfPowerAntenna2;
		this.rfPowerAntenna3 = rfPowerAntenna3;
		this.rfPowerAntenna4 = rfPowerAntenna4;
	}

	public RfParameter withRssiFilter1(int rssiFilter1) {
		this.rssiFilter1 = rssiFilter1;
		return this;
	}

	public RfParameter withRssiFilter2(int rssiFilter2) {
		this.rssiFilter2 = rssiFilter2;
		return this;
	}

	public RfParameter withRssiFilter3(int rssiFilter3) {
		this.rssiFilter3 = rssiFilter3;
		return this;
	}

	public RfParameter withRssiFilter4(int rssiFilter4) {
		this.rssiFilter4 = rssiFilter4;
		return this;
	}

	public RfParameter withRfPowerAntenna2(Level rfPowerAntenna2) {
		this.rfPowerAntenna2 = rfPowerAntenna2;
		return this;
	}

	public RfParameter withRfPowerAntenna3(Level rfPowerAntenna3) {
		this.rfPowerAntenna3 = rfPowerAntenna3;
		return this;
	}

	public RfParameter withRfPowerAntenna4(Level rfPowerAntenna4) {
		this.rfPowerAntenna4 = rfPowerAntenna4;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(0, (byte) rssiFilter1);
		buffer.put(1, (byte) rssiFilter2);
		buffer.put(2, (byte) rssiFilter3);
		buffer.put(3, (byte) rssiFilter4);
		buffer.put(10, (byte) Level.build(rfPowerAntenna2));
		buffer.put(11, (byte) Level.build(rfPowerAntenna3));
		buffer.put(12, (byte) Level.build(rfPowerAntenna4));
		return buffer.array();
	}

	@Override
	public RfParameter parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int rssiFilter1 = Byte.toUnsignedInt(buffer.get(0));
		int rssiFilter2 = Byte.toUnsignedInt(buffer.get(1));
		int rssiFilter3 = Byte.toUnsignedInt(buffer.get(2));
		int rssiFilter4 = Byte.toUnsignedInt(buffer.get(3));
		Level rfPowerAntenna2 = Level.extract(buffer.get(10));
		Level rfPowerAntenna3 = Level.extract(buffer.get(11));
		Level rfPowerAntenna4 = Level.extract(buffer.get(12));
		return new RfParameter(rssiFilter1, rssiFilter2, rssiFilter3, rssiFilter4, rfPowerAntenna2, rfPowerAntenna3,
				rfPowerAntenna4);
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public boolean updateState(String name, String value) {
		RfParameter stored = SerializationUtils.clone(this);
		switch (name) {
			case AIR_INTERFACE_ANTENNA_UHF_NO1_RSSI_FILTER: {
				rssiFilter1 = Integer.parseInt(value);
				break;
			}
			case AIR_INTERFACE_ANTENNA_UHF_NO2_RSSI_FILTER: {
				rssiFilter2 = Integer.parseInt(value);
				break;
			}
			case AIR_INTERFACE_ANTENNA_UHF_NO3_RSSI_FILTER: {
				rssiFilter3 = Integer.parseInt(value);
				break;
			}
			case AIR_INTERFACE_ANTENNA_UHF_NO4_RSSI_FILTER: {
				rssiFilter4 = Integer.parseInt(value);
				break;
			}
			case AIR_INTERFACE_ANTENNA_UHF_NO2_OUTPUT_POWER: {
				rfPowerAntenna2 = Level.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case AIR_INTERFACE_ANTENNA_UHF_NO3_OUTPUT_POWER: {
				rfPowerAntenna3 = Level.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case AIR_INTERFACE_ANTENNA_UHF_NO4_OUTPUT_POWER: {
				rfPowerAntenna4 = Level.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(AIR_INTERFACE_ANTENNA_UHF_NO1_RSSI_FILTER, Integer.toString(rssiFilter1));
		result.put(AIR_INTERFACE_ANTENNA_UHF_NO2_OUTPUT_POWER, rfPowerAntenna2.name());
		result.put(AIR_INTERFACE_ANTENNA_UHF_NO2_RSSI_FILTER, Integer.toString(rssiFilter2));
		result.put(AIR_INTERFACE_ANTENNA_UHF_NO3_OUTPUT_POWER, rfPowerAntenna3.name());
		result.put(AIR_INTERFACE_ANTENNA_UHF_NO3_RSSI_FILTER, Integer.toString(rssiFilter3));
		result.put(AIR_INTERFACE_ANTENNA_UHF_NO4_OUTPUT_POWER, rfPowerAntenna4.name());
		result.put(AIR_INTERFACE_ANTENNA_UHF_NO4_RSSI_FILTER, Integer.toString(rssiFilter4));
		return result;
	}

	@Override
	public List<String> getParams() {
		return ALL;
	}
}
