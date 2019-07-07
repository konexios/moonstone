package com.arrow.pegasus.security;

import java.util.Base64;
import java.util.Objects;

public final class CryptoImpl extends CryptoAbstract {

	public CryptoImpl() {
		super(CryptoMode.AES_256);
	}

	public String encryptApplicationId(String applicationId) {
		Objects.requireNonNull(applicationId, "applicationId cannot be null");
		int count = _countLoop(applicationId);
		for (int i = 0; i < count; i++) {
			applicationId = internalHash(applicationId);
		}
		return applicationId;
	}

	public String encryptUserId(String applicationId, String userId) {
		Objects.requireNonNull(applicationId, "applicationId cannot be null");
		Objects.requireNonNull(userId, "userId cannot be null");
		int count = _countLoop(applicationId);
		for (int i = 0; i < count; i++) {
			userId = internalEncrypt(userId);
		}
		return userId;
	}

	public String decryptUserId(String applicationId, String userId) {
		Objects.requireNonNull(applicationId, "applicationId cannot be null");
		Objects.requireNonNull(userId, "userId cannot be null");
		int count = _countLoop(applicationId);
		for (int i = 0; i < count; i++) {
			userId = internalDecrypt(userId);
		}
		return userId;
	}

	@Override
	protected String _hash(String password, String salt, int iteration) {
		if (iteration == 0) {
			return password;
		} else {
			return _hash(_hash(String.format("%s%s%s", SecretFive.magic2(_getMode(), salt, 791, 615), password,
			        SecretFour.magic2(_getMode()))), salt, --iteration);
		}
	}

	@Override
	protected byte[] _privateKey() {
		String s1 = SecretFive.magic1(_getMode(), 129, 391, 20, 473);
		byte[] result = new byte[_keySize()];
		for (int i = 0; i < _keySize(); i++) {
			String s2 = SecretFour.magic1(_getMode(), i);
			int j = SecretSix.magic1(i) % s2.length();
			int k = SecretOne.count(_getMode(), s1, s2.charAt(j));
			int l = SecretSix.magic2(k) + 7;
			result[i] = (byte) l;
		}
		return result;
	}

	@Override
	protected String _secretSalt() {
		String value = Base64.getEncoder().encodeToString(_privateKey());
		String salt = Base64.getEncoder().encodeToString(
		        SecretTwo.magic2(value.getBytes(UTF_8), SecretOne.find(_getMode(), 512).getBytes(UTF_8)));
		return hash(value, salt);
	}

	@Override
	protected String _unscramble(String data) {
		return new String(SecretTwo.magic2(Base64.getDecoder().decode(data.getBytes(UTF_8)),
		        SecretSix.magic3(_getMode(), 387).getBytes(UTF_8)));
	}

	@Override
	protected String _scramble(String data) {
		return Base64.getEncoder().encodeToString(
		        SecretTwo.magic2(data.getBytes(UTF_8), SecretSix.magic3(_getMode(), 387).getBytes(UTF_8)));
	}

	private int _countLoop(String data) {
		int count = 0;
		if (data != null && data.length() > 0) {
			for (int i = 0; i < data.length(); i++) {
				count += data.charAt(i);
			}
		}
		return count % 5 + 3;
	}
}
