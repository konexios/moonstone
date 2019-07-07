package com.arrow.pegasus.security;

public interface Crypto extends Secret {
	final static String SECRET_DIRECTORY_ENV = "moonstone.secret.directory";
	final static String SECRET_DIRECTORY = ".moonstone";
	final static String RANDOM_FILE = "random.dat";
	final static int RANDOM_FILE_NUM_LINES = 2048;
	final static String RANDOM_UNIVERSAL_FILE = "random-universal.dat";
	final static int RANDOM_UNIVERSAL_FILE_NUM_LINES = 50;

	String encrypt(String data, String privateKey);

	String decrypt(String data, String privateKey);

	String hash(String data, String salt);

	String internalEncrypt(String data);

	String internalDecrypt(String data);

	String internalHash(String data);

	String hashId(String id);

	String hmac(String data, String privateKey);

	String createPrivateKey();

	String randomToken();

	String encryptApplicationId(String applicationId);

	String encryptUserId(String applicationId, String userId);

	String decryptUserId(String applicationId, String userId);
}