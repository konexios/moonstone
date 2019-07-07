package com.arrow.pegasus.security;

final class SecretFive implements Secret {
	static String magic1(CryptoMode mode, int w, int x, int y, int z) {
		return SecretFour.magic1(mode, _f1(mode, w, x, y, z));
	}

	static String magic2(CryptoMode mode, String salt, int x, int y) {
		StringBuilder sb = new StringBuilder();
		if (salt != null && salt.length() > 0) {
			for (int i = salt.length() - 1; i >= 0; i--) {
				sb.append(salt.charAt(i));
			}
		}
		sb.append(SecretOne.find(mode, x, y));
		return sb.toString();
	}

	private static int _f1(CryptoMode mode, int w, int x, int y, int z) {
		return (SecretOne.magic(mode, w, 'o') + SecretOne.magic(mode, x, 'a'))
		        * (SecretOne.magic(mode, y, 'n') + SecretOne.magic(mode, z, 'h'));
	}

	private SecretFive() {
	}
}
