/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acs.client.model;

import java.io.Serializable;

public class ContactModel implements Serializable {
	private static final long serialVersionUID = 3006465996184026763L;

	private String firstName;
	private String lastName;
	private String sipUri;
	private String email;
	private String home;
	private String office;
	private String cell;
	private String fax;
	private String monitorExt;

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

	public ContactModel withMonitorExt(String monitorExt) {
		setMonitorExt(monitorExt);
		return this;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String fullName() {
		return firstName + " " + lastName;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cell == null) ? 0 : cell.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((home == null) ? 0 : home.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((monitorExt == null) ? 0 : monitorExt.hashCode());
		result = prime * result + ((office == null) ? 0 : office.hashCode());
		result = prime * result + ((sipUri == null) ? 0 : sipUri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactModel other = (ContactModel) obj;
		if (cell == null) {
			if (other.cell != null)
				return false;
		} else if (!cell.equals(other.cell))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (home == null) {
			if (other.home != null)
				return false;
		} else if (!home.equals(other.home))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (monitorExt == null) {
			if (other.monitorExt != null)
				return false;
		} else if (!monitorExt.equals(other.monitorExt))
			return false;
		if (office == null) {
			if (other.office != null)
				return false;
		} else if (!office.equals(other.office))
			return false;
		if (sipUri == null) {
			if (other.sipUri != null)
				return false;
		} else if (!sipUri.equals(other.sipUri))
			return false;
		return true;
	}
}
