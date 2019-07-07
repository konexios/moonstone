package com.arrow.kronos;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface GlobalActionTypeConstants {
	public interface SendEmail {
        public final static String SYSTEM_NAME = "SendEmail";
        public final static String PARAMETER_TO = "To";
        public final static String PARAMETER_CC = "Cc";
        public final static String PARAMETER_CONTENT_TYPE = "Content Type";
        public final static String PARAMETER_CONTENT_TYPE_OPTION_PLAIN = "text/plain";
        public final static String PARAMETER_CONTENT_TYPE_OPTION_HTML = "text/html";
        public final static String PARAMETER_SUBJECT = "Subject";
        public final static String PARAMETER_BODY = "Body";
    }
	
	public interface PostBackURL {
		public final static String SYSTEM_NAME = "PostBackURL";
        public final static String PARAMETER_URL = "URL";
        public final static String PARAMETER_HEADERS = "Headers";
        public final static String PARAMETER_REQUEST_BODY = "Request Body";
        public final static String PARAMETER_CONTENT_TYPE = "Content Type";

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        public enum ContentType {
            APPLICATION_JSON("application/json"), 
            APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
            APPLICATION_XML("application/xml");

            private String value;

            private ContentType(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            public static ContentType fromValue(String value) {
                if (value != null) {
                    for (ContentType item: ContentType.values()) {
                        if (item.getValue().equals(value)) {
                           return item;
                        }
                    }
                }
                throw new IllegalArgumentException("Unsupported ContentType");
            }
        }
    }
}
