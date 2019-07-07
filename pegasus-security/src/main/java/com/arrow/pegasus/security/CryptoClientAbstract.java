package com.arrow.pegasus.security;

import java.util.Base64;

public abstract class CryptoClientAbstract extends CryptoAbstract {

	public CryptoClientAbstract(CryptoMode mode) {
		super(mode);
	}

	protected String _hash(String password, String salt, int iteration) {
		if (iteration == 0) {
			return password;
		} else {
			return _hash(_hash(String.format("%s%s%s", SecretFive.magic2(_getMode(), salt, 125, 493), password,
			        SecretFour.magic2(_getMode()))), salt, --iteration);
		}
	}

	protected byte[] _privateKey() {
		String s1 = SecretFive.magic1(_getMode(), 395, 914, 846, 740);
		byte[] result = new byte[_keySize()];
		for (int i = 0; i < _keySize(); i++) {
			String s2 = SecretFour.magic1(_getMode(), i);
			int j = SecretSix.magic1(i) % s2.length();
			int k = SecretOne.count(_getMode(), s1, s2.charAt(j));
			int l = SecretSix.magic2(k) + 11;
			result[i] = (byte) l;
		}
		return result;
	}

	protected String _secretSalt() {
		String value = Base64.getEncoder().encodeToString(_privateKey());
		String salt = Base64.getEncoder().encodeToString(
		        SecretTwo.magic2(value.getBytes(UTF_8), SecretOne.find(_getMode(), 179).getBytes(UTF_8)));
		return hash(value, salt);
	}

	@Override
	public String encryptApplicationId(String applicationId) {
		throw new RuntimeException("NOT IMPLEMENTED!");
	}

	@Override
	public String encryptUserId(String applicationId, String vaultId) {
		throw new RuntimeException("NOT IMPLEMENTED!");
	}

	@Override
	public String decryptUserId(String applicationId, String userId) {
		throw new RuntimeException("NOT IMPLEMENTED!");
	}
}
