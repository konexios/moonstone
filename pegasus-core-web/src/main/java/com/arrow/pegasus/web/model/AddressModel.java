package com.arrow.pegasus.web.model;

import java.io.Serializable;

public class AddressModel implements Serializable {
	private static final long serialVersionUID = 2906660030048334395L;

	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String country;

	public AddressModel() {
	}

	public AddressModel withAddress1(String address1) {
		setAddress1(address1);
		return this;
	}

	public AddressModel withAddress2(String address2) {
		setAddress2(address2);
		return this;
	}

	public AddressModel withCity(String city) {
		setCity(city);
		return this;
	}

	public AddressModel withState(String state) {
		setState(state);
		return this;
	}

	public AddressModel withZip(String zip) {
		setZip(zip);
		return this;
	}

	public AddressModel withCountry(String country) {
		setCountry(country);
		return this;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
