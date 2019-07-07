package com.arrow.pegasus.security;

import java.util.Base64;

final class SecretTwo implements Secret {

	static String magic1(CryptoMode mode, int a, int b) {
		return Base64.getEncoder().encodeToString(magic2(SecretOne.find(mode, SecretOne.trim(mode, a)).getBytes(UTF_8),
		        SecretOne.find(mode, SecretOne.trim(mode, b)).getBytes(UTF_8)));
	}

	static byte[] magic2(byte[] data, byte[] mask) {
		byte[] result = new byte[data.length];
		int dataSize = data.length;
		int maskSize = mask.length;
		for (int i = 0; i < dataSize; i++) {
			result[i] = (byte) (data[i] ^ mask[(i * 2 + 1) % maskSize]);
		}
		return result;
	}

	private SecretTwo() {
	}
}
