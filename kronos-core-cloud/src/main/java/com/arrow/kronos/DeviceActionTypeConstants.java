package com.arrow.kronos;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface DeviceActionTypeConstants {

    public interface SendEmail {
        public final static String SYSTEM_NAME = "SendEmail";
        public final static String PARAMETER_EMAIL = "Email";
        public final static String PARAMETER_CONTENT_TYPE = "ContentType";
        public final static String PARAMETER_SUBJECT = "Subject";
        public final static String PARAMETER_BODY = "Body";
    }

    public interface InitiateSkypeCall {
        public final static String SYSTEM_NAME = "InitiateSkypeCall";
        public final static String PARAMETER_SIP_ADDRESS = "SipAddress";
        public final static String PARAMETER_PHONE = "Phone";
        public final static String PARAMETER_MESSAGE = "Message";
    }

    public interface InitiateSkypeMeeting {
        public final static String SYSTEM_NAME = "InitiateSkypeMeeting";
        public final static String PARAMETER_SIP_ADDRESS = "SipAddress";
    }

    public interface RaiseOneViewAlarm {
        public final static String SYSTEM_NAME = "RaiseOneViewAlarm";
        public final static String PARAMETER_SEVERITY = "Severity";
        public final static String PARAMETER_LOCATION = "Location";
        public final static String PARAMETER_ITEM = "Item";
        public final static String PARAMETER_CATEGORY = "Category";
        public final static String PARAMETER_COE = "COE";
    }

    public interface PostBackURL {
        public final static String SYSTEM_NAME = "PostBackURL";
        public final static String PARAMETER_URL = "URL";
        public final static String PARAMETER_HEADERS = "Headers";
        public final static String PARAMETER_REQUEST_BODY = "RequestBody";
        public final static String PARAMETER_CONTENT_TYPE = "ContentType";

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        public enum ContentType {
            APPLICATION_JSON("application/json"), APPLICATION_FORM_URLENCODED(
                    "application/x-www-form-urlencoded"), APPLICATION_XML("application/xml");

            private String value;

            private ContentType(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            public static ContentType fromValue(String value) {
                if (value != null) {
                    for (ContentType item : ContentType.values()) {
                        if (item.getValue().equals(value)) {
                            return item;
                        }
                    }
                }
                throw new IllegalArgumentException("Unsupported ContentType");
            }
        }
    }

    public interface SendCommand {
        public final static String SYSTEM_NAME = "SendCommand";
        public final static String PARAMETER_COMMAND = "command";
        public final static String PARAMETER_PAYLOAD = "payload";
        public final static String PARAMETER_COMMAND_OLD = "Command";
        public final static String PARAMETER_PAYLOAD_OLD = "Payload";
    }
}
