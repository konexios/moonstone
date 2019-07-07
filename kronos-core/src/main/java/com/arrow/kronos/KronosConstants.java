package com.arrow.kronos;

public class KronosConstants {
    public final static String KRONOS_PRI = "krn";

    public interface ProductFeatures {
        public final static String FIRMWARE_MANAGEMENT = "FIRMWARE_MANAGEMENT";
    }

    public interface KronosPri {
        public final static String KRONOS_APPLICATION = "krn-app";
        public final static String KRONOS_USER = "krn-usr";
        public final static String AWS_ACCOUNT = "aws-act";
        public final static String AWS_DEVICE = "aws-dev";
        public final static String IBM_ACCOUNT = "ibm-act";
        public final static String IBM_GATEWAY = "ibm-gwy";
        public final static String IBM_DEVICE = "ibm-dev";
        public final static String AZURE_ACCOUNT = "azu-act";
        public final static String AZURE_DEVICE = "azu-dev";
        public final static String DEVICE = "dev";
        public final static String DEVICE_EVENT = "dev-evt";
        public final static String DEVICE_TYPE = "dev-typ";
        public final static String DEVICE_ACTION_TYPE = "dev-act-typ";
        public final static String DEVICE_SHADOW = "dev-sha";
        public final static String DEVICE_SHADOW_TRANS = " dev-sha-trx";
        public final static String NODE = "nde";
        public final static String NODE_TYPE = "nde-tpe";
        public final static String PARTITION = "par";
        public final static String TELEMETRY = "tlm";
        public final static String TELEMETRY_UNIT = "tlm-uni";
        public final static String TELEMETRY_ITEM = "tlm-itm";
        public final static String TELEMETRY_REPLAY = "tlm-rpl";
        public final static String TELEMETRY_REPLAY_TYPE = "tlm-rpl-typ";
        public final static String USER_REGISTRATION = "usr-reg";
        public final static String KIOSK_SIGNUP = "ksk-sup";
        public final static String MANUFACTURER = "mnf";
        public final static String DEVICE_PRODUCT = "dvc-pdt";
        public final static String SOFTWARE_VENDOR = "sft-vdr";
        public final static String SOFTWARE_PRODUCT = "sft-pdt";
        public final static String SOFTWARE_RELEASE_SCHEDULE = "sft-rls-sch";
        public final static String SOFTWARE_RELEASE_TRANS = "sft-rls-trx";
        public final static String SOFTWARE_RELEASE = "sft-rls";
        public final static String TEST_PROCEDURE = "tst-proc";
        public final static String TEST_RESULT = "tst-res";
        public final static String CONFIG_BACKUP = "cfg-bku";
        public final static String GLOBAL_ACTION_TYPE = "glb-act-typ";
        public final static String GLOBAL_ACTION = "glb-act";
        public final static String SOCIAL_EVENT_REGISTRATION = "soc-evt-reg";
        public final static String GLOBAL_TAG = "glb-tag";
        public final static String SOCIAL_EVENT_DEVICE = "soc-evt-dev";
    }

    public interface FOTA {
        public final static long TEMP_TOKEN_BUFFER = 5 * 60;
        public final static long DEFAULT_TIME_TO_EXPIRE_SECONDS = 7 * 24 * 60 * 60;
        public final static long ONE_SECOND_IN_MILLISECONDS = 1000;
        public final static long ONE_MINUTE_IN_SECONDS = 60;
    }

    public interface PageResult {
        public final static int DEFAULT_PAGE = 0;
        public final static int DEFAULT_SIZE = 100;
        public final static int MAX_SIZE = 200;
    }

    public interface Telemetry {
        public final static String DEVICE_ID = "deviceId";
        public final static String DEVICE_HID = "deviceHid";
        public final static String TIMESTAMP = "timestamp";
        public final static String UID = "uid";
        public final static String TYPE = "type";
        public final static String LATITUDE = "latitude";
        public final static String LONGITUDE = "longitude";

        public final static String KAFKA_TOPIC_PREFIX = "kronos.telemetry";

        public static String kafkaTopic(String applicationEngineId) {
            return String.format("%s.%s", KAFKA_TOPIC_PREFIX, applicationEngineId);
        }
    }

    public interface Skype {
        public final static String EVENT_QUEUE_PREFIX = "kronos.server.to.gateway";

        public static String eventQueue(String applicationId) {
            return String.format("%s.%s", EVENT_QUEUE_PREFIX, applicationId);
        }
    }

    public interface Device {
        public final static String DEFAULT_TYPE = "Unknown";
    }

    public interface AwsDevice {
        public final static String ATTR_HID = "hid";
        public final static String ATTR_UID = "uid";
        public final static String ATTR_NAME = "name";
    }

    public interface DeviceEvent {
        public final static long DEFAULT_EXPIRES_SECS = 60;
        public final static String PARAMETER_EXPIRES = "expires";
        public final static String PARAMETER_EMAIL = "email";
    }

    public interface Kafka {
        public final static int DEFAULT_NUM_PARTITIONS = 8;
        public final static int DEFAULT_NUM_REPLICATIONS = 1;
    }
}
