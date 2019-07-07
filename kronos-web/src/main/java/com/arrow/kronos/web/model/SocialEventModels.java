package com.arrow.kronos.web.model;

import java.io.Serializable;

import com.arrow.kronos.data.SocialEventRegistration;
import com.arrow.pegasus.data.SocialEvent;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class SocialEventModels {

	public static class SocialEventModel extends CoreDocumentModel {

		private static final long serialVersionUID = 1467155510170841311L;

		private String name;

		public SocialEventModel(SocialEvent event) {
			super(event.getId(), event.getHid());
			this.name = event.getName();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public static class SocialEventRegistrationModel implements Serializable {

		private static final long serialVersionUID = 1631486232119523576L;

		private String eventId;
		private String name;
		private String email;
		private String password;
		private String eventCode;

		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getEventCode() {
			return eventCode;
		}

		public void setEventCode(String eventCode) {
			this.eventCode = eventCode;
		}
	}

	public static class SocialEventRegistrationResultModel extends CoreDocumentModel {

		private static final long serialVersionUID = -2734617230800297557L;

		public SocialEventRegistrationResultModel(SocialEventRegistration socialEventRegistration) {
			super(socialEventRegistration.getId(), socialEventRegistration.getHid());
		}

	}

	public static class SocialEventVerificationModel implements Serializable {

		private static final long serialVersionUID = 5430975918417686546L;

		private String eventId;
		private String email;

		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}

	public static class SocialEventCodeModel implements Serializable {

		private static final long serialVersionUID = -3236999662704248392L;

		private String code;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}
}
