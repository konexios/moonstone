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

import com.arrow.acs.client.model.TsModelAbstract;

public class DeviceEventModel extends TsModelAbstract<DeviceEventModel> {

    private static final long serialVersionUID = -6550295267735297943L;

    private String deviceActionTypeName;
    private String criteria;
    private String status;

    @Override
    protected DeviceEventModel self() {
        return this;
    }

    public DeviceEventModel withDeviceActionTypeName(String deviceActionTypeName) {
        setDeviceActionTypeName(deviceActionTypeName);
        return this;
    }

    public DeviceEventModel withCriteria(String criteria) {
        setCriteria(criteria);
        return this;
    }

    public DeviceEventModel withStatus(String status) {
        setStatus(status);
        return this;
    }

    public String getDeviceActionTypeName() {
        return deviceActionTypeName;
    }

    public void setDeviceActionTypeName(String deviceActionTypeName) {
        this.deviceActionTypeName = deviceActionTypeName;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
