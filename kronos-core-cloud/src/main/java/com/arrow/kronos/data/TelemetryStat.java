package com.arrow.kronos.data;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class TelemetryStat implements Serializable {
    private static final long serialVersionUID = 6187174160349447928L;

    @Id
    private String name;
    @Field
    private String value;

    public TelemetryStat withName(String name) {
        setName(name);
        return this;
    }

    public TelemetryStat withValue(String value) {
        setValue(value);
        return this;
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
}
