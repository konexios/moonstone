package com.arrow.kronos.api.model;

import java.io.Serializable;

public class SocialEventRegistrationVerifyModel implements Serializable {

	private static final long serialVersionUID = 605752316012531011L;

	private String applicationHid;
	private String companyId;
	private String userHid;

	public SocialEventRegistrationVerifyModel withApplicationHid(String applicationHid) {
		setApplicationHid(applicationHid);
		return this;
	}

	public String getApplicationHid() {
		return applicationHid;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
	}

	public String getCompanyHid() {
		return companyId;
	}

	public void setCompanyHid(String tenantHid) {
		this.companyId = tenantHid;
	}

	public String getUserHid() {
		return userHid;
	}

	public void setUserHid(String userHid) {
		this.userHid = userHid;
	}
}
