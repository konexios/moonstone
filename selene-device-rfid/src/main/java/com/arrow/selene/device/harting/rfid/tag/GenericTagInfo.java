package com.arrow.selene.device.harting.rfid.tag;

public class GenericTagInfo extends TagInfoAbstract {
	private static final long serialVersionUID = 5799899534825130283L;

	public static final String DEFAULT_DEVICE_TYPE = "generic-rfid-tag";

	public GenericTagInfo() {
		setType(DEFAULT_DEVICE_TYPE);
	}
}
