package com.arrow.pegasus.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public abstract class CryptoAbstract implements Crypto {

	public CryptoAbstract(CryptoMode mode) {
		this.mode = mode;
	}

	@Override
	public String encrypt(String data, String privateKey) {
		return _encrypt(data, Base64.getDecoder().decode(privateKey.getBytes(UTF_8)));
	}

	@Override
	public String decrypt(String data, String privateKey) {
		return _decrypt(data, Base64.getDecoder().decode(privateKey.getBytes(UTF_8)));
	}

	@Override
	public String hash(String data, String salt) {
		return _hash(data, salt, HASH_RECURSIVE);
	}

	@Override
	public String internalEncrypt(String data) {
		return _scramble(_encrypt(data, _privateKey()));
	}

	@Override
	public String internalDecrypt(String data) {
		return _decrypt(_unscramble(data), _privateKey());
	}

	@Override
	public String internalHash(String data) {
		return hash(data, _secretSalt());
	}

	@Override
	public String hashId(String id) {
		try {
			return DatatypeConverter.printHexBinary(MessageDigest.getInstance(SHA1).digest(id.getBytes(UTF_8)))
			        .toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String hmac(String data, String privateKey) {
		Objects.requireNonNull(data, "data is NULL");
		try {
			Mac hmacSHA256 = Mac.getInstance(HMAC_ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(privateKey.getBytes(), HMAC_ALGORITHM);
			hmacSHA256.init(keySpec);
			return Base64.getEncoder().encodeToString(hmacSHA256.doFinal(data.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String randomToken() {
		return _hash(new String(_createRandomSalt(RANDOM_SALT_SIZE), UTF_8));
	}

	@Override
	public String createPrivateKey() {
		return Base64.getEncoder().encodeToString(_createRandomSalt(_keySize()));
	}

	protected String _encrypt(String value, byte[] privateKey) {
		try {
			Objects.requireNonNull(value, "value is NULL");

			byte[] ivBytes = _createRandomSalt(IV_SIZE);
			IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

			SecretKeySpec keySpec = new SecretKeySpec(privateKey, ENCRYPTION_ALGORITHM);

			Cipher cipher = Cipher.getInstance(CIPHER);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

			byte[] encrypted = cipher.doFinal(value.getBytes(UTF_8));

			return String.format("%s%s", Base64.getEncoder().encodeToString(ivBytes),
			        Base64.getEncoder().encodeToString(encrypted));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String _decrypt(String value, byte[] privateKey) {
		if (value == null || value.length() == 0) {
			return null;
		}
		try {
			if (value.length() <= BASE64_IV_SIZE)
				throw new IllegalArgumentException("Invalid encrypted value");

			String salt = value.substring(0, BASE64_IV_SIZE);
			IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(salt.getBytes(UTF_8)));

			SecretKeySpec keySpec = new SecretKeySpec(privateKey, ENCRYPTION_ALGORITHM);

			Cipher cipher = Cipher.getInstance(CIPHER);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

			byte[] encrypted = Base64.getDecoder().decode(value.substring(BASE64_IV_SIZE).getBytes(UTF_8));
			byte[] decrypted = cipher.doFinal(encrypted);

			return new String(decrypted, UTF_8);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String _hash(String value) {
		try {
			return DatatypeConverter.printHexBinary(MessageDigest.getInstance(SHA256).digest(value.getBytes(UTF_8)))
			        .toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	protected byte[] _createRandomSalt(int length) {
		byte[] result = new byte[length];
		random.nextBytes(result);
		return result;
	}

	protected int _keySize() {
		return mode == CryptoMode.AES_256 ? KEY_SIZE_256 : KEY_SIZE_128;
	}

	protected CryptoMode _getMode() {
		return mode;
	}

	protected abstract String _hash(String password, String salt, int iteration);

	protected abstract byte[] _privateKey();

	protected abstract String _secretSalt();

	protected abstract String _unscramble(String data);

	protected abstract String _scramble(String data);

	protected final static String ENCRYPTION_ALGORITHM = "AES";
	protected final static String HMAC_ALGORITHM = "HmacSHA256";
	protected final static String CIPHER = "AES/CBC/PKCS5PADDING";
	protected final static String SHA256 = "SHA-256";
	protected final static String SHA1 = "SHA-1";
	protected final static int KEY_SIZE_256 = 32;
	protected final static int KEY_SIZE_128 = 16;
	protected final static int IV_SIZE = 16;
	protected final static int BASE64_IV_SIZE = 24;
	protected final static int HASH_RECURSIVE = 5;
	protected final static int RANDOM_SALT_SIZE = 64;

	protected SecureRandom random = new SecureRandom();
	private final CryptoMode mode;
}
