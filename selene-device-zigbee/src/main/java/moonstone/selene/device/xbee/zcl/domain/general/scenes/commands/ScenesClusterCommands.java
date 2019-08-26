package moonstone.selene.device.xbee.zcl.domain.general.scenes.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ScenesClusterCommands {
	public static final int ADD_SCENE_COMMAND_ID = 0x00;
	public static final int VIEW_SCENE_COMMAND_ID = 0x01;
	public static final int REMOVE_SCENE_COMMAND_ID = 0x02;
	public static final int REMOVE_ALL_SCENES_COMMAND_ID = 0x03;
	public static final int STORE_SCENE_COMMAND_ID = 0x04;
	public static final int RECALL_SCENE_COMMAND_ID = 0x05;
	public static final int GET_SCENE_MEMBERSHIP_COMMAND_ID = 0x06;
	public static final int ENHANCED_ADD_SCENE_COMMAND_ID = 0x40;
	public static final int ENHANCED_VIEW_SCENE_COMMAND_ID = 0x41;
	public static final int COPY_SCENE_COMMAND_ID = 0x42;

	public static final int ADD_SCENE_RESPONSE_COMMAND_ID = 0x00;
	public static final int VIEW_SCENE_RESPONSE_COMMAND_ID = 0x01;
	public static final int REMOVE_SCENE_RESPONSE_COMMAND_ID = 0x02;
	public static final int REMOVE_ALL_SCENES_RESPONSE_COMMAND_ID = 0x03;
	public static final int STORE_SCENE_RESPONSE_COMMAND_ID = 0x04;
	public static final int GET_SCENE_MEMBERSHIP_RESPONSE_COMMAND_ID = 0x06;
	public static final int ENHANCED_ADD_SCENE_RESPONSE_COMMAND_ID = 0x40;
	public static final int ENHANCED_VIEW_SCENE_RESPONSE_COMMAND_ID = 0x41;
	public static final int COPY_SCENE_RESPONSE_COMMAND_ID = 0x42;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(ADD_SCENE_COMMAND_ID, new ImmutablePair<>("Add Scene", AddScene.class));
		ALL_RECEIVED.put(VIEW_SCENE_COMMAND_ID, new ImmutablePair<>("View Scene", ViewScene.class));
		ALL_RECEIVED.put(REMOVE_SCENE_COMMAND_ID, new ImmutablePair<>("Remove Scene", RemoveScene.class));
		ALL_RECEIVED.put(REMOVE_ALL_SCENES_COMMAND_ID, new ImmutablePair<>("Remove All Scenes", RemoveAllScenes
				.class));
		ALL_RECEIVED.put(STORE_SCENE_COMMAND_ID, new ImmutablePair<>("Store Scene", StoreScene.class));
		ALL_RECEIVED.put(RECALL_SCENE_COMMAND_ID, new ImmutablePair<>("Recall Scene", RecallScene.class));
		ALL_RECEIVED.put(GET_SCENE_MEMBERSHIP_COMMAND_ID,
				new ImmutablePair<>("Get Scene Membership", GetSceneMembership.class));
		ALL_RECEIVED.put(ENHANCED_ADD_SCENE_COMMAND_ID,
				new ImmutablePair<>("Enhanced Add Scene", EnhancedAddScene.class));
		ALL_RECEIVED.put(ENHANCED_VIEW_SCENE_COMMAND_ID,
				new ImmutablePair<>("Enhanced View Scene", EnhancedViewScene.class));
		ALL_RECEIVED.put(COPY_SCENE_COMMAND_ID, new ImmutablePair<>("Copy Scene", CopyScene.class));

		ALL_GENERATED.put(ADD_SCENE_RESPONSE_COMMAND_ID, "Add Scene Response");
		ALL_GENERATED.put(VIEW_SCENE_RESPONSE_COMMAND_ID, "View Scene Response");
		ALL_GENERATED.put(REMOVE_SCENE_RESPONSE_COMMAND_ID, "Remove Scene Response");
		ALL_GENERATED.put(REMOVE_ALL_SCENES_RESPONSE_COMMAND_ID, "Remove All Scenes Response");
		ALL_GENERATED.put(STORE_SCENE_RESPONSE_COMMAND_ID, "Store Scene Response");
		ALL_GENERATED.put(GET_SCENE_MEMBERSHIP_RESPONSE_COMMAND_ID, "Get Scene Membership Response");
		ALL_GENERATED.put(ENHANCED_ADD_SCENE_RESPONSE_COMMAND_ID, "Enhanced Add Scene Response");
		ALL_GENERATED.put(ENHANCED_VIEW_SCENE_RESPONSE_COMMAND_ID, "Enhanced View Scene Response");
		ALL_GENERATED.put(COPY_SCENE_RESPONSE_COMMAND_ID, "Copy Scene Response");
	}
}
