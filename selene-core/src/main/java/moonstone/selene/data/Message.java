package moonstone.selene.data;

import java.time.Instant;

public class Message extends EntityAbstract {
	private static final long serialVersionUID = 9125544714416338162L;

	private MessageSeverity severity;
	private long deviceId;
	private long timestamp;
	private String className;
	private String methodName;
	private String objectName;
	private String objectId;
	private String message;

	public Message() {
	}

	public Message(MessageSeverity severity, String className, String methodName, String message) {
		this.severity = severity;
		this.className = className;
		this.methodName = methodName;
		this.message = message;
		timestamp = Instant.now().toEpochMilli();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public MessageSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(MessageSeverity severity) {
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
}
