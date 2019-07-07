package com.arrow.selene.device.zigbee.data;

import java.io.Serializable;

public class BindingInfo implements Serializable {
	private static final long serialVersionUID = -7444115941795102059L;

	private int srcEndpoint;
	private String dstAddress;
	private int dstEndpoint;
	private int clusterId;

	public int getSrcEndpoint() {
		return srcEndpoint;
	}

	public BindingInfo withSrcEndpoint(int srcEndpoint) {
		this.srcEndpoint = srcEndpoint;
		return this;
	}

	public String getDstAddress() {
		return dstAddress;
	}

	public BindingInfo withDstAddress(String dstAddress) {
		this.dstAddress = dstAddress;
		return this;
	}

	public int getDstEndpoint() {
		return dstEndpoint;
	}

	public BindingInfo withDstEndpoint(int dstEndpoint) {
		this.dstEndpoint = dstEndpoint;
		return this;
	}

	public int getClusterId() {
		return clusterId;
	}

	public BindingInfo withClusterId(int clusterId) {
		this.clusterId = clusterId;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		BindingInfo bindingInfo = (BindingInfo) o;

		if (srcEndpoint != bindingInfo.srcEndpoint) {
			return false;
		}
		if (dstEndpoint != bindingInfo.dstEndpoint) {
			return false;
		}
		if (clusterId != bindingInfo.clusterId) {
			return false;
		}
		return dstAddress != null ? dstAddress.equals(bindingInfo.dstAddress) : bindingInfo.dstAddress == null;
	}

	@Override
	public int hashCode() {
		int result = srcEndpoint;
		result = 31 * result + (dstAddress != null ? dstAddress.hashCode() : 0);
		result = 31 * result + dstEndpoint;
		result = 31 * result + clusterId;
		return result;
	}
}
