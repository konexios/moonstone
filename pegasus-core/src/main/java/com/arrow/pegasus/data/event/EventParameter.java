package com.arrow.pegasus.data.event;

import java.util.Date;

public class EventParameter {
    public static EventParameter InString(String name, String value) {
        return new EventParameter(EventParameterType.In, EventParameterDataType.String, name, value);
    }

    public static EventParameter InInteger(String name, int value) {
        return new EventParameter(EventParameterType.In, EventParameterDataType.Integer, name, Integer.toString(value));
    }

    public static EventParameter InLong(String name, long value) {
        return new EventParameter(EventParameterType.In, EventParameterDataType.Long, name, Long.toString(value));
    }

    public static EventParameter InBoolean(String name, boolean value) {
        return new EventParameter(EventParameterType.In, EventParameterDataType.Boolean, name, Boolean.toString(value));
    }

    public static EventParameter InDateTime(String name, Date value) {
        return new EventParameter(EventParameterType.In, EventParameterDataType.DateTime, name, value.toString());
    }

    public static EventParameter OutString(String name, String value) {
        return new EventParameter(EventParameterType.Out, EventParameterDataType.String, name, value);
    }

    public static EventParameter OutInteger(String name, int value) {
        return new EventParameter(EventParameterType.Out, EventParameterDataType.Integer, name,
                Integer.toString(value));
    }

    public static EventParameter OutLong(String name, long value) {
        return new EventParameter(EventParameterType.Out, EventParameterDataType.Long, name, Long.toString(value));
    }

    public static EventParameter OutBoolean(String name, boolean value) {
        return new EventParameter(EventParameterType.Out, EventParameterDataType.Boolean, name,
                Boolean.toString(value));
    }

    public static EventParameter OutDateTime(String name, Date value) {
        return new EventParameter(EventParameterType.Out, EventParameterDataType.DateTime, name, value.toString());
    }

    private EventParameterType type;
    private EventParameterDataType dataType;
    private String name;
    private String value;

    public EventParameter() {
    }

    public EventParameter(EventParameterType type, EventParameterDataType dataType, String name, String value) {
        super();
        this.type = type;
        this.dataType = dataType;
        this.name = name;
        this.value = value;
    }

    public EventParameterType getType() {
        return type;
    }

    public void setType(EventParameterType type) {
        this.type = type;
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

    public EventParameterDataType getDataType() {
        return dataType;
    }

    public void setDataType(EventParameterDataType dataType) {
        this.dataType = dataType;
    }
}
