package moonstone.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class LightingColorClusterCommands {
	public static final int MOVE_TO_HUE_COMMAND_ID = 0x00;
	public static final int MOVE_HUE_COMMAND_ID = 0x01;
	public static final int STEP_HUE_COMMAND_ID = 0x02;
	public static final int MOVE_TO_SATURATION_COMMAND_ID = 0x03;
	public static final int MOVE_SATURATION_COMMAND_ID = 0x04;
	public static final int STEP_SATURATION_COMMAND_ID = 0x05;
	public static final int MOVE_TO_HUE_AND_SATURATION_COMMAND_ID = 0x06;
	public static final int MOVE_TO_COLOR_COMMAND_ID = 0x07;
	public static final int MOVE_COLOR_COMMAND_ID = 0x08;
	public static final int STEP_COLOR_COMMAND_ID = 0x09;
	public static final int MOVE_TO_COLOR_TEMPERATURE_COMMAND_ID = 0x0a;
	public static final int ENHANCED_MOVE_TO_HUE_COMMAND_ID = 0x40;
	public static final int ENHANCED_MOVE_HUE_COMMAND_ID = 0x41;
	public static final int ENHANCED_STEP_HUE_COMMAND_ID = 0x42;
	public static final int ENHANCED_MOVE_TO_HUE_AND_SATURATION_COMMAND_ID = 0x43;
	public static final int COLOR_LOOP_SET_COMMAND_ID = 0x44;
	public static final int STOP_MOVE_STEP_COMMAND_ID = 0x47;
	public static final int MOVE_COLOR_TEMPERATURE_COMMAND_ID = 0x4B;
	public static final int STEP_COLOR_TEMPERATURE_COMMAND_ID = 0x4C;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(MOVE_TO_HUE_COMMAND_ID, new ImmutablePair<>("Move to Hue", MoveToHue.class));
		ALL_RECEIVED.put(MOVE_HUE_COMMAND_ID, new ImmutablePair<>("Move Hue", MoveHue.class));
		ALL_RECEIVED.put(STEP_HUE_COMMAND_ID, new ImmutablePair<>("Step Hue", StepHue.class));
		ALL_RECEIVED.put(MOVE_TO_SATURATION_COMMAND_ID,
				new ImmutablePair<>("Move to Saturation", MoveToSaturation.class));
		ALL_RECEIVED.put(MOVE_SATURATION_COMMAND_ID, new ImmutablePair<>("Move Saturation", MoveSaturation.class));
		ALL_RECEIVED.put(STEP_SATURATION_COMMAND_ID, new ImmutablePair<>("Step Saturation", StepSaturation.class));
		ALL_RECEIVED.put(MOVE_TO_HUE_AND_SATURATION_COMMAND_ID,
				new ImmutablePair<>("Move to Hue and Saturation", MoveToHueAndSaturation.class));
		ALL_RECEIVED.put(MOVE_TO_COLOR_COMMAND_ID, new ImmutablePair<>("Move to Color", MoveToColor.class));
		ALL_RECEIVED.put(MOVE_COLOR_COMMAND_ID, new ImmutablePair<>("Move Color", MoveColor.class));
		ALL_RECEIVED.put(STEP_COLOR_COMMAND_ID, new ImmutablePair<>("Step Color", StepColor.class));
		ALL_RECEIVED.put(MOVE_TO_COLOR_TEMPERATURE_COMMAND_ID,
				new ImmutablePair<>("Move to Color Temperature", MoveToColorTemperature.class));
		ALL_RECEIVED.put(ENHANCED_MOVE_TO_HUE_COMMAND_ID,
				new ImmutablePair<>("Enhanced Move To Hue", EnhancedMoveToHue.class));
		ALL_RECEIVED.put(ENHANCED_MOVE_HUE_COMMAND_ID, new ImmutablePair<>("Enhanced Move Hue", EnhancedMoveHue
				.class));
		ALL_RECEIVED.put(ENHANCED_STEP_HUE_COMMAND_ID, new ImmutablePair<>("Enhanced Step Hue", EnhancedStepHue
				.class));
		ALL_RECEIVED.put(ENHANCED_MOVE_TO_HUE_AND_SATURATION_COMMAND_ID,
				new ImmutablePair<>("Enhanced Move To Hue And Saturation", EnhancedMoveToHueAndSaturation.class));
		ALL_RECEIVED.put(COLOR_LOOP_SET_COMMAND_ID, new ImmutablePair<>("Color Loop Set", ColorLoopSet.class));
		ALL_RECEIVED.put(STOP_MOVE_STEP_COMMAND_ID, new ImmutablePair<>("Stop Move Step", StopMoveStep.class));
		ALL_RECEIVED.put(MOVE_COLOR_TEMPERATURE_COMMAND_ID,
				new ImmutablePair<>("Move Color Temperature", MoveColorTemperature.class));
		ALL_RECEIVED.put(STEP_COLOR_TEMPERATURE_COMMAND_ID,
				new ImmutablePair<>("Step Color Temperature", StepColorTemperature.class));
	}
}
