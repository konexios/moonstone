package com.arrow.pegasus.security;

final class SecretFour implements Secret {

	static String magic1(CryptoMode mode, int a) {
		return SecretTwo.magic1(mode, a + SecretThree.magic1(mode, _f1(mode)).length(), _f2(mode));
	}

	static String magic2(CryptoMode mode) {
		return new StringBuilder().append(SecretOne.find(mode, 129, 544)).append(SecretOne.find(mode, 814, 4))
		        .append(SecretOne.find(mode, 315, 215)).append(SecretOne.find(mode, 584, 3))
		        .append(SecretOne.find(mode, 142, 145)).append(SecretOne.find(mode, 645, 85))
		        .append(SecretOne.find(mode, 874, 48)).append(SecretOne.find(mode, 11, 43))
		        .append(SecretOne.find(mode, 98, 24)).append(SecretOne.find(mode, 28, 102))
		        .append(SecretOne.find(mode, 745, 444)).append(SecretOne.find(mode, 102, 666)).toString();
	}

	private static String _f1(CryptoMode mode) {
		int a = SecretOne.magic(mode, 129, 'a');
		int b = SecretOne.magic(mode, 814, 'i');
		int c = SecretOne.magic(mode, 298, 'd');
		int d = SecretOne.magic(mode, 548, 'a');
		int e = SecretOne.magic(mode, 108, 'n');
		return new StringBuilder().append(SecretTwo.magic1(mode, a, b)).append(SecretTwo.magic1(mode, b, c))
		        .append(SecretTwo.magic1(mode, c, d)).append(SecretTwo.magic1(mode, d, e))
		        .append(SecretTwo.magic1(mode, e, a)).toString();
	}

	private static int _f2(CryptoMode mode) {
		return (SecretOne.magic(mode, 190, 'h') + SecretOne.magic(mode, 945, 'i'))
		        * (SecretOne.magic(mode, 152, 'e') + SecretOne.magic(mode, 647, 'n'));
	}

	private SecretFour() {
	}
}
