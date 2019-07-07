package com.arrow.pegasus.data.profile;

import java.io.Serializable;
import java.time.Instant;

import javax.validation.constraints.NotNull;

public class UserEULA implements Serializable {
	private static final long serialVersionUID = 6085413745671136001L;

	@NotNull
	private String productId;
	@NotNull
	private Instant agreedDate;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Instant getAgreedDate() {
		return agreedDate;
	}

	public void setAgreedDate(Instant agreedDate) {
		this.agreedDate = agreedDate;
	}
}