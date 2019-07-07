package com.arrow.kronos.data;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.Enabled;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "device")
@CompoundIndexes({
		@CompoundIndex(name = "applicationId_gatewayId", background = true, def = "{'applicationId': 1, 'gatewayId' : 1}") })
public class Device extends BaseDeviceAbstract implements Enabled {
	private static final long serialVersionUID = 2296360232325401772L;

	@NotBlank
	private String gatewayId;
	private List<DeviceAction> actions = new ArrayList<>();

	@Transient
	@JsonIgnore
	private Gateway refGateway;

	public Gateway getRefGateway() {
		return refGateway;
	}

	public void setRefGateway(Gateway refGateway) {
		this.refGateway = refGateway;
	}

	public List<DeviceAction> getActions() {
		return actions;
	}

	public void setActions(List<DeviceAction> actions) {
		this.actions = actions;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.DEVICE;
	}
}
