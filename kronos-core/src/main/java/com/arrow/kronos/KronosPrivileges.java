package com.arrow.kronos;

public interface KronosPrivileges {
    public interface General {
        public final static String KRONOS_ACCESS = "KRONOS_ACCESS";
        public final static String KRONOS_EDIT_IOT_CONFIGURATION = "KRONOS_EDIT_IOT_CONFIGURATION";
    }

    public interface Gateway {
        public final static String KRONOS_VIEW_ALL_GATEWAYS = "KRONOS_VIEW_ALL_GATEWAYS";
        public final static String KRONOS_VIEW_GATEWAYS = "KRONOS_VIEW_GATEWAYS";
        public final static String KRONOS_EDIT_GATEWAY = "KRONOS_EDIT_GATEWAY";

        public final static String KRONOS_VIEW_GATEWAY_AUDIT_LOGS = "KRONOS_VIEW_GATEWAY_AUDIT_LOGS";
    }

    public interface Device {
        public final static String KRONOS_VIEW_ALL_DEVICES = "KRONOS_VIEW_ALL_DEVICES";
        public final static String KRONOS_CREATE_DEVICE = "KRONOS_CREATE_DEVICE";
        public final static String KRONOS_EDIT_DEVICE = "KRONOS_EDIT_DEVICE";
        public final static String KRONOS_VIEW_DEVICES = "KRONOS_VIEW_DEVICES";

        public final static String KRONOS_CREATE_DEVICE_ACTION = "KRONOS_CREATE_DEVICE_ACTION";
        public final static String KRONOS_EDIT_DEVICE_ACTION = "KRONOS_EDIT_DEVICE_ACTION";

        public final static String KRONOS_ADD_DEVICE_TAG = "KRONOS_ADD_DEVICE_TAG";
        public final static String KRONOS_REMOVE_DEVICE_TAG = "KRONOS_REMOVE_DEVICE_TAG";

        public final static String KRONOS_SEND_COMMAND_TO_DEVICE = "KRONOS_SEND_COMMAND_TO_DEVICE";
        public final static String KRONOS_VIEW_DEVICE_AUDIT_LOGS = "KRONOS_VIEW_DEVICE_AUDIT_LOGS";

        public final static String KRONOS_VIEW_DEVICE_TYPES = "KRONOS_VIEW_DEVICE_TYPES";
        public final static String KRONOS_EDIT_DEVICE_TYPE = "KRONOS_EDIT_DEVICE_TYPE";
        public final static String KRONOS_CREATE_DEVICE_TYPE = "KRONOS_CREATE_DEVICE_TYPE";
    }

    public interface Node {
        public final static String KRONOS_CREATE_NODE = "KRONOS_CREATE_NODE";
        public final static String KRONOS_EDIT_NODE = "KRONOS_EDIT_NODE";
        public final static String KRONOS_VIEW_NODES = "KRONOS_VIEW_NODES";

        public final static String KRONOS_VIEW_NODE_TYPES = "KRONOS_VIEW_NODE_TYPES";
        public final static String KRONOS_CREATE_NODE_TYPE = "KRONOS_CREATE_NODE_TYPE";
        public final static String KRONOS_EDIT_NODE_TYPE = "KRONOS_EDIT_NODE_TYPE";
    }
}
