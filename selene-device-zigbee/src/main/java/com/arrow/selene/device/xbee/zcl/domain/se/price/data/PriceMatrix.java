package com.arrow.selene.device.xbee.zcl.domain.se.price.data;

public class PriceMatrix {
	private int tierBlockId;
	private long price;

	public int getTierBlockId() {
		return tierBlockId;
	}

	public PriceMatrix withTierBlockId(int tierBlockId) {
		this.tierBlockId = tierBlockId;
		return this;
	}

	public long getPrice() {
		return price;
	}

	public PriceMatrix withPrice(long price) {
		this.price = price;
		return this;
	}
}
