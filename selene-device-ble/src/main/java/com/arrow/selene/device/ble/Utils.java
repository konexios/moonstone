package com.arrow.selene.device.ble;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utils {

	public static class LittleEndian {

		/**
		 * Returns the short value for the specified bytes array in little
		 * endian format, from a specified index start in the array and 2 bytes
		 * length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @param start
		 *            start index in the array of the value to convert
		 * @return the short value converted
		 */
		public static short bytesToInt16(byte[] arr, int start) {
			return ByteBuffer.wrap(arr, start, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
		}

		/**
		 * Returns the short value for the specified bytes array in little
		 * endian format, from index 0 in the array and 2 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @return the short value converted
		 */
		public static short bytesToInt16(byte[] arr) {
			return bytesToInt16(arr, 0);
		}

		/**
		 * Returns the int value for the specified bytes array in little endian
		 * format, from a specified index start in the array and 4 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @param start
		 *            start index in the array of the value to convert
		 * @return the int value contained in the array
		 */
		public static int bytesToInt32(byte[] arr, int start) {
			return ByteBuffer.wrap(arr, start, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
		}

		/**
		 * Returns the int value for the specified bytes array in little endian
		 * format, from index 0 in the array and 4 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @return the int value contained in the array
		 */
		public static int bytesToInt32(byte[] arr) {
			return bytesToInt32(arr, 0);
		}

		/**
		 * Returns the unsigned short value for the specified bytes array in
		 * little endian format, from a specified index start in the array and 2
		 * bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @param start
		 *            start index in the array of the value to convert
		 * @return the unsigned short value converted
		 */
		public static int bytesToUInt16(byte[] arr, int start) {
			return Short.toUnsignedInt(ByteBuffer.wrap(arr, start, 2).order(ByteOrder.LITTLE_ENDIAN).getShort());
		}

		/**
		 * Returns the unsigned short value for the specified bytes array in
		 * little endian format, from index 0 in the array and 2 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @return the unsigned short value converted
		 */
		public static int bytesToUInt16(byte[] arr) {
			return bytesToUInt16(arr, 0);
		}

		/**
		 * Returns the unsigned int value for the specified bytes array in
		 * little endian format, from a specified index start in the array and 4
		 * bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @param start
		 *            start index in the array of the value to convert
		 * @return the unsigned int value contained in the array
		 */
		public static long bytesToUInt32(byte[] arr, int start) {
			return Integer.toUnsignedLong(ByteBuffer.wrap(arr, start, 4).order(ByteOrder.LITTLE_ENDIAN).getInt());
		}

		/**
		 * Returns the unsigned int value for the specified bytes array in
		 * little endian format, from index 0 in the array and 4 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @return the unsigned int value contained in the array
		 */
		public static long bytesToUInt32(byte[] arr) {
			return bytesToUInt32(arr, 0);
		}

		/**
		 * Returns the float value for the specified bytes array in little
		 * endian format, from a specified index start in the array and 4 bytes
		 * length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @param start
		 *            start index in the array of the value to convert
		 * @return the float value contained in the array
		 */
		public static float bytesToFloat(byte[] arr, int start) {
			return Float.intBitsToFloat(bytesToInt32(arr, start));
		}

		/**
		 * Returns the float value for the specified bytes array in little
		 * endian format, from index 0 in the array and 4 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @return the float value contained in the array
		 */
		public static float bytesToFloat(byte[] arr) {
			return bytesToFloat(arr, 0);
		}

		/**
		 * Returns the bytes array in little endian format of the value to
		 * convert.
		 *
		 * @param value
		 *            the short value to convert
		 * @return the bytes array in little endian of the value, the array is 2
		 *         bytes length
		 */
		public static byte[] int16ToBytes(short value) {
			return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value).array();
		}

		/**
		 * Returns the bytes array in little endian format of the value to
		 * convert.
		 *
		 * @param value
		 *            the int value to convert
		 * @return the bytes array in little endian of the value, the array is 4
		 *         bytes length
		 */
		public static byte[] int32ToBytes(int value) {
			return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
		}

		/**
		 * Returns the bytes array in little endian format of the value to
		 * convert.
		 *
		 * @param value
		 *            the unsigned short value to convert
		 * @return the bytes array in little endian of the value, the array is 2
		 *         bytes length
		 */
		public static byte[] uint16ToBytes(int value) {
			return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) (value & 0xFFFF)).array();
		}

		/**
		 * Returns the bytes array in little endian format of the value to
		 * convert.
		 *
		 * @param value
		 *            the unsigned int value to convert
		 * @return the bytes array in little endian of the value, the array is 4
		 *         bytes length
		 */
		public static byte[] uint32ToBytes(long value) {
			return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt((int) (value & 0xFFFFFFFFL)).array();
		}

		/**
		 * Returns the bytes array in little endian format of the value to
		 * convert.
		 *
		 * @param value
		 *            the float value to convert
		 * @return the bytes array in little endian of the value, the array is 4
		 *         bytes length
		 */
		public static byte[] floatToBytes(float value) {
			return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value).array();
		}
	}

	/**
	 * This class implements the conversion for bytes array to different formats
	 * values types and from values types to byte array for Big endian base
	 * order
	 * <p>
	 * 
	 * @author STMicroelectronics - Central Labs.
	 * @version 1.0, 17 Feb 2015
	 */
	public static class BigEndian {
		/**
		 * Returns the short value for the specified bytes array in big endian
		 * format, from a specified index start in the array and 2 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @param start
		 *            start index in the array of the value to convert
		 * @return the short value converted
		 */
		public static short bytesToInt16(byte[] arr, int start) {
			return ByteBuffer.wrap(arr, start, 2).order(ByteOrder.BIG_ENDIAN).getShort();
		}

		/**
		 * Returns the short value for the specified bytes array in big endian
		 * format, from index 0 in the array and 2 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @return the short value converted
		 */
		public static short bytesToInt16(byte[] arr) {
			return bytesToInt16(arr, 0);
		}

		/**
		 * Returns the int value for the specified bytes array in big endian
		 * format, from a specified index start in the array and 4 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @param start
		 *            start index in the array of the value to convert
		 * @return the int value contained in the array
		 */
		public static int bytesToInt32(byte[] arr, int start) {
			return ByteBuffer.wrap(arr, start, 4).order(ByteOrder.BIG_ENDIAN).getInt();
		}

		/**
		 * Returns the int value for the specified bytes array in big endian
		 * format, from index 0 in the array and 4 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @return the int value contained in the array
		 */
		public static int bytesToInt32(byte[] arr) {
			return bytesToInt32(arr, 0);
		}

		/**
		 * Returns the unsigned short value for the specified bytes array in big
		 * endian format, from a specified index start in the array and 2 bytes
		 * length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @param start
		 *            start index in the array of the value to convert
		 * @return the unsigned short value converted
		 */
		public static int bytesToUInt16(byte[] arr, int start) {
			return Short.toUnsignedInt(ByteBuffer.wrap(arr, start, 2).order(ByteOrder.BIG_ENDIAN).getShort());
		}

		/**
		 * Returns the unsigned short value for the specified bytes array in big
		 * endian format, from index 0 in the array and 2 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @return the unsigned short value converted
		 */
		public static int bytesToUInt16(byte[] arr) {
			return bytesToUInt16(arr, 0);
		}

		/**
		 * Returns the unsigned int value for the specified bytes array in big
		 * endian format, from a specified index start in the array and 4 bytes
		 * length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @param start
		 *            start index in the array of the value to convert
		 * @return the unsigned int value contained in the array
		 */
		public static long bytesToUInt32(byte[] arr, int start) {
			return Integer.toUnsignedLong(ByteBuffer.wrap(arr, start, 4).order(ByteOrder.BIG_ENDIAN).getInt());
		}

		/**
		 * Returns the unsigned int value for the specified bytes array in big
		 * endian format, from index 0 in the array and 4 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @return the unsigned int value contained in the array
		 */
		public static long bytesToUInt32(byte[] arr) {
			return bytesToUInt32(arr, 0);
		}

		/**
		 * Returns the float value for the specified bytes array in big endian
		 * format, from a specified index start in the array and 4 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @param start
		 *            start index in the array of the value to convert
		 * @return the float value contained in the array
		 */
		public static float bytesToFloat(byte[] arr, int start) {
			return Float.intBitsToFloat(bytesToInt32(arr, start));
		}

		/**
		 * Returns the float value for the specified bytes array in big endian
		 * format, from index 0 in the array and 4 bytes length.
		 *
		 * @param arr
		 *            input bytes array that contains the value to convert
		 * @return the float value contained in the array
		 */
		public static float bytesToFloat(byte[] arr) {
			return bytesToFloat(arr, 0);
		}

		/**
		 * Returns the bytes array in little endian format of the value to
		 * convert.
		 *
		 * @param value
		 *            the short value to convert
		 * @return the bytes array in big endian of the value, the array is 2
		 *         bytes length
		 */
		public static byte[] int16ToBytes(short value) {
			return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(value).array();
		}

		/**
		 * Returns the bytes array in big endian format of the value to convert.
		 *
		 * @param value
		 *            the int value to convert
		 * @return the bytes array in big endian of the value, the array is 4
		 *         bytes length
		 */
		public static byte[] int32ToBytes(int value) {
			return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(value).array();
		}

		/**
		 * Returns the bytes array in big endian format of the value to convert.
		 *
		 * @param value
		 *            the unsigned short value to convert
		 * @return the bytes array in big endian of the value, the array is 2
		 *         bytes length
		 */
		public static byte[] uint16ToBytes(int value) {
			return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort((short) (value & 0xFFFF)).array();
		}

		/**
		 * Returns the bytes array in big endian format of the value to convert.
		 *
		 * @param value
		 *            the unsigned int value to convert
		 * @return the bytes array in big endian of the value, the array is 4
		 *         bytes length
		 */
		public static byte[] uint32ToBytes(long value) {
			return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt((int) (value & 0xFFFFFFFFL)).array();
		}

		/**
		 * Returns the bytes array in big endian format of the value to convert.
		 *
		 * @param value
		 *            the float value to convert
		 * @return the bytes array in big endian of the value, the array is 4
		 *         bytes length
		 */
		public static byte[] floatToBytes(float value) {
			return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putFloat(value).array();
		}
	}

}
