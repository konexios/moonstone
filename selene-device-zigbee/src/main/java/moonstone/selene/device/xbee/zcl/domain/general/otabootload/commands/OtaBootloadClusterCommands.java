package moonstone.selene.device.xbee.zcl.domain.general.otabootload.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class OtaBootloadClusterCommands {
	public static final int IMAGE_NOTIFY_COMMAND_ID = 0x00;
	public static final int QUERY_NEXT_IMAGE_RESPONSE_COMMAND_ID = 0x02;
	public static final int IMAGE_BLOCK_RESPONSE_COMMAND_ID = 0x05;
	public static final int UPGRADE_END_RESPONSE_COMMAND_ID = 0x07;
	public static final int QUERY_SPECIFIC_FILE_RESPONSE_COMMAND_ID = 0x09;

	public static final int QUERY_NEXT_IMAGE_REQUEST_COMMAND_ID = 0x01;
	public static final int IMAGE_BLOCK_REQUEST_COMMAND_ID = 0x03;
	public static final int IMAGE_PAGE_REQUEST_COMMAND_ID = 0x04;
	public static final int UPGRADE_END_REQUEST_COMMAND_ID = 0x06;
	public static final int QUERY_SPECIFIC_FILE_REQUEST_COMMAND_ID = 0x08;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(IMAGE_NOTIFY_COMMAND_ID, "Image Notify");
		ALL_GENERATED.put(QUERY_NEXT_IMAGE_RESPONSE_COMMAND_ID, "Query Next Image Response");
		ALL_GENERATED.put(IMAGE_BLOCK_RESPONSE_COMMAND_ID, "Image Block Response");
		ALL_GENERATED.put(UPGRADE_END_RESPONSE_COMMAND_ID, "Upgrade End Response");
		ALL_GENERATED.put(QUERY_SPECIFIC_FILE_RESPONSE_COMMAND_ID, "Query Specific File Response");

		ALL_RECEIVED.put(QUERY_NEXT_IMAGE_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Query Next Image Request", NoPayloadCommand.class));
		ALL_RECEIVED.put(IMAGE_BLOCK_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Image Block Request", NoPayloadCommand.class));
		ALL_RECEIVED.put(IMAGE_PAGE_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Image Page Request", NoPayloadCommand.class));
		ALL_RECEIVED.put(UPGRADE_END_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Upgrade End Request", NoPayloadCommand.class));
		ALL_RECEIVED.put(QUERY_SPECIFIC_FILE_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Query Specific File Request", NoPayloadCommand.class));
	}
}
