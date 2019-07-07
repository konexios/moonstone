package com.arrow.kronos.web.model;

import java.io.Serializable;

public class BulkActionResultModel implements Serializable {
	private static final long serialVersionUID = 6070012342888892378L;

	private int total;
	private int succeeded;
	private int failed;

	public BulkActionResultModel(int total, int succeeded, int failed) {
		this.total = total;
		this.succeeded = succeeded;
		this.failed = failed;
	}

	public int getTotal() {
		return total;
	}

	public int getSucceeded() {
		return succeeded;
	}

	public int getFailed() {
		return failed;
	}
}
