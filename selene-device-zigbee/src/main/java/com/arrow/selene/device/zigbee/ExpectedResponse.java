package com.arrow.selene.device.zigbee;

public class ExpectedResponse {
	private static final int DEFAULT_NUMBER_OF_RETRIES = 3;

	private int id;
	private int numberOfRetries;

	public ExpectedResponse(int id) {
		this(id, DEFAULT_NUMBER_OF_RETRIES);
	}

	public ExpectedResponse(int id, int numberOfRetries) {
		this.id = id;
		this.numberOfRetries = numberOfRetries;
	}

	public int getId() {
		return id;
	}

	public int getNumberOfRetries() {
		return numberOfRetries;
	}

	public int decrementRetries() {
		return --numberOfRetries;
	}
}
