package com.arrow.pegasus.data;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;

public class ConfigurationProperty implements Serializable {

    private static final long serialVersionUID = -280918469715264350L;

    public static ConfigurationProperty WithStringValue(ConfigurationPropertyCategory category, String name,
            String value) {
        return new ConfigurationProperty(ConfigurationPropertyDataType.String, category, name, value);
    }

    public static ConfigurationProperty WithIntegerValue(ConfigurationPropertyCategory category, String name,
            int value) {
        return new ConfigurationProperty(ConfigurationPropertyDataType.Integer, category, name,
                Integer.toString(value));
    }

    public static ConfigurationProperty WithLongValue(ConfigurationPropertyCategory category, String name, long value) {
        return new ConfigurationProperty(ConfigurationPropertyDataType.Long, category, name, Long.toString(value));
    }

    public static ConfigurationProperty WithBooleanValue(ConfigurationPropertyCategory category, String name,
            boolean value) {
        return new ConfigurationProperty(ConfigurationPropertyDataType.Boolean, category, name,
                Boolean.toString(value));
    }

    // ECAHR-160
    public static ConfigurationProperty WithJsonValue(ConfigurationPropertyCategory category, String name, String value,
            Class<?> jsonClass) {
        return new ConfigurationProperty(ConfigurationPropertyDataType.Json, category, name, value, jsonClass);
    }

    private ConfigurationPropertyDataType dataType;
    private ConfigurationPropertyCategory category;
    private String name;
    private String value;
    // ECAHR-160
    private String jsonClass;

    public ConfigurationProperty() {
    }

    public ConfigurationProperty(ConfigurationPropertyDataType dataType, ConfigurationPropertyCategory category,
            String name, String value) {
        super();
        this.dataType = dataType;
        this.category = category;
        this.name = name;
        this.value = value;
    }

    // ECAHR-160
    public ConfigurationProperty(ConfigurationPropertyDataType dataType, ConfigurationPropertyCategory category,
            String name, String value, Class<?> jsonClass) {
        this(dataType, category, name, value);
        this.jsonClass = jsonClass.getName();
    }

    public ConfigurationPropertyCategory getCategory() {
        return category;
    }

    public void setCategory(ConfigurationPropertyCategory category) {
        this.category = category;
    }

    public ConfigurationPropertyDataType getDataType() {
        return dataType;
    }

    public void setDataType(ConfigurationPropertyDataType dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String strValue() {
        return getValue();
    }

    public Integer intValue() {
        return Integer.parseInt(getValue());
    }

    public Long longValue() {
        return Long.parseLong(getValue());
    }

    public Boolean boolValue() {
        return Boolean.parseBoolean(getValue());
    }

    public Object jsonValue() {
        if (!StringUtils.isEmpty(this.value) && !StringUtils.isEmpty(this.jsonClass)) {
            try {
                return JsonUtils.fromJson(this.value, Class.forName(this.jsonClass));
            } catch (ClassNotFoundException e) {
                throw new AcsLogicalException("Invalid jsonClass: " + this.jsonClass);
            }
        } else {
            return this.value;
        }
    }

    public <T> T jsonValue(TypeReference<T> typeRef) {
        if (StringUtils.isEmpty(this.value)) {
            return null;
        } else {
            return JsonUtils.fromJson(this.value, typeRef);
        }
    }

    // ECAHR-160
    public String getJsonClass() {
        return jsonClass;
    }

    // ECAHR-160
    public void setJsonClass(String jsonClass) {
        this.jsonClass = jsonClass;
    }
}
