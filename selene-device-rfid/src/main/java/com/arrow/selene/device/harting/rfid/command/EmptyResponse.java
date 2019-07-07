package com.arrow.selene.device.harting.rfid.command;

public class EmptyResponse implements Response<EmptyResponse> {
	private static final long serialVersionUID = -1638904088976947459L;
	private int status;

	public EmptyResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public EmptyResponse parse(int mode, byte... payload) {
		return new EmptyResponse().withStatus(payload[2]);
	}
}
