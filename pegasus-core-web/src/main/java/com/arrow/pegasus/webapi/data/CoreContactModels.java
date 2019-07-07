package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

import com.arrow.pegasus.data.profile.Contact;

public class CoreContactModels {

	public static class ContactModel implements Serializable {
		private static final long serialVersionUID = 4430158110499761157L;

		private String firstName;
		private String lastName;
		private String sipUri;
		private String email;
		private String home;
		private String office;
		private String cell;
		private String fax;
		private String monitorExt;

		public ContactModel() {
		}

		public ContactModel(Contact contact) {
			this.firstName = contact.getFirstName();
			this.lastName = contact.getLastName();
			this.sipUri = contact.getSipUri();
			this.email = contact.getEmail();
			this.home = contact.getHome();
			this.office = contact.getOffice();
			this.cell = contact.getCell();
			this.fax = contact.getFax();
			this.monitorExt = contact.getMonitorExt();
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getSipUri() {
			return sipUri;
		}

		public String getEmail() {
			return email;
		}

		public String getHome() {
			return home;
		}

		public String getOffice() {
			return office;
		}

		public String getCell() {
			return cell;
		}

		public String getFax() {
			return fax;
		}

		public String getMonitorExt() {
			return monitorExt;
		}
	}
}
