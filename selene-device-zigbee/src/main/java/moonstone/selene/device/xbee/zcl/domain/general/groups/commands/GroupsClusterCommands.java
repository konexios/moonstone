package moonstone.selene.device.xbee.zcl.domain.general.groups.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GroupsClusterCommands {
	public static final int ADD_GROUP_COMMAND_ID = 0x00;
	public static final int VIEW_GROUP_COMMAND_ID = 0x01;
	public static final int GET_GROUP_MEMBERSHIP_COMMAND_ID = 0x02;
	public static final int REMOVE_GROUP_COMMAND_ID = 0x03;
	public static final int REMOVE_ALL_GROUPS_COMMAND_ID = 0x04;
	public static final int ADD_GROUP_IDENTIFYING_COMMAND_ID = 0x05;

	public static final int ADD_GROUP_RESPONSE_COMMAND_ID = 0x00;
	public static final int VIEW_GROUP_RESPONSE_COMMAND_ID = 0x01;
	public static final int GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND_ID = 0x02;
	public static final int REMOVE_GROUP_RESPONSE_COMMAND_ID = 0x03;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(ADD_GROUP_COMMAND_ID, new ImmutablePair<>("Add Group", AddGroup.class));
		ALL_RECEIVED.put(VIEW_GROUP_COMMAND_ID, new ImmutablePair<>("View Group", ViewGroup.class));
		ALL_RECEIVED.put(GET_GROUP_MEMBERSHIP_COMMAND_ID,
				new ImmutablePair<>("Get Group Membership", GetGroupMembership.class));
		ALL_RECEIVED.put(REMOVE_GROUP_COMMAND_ID, new ImmutablePair<>("Remove Group", RemoveGroup.class));
		ALL_RECEIVED.put(REMOVE_ALL_GROUPS_COMMAND_ID, new ImmutablePair<>("Remove All Groups", RemoveAllGroups
				.class));
		ALL_RECEIVED.put(ADD_GROUP_IDENTIFYING_COMMAND_ID,
				new ImmutablePair<>("Add Group Identifying", AddGroupIdentifying.class));

		ALL_GENERATED.put(ADD_GROUP_RESPONSE_COMMAND_ID, "Add Group Response");
		ALL_GENERATED.put(VIEW_GROUP_RESPONSE_COMMAND_ID, "View Group Response");
		ALL_GENERATED.put(GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND_ID, "Get Group Membership Response");
		ALL_GENERATED.put(REMOVE_GROUP_RESPONSE_COMMAND_ID, "Remove Group Response");
	}
}
