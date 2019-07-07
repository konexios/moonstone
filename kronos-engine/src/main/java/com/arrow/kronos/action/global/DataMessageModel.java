package com.arrow.kronos.action.global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataMessageModel implements Serializable {

	private static final long serialVersionUID = -1635656953127187959L;

	private String globalActionHid;
	private String globalActionId;
	private String applicationId;
	private String systemName;
	private List<MessageInputModel> payload = new ArrayList<>();

	public String getGlobalActionHid() {
		return globalActionHid;
	}

	public void setGlobalActionHid(String globalActionHid) {
		this.globalActionHid = globalActionHid;
	}

	public String getGlobalActionId() {
		return globalActionId;
	}

	public void setGlobalActionId(String globalActionId) {
		this.globalActionId = globalActionId;
	}

	public List<MessageInputModel> getPayload() {
		return payload;
	}

	public void setPayload(List<MessageInputModel> payload) {
		this.payload = payload;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

}
