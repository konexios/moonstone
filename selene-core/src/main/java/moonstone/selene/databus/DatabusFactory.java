package moonstone.selene.databus;

import org.apache.commons.lang3.Validate;

import moonstone.selene.SeleneException;

public class DatabusFactory {
	private final static String REDIS_DATABUS_CLASS = "moonstone.selene.databus.RedisDatabus";
	private final static String RABBITMQ_DATABUS_CLASS = "moonstone.selene.databus.RabbitmqDatabus";
	private final static String FILE_DATABUS_CLASS = "moonstone.selene.databus.FileDatabus";
	private final static String MQTT_DATABUS_CLASS = "moonstone.selene.databus.MqttDatabus";

	public static Databus createDatabus(String databusType) {
		Validate.notBlank(databusType, "databusType is empty");
		try {
			switch (databusType) {
			case Databus.REDIS: {
				return (Databus) Class.forName(REDIS_DATABUS_CLASS).newInstance();
			}
			case Databus.RABBITMQ: {
				return (Databus) Class.forName(RABBITMQ_DATABUS_CLASS).newInstance();
			}
			case Databus.FILE: {
				return (Databus) Class.forName(FILE_DATABUS_CLASS).newInstance();
			}
			case Databus.MQTT: {
				return (Databus) Class.forName(MQTT_DATABUS_CLASS).newInstance();
			}
			default: {
				throw new SeleneException("unsupported databus type: " + databusType);
			}
			}
		} catch (SeleneException e) {
			throw e;
		} catch (Exception e) {
			throw new SeleneException("Error instantiating databus: " + e.getMessage(), e);
		}
	}
}
