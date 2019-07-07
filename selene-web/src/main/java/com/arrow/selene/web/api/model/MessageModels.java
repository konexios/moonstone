package com.arrow.selene.web.api.model;

import java.io.Serializable;

import com.arrow.selene.data.Message;
import com.arrow.selene.data.MessageSeverity;

public class MessageModels {

	public static class MessageModel extends BaseModels.EntityAbstract {
		private static final long serialVersionUID = 2018913667484029641L;

		private MessageSeverity severity;
		private long deviceId;
		private long timestamp;
		private String className;
		private String methodName;
		private String objectName;
		private String objectId;
		private String message;

		public MessageModel(Message message) {
			super(message.getId());
			this.severity = message.getSeverity();
			this.deviceId = message.getDeviceId();
			this.timestamp = message.getTimestamp();
			this.className = message.getClassName();
			this.methodName = message.getMethodName();
			this.objectName = message.getObjectName();
			this.objectId = message.getObjectId();
			this.message = message.getMessage();
		}

		public MessageSeverity getSeverity() {
			return severity;
		}

		public long getDeviceId() {
			return deviceId;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public String getClassName() {
			return className;
		}

		public String getMethodName() {
			return methodName;
		}

		public String getObjectName() {
			return objectName;
		}

		public String getObjectId() {
			return objectId;
		}

		public String getMessage() {
			return message;
		}
	}

	public static class MessageUpsert implements Serializable {
		private static final long serialVersionUID = 5359902438457846099L;

		private MessageModel message;

		public MessageUpsert(MessageModel message) {
			this.message = message;
		}

		public MessageModel getMessage() {
			return message;
		}
	}

}
