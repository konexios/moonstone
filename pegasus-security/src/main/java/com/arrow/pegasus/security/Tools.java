package com.arrow.pegasus.security;

import java.security.SecureRandom;
import java.util.Arrays;

public class Tools {
	private final static String ALPHA_NUMERICS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private final static SecureRandom RANDOM = new SecureRandom();

	public static void main(String[] args) {
		if (args == null || args.length < 1)
			syntaxError();
		String action = args[0];
		if (!Arrays.asList("encrypt", "decrypt", "generate-random", "generate-universal").contains(action))
			syntaxError();

		if (Arrays.asList("encrypt", "decrypt").contains(action)) {
			String type = args[1];
			String size = args[2];
			String data = args[3];
			if (!Arrays.asList("server", "client").contains(type))
				syntaxError();
			if (!Arrays.asList("128", "256", "universal").contains(size))
				syntaxError();
			if (type.equals("server") && !size.equals("256")) {
				System.err.println("server mode only supports 256 bits!");
				syntaxError();
			}
			String result = "";
			if (action.equals("encrypt")) {
				if (type.equals("server")) {
					result = new CryptoImpl().internalEncrypt(data);
				} else {
					if (size.equals("128")) {
						result = new CryptoClient128Impl().internalEncrypt(data);
					} else if (size.equals("256")) {
						result = new CryptoClientImpl().internalEncrypt(data);
					} else {
						result = new CryptoClientUniversalImpl().internalEncrypt(data);
					}
				}
			} else if (action.equals("decrypt")) {
				if (type.equals("server")) {
					result = new CryptoImpl().internalDecrypt(data);
				} else {
					if (size.equals("128")) {
						result = new CryptoClient128Impl().internalDecrypt(data);
					} else if (size.equals("256")) {
						result = new CryptoClientImpl().internalDecrypt(data);
					} else {
						result = new CryptoClientUniversalImpl().internalDecrypt(data);
					}
				}
			}
			System.out.println("result: " + result);
		} else if (action.equals("generate-random")) {
			for (int i = 0; i < Crypto.RANDOM_FILE_NUM_LINES; i++) {
				System.out.println(nextAlphaNumeric(2048));
			}
		} else if (action.equals("generate-universal")) {
			for (int i = 0; i < Crypto.RANDOM_UNIVERSAL_FILE_NUM_LINES; i++) {
				System.out.println(nextAlphaNumeric(2048));
			}
		}
	}

	private static void syntaxError() {
		System.err.println(
				"Syntax: [encrypt,decrypt,generate-random,generate-universal] [server,client] [128,256,universal] [data]");
		System.exit(1);
	}

	private static String nextAlphaNumeric(int size) {
		return RANDOM.ints(size, 0, ALPHA_NUMERICS.length()).mapToObj(i -> ALPHA_NUMERICS.charAt(i))
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
	}
}
