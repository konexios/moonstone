package com.arrow.selene;

public interface SeleneConstants {
	String ENV_SELENE_CONFIG = "seleneConfig";
	String ENV_DATABASE_LOGGING = "selene.databaseLogging";
	String ENV_SECURITY_AES128 = "security.aes128";
	String ENV_CRYPTO_MODE = "crypto.mode";
	String DEFAULT_SELENE_CONFIG = "/selene-sample.properties";

	String DEFAULT_PERSISTENCE_XML = "persistence.xml";
	String PERSISTENCE_UNIT_NAME = "selene";

	long DEFAULT_DATA_BUS_MAX_BUFFER = Long.MAX_VALUE;
	long DEFAULT_DATA_BUS_POLLING_INTERVAL = 1000L;
	String DEFAULT_FILE_DATABUS_DIRECTORY = "./databus/";
	String DEFAULT_MQTT_DATABUS_URL = "tcp://localhost:1883";
}
