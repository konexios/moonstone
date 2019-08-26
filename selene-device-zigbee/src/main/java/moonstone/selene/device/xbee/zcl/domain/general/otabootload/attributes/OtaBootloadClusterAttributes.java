package moonstone.selene.device.xbee.zcl.domain.general.otabootload.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class OtaBootloadClusterAttributes {
	public static final int UPGRADE_SERVER_ID_ATTRIBUTE_ID = 0x0000;
	public static final int FILE_OFFSET_ATTRIBUTE_ID = 0x0001;
	public static final int CURRENT_FILE_VERSION_ATTRIBUTE_ID = 0x0002;
	public static final int CURRENT_ZIGBEE_STACK_VERSION_ATTRIBUTE_ID = 0x0003;
	public static final int DOWNLOADED_FILE_VERSION_ATTRIBUTE_ID = 0x0004;
	public static final int DOWNLOADED_ZIGBEE_STACK_VERSION_ATTRIBUTE_ID = 0x0005;
	public static final int IMAGE_UPGRADE_STATUS_ATTRIBUTE_ID = 0x0006;
	public static final int MANUFACTURER_ID_ATTRIBUTE_ID = 0x0007;
	public static final int IMAGE_TYPE_ID_ATTRIBUTE_ID = 0x0008;
	public static final int MINIMUM_BLOCK_REQUEST_PERIOD_ATTRIBUTE_ID = 0x0009;
	public static final int IMAGE_STAMP_ATTRIBUTE_ID = 0x000A;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(UPGRADE_SERVER_ID_ATTRIBUTE_ID, new ImmutablePair<>("Upgrade Server Id", null));
		ALL.put(FILE_OFFSET_ATTRIBUTE_ID, new ImmutablePair<>("File Offset", null));
		ALL.put(CURRENT_FILE_VERSION_ATTRIBUTE_ID, new ImmutablePair<>("Current File Version", null));
		ALL.put(CURRENT_ZIGBEE_STACK_VERSION_ATTRIBUTE_ID, new ImmutablePair<>("Current Zigbee Stack Version", null));
		ALL.put(DOWNLOADED_FILE_VERSION_ATTRIBUTE_ID, new ImmutablePair<>("Downloaded File Version", null));
		ALL.put(DOWNLOADED_ZIGBEE_STACK_VERSION_ATTRIBUTE_ID,
				new ImmutablePair<>("Downloaded Zigbee Stack Version", null));
		ALL.put(IMAGE_UPGRADE_STATUS_ATTRIBUTE_ID, new ImmutablePair<>("Image Upgrade Status", null));
		ALL.put(MANUFACTURER_ID_ATTRIBUTE_ID, new ImmutablePair<>("Manufacturer Id", null));
		ALL.put(IMAGE_TYPE_ID_ATTRIBUTE_ID, new ImmutablePair<>("Image Type Id", null));
		ALL.put(MINIMUM_BLOCK_REQUEST_PERIOD_ATTRIBUTE_ID, new ImmutablePair<>("Minimum Block Request Period", null));
		ALL.put(IMAGE_STAMP_ATTRIBUTE_ID, new ImmutablePair<>("Image Stamp", null));
	}
}
