package com.arrow.selene.device.cli;

import java.util.Map;

import com.arrow.selene.engine.DeviceProperties;

public class CliProperties extends DeviceProperties {
    private static final long serialVersionUID = 5997752900558460342L;

    private static final long DEFAULT_COMMAND_INTERVAL_IN_SECS = 5L;
    private static final String DEFAULT_ARRAY_DELIMITER = ",";

    public static final String STRING_DATA_TYPE = "string";
    public static final String LONG_DATA_TYPE = "long";
    public static final String DOUBLE_DATA_TYPE = "double";
    public static final String STRING_ARRAY_DATA_TYPE = "string-array";
    public static final String LONG_ARRAY_DATA_TYPE = "long-array";
    public static final String DOUBLE_ARRAY_DATA_TYPE = "double-array";
    public static final String RAW_DATA_TYPE = "raw";

    private String dataType = STRING_DATA_TYPE;
    private String command;
    private long commandIntervalInSecs = DEFAULT_COMMAND_INTERVAL_IN_SECS;
    private String arrayDelimiter = DEFAULT_ARRAY_DELIMITER;

    @Override
    public CliProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        dataType = map.getOrDefault("dataType", dataType);
        command = map.getOrDefault("command", command);
        commandIntervalInSecs = Long
                .parseLong(map.getOrDefault("commandIntervalMs", Long.toString(commandIntervalInSecs)));
        arrayDelimiter = map.getOrDefault("arrayDelimiter", arrayDelimiter);
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (dataType != null)
            map.put("dataType", dataType);
        if (command != null)
            map.put("command", command);
        map.put("commandIntervalInSecs", Long.toString(commandIntervalInSecs));
        if (arrayDelimiter != null)
            map.put("arrayDelimiter", arrayDelimiter);
        return map;
    }

    public String getArrayDelimiter() {
        return arrayDelimiter;
    }

    public void setArrayDelimiter(String arrayDelimiter) {
        this.arrayDelimiter = arrayDelimiter;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getCommandIntervalInSecs() {
        return commandIntervalInSecs;
    }

    public void setCommandIntervalInSecs(long commandIntervalInSecs) {
        this.commandIntervalInSecs = commandIntervalInSecs;
    }
}
