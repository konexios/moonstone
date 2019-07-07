package com.arrow.pegasus.security;

final class SecretSix implements Secret {

	static int magic1(int a) {
		return a * 2 + 9;
	}

	static int magic2(int a) {
		return a * 37 - 19;
	}

	static String magic3(CryptoMode mode, int a) {
		return SecretOne.find(mode, f4(a));
	}

	private static int f4(int a) {
		return a * 4 - 23;
	}

	private SecretSix() {
	}
}
