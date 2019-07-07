package com.arrow.pegasus.web.model;

import java.io.Serializable;

public class ResponseContext implements Serializable {
	private static final long serialVersionUID = -8348681863865267189L;
	private long start;
	private long end;
	private long duration;
	private int status;
	private Object data;

	public ResponseContext() {
		this.start = System.currentTimeMillis();
	}

	public long getStart() {
		return start;
	}

	private void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	private void setEnd(long end) {
		this.end = end;
	}

	public long getDuration() {
		return duration;
	}

	private void setDuration(long duration) {
		this.duration = duration;
	}

	public int getStatus() {
		return status;
	}

	private void setStatus(int status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ResponseContext start() {
		setStart(System.currentTimeMillis());

		return this;
	}

	public ResponseContext end() {
		setEnd(System.currentTimeMillis());
		setDuration(this.end - this.start);

		return this;
	}

	public ResponseContext withStatus(int status) {
		setStatus(status);

		return this;
	}

	public ResponseContext withDatat(Object data) {
		setData(data);

		return this;
	}
}
