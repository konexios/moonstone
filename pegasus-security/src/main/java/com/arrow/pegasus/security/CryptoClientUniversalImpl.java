package com.arrow.pegasus.security;

import java.util.Base64;

public class CryptoClientUniversalImpl extends CryptoClientAbstract {

	public CryptoClientUniversalImpl() {
		super(CryptoMode.AES_UNIVERSAL);
	}

	protected String _unscramble(String data) {
		return new String(SecretTwo.magic2(Base64.getDecoder().decode(data.getBytes(UTF_8)),
		        SecretSix.magic3(_getMode(), 945).getBytes(UTF_8)));
	}

	protected String _scramble(String data) {
		return Base64.getEncoder().encodeToString(
		        SecretTwo.magic2(data.getBytes(UTF_8), SecretSix.magic3(_getMode(), 945).getBytes(UTF_8)));
	}
}
