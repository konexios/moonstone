package com.arrow.pegasus.data.profile;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class Contact implements Serializable {
	private static final long serialVersionUID = 3006465996184026763L;

	@NotBlank
	private String firstName;
	private String middleName;
	@NotBlank
	private String lastName;
	private String sipUri;
	@NotBlank
	private String email;
	private String home;
	private String office;
	private String cell;
	private String fax;
	private String monitorExt;

	public Contact() {
	}

	public Contact(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String fullName() {
		return (firstName != null ? firstName : "") + 
				(firstName != null && lastName != null ? " " : "") + 
				(lastName != null? lastName : "");
	}

	public String getMonitorExt() {
		return monitorExt;
	}

	public void setMonitorExt(String monitorExt) {
		this.monitorExt = monitorExt;
	}

	public String getSipUri() {
		return sipUri;
	}

	public void setSipUri(String sipUri) {
		this.sipUri = sipUri;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
}
