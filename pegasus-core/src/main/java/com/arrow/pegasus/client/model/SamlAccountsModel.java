package com.arrow.pegasus.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import moonstone.acs.client.model.SamlAccountModel;

public class SamlAccountsModel implements Serializable {

	private static final long serialVersionUID = -4805221645466718387L;

	private String applicationId;
	private List<SamlAccountModel> samlAccounts = new ArrayList<>();

	public SamlAccountsModel withApplicationId(String applicationId) {
		setApplicationId(applicationId);
		return this;
	}

	public SamlAccountsModel withSamlAccounts(List<SamlAccountModel> samlAccounts) {
		setSamlAccounts(samlAccounts);
		return this;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public List<SamlAccountModel> getSamlAccounts() {
		return samlAccounts;
	}

	public void setSamlAccounts(List<SamlAccountModel> samlAccounts) {
		this.samlAccounts = samlAccounts;
	}
}
