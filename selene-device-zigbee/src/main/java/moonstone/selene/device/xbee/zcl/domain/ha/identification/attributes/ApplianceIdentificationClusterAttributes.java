package moonstone.selene.device.xbee.zcl.domain.ha.identification.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class ApplianceIdentificationClusterAttributes {
	public static final int BASIC_IDENTIFICATION_ATTRIBUTE_ID = 0x0000;
	public static final int APPLIANCE_COMPANY_NAME_ATTRIBUTE_ID = 0x0010;
	public static final int COMPANY_ID_ATTRIBUTE_ID = 0x0011;
	public static final int BRAND_NAME_ATTRIBUTE_ID = 0x0012;
	public static final int BRAND_ID_ATTRIBUTE_ID = 0x0013;
	public static final int APPLIANCE_MODEL_ATTRIBUTE_ID = 0x0014;
	public static final int APPLIANCE_PART_NUMBER_ATTRIBUTE_ID = 0x0015;
	public static final int APPLIANCE_PRODUCT_REVISION_ATTRIBUTE_ID = 0x0016;
	public static final int APPLIANCE_SOFTWARE_REVISION_ATTRIBUTE_ID = 0x0017;
	public static final int PRODUCT_TYPE_NAME_ATTRIBUTE_ID = 0x0018;
	public static final int PRODUCT_TYPE_ID_ATTRIBUTE_ID = 0x0019;
	public static final int CECED_SPECIFICATION_VERSION_ATTRIBUTE_ID = 0x001A;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(BASIC_IDENTIFICATION_ATTRIBUTE_ID,
				new ImmutablePair<>("Basic Identification", new BasicIdentification()));
		ALL.put(APPLIANCE_COMPANY_NAME_ATTRIBUTE_ID, new ImmutablePair<>("Appliance Company Name", null));
		ALL.put(COMPANY_ID_ATTRIBUTE_ID, new ImmutablePair<>("Company Id", null));
		ALL.put(BRAND_NAME_ATTRIBUTE_ID, new ImmutablePair<>("Brand Name", null));
		ALL.put(BRAND_ID_ATTRIBUTE_ID, new ImmutablePair<>("Brand Id", null));
		ALL.put(APPLIANCE_MODEL_ATTRIBUTE_ID, new ImmutablePair<>("Appliance Model", null));
		ALL.put(APPLIANCE_PART_NUMBER_ATTRIBUTE_ID, new ImmutablePair<>("Appliance Part Number", null));
		ALL.put(APPLIANCE_PRODUCT_REVISION_ATTRIBUTE_ID, new ImmutablePair<>("Appliance Product Revision", null));
		ALL.put(APPLIANCE_SOFTWARE_REVISION_ATTRIBUTE_ID, new ImmutablePair<>("Appliance Software Revision", null));
		ALL.put(PRODUCT_TYPE_NAME_ATTRIBUTE_ID, new ImmutablePair<>("Product Type Name", null));
		ALL.put(PRODUCT_TYPE_ID_ATTRIBUTE_ID, new ImmutablePair<>("Product Type Id", null));
		ALL.put(CECED_SPECIFICATION_VERSION_ATTRIBUTE_ID, new ImmutablePair<>("CECED Specification Version",
				CecedSpecificationVersion.COMPLIANT_WITH_V1_0_CERTIFIED));
	}
}
