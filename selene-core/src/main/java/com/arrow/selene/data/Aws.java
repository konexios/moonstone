package com.arrow.selene.data;

public class Aws extends BaseEntity {
	private static final long serialVersionUID = 6867319858197265088L;

	private String host;
	private int port;
	private String rootCert;
	private String clientCert;
	private String privateKey;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRootCert() {
		return rootCert;
	}

	public void setRootCert(String rootCert) {
		this.rootCert = rootCert;
	}

	public String getClientCert() {
		return clientCert;
	}

	public void setClientCert(String clientCert) {
		this.clientCert = clientCert;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
}
