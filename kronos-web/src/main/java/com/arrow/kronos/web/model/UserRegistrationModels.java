package com.arrow.kronos.web.model;

import java.io.Serializable;

public class UserRegistrationModels {

	public static class PreRegistrationModel implements Serializable {
		private static final long serialVersionUID = 3082282262539319737L;

		private String email;
		private String referralCode;
		private String eventCode;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public PreRegistrationModel withEmail(String email) {
			setEmail(email);

			return this;
		}

		public String getReferralCode() {
			return referralCode;
		}

		public void setReferralCode(String referralCode) {
			this.referralCode = referralCode;
		}

		public PreRegistrationModel withReferralCode(String referralCode) {
			setReferralCode(referralCode);

			return this;
		}

		public String getEventCode() {
			return eventCode;
		}

		public void setEventCode(String eventCode) {
			this.eventCode = eventCode;
		}

		public PreRegistrationModel withEventCode(String eventCode) {
			setEventCode(eventCode);

			return this;
		}
	}

	public static class UserRegistrationModel implements Serializable {
		private static final long serialVersionUID = -6419882498008441761L;

		private String email;
		private String firstName;
		private String lastName;
		private String title;
		private String companyName;
		private String companyWebSite;
		private String projectDescription;
		private String referralCode;

		public UserRegistrationModel() {
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
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

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getCompanyWebSite() {
			return companyWebSite;
		}

		public void setCompanyWebSite(String comapnyWebSite) {
			this.companyWebSite = comapnyWebSite;
		}

		public String getProjectDescription() {
			return projectDescription;
		}

		public void setProjectDescription(String projectDescription) {
			this.projectDescription = projectDescription;
		}

		public String getReferralCode() {
			return referralCode;
		}

		public void setReferralCode(String referralCode) {
			this.referralCode = referralCode;
		}
	}
}
