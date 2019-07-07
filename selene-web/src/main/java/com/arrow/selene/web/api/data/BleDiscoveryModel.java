package com.arrow.selene.web.api.data;

import java.io.Serializable;

public class BleDiscoveryModel implements Serializable {
	private static final long serialVersionUID = 3570613626753262294L;
	private String interfaceName;
	private long discoveryTimeout;

	public BleDiscoveryModel() {
		super();
	}

	public BleDiscoveryModel(String interfaceName, long discoveryTimeout) {
		super();
		this.interfaceName = interfaceName;
		this.discoveryTimeout = discoveryTimeout;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public long getDiscoveryTimeout() {
		return discoveryTimeout;
	}

}
