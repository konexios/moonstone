package moonstone.selene.device.xbee.zcl;

import java.util.HashMap;
import java.util.Map;

public final class HaDeviceProfiles {
	public static final int ON_OFF_SWITCH = 0x0000;
	public static final int LEVEL_CONTROL_SWITCH = 0x0001;
	public static final int ON_OFF_OUTPUT = 0x0002;
	public static final int LEVEL_CONTROLLABLE_OUTPUT = 0x0003;
	public static final int SCENE_SELECTOR = 0x0004;
	public static final int CONFIGURATION_TOOL = 0x0005;
	public static final int REMOTE_CONTROL = 0x0006;
	public static final int COMBINED_INTERFACE = 0x0007;
	public static final int RANGE_EXTENDER = 0x0008;
	public static final int MAINS_POWER_OUTLET = 0x0009;
	public static final int DOOR_LOCK = 0x000A;
	public static final int DOOR_LOCK_CONTROLLER = 0x000B;
	public static final int SIMPLE_SENSOR = 0x000C;
	public static final int CONSUMPTION_AWARENESS_DEVICE = 0x000D;
	public static final int HOME_GATEWAY = 0x0050;
	public static final int SMART_PLUG = 0x0051;
	public static final int WHITE_GOODS = 0x0052;
	public static final int METER_INTERFACE = 0x0053;
	public static final int ON_OFF_LIGHT = 0X0100;
	public static final int DIMMABLE_LIGHT = 0x0101;
	public static final int COLOR_DIMMABLE_LIGHT = 0x0102;
	public static final int ON_OFF_LIGHT_SWITCH = 0x0103;
	public static final int DIMMER_SWITCH = 0x0104;
	public static final int COLOR_DIMMER_SWITCH = 0x0105;
	public static final int LIGHT_SENSOR = 0x0106;
	public static final int OCCUPANCY_SENSOR = 0x0107;
	public static final int SHADE = 0x0200;
	public static final int SHADE_CONTROLLER = 0x0201;
	public static final int WINDOW_COVERING_DEVICE = 0x0202;
	public static final int WINDOW_COVERING_CONTROLLER = 0x0203;
	public static final int HEATING_COOLING_UNIT = 0x0300;
	public static final int THERMOSTAT = 0x0301;
	public static final int TEMPERATURE_SENSOR = 0x0302;
	public static final int PUMP = 0x0303;
	public static final int PUMP_CONTROLLER = 0x0304;
	public static final int PRESSURE_SENSOR = 0x0305;
	public static final int FLOW_SENSOR = 0x0306;
	public static final int MINI_SPLIT_AC = 0x0307;
	public static final int IAS_CONTROL_AND_INDICATING_EQUIPMENT = 0x0400;
	public static final int IAS_ANCILLARY_CONTROL_EQUIPMENT = 0x0401;
	public static final int IAS_ZONE = 0x0402;
	public static final int IAS_WARNING_DEVICE = 0x0403;

	private static final Map<Integer, String> ALL = new HashMap<>();

	static {
		ALL.put(ON_OFF_SWITCH, "On/Off Switch");
		ALL.put(LEVEL_CONTROL_SWITCH, "Level Control Switch");
		ALL.put(ON_OFF_OUTPUT, "On/Off Output");
		ALL.put(LEVEL_CONTROLLABLE_OUTPUT, "Level Controllable Output");
		ALL.put(SCENE_SELECTOR, "Scene Selector");
		ALL.put(CONFIGURATION_TOOL, "Configuration Tool");
		ALL.put(REMOTE_CONTROL, "Remote Control");
		ALL.put(COMBINED_INTERFACE, "Combined Interface");
		ALL.put(RANGE_EXTENDER, "Range Extender");
		ALL.put(MAINS_POWER_OUTLET, "Mains Power Outlet");
		ALL.put(DOOR_LOCK, "Door Lock");
		ALL.put(DOOR_LOCK_CONTROLLER, "Door Lock Controller");
		ALL.put(SIMPLE_SENSOR, "Simple Sensor");
		ALL.put(CONSUMPTION_AWARENESS_DEVICE, "Consumption Awareness Device");
		ALL.put(HOME_GATEWAY, "Home Gateway");
		ALL.put(SMART_PLUG, "Smart Plug");
		ALL.put(WHITE_GOODS, "White Goods");
		ALL.put(METER_INTERFACE, "Meter Interface");
		ALL.put(ON_OFF_LIGHT, "On/Off Light");
		ALL.put(DIMMABLE_LIGHT, "Dimmable Light");
		ALL.put(COLOR_DIMMABLE_LIGHT, "Color Dimmable Light");
		ALL.put(ON_OFF_LIGHT_SWITCH, "On Off Light Switch");
		ALL.put(DIMMER_SWITCH, "Dimmer Switch");
		ALL.put(COLOR_DIMMER_SWITCH, "Color Dimmer Switch");
		ALL.put(LIGHT_SENSOR, "Light Sensor");
		ALL.put(OCCUPANCY_SENSOR, "Occupancy Sensor");
		ALL.put(SHADE, "Shade");
		ALL.put(SHADE_CONTROLLER, "Shade Controller");
		ALL.put(WINDOW_COVERING_DEVICE, "Window Covering Device");
		ALL.put(WINDOW_COVERING_CONTROLLER, "Window Covering Controller");
		ALL.put(HEATING_COOLING_UNIT, "Heating Cooling Unit");
		ALL.put(THERMOSTAT, "Thermostat");
		ALL.put(TEMPERATURE_SENSOR, "Temperature Sensor");
		ALL.put(PUMP, "Pump");
		ALL.put(PUMP_CONTROLLER, "Pump Controller");
		ALL.put(PRESSURE_SENSOR, "Pressure Sensor");
		ALL.put(FLOW_SENSOR, "Flow Sensor");
		ALL.put(MINI_SPLIT_AC, "Mini Split AC");
		ALL.put(IAS_CONTROL_AND_INDICATING_EQUIPMENT, "IAS Control And Indicating Equipment");
		ALL.put(IAS_ANCILLARY_CONTROL_EQUIPMENT, "IAS Ancillary Control Equipment");
		ALL.put(IAS_ZONE, "IAS Zone");
		ALL.put(IAS_WARNING_DEVICE, "IAS Warning Device");
	}

	public static String getName(Integer id) {
		String name = ALL.get(id);
		return name == null ? "Unknown Device Profile" : name;
	}
}
