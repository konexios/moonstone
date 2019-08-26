package moonstone.selene.engine;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.arrow.pegasus.security.CryptoMode;
import com.fasterxml.jackson.core.type.TypeReference;

public interface EngineConstants {
    String GATEWAY_DEVICE_TYPE = "Arrow Connect EDGE";
    long DEFAULT_HEARTBEAT_INTERVAL_MS = 10000L;
    long DEFAULT_GATEWAY_REGISTRATION_RETRY_MS = 5000L;
    long DEFAULT_GATEWAY_CHECK_IN_RETRY_MS = 15000L;
    // disabled by default due to OS dependency
    int DEFAULT_HEALTH_CHECK_INTERVAL_MINS = 0;

    long DEFAULT_CLOUD_CONNECTION_RETRY_INTERVAL_MS = 10000L;
    long DEFAULT_CLOUD_SENDING_RETRY_MS = 5000L;
    long DEFAULT_CLOUD_BATCH_SENDING_INTERVAL_MS = 200L;

    int DEFAULT_PURGE_TELEMETRY_INTERVAL_DAYS = 7;
    int DEFAULT_PURGE_MESSAGES_INTERVAL_DAYS = 7;

    long DEFAULT_MAX_POLLING_INTERVAL_MS = 100L;
    boolean DEFAULT_DEVICE_PERSIST_TELEMETRY = false;

    int DEFAULT_AWS_MQTT_CONNECTION_TIMEOUT_SECS = 60;
    int DEFAULT_AWS_MQTT_KEEP_ALIVE_INTERVAL_SECS = 60;

    int UDP_PACKET_BUFFER_SIZE = 1024;

    long DEFAULT_BLE_TELEMETRY_SENDING_INTERVAL_MS = 1000;

    String FORMAT_DECIMAL_2 = "%.2f";
    String FORMAT_DECIMAL_3 = "%.3f";
    String FORMAT_DECIMAL_8 = "%.8f";

    DecimalFormat DECIMAL_2 = new DecimalFormat("#.##");
    DecimalFormat DECIMAL_3 = new DecimalFormat("#.###");
    DecimalFormat DECIMAL_8 = new DecimalFormat("#.########");

    String ENV_CONFIG_FILE = "selene.properties";

    String DEVICE_CLASS = "deviceClass";

    int DEFAULT_CLOUD_MODULE_NUM_THREADS = 5;

    String DEFAULT_CRYPTO_MODE = CryptoMode.AES_256.name();

    String DEFAULT_TELEMETRY_QUEUE = "selene-telemetry";
    String DEFAULT_STATE_QUEUE = "selene-state";
    String DEFAULT_DEVICE_ERROR_QUEUE = "selene-device-error";
    String DEFAULT_GATEWAY_ERROR_QUEUE = "selene-gateway-error";

    String DEFAULT_HOME_DIRECTORY_WINDOWS = "c:/selene";
    String DEFAULT_HOME_DIRECTORY_LINUX = "/opt/selene";
    String DEFAULT_SOFTWARE_UPDATE_DIRECTORY = "updates";
    String DEFAULT_SOFTWARE_DOWNLOAD_DIRECTORY = "download";
    String DEFAULT_SOFTWARE_BACKUP_DIRECTORY = "backup";
    String DEFAULT_CONFIG_DIRECTORY = "config";
    String DEFAULT_DEVICE_DIRECTORY = "devices";
    String DEFAULT_LIB_DIRECTORY = "lib";
    String DEFAULT_BIN_DIRECTORY = "bin";
    String DEFAULT_LOG_DIRECTORY = "log";

    String DEFAULT_SCRIPTING_ENGINE = "nashorn";

    String DEFAULT_RESTART_SCRIPT_FILENAME = "restart.sh";

    String DEFAULT_MK_ROUTER_COMMAND_TOPIC = "${deviceType}/${deviceUid}/cmd";

    TypeReference<Map<String, String>> MAP_TYPE_REF = new TypeReference<Map<String, String>>() {
    };
    TypeReference<List<String>> LIST_TYPE_REF = new TypeReference<List<String>>() {
    };

    static String deviceCommandQueue(String deviceId) {
        return String.format("selene-command-%s", deviceId);
    }

    static String deviceCommandQueue(long deviceId) {
        return String.format("selene-command-%s", deviceId);
    }

    interface RedisConfig {
        String DEFAULT_HOST = "localhost";
        int DEFAULT_PORT = 6379;
    }

    interface RabbitmqConfig {
        String DEFAULT_HOST = "localhost";
        int DEFAULT_PORT = 5672;
    }

    interface Mqtt {
        String DEFAULT_URL = "tcp://localhost:1883";
        String DEFAULT_USERNAME = "";
        String DEFAULT_PASSWORD = "";
    }

    interface Variable {
        String GATEWAY_UID = "gateway.uid";
        String GATEWAY_NAME = "gateway.name";
        String MAC_FULL = "mac.full";
        String MAC_SIMPLE = "mac.simple";
    }
}
