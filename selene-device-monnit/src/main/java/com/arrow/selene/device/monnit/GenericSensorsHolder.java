package com.arrow.selene.device.monnit;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.arrow.acs.JsonUtils;

public class GenericSensorsHolder implements Serializable {
    private static final long serialVersionUID = -3250634416517714763L;

    private Set<GenericSensorInfo> values = new HashSet<>();

    public Set<GenericSensorInfo> getValues() {
        return values;
    }

    public void setValues(Set<GenericSensorInfo> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
