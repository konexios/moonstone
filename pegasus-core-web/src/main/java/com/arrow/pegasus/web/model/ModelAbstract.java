package com.arrow.pegasus.web.model;

import java.io.Serializable;

public abstract class ModelAbstract<T extends ModelAbstract<T>> implements Serializable {
	private static final long serialVersionUID = 6072452513536237944L;

	private String id;
	private String hid;

	protected abstract T self();

	public T withId(String id) {
		setId(id);
		return self();
	}

	public T withHid(String hid) {
		setHid(hid);
		return self();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}
}
