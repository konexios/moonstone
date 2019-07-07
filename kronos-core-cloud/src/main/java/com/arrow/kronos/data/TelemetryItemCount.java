package com.arrow.kronos.data;

import java.io.Serializable;

public class TelemetryItemCount implements Serializable {
	private static final long serialVersionUID = -1994686131890918518L;

	private Long timestamp;
	private Long total;

	public TelemetryItemCount() {
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
