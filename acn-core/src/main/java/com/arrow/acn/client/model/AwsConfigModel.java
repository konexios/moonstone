/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acn.client.model;

import java.io.Serializable;

public class AwsConfigModel implements Serializable {
    private static final long serialVersionUID = -3541308072074902349L;

    private String host;
    private int port = 8883;
    private String caCert;
    private String clientCert;
    private String privateKey;

    public AwsConfigModel withHost(String host) {
        setHost(host);
        return this;
    }

    public AwsConfigModel withPort(int port) {
        setPort(port);
        return this;
    }

    public AwsConfigModel withClientCert(String clientCert) {
        setClientCert(clientCert);
        return this;
    }

    public AwsConfigModel withPrivateKey(String privateKey) {
        setPrivateKey(privateKey);
        return this;
    }

    public AwsConfigModel withCaCert(String caCert) {
        setCaCert(caCert);
        return this;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getClientCert() {
        return clientCert;
    }

    public void setClientCert(String clientCert) {
        this.clientCert = clientCert;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getCaCert() {
        return caCert;
    }

    public void setCaCert(String caCert) {
        this.caCert = caCert;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((caCert == null) ? 0 : caCert.hashCode());
        result = prime * result + ((clientCert == null) ? 0 : clientCert.hashCode());
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + port;
        result = prime * result + ((privateKey == null) ? 0 : privateKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AwsConfigModel other = (AwsConfigModel) obj;
        if (caCert == null) {
            if (other.caCert != null)
                return false;
        } else if (!caCert.equals(other.caCert))
            return false;
        if (clientCert == null) {
            if (other.clientCert != null)
                return false;
        } else if (!clientCert.equals(other.clientCert))
            return false;
        if (host == null) {
            if (other.host != null)
                return false;
        } else if (!host.equals(other.host))
            return false;
        if (port != other.port)
            return false;
        if (privateKey == null) {
            if (other.privateKey != null)
                return false;
        } else if (!privateKey.equals(other.privateKey))
            return false;
        return true;
    }
}
