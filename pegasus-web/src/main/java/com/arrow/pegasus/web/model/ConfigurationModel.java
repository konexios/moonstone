package com.arrow.pegasus.web.model;

import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.ConfigurationPropertyDataType;

import java.io.Serializable;
import java.util.List;

public class ConfigurationModel implements Serializable {
    private static final long serialVersionUID = 4244557465350415592L;

    private ConfigurationPropertyDataType dataType;
    private ConfigurationPropertyCategory category;
    private String name;
    private String value;
    private String jsonClass;

    public ConfigurationModel() {
    }

    public ConfigurationModel(ConfigurationProperty configurationProperty) {
        this.dataType = configurationProperty.getDataType();
        this.category = configurationProperty.getCategory();
        this.name = configurationProperty.getName();
        this.value = configurationProperty.getValue();
        this.jsonClass = configurationProperty.getJsonClass();
    }

    public ConfigurationPropertyDataType getDataType() {
        return dataType;
    }

    public ConfigurationPropertyCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getJsonClass() {
        return jsonClass;
    }

    private List<ConfigurationProperty> configurations;

    public List<ConfigurationProperty> getConfigurations() {
        return configurations;
    }
}
