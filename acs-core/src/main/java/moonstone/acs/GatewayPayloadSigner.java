/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package moonstone.acs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GatewayPayloadSigner extends Loggable {
	public static final String PAYLOAD_SIGNATURE_VERSION_1 = "1";
	private String secretKey;
	private String hid;
	private String name;
	private boolean encrypted;
	private String apiKey;
	private List<String> parameters = new ArrayList<>();

	private GatewayPayloadSigner(String secretKey) {
		this.secretKey = secretKey;
	}

	public static GatewayPayloadSigner create(String secretKey) {
		AcsUtils.notEmpty(secretKey, "secretKey is empty");
		return new GatewayPayloadSigner(secretKey);
	}

	public GatewayPayloadSigner withHid(String hid) {
		AcsUtils.notEmpty(hid, "hid is empty");
		this.hid = hid;
		return this;
	}

	public GatewayPayloadSigner withName(String name) {
		AcsUtils.notEmpty(name, "name is empty");
		this.name = name;
		return this;
	}

	public GatewayPayloadSigner withEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
		return this;
	}

	public GatewayPayloadSigner withApiKey(String apiKey) {
		AcsUtils.notEmpty(apiKey, "apiKey is empty");
		this.apiKey = apiKey;
		return this;
	}

	public GatewayPayloadSigner withParameter(String name, String value) {
		AcsUtils.notEmpty(name, "name is empty");
		parameters.add(String.format("%s=%s", name.toLowerCase(), value));
		return this;
	}

	public String signV1() {
		String method = "signV1";
		AcsUtils.notEmpty(apiKey, "apiKey is required");
		AcsUtils.notEmpty(secretKey, "secretKey is required");

		StringBuilder stringToSign = new StringBuilder();
		String canonicalRequest = buildCanonicalRequest();
		logDebug(method, "canonicalRequest: \n%s", canonicalRequest);
		stringToSign.append(AcsUtils.sha256Hex(canonicalRequest)).append('\n');
		stringToSign.append(apiKey).append('\n');
		stringToSign.append(PAYLOAD_SIGNATURE_VERSION_1);
		logDebug(method, "stringToSign: \n%s", stringToSign);

		String signingKey = AcsUtils.hmacSha256Hex(PAYLOAD_SIGNATURE_VERSION_1,
				AcsUtils.hmacSha256Hex(apiKey, secretKey));
		logDebug(method, "signingKey: \n%s", signingKey);

		String signature = AcsUtils.hmacSha256Hex(signingKey, stringToSign.toString());
		logDebug(method, "signature: \n%s", signature);

		return signature;
	}

	private String buildCanonicalRequest() {
		AcsUtils.notEmpty(hid, "hid is empty");
		AcsUtils.notEmpty(name, "name is empty");

		StringBuilder builder = new StringBuilder();
		builder.append(hid).append('\n').append(name).append('\n').append(encrypted).append('\n');
		if (!parameters.isEmpty()) {
			Collections.sort(parameters);
			parameters.forEach(param -> builder.append(param).append('\n'));
		}
		return builder.toString();
	}
}