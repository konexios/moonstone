package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

public class ResponseWrapper implements Serializable {
	private static final long serialVersionUID = 4262017956087443219L;

	private long start;
	private long end;
	private Object data;

	public ResponseWrapper(long start, long end, Object data) {
		this.start = start;
		this.end = end;
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public long getDuration() {
		return end - start;
	}
}
