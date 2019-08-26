package moonstone.selene.device.xbee.zcl;

import java.util.HashMap;
import java.util.Map;

public final class ZllDeviceProfiles {
	public static final int ON_OFF_LIGHT = 0x0000;
	public static final int ON_OFF_PLUGIN_UNIT = 0x0010;
	public static final int DIMMABLE_LIGHT = 0x0100;
	public static final int DIMMABLE_PLUGIN_UNIT = 0x0110;
	public static final int COLOUR_LIGHT = 0x0200;
	public static final int EXTENDED_COLOUR_LIGHT = 0x0210;
	public static final int COLOUR_TEMPERATURE_LIGHT = 0x0220;

	public static final int COLOUR_CONTROLLER = 0x0800;
	public static final int COLOUR_SCENE_CONTROLLER = 0x0810;
	public static final int NON_COLOUR_CONTROLLER = 0x0820;
	public static final int NON_COLOUR_SCENE_CONTROLLER = 0x0830;
	public static final int CONTROL_BRIDGE = 0x0840;
	public static final int ON_OFF_SENSOR = 0x0850;

	private static final Map<Integer, String> ALL = new HashMap<>();

	static {
		ALL.put(ON_OFF_LIGHT, "On/Off Light");
		ALL.put(ON_OFF_PLUGIN_UNIT, "On/Off Plug-in Unit");
		ALL.put(DIMMABLE_LIGHT, "Dimmable Light");
		ALL.put(DIMMABLE_PLUGIN_UNIT, "Dimmable Plug-in Unit");
		ALL.put(COLOUR_LIGHT, "Colour Light");
		ALL.put(EXTENDED_COLOUR_LIGHT, "Extended Colour Light");
		ALL.put(COLOUR_TEMPERATURE_LIGHT, "Colour Temperature Light");
		ALL.put(COLOUR_CONTROLLER, "Colour Controller");
		ALL.put(COLOUR_SCENE_CONTROLLER, "Colour Scenr Controller");
		ALL.put(NON_COLOUR_CONTROLLER, "Non-Colour Controller");
		ALL.put(NON_COLOUR_SCENE_CONTROLLER, "Non-Colour Scene Controller");
		ALL.put(CONTROL_BRIDGE, "Control Bridge");
		ALL.put(ON_OFF_SENSOR, "On/Off Sensor");
	}

	public static String getName(Integer id) {
		String name = ALL.get(id);
		return name == null ? "Unknown Device Profile" : name;
	}
}
