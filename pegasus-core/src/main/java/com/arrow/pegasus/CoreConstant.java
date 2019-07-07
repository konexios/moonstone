package com.arrow.pegasus;

public interface CoreConstant {
    public final static String ADMIN_USER = "admin";
    public final static String ANONYMOUS_USER = "anonymous";
    public final static String CRON_USER = "cron";

    public final static String API_KEY_CONTEXT = "com.arrow.pegasus.ApiKeyContext";
    public final static String API_PAYLOAD = "com.arrow.pegasus.ApiPayload";

    public final static String CURRENT_APPLICATION_ID = "com.arrow.pegasus.CurrentApplicationId";
    public final static long DEFAULT_SHUTDOWN_WAIT_TIMEOUT_MS = 5000;
    public final static long DEFAULT_EVENT_SERVICE_TIMER__MS = 200;

    public final static boolean DEFAULT_ENABLED = true;
    public final static boolean DEFAULT_EDITABLE = true;
    public final static boolean DEFAULT_HIDDEN = false;

    public final static String PGS_ZONE_SYSTEM_NAME = "pgs";
    public final static String DEFAULT_ZONE_SYSTEM_NAME = "a01";

    public final static String ROOT_PRI = "arw";
    public final static int DEFAULT_PROCESSOR_NUM_THREADS = 10;
    public final static int DEFAULT_KAFKA_CONSUMER_NUM_THREADS = 10;

    public interface PegasusPri {
        public final static String BASE = "pgs";
        public final static String APPLICATION_ENGINE = "app-eng";
        public final static String AUDIT_LOG = "adt";
        public final static String ACCESS_KEY = "key";
        public final static String EVENT = "evt";
        public final static String HEART_BEAT = "hbt";
        public final static String LAST_HEARTBEAT = "lst-hbt";
        public final static String APPLICATION = "app";
        public final static String REGION = "rgn";
        public final static String ZONE = "zon";
        public final static String COMPANY = "com";
        public final static String GATEWAY = "gwy";
        public final static String PRODUCT = "prd";
        public final static String SUBSCRIPTION = "sub";
        public final static String USER = "usr";
        public final static String AUTH = "aut";
        public final static String PRIVILEGE = "prv";
        public final static String ROLE = "rol";
        public final static String SURVEY = "svy";
        public final static String SURVEY_INSTANCE = "svy-ins";
        public final static String MAPPING = "map";
        public final static String LAST_LOCATION = "lst-loc";
        public final static String MIGRATION_TASK = "mig-tsk";
        public final static String VERSION = "ver";
        public final static String TEMP_TOKEN = "tmp-tok";
        public final static String SOCIAL_EVENT = "soc-evt";
        public final static String PLATFORM_CONFIG = "pla-cfg";

        // dynamic dashboard
        public final static String DYNAMIC_DASHBOARD_USER_DATA = "dd-usr-dat";
        public final static String DYNAMIC_DASHBOARD_BOARD = "dd-brd";
        public final static String DYNAMIC_DASHBOARD_BOARD_SIZES = "dd-brd-szs";
        public final static String DYNAMIC_DASHBOARD_BOARD_RUNTIME = "dd-brd-rtm";
        public final static String DYNAMIC_DASHBOARD_CONTAINER = "dd-cnt";
        public final static String DYNAMIC_DASHBOARD_CONTAINER_RUNTIME = "dd-cnt-rtm";
        public final static String DYNAMIC_DASHBOARD_WIDGET_TYPE = "dd-wgt-typ";
        public final static String DYNAMIC_DASHBOARD_WIDGET = "dd-wgt";
        public final static String DYNAMIC_DASHBOARD_WIDGET_CONFIGURATION = "dd-wgt-cfg";
        public final static String DYNAMIC_DASHBOARD_CONFIGURATION_PAGE = "dd-cfg-pg";
        public final static String DYNAMIC_DASHBOARD_PAGE_PROPERTY = "dd-pg-prt";
        public final static String DYNAMIC_DASHBOARD_PROPERTY_VALUE = "dd-prt-vlu";
    }

    public interface Events {
        public final static String EVENTS_COMPANY_NAME = "Events";
        public final static String MIRAMONTI_COMPANY_NAME = "EW2018";
        public final static String MIRAMONTI_APPLICATION_ENGINE = "kronos-engine-01";
    }
}
