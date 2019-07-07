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

public class IbmConfigModel implements Serializable {
    private static final long serialVersionUID = -2213226841262811624L;

    private String organizationId;
    private String gatewayType;
    private String gatewayId;
    private String authMethod;
    private String authToken;

    public IbmConfigModel withOrganizationId(String organizationId) {
        setOrganizationId(organizationId);
        return this;
    }

    public IbmConfigModel withGatewayType(String gatewayType) {
        setGatewayType(gatewayType);
        return this;
    }

    public IbmConfigModel withGatewayId(String gatewayId) {
        setGatewayId(gatewayId);
        return this;
    }

    public IbmConfigModel withAuthMethod(String authMethod) {
        setAuthMethod(authMethod);
        return this;
    }

    public IbmConfigModel withAuthToken(String authToken) {
        setAuthToken(authToken);
        return this;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authMethod == null) ? 0 : authMethod.hashCode());
        result = prime * result + ((authToken == null) ? 0 : authToken.hashCode());
        result = prime * result + ((gatewayId == null) ? 0 : gatewayId.hashCode());
        result = prime * result + ((gatewayType == null) ? 0 : gatewayType.hashCode());
        result = prime * result + ((organizationId == null) ? 0 : organizationId.hashCode());
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
        IbmConfigModel other = (IbmConfigModel) obj;
        if (authMethod == null) {
            if (other.authMethod != null)
                return false;
        } else if (!authMethod.equals(other.authMethod))
            return false;
        if (authToken == null) {
            if (other.authToken != null)
                return false;
        } else if (!authToken.equals(other.authToken))
            return false;
        if (gatewayId == null) {
            if (other.gatewayId != null)
                return false;
        } else if (!gatewayId.equals(other.gatewayId))
            return false;
        if (gatewayType == null) {
            if (other.gatewayType != null)
                return false;
        } else if (!gatewayType.equals(other.gatewayType))
            return false;
        if (organizationId == null) {
            if (other.organizationId != null)
                return false;
        } else if (!organizationId.equals(other.organizationId))
            return false;
        return true;
    }
}
