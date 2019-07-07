package com.arrow.pegasus.web.model;

import java.io.Serializable;

public class EntityPri implements Serializable {
	private static final long serialVersionUID = 6055872620908299507L;

	private String pri;

	public EntityPri() {
	}

	public EntityPri(String pri) {
		setPri(pri);
	}

	public String getPri() {
		return pri;
	}

	public void setPri(String pri) {
		this.pri = pri;
	}
}
