package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

public class CoreDocumentModel implements Serializable {
	
	private static final long serialVersionUID = -6352522975814434245L;

	protected String id;
	protected String hid;
	
	public CoreDocumentModel(String id, String hid) {
		this.id = id;
		this.hid = hid;
	}
	
	public String getId() {
		return id;
	}
	
	public String getHid() {
		return hid;
	}
}
