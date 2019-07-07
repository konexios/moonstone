package com.arrow.selene.device.ble;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;

public class BleUtils {
    private static final Pattern SPACE = Pattern.compile(" ", Pattern.LITERAL);

    public static int unsignedToSigned(int unsigned, int bitLength) {
        if ((unsigned & 1 << bitLength - 1) != 0) {
            unsigned = -1 * ((1 << bitLength - 1) - (unsigned & (1 << bitLength - 1) - 1));
        }
        return unsigned;
    }

    public static String hexAsciiToString(String hex) {
        hex = SPACE.matcher(hex).replaceAll(Matcher.quoteReplacement(""));
        StringBuilder output = new StringBuilder();
        for (int i = 0, length = hex.length(); i < length; i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        s = SPACE.matcher(s).replaceAll(Matcher.quoteReplacement(""));
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static Integer byteUnsignedAtOffset(byte[] c, int offset) {
        return c[offset] & 0xFF;
    }

    public static Integer shortSignedAtOffset(byte[] c, int offset) {
        Integer lowerByte = c[offset] & 0xFF;
        Integer upperByte = (int) c[offset + 1];
        return (upperByte << 8) + lowerByte;
    }

    public static Integer shortUnsignedAtOffset(byte[] c, int offset) {
        Integer lowerByte = c[offset] & 0xFF;
        Integer upperByte = c[offset + 1] & 0xFF;
        return (upperByte << 8) + lowerByte;
    }

    public static Integer twentyFourBitUnsignedAtOffset(byte[] c, int offset) {
        Integer lowerByte = c[offset] & 0xFF;
        Integer mediumByte = c[offset + 1] & 0xFF;
        Integer upperByte = c[offset + 2] & 0xFF;
        return (upperByte << 16) + (mediumByte << 8) + lowerByte;
    }

    public static Integer thirtyTwoBitUnsignedAtOffset(byte[] c, int offset) {
        Integer firstByte = c[offset] & 0xFF;
        Integer secondByte = c[offset + 1] & 0xFF;
        Integer thirdByte = c[offset + 2] & 0xFF;
        Integer fourthByte = c[offset + 3] & 0xFF;
        return (fourthByte << 24) + (thirdByte << 16) + (secondByte << 8) + firstByte;
    }

    public static String formatMacAddress(byte[] address) {
        Validate.isTrue(address != null && address.length == 6, "invalid address");
        return String.format("%02X:%02X:%02X:%02X:%02X:%02X", address[5], address[4], address[3], address[2],
                address[1], address[0]);
    }
}
