package com.arrow.kronos;

public interface KronosEngineConstants {
    public final static int DEFAULT_PROCESSOR_NUM_THREADS = 100;
    public final static int DEFAULT_CONCURRENT_CONSUMERS = 5;
    public final static int DEFAULT_PREFETCH_COUNT = 10;

    public interface KafkaTelemetryProcessor {
        public final static String ACTION = "action";
        public final static String LAST_TELEMETRY_ITEM = "lastTelemetryItem";
        public final static String LOCATION = "location";
        public final static String ES_TELEMETRY = "esTelemetry";
        public final static String PERSISTENCE = "persistence";

        public static String topic(String applicationEngineId, String suffix) {
            return String.format("kronos.telemetry.%s.%s", applicationEngineId, suffix);
        }
    }

    public interface ProcessorQueue {
        public final static String EXCHANGE = "kronos.engine.processor";
        public final static String ACTION = EXCHANGE + ".action";
        public final static String LAST_TELEMETRY_ITEM = EXCHANGE + ".lastTelemetryItem";
        public final static String LOCATION = EXCHANGE + ".location";
        public final static String ES_TELEMETRY = EXCHANGE + ".esTelemetry";
        public final static String IOT_CENTRAL_WEBHOOK_TELEMETRY = EXCHANGE + ".iotcwhTelemetry";
        public final static String PERSISTENCE = EXCHANGE + ".persistence";

        public static String queueName(String prefix, String applicationEngineId) {
            return String.format("%s.%s", prefix, applicationEngineId);
        }
    }
}
