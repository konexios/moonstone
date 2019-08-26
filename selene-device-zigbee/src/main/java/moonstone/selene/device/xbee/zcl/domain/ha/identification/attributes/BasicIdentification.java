package moonstone.selene.device.xbee.zcl.domain.ha.identification.attributes;

import java.util.List;

import com.digi.xbee.api.utils.ByteUtils;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;
import moonstone.selene.device.sensor.SensorDataAbstract;
import moonstone.selene.device.xbee.zcl.data.Attribute;
import moonstone.selene.device.xbee.zcl.domain.ha.identification.attributes.BasicIdentification.BasicIdentificationData;

public class BasicIdentification implements Attribute<BasicIdentificationData> {
	private static final long COMPANY_ID_MASK = 0x000000000000ffffL;
	private static final long BRAND_ID_MASK = 0x00000000ffff0000L;
	private static final long PRODUCT_TYPE_ID_MASK = 0x0000ffff00000000L;
	private static final long SPEC_VERSION_MASK = 0x00ff000000000000L;

	public static long getCompanyId(long value) {
		return value & COMPANY_ID_MASK;
	}

	public static long getBrandId(long value) {
		return (value & BRAND_ID_MASK) >> 16;
	}

	public static ProductTypeId getProductTypeId(long value) {
		return ProductTypeId.getByValue((value & PRODUCT_TYPE_ID_MASK) >> 32);
	}

	public static long getSpecVersion(long value) {
		return (value & SPEC_VERSION_MASK) >> 48;
	}

	public static long getCompanyId(byte... value) {
		return getCompanyId(ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(value)));
	}

	public static long getBrandId(byte... value) {
		return getBrandId(ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(value)));
	}

	public static ProductTypeId getProductTypeId(byte... value) {
		return getProductTypeId(ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(value)));
	}

	public static long getSpecVersion(byte... value) {
		return getSpecVersion(ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(value)));
	}

	@Override
	public int getId() {
		return ApplianceIdentificationClusterAttributes.BASIC_IDENTIFICATION_ATTRIBUTE_ID;
	}

	@Override
	public BasicIdentificationData toData(String name, byte... value) {
		return new BasicIdentificationData(value);
	}

	public enum ProductTypeId {
		WHITE_GOODS(0x0000L),
		DISHWASHER(0x5601L),
		TUMBLE_DRYER(0x5602L),
		WASHER_DRYER(0x5603L),
		WASHING_MACHINE(0x5604L),
		HOBS(0x5e03L),
		INDUCTION_HOBS(0x5e09L),
		OVEN(0x5e01L),
		ELECTRICAL_OVEN(0x5e06L),
		REFRIGERATOR_FREEZER(0x6601L);

		private final long value;

		ProductTypeId(long value) {
			this.value = value;
		}

		public static ProductTypeId getByValue(long value) {
			for (ProductTypeId item : values()) {
				if (item.value == value) {
					return item;
				}
			}
			return null;
		}
	}

	class BasicIdentificationData extends SensorDataAbstract<BasicIdentification> {
		private byte[] data;

		public BasicIdentificationData(byte... data) {
			super("BasicIdentification", null);
			this.data = data;
		}

		@Override
		public void writeIoTParameters(IotParameters parameters) {
			parameters.setLong("companyId", getCompanyId(data));
			parameters.setLong("brandId", getBrandId(data));
			parameters.setString("productTypeId", getProductTypeId(data).name());
			parameters.setLong("specVersion", getSpecVersion(data));
		}

		@Override
		public void writeTelemetry(List<Telemetry> telemetries, long timestamp) {
			Telemetry companyId = new Telemetry(TelemetryItemType.Integer, "companyId", timestamp);
			companyId.setIntValue(getCompanyId(data));
			telemetries.add(companyId);
			Telemetry brandId = new Telemetry(TelemetryItemType.Integer, "brandId", timestamp);
			companyId.setIntValue(getBrandId(data));
			telemetries.add(brandId);
			Telemetry productTypeId = new Telemetry(TelemetryItemType.String, "productTypeId", timestamp);
			companyId.setStrValue(getProductTypeId(data).name());
			telemetries.add(productTypeId);
			Telemetry specVersion = new Telemetry(TelemetryItemType.Integer, "specVersion", timestamp);
			companyId.setIntValue(getSpecVersion(data));
			telemetries.add(specVersion);
		}
	}
}
