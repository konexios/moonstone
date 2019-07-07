package com.arrow.pegasus.security;

import java.util.Base64;

final class SecretThree implements Secret {

	static String magic1(CryptoMode mode, String data) {
		int length = _f1(mode).length();
		return _f2(mode, Base64.getEncoder().encodeToString(
		        new StringBuilder().append(length).append(_f2(mode, data)).append(length).toString().getBytes(UTF_8)));
	}

	private static String _f1(CryptoMode mode) {
		int a = SecretOne.magic(mode, 129, 't');
		int b = SecretOne.magic(mode, 814, 'a');
		int c = SecretOne.magic(mode, 298, 'm');
		return new StringBuilder().append(SecretTwo.magic1(mode, a, b)).append(SecretTwo.magic1(mode, b, c))
		        .append(SecretTwo.magic1(mode, c, a)).toString();
	}

	private static String _f2(CryptoMode mode, String data) {
		int a = 0;
		int b = 0;
		for (int i = 0; i < data.length(); i++) {
			char ch = data.charAt(i);
			if (Character.isDigit(ch)) {
				a++;
			} else if (Character.isAlphabetic(ch)) {
				b++;
			}
		}
		return SecretTwo.magic1(mode, a, b);
	}

	private SecretThree() {
	}
}
