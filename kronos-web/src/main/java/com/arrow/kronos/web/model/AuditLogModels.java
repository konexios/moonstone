package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class AuditLogModels {

	public static class AuditLogList extends CoreDocumentModel {
		private static final long serialVersionUID = 5914034620445276380L;

		private String type;
		private long createdDate;
		private String createdBy;
		private String logMessage;

		public AuditLogList(AuditLog auditLog, User createdBy) {
			super(auditLog.getId(), auditLog.getHid());
			this.type = auditLog.getType();
			this.createdDate = auditLog.getCreatedDate().getEpochSecond();
			this.createdBy = createdBy != null ? createdBy.getContact().fullName() : null;
		}

		public AuditLogList withCreatedBy(String createdBy) {
			setCreatedBy(createdBy);
			return this;
		}

		public String getType() {
			return type;
		}

		public long getCreatedDate() {
			return createdDate;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public String getLogMessage() {
			return logMessage;
		}

		public void setLogMessage(String logMessage) {
			this.logMessage = logMessage;
		}
	}

	public static class AuditLogDetails extends AuditLogList {
		private static final long serialVersionUID = 8845868875909027179L;

		private String applicationName;
		private String productName;
		private Map<String, String> parameters;

		public AuditLogDetails(AuditLog auditLog, User createdBy) {
			super(auditLog, createdBy);

			this.applicationName = auditLog.getRefApplication() != null ? auditLog.getRefApplication().getName() : null;
			this.productName = auditLog.getProductName();
			this.parameters = auditLog.getParameters();
		}

		public String getApplicationName() {
			return applicationName;
		}

		public String getProductName() {
			return productName;
		}

		public Map<String, String> getParameters() {
			return parameters;
		}
	}

	public static class AuditLogParameter implements Serializable {
		private static final long serialVersionUID = 6088167714414954753L;

		private String parameter;
		private String oldValue;
		private String newValue;

		public String getParameter() {
			return parameter;
		}

		public AuditLogParameter withParameter(String parameter) {
			this.parameter = parameter;
			return this;
		}

		public String getOldValue() {
			return oldValue;
		}

		public AuditLogParameter withOldValue(String oldValue) {
			this.oldValue = oldValue;
			return this;
		}

		public String getNewValue() {
			return newValue;
		}

		public AuditLogParameter withNewValue(String newValue) {
			this.newValue = newValue;
			return this;
		}
	}

	public static class AuditLogParameterList extends AuditLogList {
		private static final long serialVersionUID = 1848935260801350188L;

		private List<AuditLogParameter> parameters = new ArrayList<>();

		public AuditLogParameterList(AuditLog auditLog, User createdBy) {
			super(auditLog, createdBy);

			if (auditLog.getParameters() != null) {
				for (String key : auditLog.getParameters().keySet()) {
					String value = auditLog.getParameters().get(key);
					addParameter(new AuditLogParameter().withParameter(key).withOldValue(value).withNewValue(value));
				}
			}
		}

		public AuditLogParameterList addParameter(AuditLogParameter parameter) {
			this.parameters.add(parameter);
			return this;
		}

		public List<AuditLogParameter> getParameters() {
			return parameters;
		}

		public void setParameters(List<AuditLogParameter> parameters) {
			this.parameters = parameters;
		}
	}
}
