package moonstone.selene.device.xbee.zcl.domain.lighting.color.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class LightingColorClusterAttributes {
	public static final int CURRENT_HUE_ATTRIBUTE_ID = 0x0000;
	public static final int CURRENT_SATURATION_ATTRIBUTE_ID = 0x0001;
	public static final int REMAINING_TIME_ATTRIBUTE_ID = 0x0002;
	public static final int CURRENT_X_ATTRIBUTE_ID = 0x0003;
	public static final int CURRENT_Y_ATTRIBUTE_ID = 0x0004;
	public static final int DRIFT_COMPENSATION_ATTRIBUTE_ID = 0x0005;
	public static final int COMPENSATION_TEXT_ATTRIBUTE_ID = 0x0006;
	public static final int COLOR_TEMPERATURE_ATTRIBUTE_ID = 0x0007;
	public static final int COLOR_MODE_ATTRIBUTE_ID = 0x0008;

	public static final int NUMBER_OF_PRIMARIES_ATTRIBUTE_ID = 0x0010;
	public static final int PRIMARY_1_X_ATTRIBUTE_ID = 0x0011;
	public static final int PRIMARY_1_Y_ATTRIBUTE_ID = 0x0012;
	public static final int PRIMARY_1_INTENSITY_ATTRIBUTE_ID = 0x0013;
	public static final int PRIMARY_2_X_ATTRIBUTE_ID = 0x0015;
	public static final int PRIMARY_2_Y_ATTRIBUTE_ID = 0x0016;
	public static final int PRIMARY_2_INTENSITY_ATTRIBUTE_ID = 0x0017;
	public static final int PRIMARY_3_X_ATTRIBUTE_ID = 0x0019;
	public static final int PRIMARY_3_Y_ATTRIBUTE_ID = 0x001a;
	public static final int PRIMARY_3_INTENSITY_ATTRIBUTE_ID = 0x001b;

	public static final int PRIMARY_4_X_ATTRIBUTE_ID = 0x0020;
	public static final int PRIMARY_4_Y_ATTRIBUTE_ID = 0x0021;
	public static final int PRIMARY_4_INTENSITY_ATTRIBUTE_ID = 0x0022;
	public static final int PRIMARY_5_X_ATTRIBUTE_ID = 0x0024;
	public static final int PRIMARY_5_Y_ATTRIBUTE_ID = 0x0025;
	public static final int PRIMARY_5_INTENSITY_ATTRIBUTE_ID = 0x0026;
	public static final int PRIMARY_6_X_ATTRIBUTE_ID = 0x0028;
	public static final int PRIMARY_6_Y_ATTRIBUTE_ID = 0x0029;
	public static final int PRIMARY_6_INTENSITY_ATTRIBUTE_ID = 0x002a;

	public static final int WHITE_POINT_X_ATTRIBUTE_ID = 0x0030;
	public static final int WHITE_POINT_Y_ATTRIBUTE_ID = 0x0031;
	public static final int COLOR_POINT_R_X_ATTRIBUTE_ID = 0x0032;
	public static final int COLOR_POINT_R_Y_ATTRIBUTE_ID = 0x0033;
	public static final int COLOR_POINT_R_INTENSITY_ATTRIBUTE_ID = 0x0034;
	public static final int COLOR_POINT_G_X_ATTRIBUTE_ID = 0x0036;
	public static final int COLOR_POINT_G_Y_ATTRIBUTE_ID = 0x0037;
	public static final int COLOR_POINT_G_INTENSITY_ATTRIBUTE_ID = 0x0038;
	public static final int COLOR_POINT_B_X_ATTRIBUTE_ID = 0x003a;
	public static final int COLOR_POINT_B_Y_ATTRIBUTE_ID = 0x003b;
	public static final int COLOR_POINT_B_INTENSITY_ATTRIBUTE_ID = 0x003c;

	public static final int ENHANCED_CURRENT_HUE_ATTRIBUTE_ID = 0x4000;
	public static final int ENHANCED_COLOR_MODE_ATTRIBUTE_ID = 0x4001;
	public static final int COLOR_LOOP_ACTIVE_ATTRIBUTE_ID = 0x4002;
	public static final int COLOR_LOOP_DIRECTION_ATTRIBUTE_ID = 0x4003;
	public static final int COLOR_LOOP_TIME_ATTRIBUTE_ID = 0x4004;
	public static final int COLOR_LOOP_START_ENHANCED_HUE_ATTRIBUTE_ID = 0x4005;
	public static final int COLOR_LOOP_STORED_ENHANCED_HUE_ATTRIBUTE_ID = 0x4006;
	public static final int COLOR_CAPABILITIES_ATTRIBUTE_ID = 0x400a;
	public static final int COLOR_TEMP_PHYSICAL_MIN_ATTRIBUTE_ID = 0x400b;
	public static final int COLOR_TEMP_PHYSICAL_MAX_ATTRIBUTE_ID = 0x400c;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(CURRENT_HUE_ATTRIBUTE_ID, new ImmutablePair<>("Current Hue", new CurrentHue()));
		ALL.put(CURRENT_SATURATION_ATTRIBUTE_ID, new ImmutablePair<>("Current Saturation", new CurrentSaturation()));
		ALL.put(REMAINING_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Remaining Time", new RemainingTime()));
		ALL.put(CURRENT_X_ATTRIBUTE_ID, new ImmutablePair<>("Current X", new CurrentXY()));
		ALL.put(CURRENT_Y_ATTRIBUTE_ID, new ImmutablePair<>("Current Y", new CurrentXY()));
		ALL.put(DRIFT_COMPENSATION_ATTRIBUTE_ID, new ImmutablePair<>("Drift Compensation", DriftCompensation
				.RESERVED));
		ALL.put(COMPENSATION_TEXT_ATTRIBUTE_ID, new ImmutablePair<>("Compensation Text", null));
		ALL.put(COLOR_TEMPERATURE_ATTRIBUTE_ID, new ImmutablePair<>("Color Temperature", new ColorTemperature()));
		ALL.put(COLOR_MODE_ATTRIBUTE_ID, new ImmutablePair<>("Color Mode", ColorMode.RESERVED));
		ALL.put(NUMBER_OF_PRIMARIES_ATTRIBUTE_ID, new ImmutablePair<>("Number of Primaries", null));
		ALL.put(PRIMARY_1_X_ATTRIBUTE_ID, new ImmutablePair<>("Primary 1 X", new Primary()));
		ALL.put(PRIMARY_1_Y_ATTRIBUTE_ID, new ImmutablePair<>("Primary 1 Y", new Primary()));
		ALL.put(PRIMARY_1_INTENSITY_ATTRIBUTE_ID, new ImmutablePair<>("Primary 1 Intensity", new Primary()));
		ALL.put(PRIMARY_2_X_ATTRIBUTE_ID, new ImmutablePair<>("Primary 2 X", new Primary()));
		ALL.put(PRIMARY_2_Y_ATTRIBUTE_ID, new ImmutablePair<>("Primary 2 Y", new Primary()));
		ALL.put(PRIMARY_2_INTENSITY_ATTRIBUTE_ID, new ImmutablePair<>("Primary 2 Intensity", new Primary()));
		ALL.put(PRIMARY_3_X_ATTRIBUTE_ID, new ImmutablePair<>("Primary 3 X", new Primary()));
		ALL.put(PRIMARY_3_Y_ATTRIBUTE_ID, new ImmutablePair<>("Primary 3 Y", new Primary()));
		ALL.put(PRIMARY_3_INTENSITY_ATTRIBUTE_ID, new ImmutablePair<>("Primary 3 Intensity", new Primary()));
		ALL.put(PRIMARY_4_X_ATTRIBUTE_ID, new ImmutablePair<>("Primary 4 X", new Primary()));
		ALL.put(PRIMARY_4_Y_ATTRIBUTE_ID, new ImmutablePair<>("Primary 4 Y", new Primary()));
		ALL.put(PRIMARY_4_INTENSITY_ATTRIBUTE_ID, new ImmutablePair<>("Primary 4 Intensity", new Primary()));
		ALL.put(PRIMARY_5_X_ATTRIBUTE_ID, new ImmutablePair<>("Primary 5 X", new Primary()));
		ALL.put(PRIMARY_5_Y_ATTRIBUTE_ID, new ImmutablePair<>("Primary 5 Y", new Primary()));
		ALL.put(PRIMARY_5_INTENSITY_ATTRIBUTE_ID, new ImmutablePair<>("Primary 5 Intensity", new Primary()));
		ALL.put(PRIMARY_6_X_ATTRIBUTE_ID, new ImmutablePair<>("Primary 6 X", new Primary()));
		ALL.put(PRIMARY_6_Y_ATTRIBUTE_ID, new ImmutablePair<>("Primary 6 Y", new Primary()));
		ALL.put(PRIMARY_6_INTENSITY_ATTRIBUTE_ID, new ImmutablePair<>("Primary 6 Intensity", new Primary()));
		ALL.put(WHITE_POINT_X_ATTRIBUTE_ID, new ImmutablePair<>("White Point X", new Primary()));
		ALL.put(WHITE_POINT_Y_ATTRIBUTE_ID, new ImmutablePair<>("White Point Y", new Primary()));
		ALL.put(COLOR_POINT_R_X_ATTRIBUTE_ID, new ImmutablePair<>("Color Point Red X", new Point()));
		ALL.put(COLOR_POINT_R_Y_ATTRIBUTE_ID, new ImmutablePair<>("Color Point Red Y", new Point()));
		ALL.put(COLOR_POINT_R_INTENSITY_ATTRIBUTE_ID, new ImmutablePair<>("Color Point Red Intensity", new Point()));
		ALL.put(COLOR_POINT_G_X_ATTRIBUTE_ID, new ImmutablePair<>("Color Point Green X", new Point()));
		ALL.put(COLOR_POINT_G_Y_ATTRIBUTE_ID, new ImmutablePair<>("Color Point Green Y", new Point()));
		ALL.put(COLOR_POINT_G_INTENSITY_ATTRIBUTE_ID, new ImmutablePair<>("Color Point Green Intensity", new Point()));
		ALL.put(COLOR_POINT_B_X_ATTRIBUTE_ID, new ImmutablePair<>("Color Point Blue X", new Point()));
		ALL.put(COLOR_POINT_B_Y_ATTRIBUTE_ID, new ImmutablePair<>("Color Point Blue Y", new Point()));
		ALL.put(COLOR_POINT_B_INTENSITY_ATTRIBUTE_ID, new ImmutablePair<>("Color Point Blue Intensity", new Point()));

		ALL.put(ENHANCED_CURRENT_HUE_ATTRIBUTE_ID, new ImmutablePair<>("Enhanced Current Hue", null));
		ALL.put(ENHANCED_COLOR_MODE_ATTRIBUTE_ID, new ImmutablePair<>("Enhanced Color Mode", null));
		ALL.put(COLOR_LOOP_ACTIVE_ATTRIBUTE_ID, new ImmutablePair<>("Color Loop Active", null));
		ALL.put(COLOR_LOOP_DIRECTION_ATTRIBUTE_ID, new ImmutablePair<>("Color Loop Direction", null));
		ALL.put(COLOR_LOOP_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Color Loop Time", null));
		ALL.put(COLOR_LOOP_START_ENHANCED_HUE_ATTRIBUTE_ID, new ImmutablePair<>("Color Loop Start Enhanced Hue",
				null));
		ALL.put(COLOR_LOOP_STORED_ENHANCED_HUE_ATTRIBUTE_ID,
				new ImmutablePair<>("Color Loop Stored Enhanced Hue", null));
		ALL.put(COLOR_CAPABILITIES_ATTRIBUTE_ID, new ImmutablePair<>("Color Capabilities", null));
		ALL.put(COLOR_TEMP_PHYSICAL_MIN_ATTRIBUTE_ID, new ImmutablePair<>("Color Temperature Physical Minimum", null));
		ALL.put(COLOR_TEMP_PHYSICAL_MAX_ATTRIBUTE_ID, new ImmutablePair<>("Color Temperature Physical Maximum", null));
	}
}
