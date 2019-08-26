package moonstone.selene.engine;

import java.util.Vector;

import org.freedesktop.dbus.Variant;

import moonstone.selene.Loggable;

public class DbusUtils {
	private static final Loggable LOGGER = new Loggable();
	private static final Object[] EMPTY_OBJECTS_ARRAY = new Object[0];
	private static final String[] EMPTY_STRINGS_ARRAY = new String[0];
	private static final byte[] EMPTY_BYTES_ARRAY = new byte[0];

	public static String getString(Variant<?> variant) {
		return variant == null ? null : variant.getValue().toString();
	}

	public static Long getLong(Variant<?> variant) {
		String method = "getLong";
		Long result = null;
		if (variant != null) {
			try {
				result = Long.parseLong(variant.getValue().toString());
			} catch (Exception e) {
				LOGGER.logError(method, e);
			}
		}
		return result;
	}

	public static Object[] getStruct(Variant<?> variant) {
		String method = "getStruct";
		Object[] result = EMPTY_OBJECTS_ARRAY;
		if (variant != null) {
			try {
				result = (Object[]) variant.getValue();
			} catch (Exception e) {
				LOGGER.logError(method, e);
			}
		}
		return result;
	}

	public static String[] getStringArray(Variant<?> variant) {
		String method = "getStringArray";
		String[] result = EMPTY_STRINGS_ARRAY;
		if (variant != null) {
			try {
				@SuppressWarnings("unchecked")
				Vector<String> vector = (Vector<String>) variant.getValue();
				result = vector.toArray(new String[vector.size()]);
			} catch (Exception e) {
				LOGGER.logError(method, e);
			}
		}
		return result;
	}

	public static byte[] getByteArray(Variant<?> variant) {
		String method = "getByeArray";
		byte[] result = EMPTY_BYTES_ARRAY;
		if (variant != null) {
			try {
				result = (byte[]) variant.getValue();
			} catch (Exception e) {
				LOGGER.logError(method, e);
			}
		}
		return result;
	}
}
