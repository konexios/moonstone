package com.arrow.pegasus.client.model;

import java.io.Serializable;

import com.arrow.pegasus.data.profile.Company;

public class CompanyChangeModel implements Serializable {

	private static final long serialVersionUID = 5766196453268902920L;

	private Company company;
	private String who;

	public CompanyChangeModel withCompany(Company company) {
		setCompany(company);
		return this;
	}

	public CompanyChangeModel withWho(String who) {
		setWho(who);
		return this;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}
}
