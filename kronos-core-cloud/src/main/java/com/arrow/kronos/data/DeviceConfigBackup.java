package com.arrow.kronos.data;

import java.util.ArrayList;
import java.util.List;

public class DeviceConfigBackup extends BaseDeviceConfigBackup {
	private static final long serialVersionUID = -3579775543133110537L;

	private String gatewayId;
	private List<DeviceAction> actions = new ArrayList<>();

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public List<DeviceAction> getActions() {
		return actions;
	}

	public void setActions(List<DeviceAction> actions) {
		this.actions = actions;
	}
}
