package com.arrow.pegasus;

import org.springframework.session.web.http.DefaultCookieSerializer;

public class CoreSessionCookieSerializer extends DefaultCookieSerializer {

    public CoreSessionCookieSerializer(String productName) {
        super();
        setCookieName(String.format("PSESSION-%s", productName.toUpperCase()));
    }
}
