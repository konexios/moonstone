package com.arrow.pegasus.security;

final class SecretOne implements Secret {

	static int magic(CryptoMode mode, int line, char c) {
		return count(mode, find(mode, line), c);
	}

	static int count(CryptoMode mode, String data, char c) {
		int result = 0;
		int max = random(mode).numLines();
		for (int i = 0; i < max; i++) {
			if (c == data.charAt(i)) {
				result++;
			}
		}
		return result;
	}

	static String find(CryptoMode mode, int line) {
		return random(mode).find(line);
	}

	static char find(CryptoMode mode, int a, int b) {
		char c = 0;
		for (int i = 0; i < 5; i++) {
			c = find(mode, trim(mode, a)).charAt(trim(mode, b));
			b = a;
			a = c;
		}
		return c;
	}

	static int trim(CryptoMode mode, int a) {
		return random(mode).trim(a);
	}

	static RandomLoader random(CryptoMode mode) {
		if (mode == CryptoMode.AES_UNIVERSAL) {
			if (universalRandom == null) {
				loadUniversalRandom();
			}
			return universalRandom;
		} else {
			if (classicRandom == null) {
				loadClassicRandom();
			}
			return classicRandom;
		}
	}

	synchronized static void loadClassicRandom() {
		if (classicRandom == null) {
			classicRandom = new RandomLoader(SecretOne.class.getResourceAsStream(Crypto.RANDOM_FILE),
					Crypto.RANDOM_FILE_NUM_LINES);
		}
	}

	synchronized static void loadUniversalRandom() {
		if (universalRandom == null) {
			universalRandom = new RandomLoader(SecretOne.class.getResourceAsStream(Crypto.RANDOM_UNIVERSAL_FILE),
					Crypto.RANDOM_UNIVERSAL_FILE_NUM_LINES);
		}
	}

	private SecretOne() {
	}

	private static RandomLoader classicRandom;
	private static RandomLoader universalRandom;
}
