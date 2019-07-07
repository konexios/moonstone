package com.arrow.pegasus.webapi.data;

public class CoreDefinitionModelOption extends CoreDocumentModel {
	private static final long serialVersionUID = -6945793759747837941L;

	private String name;

	public CoreDefinitionModelOption(String id, String hid, String name) {
		super(id, hid);
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
