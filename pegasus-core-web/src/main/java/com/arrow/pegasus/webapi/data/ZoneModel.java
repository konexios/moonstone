package com.arrow.pegasus.webapi.data;

public class ZoneModel extends DefinitionModel {
	private static final long serialVersionUID = -7290748466428968494L;

	private String zoneSystemName;
	
	public ZoneModel(String id, String name, String description, String zoneSystemName) {
		super(id, name, description);
		
		this.zoneSystemName = zoneSystemName;
	}
	
	public String getZoneSystemName() {
		return zoneSystemName;
	}
}
