package com.arrow.kronos.data;

import java.io.Serializable;

public class DeviceEventCount implements Serializable {
	private static final long serialVersionUID = 3560034807374716939L;

	private Long timestamp;
	private Long total;

	public DeviceEventCount() {
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
