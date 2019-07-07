package com.arrow.selene.device.xbee.zcl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Hex;

import com.arrow.acs.Loggable;
import com.arrow.selene.device.sensor.BooleanSensorData;
import com.arrow.selene.device.sensor.DoubleSensorData;
import com.arrow.selene.device.sensor.FloatSensorData;
import com.arrow.selene.device.sensor.IntegerSensorData;
import com.arrow.selene.device.sensor.LongSensorData;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.engine.EngineConstants;
import com.digi.xbee.api.utils.ByteUtils;

public enum ZclDataType {
	DATA_NULL((byte) 0x00, 0, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return null;
		}
	},
	DATA_8BIT((byte) 0x08, 1, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new IntegerSensorData(name, (int) data[0]);
		}
	},
	DATA_16BIT((byte) 0x09, 2, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new IntegerSensorData(name, (int) ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort());
		}
	},
	DATA_24BIT((byte) 0x0A, 3, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new IntegerSensorData(name, ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_32BIT((byte) 0x0B, 4, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new IntegerSensorData(name, ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getInt());
		}
	},
	DATA_40BIT((byte) 0x0C, 5, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_48BIT((byte) 0x0D, 6, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_56BIT((byte) 0x0E, 7, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_64BIT((byte) 0x0F, 8, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_BOOLEAN((byte) 0x10, 1, false) {
		@Override
		public boolean isValid(byte... value) {
			return (value[0] & 0xff) != 0xff;
		}

		@Override
		public String getStringValue(byte[] data) {
			return isValid(data) ? Boolean.valueOf((data[0] & 0x01) == 1).toString() : "invalid";
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new BooleanSensorData(name, data[0] != 0);
		}
	},
	DATA_8BIT_BITMAP((byte) 0x18, 1, false) {
		@Override
		public String getStringValue(byte[] data) {
			return Integer.toBinaryString(Byte.toUnsignedInt(data[0]));
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, getStringValue(data));
		}
	},
	DATA_16BIT_BITMAP((byte) 0x19, 2, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, Integer.toBinaryString(
			        Short.toUnsignedInt(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort())));
		}
	},
	DATA_24BIT_BITMAP((byte) 0x1A, 3, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name,
			        Integer.toBinaryString(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(data))));
		}
	},
	DATA_32BIT_BITMAP((byte) 0x1B, 4, false) {
		@Override
		public String getStringValue(byte[] data) {
			return Integer.toBinaryString(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(data)));
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, getStringValue(data));
		}
	},
	DATA_40BIT_BITMAP((byte) 0x1C, 5, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name,
			        Long.toBinaryString(ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data))));
		}
	},
	DATA_48BIT_BITMAP((byte) 0x1D, 6, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name,
			        Long.toBinaryString(ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data))));
		}
	},
	DATA_56BIT_BITMAP((byte) 0x1E, 7, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name,
			        Long.toBinaryString(ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data))));
		}
	},
	DATA_64BIT_BITMAP((byte) 0x1F, 8, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name,
			        Long.toBinaryString(ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data))));
		}
	},
	DATA_UNSIGNED_8BIT_INT((byte) 0x20, 1, true) {
		@Override
		public boolean isValid(byte... value) {
			return (value[0] & 0xff) != 0xff;
		}

		@Override
		public String getStringValue(byte[] data) {
			return Integer.toString(Byte.toUnsignedInt(data[0]));
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			String method = "DATA_UNSIGNED_8BIT_INT";
			String hex = Hex.encodeHexString(data);
			int value = Byte.toUnsignedInt(data[0]);
			LOG.logInfo(method, "hex: %s, value: %d", hex, value);
			return new IntegerSensorData(name, value);
		}
	},
	DATA_UNSIGNED_16BIT_INT((byte) 0x21, 2, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isUnsignedValid(value);
		}

		@Override
		public String getStringValue(byte[] data) {
			return Integer.toString(Short.toUnsignedInt(ByteUtils.byteArrayToShort(ByteUtils.swapByteArray(data))));
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			String method = "DATA_UNSIGNED_16BIT_INT";
			String hex = Hex.encodeHexString(data);
			int value = Short.toUnsignedInt(ByteUtils.byteArrayToShort(ByteUtils.swapByteArray(data)));
			LOG.logInfo(method, "hex: %s, value: %d", hex, value);
			return new IntegerSensorData(name, value);
		}
	},
	DATA_UNSIGNED_24BIT_INT((byte) 0x22, 3, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isUnsignedValid(value);
		}

		@Override
		public String getStringValue(byte[] data) {
			return Integer.toUnsignedString(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(data)));
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			String method = "DATA_UNSIGNED_24BIT_INT";
			String hex = Hex.encodeHexString(data);
			int value = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(data));
			LOG.logInfo(method, "hex: %s, value: %d", hex, value);
			return new IntegerSensorData(name, value);
		}
	},
	DATA_UNSIGNED_32BIT_INT((byte) 0x23, 4, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isUnsignedValid(value);
		}

		@Override
		public String getStringValue(byte[] data) {
			return Long.toString(Integer.toUnsignedLong(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(data))));
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			String method = "DATA_UNSIGNED_32BIT_INT";
			String hex = Hex.encodeHexString(data);
			long value = Integer.toUnsignedLong(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getInt());
			LOG.logInfo(method, "hex: %s, value: %d", hex, value);
			return new LongSensorData(name, value);
		}
	},
	DATA_UNSIGNED_40BIT_INT((byte) 0x24, 5, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isUnsignedValid(value);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_UNSIGNED_48BIT_INT((byte) 0x25, 6, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isUnsignedValid(value);
		}

		@Override
		public String getStringValue(byte[] data) {
			return Long.toUnsignedString(ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_UNSIGNED_56BIT_INT((byte) 0x26, 7, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isUnsignedValid(value);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_UNSIGNED_64BIT_INT((byte) 0x27, 8, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isUnsignedValid(value);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name,
			        Long.toUnsignedString(ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data))));
		}
	},
	DATA_SIGNED_8BIT_INT((byte) 0x28, 1, true) {
		@Override
		public boolean isValid(byte... value) {
			return (value[0] & 0xff) != 0x80;
		}

		@Override
		public String getStringValue(byte[] data) {
			return Byte.toString(data[0]);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			String method = "DATA_SIGNED_8BIT_INT";
			String hex = Hex.encodeHexString(data);
			int value = (int) data[0];
			LOG.logInfo(method, "hex: %s, value: %d", hex, value);
			return new IntegerSensorData(name, value);
		}
	},

	DATA_SIGNED_16BIT_INT((byte) 0x29, 2, true) {
		@Override
		public String getStringValue(byte[] data) {
			return Short.toString(ByteUtils.byteArrayToShort(ByteUtils.swapByteArray(data)));
		}

		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isSignedValid(value);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			String method = "DATA_SIGNED_16BIT_INT";
			String hex = Hex.encodeHexString(data);
			int value = (int) ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort();
			LOG.logInfo(method, "hex: %s, value: %d", hex, value);
			return new IntegerSensorData(name, value);
		}
	},
	DATA_SIGNED_24BIT_INT((byte) 0x2A, 3, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isSignedValid(value);
		}

		@Override
		public String getStringValue(byte[] data) {
			return Integer.toString(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(data)));
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			String method = "DATA_SIGNED_24BIT_INT";
			String hex = Hex.encodeHexString(data);
			int value = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(data));
			LOG.logInfo(method, "hex: %s, value: %d", hex, value);
			return new IntegerSensorData(name, value);
		}
	},
	DATA_SIGNED_32BIT_INT((byte) 0x2B, 4, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isSignedValid(value);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			String method = "DATA_SIGNED_32BIT_INT";
			String hex = Hex.encodeHexString(data);
			int value = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getInt();
			LOG.logInfo(method, "hex: %s, value: %d", hex, value);
			return new IntegerSensorData(name, value);
		}
	},
	DATA_SIGNED_40BIT_INT((byte) 0x2C, 5, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isSignedValid(value);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_SIGNED_48BIT_INT((byte) 0x2D, 6, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isSignedValid(value);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_SIGNED_56BIT_INT((byte) 0x2E, 7, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isSignedValid(value);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_SIGNED_64BIT_INT((byte) 0x2F, 8, true) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isSignedValid(value);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name, ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_8BIT_ENUMERATION((byte) 0x30, 1, false) {
		@Override
		public boolean isValid(byte... value) {
			return (value[0] & 0xff) != 0xff;
		}

		@Override
		public String getStringValue(byte[] data) {
			return Integer.toString(Byte.toUnsignedInt(data[0]));
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new IntegerSensorData(name, Byte.toUnsignedInt(data[0]));
		}
	},
	DATA_16BIT_ENUMERATION((byte) 0x31, 2, false) {
		@Override
		public boolean isValid(byte... value) {
			return ZclDataType.isUnsignedValid(value);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new IntegerSensorData(name,
			        Short.toUnsignedInt(ByteUtils.byteArrayToShort(ByteUtils.swapByteArray(data))));
		}
	},
	DATA_SEMI_PRECISION((byte) 0x38, 2, true) {
		@Override
		public boolean isValid(byte... value) {
			return (value[1] & 0b00111110) != 62 || value[0] == 0 && (value[1] & 0b11000000) == 0;
		}

		public SensorData<?> toData(String name, byte... data) {
			return new FloatSensorData(name, (float) (Math.pow(-1.0D, data[1] >> 7)
			        * (data[0] + (data[1] & 0b00000011) * 256) * Math.pow(2, (data[0] >> 2) & 0b00011111)),
			        EngineConstants.FORMAT_DECIMAL_2);
		}
	},
	DATA_SINGLE_PRECISION((byte) 0x39, 4, true) {
		@Override
		public boolean isValid(byte... value) {
			return !Float.isNaN(ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getFloat());
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new FloatSensorData(name, ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat(),
			        EngineConstants.FORMAT_DECIMAL_2);
		}
	},
	DATA_DOUBLE_PRECISION((byte) 0x3A, 8, true) {
		@Override
		public boolean isValid(byte... value) {
			return !Double.isNaN(ByteBuffer.wrap(value).getDouble());
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new DoubleSensorData(name, ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getDouble(),
			        EngineConstants.FORMAT_DECIMAL_2);
		}
	},
	DATA_OCTET_STRING((byte) 0x41, null, false) {
		@Override
		public RawValue getRawValue(ByteBuffer buffer) {
			return getShortValue(buffer);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, Hex.encodeHexString(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_CHARACTER_STRING((byte) 0x42, null, false) {
		@Override
		public RawValue getRawValue(ByteBuffer buffer) {
			return getShortValue(buffer);
		}

		@Override
		public String getStringValue(byte[] data) {
			return new String(data, StandardCharsets.UTF_8);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, new String(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_LONG_OCTET_STRING((byte) 0x43, null, false) {
		@Override
		public RawValue getRawValue(ByteBuffer buffer) {
			return getLongValue(buffer);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, Hex.encodeHexString(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_LONG_CHARACTER_STRING((byte) 0x44, null, false) {
		@Override
		public RawValue getRawValue(ByteBuffer buffer) {
			return getLongValue(buffer);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, new String(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_ARRAY((byte) 0x48, null, false) {
		@Override
		public RawValue getRawValue(ByteBuffer buffer) {
			return getLongValue(buffer);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, Hex.encodeHexString(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_STRUCTURE((byte) 0x4C, null, false) {
		@Override
		public RawValue getRawValue(ByteBuffer buffer) {
			return getLongValue(buffer);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, Hex.encodeHexString(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_SET((byte) 0x50, null, false) {
		@Override
		public RawValue getRawValue(ByteBuffer buffer) {
			return getLongValue(buffer);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, Hex.encodeHexString(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_BAG((byte) 0x51, null, false) {
		@Override
		public RawValue getRawValue(ByteBuffer buffer) {
			return getLongValue(buffer);
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, Hex.encodeHexString(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_TIME_OF_DAY((byte) 0xE0, 4, true) {
		@Override
		public boolean isValid(byte... value) {
			return ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)) != 0xffffffff;
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, String.format("%d:%d:%d.%02d", data[3], data[2], data[1], data[0]));
		}
	},
	DATA_DATE((byte) 0xE1, 4, true) {
		@Override
		public boolean isValid(byte... value) {
			return ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)) != 0xffffffff;
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, String.format("%d-%d-%dW%d", data[3] + 1900, data[2], data[1], data[0]));
		}
	},
	DATA_UTC_TIME((byte) 0xE2, 4, true) {
		@Override
		public boolean isValid(byte... value) {
			return ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)) != 0xffffffff;
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new LongSensorData(name,
			        Integer.toUnsignedLong(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getInt()));
		}
	},
	DATA_CLUSTER_ID((byte) 0xE8, 2, false) {
		@Override
		public boolean isValid(byte... value) {
			return ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)) != 0xffff;
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new IntegerSensorData(name,
			        Short.toUnsignedInt(ByteUtils.byteArrayToShort(ByteUtils.swapByteArray(data))));
		}
	},
	DATA_ATTRIBUTE_ID((byte) 0xE9, 2, false) {
		@Override
		public boolean isValid(byte... value) {
			return ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)) != 0xffff;
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new IntegerSensorData(name,
			        Short.toUnsignedInt(ByteUtils.byteArrayToShort(ByteUtils.swapByteArray(data))));
		}
	},
	DATA_BACNET_OID((byte) 0xEA, 4, false) {
		@Override
		public boolean isValid(byte... value) {
			return ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)) != 0xffffffff;
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, Hex.encodeHexString(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_IEEE_ADDR((byte) 0xF0, 8, false) {
		@Override
		public boolean isValid(byte... value) {
			return ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(value)) != 0xffffffffffffffffL;
		}

		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, Hex.encodeHexString(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_128BIT_SECURITY_KEY((byte) 0xF1, 16, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return new StringSensorData(name, Hex.encodeHexString(ByteUtils.swapByteArray(data)));
		}
	},
	DATA_UNKNOWN((byte) 0xFF, 0, false) {
		@Override
		public SensorData<?> toData(String name, byte... data) {
			return null;
		}
	};

	static RawValue getShortValue(ByteBuffer buffer) {
		byte length = buffer.get();
		RawValue result;
		if ((length & 0xff) == 0xff) {
			result = new RawValue(null, false);
		} else {
			byte[] values = new byte[length];
			buffer.get(values);
			result = new RawValue(values, true);
		}
		return result;
	}

	static RawValue getLongValue(ByteBuffer buffer) {
		short length = buffer.getShort();
		RawValue result;
		if ((length & 0xffff) == 0xffff) {
			result = new RawValue(null, false);
		} else {
			byte[] values = new byte[length];
			buffer.get(values);
			result = new RawValue(values, true);
		}
		return result;
	}

	static Loggable LOG = new Loggable(ZclDataType.class.getName()) {
	};
	private final byte id;
	private final Integer length;
	private final boolean analog;

	ZclDataType(byte id, Integer length, boolean analog) {
		this.id = id;
		this.length = length;
		this.analog = analog;
	}

	public int getId() {
		return id;
	}

	public RawValue getRawValue(ByteBuffer buffer) {
		byte[] value = new byte[length];
		buffer.get(value);
		return new RawValue(value, isValid(value));
	}

	public String getStringValue(byte[] data) {
		return null;
	}

	public boolean isValid(byte... value) {
		return true;
	}

	public boolean isAnalog() {
		return analog;
	}

	public Integer getLength() {
		return length;
	}

	static boolean isUnsignedValid(byte[] bytes) {
		for (byte value : bytes) {
			if ((value & 0xff) != 0xff) {
				return true;
			}
		}
		return false;
	}

	static boolean isSignedValid(byte[] bytes) {
		if ((bytes[0] & 0xff) != 0x80) {
			return true;
		}
		for (int i = 1, length = bytes.length; i < length; i++) {
			byte value = bytes[i];
			if (value != 0x00) {
				return true;
			}
		}
		return false;
	}

	public static ZclDataType findById(int id) {
		for (ZclDataType zclDataType : values()) {
			if (zclDataType.id == id) {
				return zclDataType;
			}
		}
		return null;
	}

	public abstract SensorData<?> toData(String name, byte... data);
}
