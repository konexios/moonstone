package com.arrow.pegasus.security;

import java.util.Base64;

public class CryptoClient128Impl extends CryptoClientAbstract {

	public CryptoClient128Impl() {
		super(CryptoMode.AES_128);
	}

	@Override
	protected String _unscramble(String data) {
		return new String(SecretTwo.magic2(Base64.getDecoder().decode(data.getBytes(UTF_8)),
		        SecretSix.magic3(_getMode(), 716).getBytes(UTF_8)));
	}

	@Override
	protected String _scramble(String data) {
		return Base64.getEncoder().encodeToString(
		        SecretTwo.magic2(data.getBytes(UTF_8), SecretSix.magic3(_getMode(), 716).getBytes(UTF_8)));
	}
}
