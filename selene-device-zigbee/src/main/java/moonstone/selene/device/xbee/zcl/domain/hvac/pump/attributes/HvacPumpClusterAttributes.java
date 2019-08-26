package moonstone.selene.device.xbee.zcl.domain.hvac.pump.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;
import moonstone.selene.device.xbee.zcl.domain.closures.door.attributes.OperatingMode;

public class HvacPumpClusterAttributes {
	public static final int MAX_PRESSURE_ATTRIBUTE_ID = 0x0000;
	public static final int MAX_SPEED_ATTRIBUTE_ID = 0x0001;
	public static final int MAX_FLOW_ATTRIBUTE_ID = 0x0002;
	public static final int MIN_CONST_PRESSURE_ATTRIBUTE_ID = 0x0003;
	public static final int MAX_CONST_PRESSURE_ATTRIBUTE_ID = 0x0004;
	public static final int MIN_COMP_PRESSURE_ATTRIBUTE_ID = 0x0005;
	public static final int MAX_COMP_PRESSURE_ATTRIBUTE_ID = 0x0006;
	public static final int MIN_CONST_SPEED_ATTRIBUTE_ID = 0x0007;
	public static final int MAX_CONST_SPEED_ATTRIBUTE_ID = 0x0008;
	public static final int MIN_CONST_FLOW_ATTRIBUTE_ID = 0x0009;
	public static final int MAX_CONST_FLOW_ATTRIBUTE_ID = 0x000a;
	public static final int MIN_CONST_TEMP_ATTRIBUTE_ID = 0x000b;
	public static final int MAX_CONST_TEMP_ATTRIBUTE_ID = 0x000c;

	public static final int PUMP_STATUS_ATTRIBUTE_ID = 0x0010;
	public static final int EFFECTIVE_OPERATION_MODE_ATTRIBUTE_ID = 0x0011;
	public static final int EFFECTIVE_CONTROL_MODE_ATTRIBUTE_ID = 0x0012;
	public static final int CAPACITY_ATTRIBUTE_ID = 0x0013;
	public static final int SPEED_ATTRIBUTE_ID = 0x0014;
	public static final int LIFETIME_RUNNING_HOURS_ATTRIBUTE_ID = 0x0015;
	public static final int POWER_ATTRIBUTE_ID = 0x0016;
	public static final int LIFETIME_ENERGY_CONSUMED_ATTRIBUTE_ID = 0x0017;

	public static final int OPERATION_MODE_ATTRIBUTE_ID = 0x0020;
	public static final int CONTROL_MODE_ATTRIBUTE_ID = 0x0021;
	public static final int ALARM_MASK_ATTRIBUTE_ID = 0x0022;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(MAX_PRESSURE_ATTRIBUTE_ID, new ImmutablePair<>("Max Pressure", new Pressure()));
		ALL.put(MAX_SPEED_ATTRIBUTE_ID, new ImmutablePair<>("Max Speed", null));
		ALL.put(MAX_FLOW_ATTRIBUTE_ID, new ImmutablePair<>("Max Flow", new Flow()));
		ALL.put(MIN_CONST_PRESSURE_ATTRIBUTE_ID, new ImmutablePair<>("Min Const Pressure", new Pressure()));
		ALL.put(MAX_CONST_PRESSURE_ATTRIBUTE_ID, new ImmutablePair<>("Max Const Pressure", new Pressure()));
		ALL.put(MIN_COMP_PRESSURE_ATTRIBUTE_ID, new ImmutablePair<>("Min Comp Pressure", new Pressure()));
		ALL.put(MAX_COMP_PRESSURE_ATTRIBUTE_ID, new ImmutablePair<>("Max Comp Pressure", new Pressure()));
		ALL.put(MIN_CONST_SPEED_ATTRIBUTE_ID, new ImmutablePair<>("Min Const Speed", null));
		ALL.put(MAX_CONST_SPEED_ATTRIBUTE_ID, new ImmutablePair<>("Max Const Speed", null));
		ALL.put(MIN_CONST_FLOW_ATTRIBUTE_ID, new ImmutablePair<>("Min Const Flow", new Flow()));
		ALL.put(MAX_CONST_FLOW_ATTRIBUTE_ID, new ImmutablePair<>("Max Const Flow", new Flow()));
		ALL.put(MIN_CONST_TEMP_ATTRIBUTE_ID, new ImmutablePair<>("Min Const Temp", new Temp()));
		ALL.put(MAX_CONST_TEMP_ATTRIBUTE_ID, new ImmutablePair<>("Max Const Temp", new Temp()));
		ALL.put(PUMP_STATUS_ATTRIBUTE_ID, new ImmutablePair<>("Pump Status", PumpStatus.RUNNING));
		ALL.put(EFFECTIVE_OPERATION_MODE_ATTRIBUTE_ID,
				new ImmutablePair<>("Effective Operation Mode", OperatingMode.NORMAL));
		ALL.put(EFFECTIVE_CONTROL_MODE_ATTRIBUTE_ID,
				new ImmutablePair<>("Effective Control Mode", ControlMode.RESERVED));
		ALL.put(CAPACITY_ATTRIBUTE_ID, new ImmutablePair<>("Capacity", new Capacity()));
		ALL.put(SPEED_ATTRIBUTE_ID, new ImmutablePair<>("Speed", null));
		ALL.put(LIFETIME_RUNNING_HOURS_ATTRIBUTE_ID, new ImmutablePair<>("Lifetime Running Hours", null));
		ALL.put(POWER_ATTRIBUTE_ID, new ImmutablePair<>("Power", null));
		ALL.put(LIFETIME_ENERGY_CONSUMED_ATTRIBUTE_ID, new ImmutablePair<>("Lifetime Energy Consumed", null));
		ALL.put(OPERATION_MODE_ATTRIBUTE_ID, new ImmutablePair<>("Operation Mode", OperatingMode.NORMAL));
		ALL.put(CONTROL_MODE_ATTRIBUTE_ID, new ImmutablePair<>("Control Mode", ControlMode.RESERVED));
		ALL.put(ALARM_MASK_ATTRIBUTE_ID, new ImmutablePair<>("Alarm Mask", AlarmMask.DRY_RUNNING));
	}
}
