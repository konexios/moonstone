package com.arrow.kronos.web.model;

import java.io.Serializable;

public class KioskRegistrationModels {

	public static class SignupModel implements Serializable {
		private static final long serialVersionUID = -2697079296290227404L;

		private String email;
		private String referralCode;
		private String eventCode;

		public SignupModel() {
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getReferralCode() {
			return referralCode;
		}

		public void setReferralCode(String referralCode) {
			this.referralCode = referralCode;
		}

		public String getEventCode() {
			return eventCode;
		}

		public void setEventCode(String eventCode) {
			this.eventCode = eventCode;
		}
	}
}
