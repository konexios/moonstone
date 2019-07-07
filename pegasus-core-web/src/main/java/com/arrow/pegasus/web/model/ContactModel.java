package com.arrow.pegasus.web.model;

import java.io.Serializable;

public class ContactModel implements Serializable {
	private static final long serialVersionUID = 8612073536788740528L;

	private String firstName;
	private String lastName;
	// TODO revisit themis specific property
	private String sipUri;
	private String email;
	private String home;
	private String office;
	private String cell;
	private String fax;
	// TODO revisit themis specific property
	private String monitorExt;

	public ContactModel() {
	}

	public ContactModel withFirstName(String firstName) {
		setFirstName(firstName);
		return this;
	}

	public ContactModel withLastName(String lastName) {
		setLastName(lastName);
		return this;
	}

	public ContactModel withSipUri(String sipUri) {
		setSipUri(sipUri);
		return this;
	}

	public ContactModel withEmail(String email) {
		setEmail(email);
		return this;
	}

	public ContactModel withHome(String home) {
		setHome(home);
		return this;
	}

	public ContactModel withOffice(String office) {
		setOffice(office);
		return this;
	}

	public ContactModel withCell(String cell) {
		setCell(cell);
		return this;
	}

	public ContactModel withFax(String fax) {
		setFax(fax);
		return this;
	}

	public ContactModel withMonitorExt(String monitorExt) {
		setMonitorExt(monitorExt);
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSipUri() {
		return sipUri;
	}

	public void setSipUri(String sipUri) {
		this.sipUri = sipUri;
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

	public String getMonitorExt() {
		return monitorExt;
	}

	public void setMonitorExt(String monitorExt) {
		this.monitorExt = monitorExt;
	}
}