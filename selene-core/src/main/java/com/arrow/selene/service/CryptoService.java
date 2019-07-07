package com.arrow.selene.service;

import org.apache.commons.lang3.Validate;

import com.arrow.acs.AcsUtils;
import com.arrow.pegasus.security.Crypto;
import com.arrow.pegasus.security.CryptoClient128Impl;
import com.arrow.pegasus.security.CryptoClientImpl;
import com.arrow.pegasus.security.CryptoClientUniversalImpl;
import com.arrow.pegasus.security.CryptoMode;
import com.arrow.selene.Loggable;
import com.arrow.selene.SeleneConstants;
import com.arrow.selene.SeleneException;

public class CryptoService extends Loggable {
	private static final String RAW = "raw:";

	private static class SingletonHolder {
		private static final CryptoService SINGLETON = new CryptoService();
	}

	public static CryptoService getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private Crypto crypto;

	private CryptoService() {
	}

	public void init(String mode) {
		String method = "init";

		CryptoMode cryptoMode = CryptoMode.valueOf(mode);

		// backward compatible
		if (Boolean.getBoolean(SeleneConstants.ENV_SECURITY_AES128)) {
			cryptoMode = CryptoMode.AES_128;
		}

		logInfo(method, "CryptoMode: %s", cryptoMode.name());

		switch (cryptoMode) {
		case AES_128:
			crypto = new CryptoClient128Impl();
			break;
		case AES_256:
			crypto = new CryptoClientImpl();
			break;
		case AES_UNIVERSAL:
			crypto = new CryptoClientUniversalImpl();
			break;
		default:
			throw new SeleneException("CryptoMode not supported: " + cryptoMode.name());
		}
		logInfo(method, "done!");
	}

	public String encrypt(String data) {
		data = AcsUtils.trimToEmpty(data);
		return getCrypto().internalEncrypt(data);
	}

	public String decrypt(String data) {
		data = AcsUtils.trimToEmpty(data);
		return data.startsWith(RAW) ? data.substring(4) : getCrypto().internalDecrypt(data);
	}

	public String hash(String data) {
		return getCrypto().internalHash(data);
	}

	private Crypto getCrypto() {
		Validate.notNull(crypto, "Crypto Engine is not initialized!");
		return crypto;
	}
}
