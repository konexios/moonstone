package com.arrow.pegasus.itus;

import java.io.Serializable;

public class GatewayLdapAuth implements Serializable {
    private static final long serialVersionUID = 3023264829046031022L;

    private String domain;
    private String url;
    private String username;
    private String password;

    public GatewayLdapAuth(String domain, String url, String username, String password) {
        super();
        this.domain = domain;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getDomain() {
        return domain;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
