package com.arrow.selene;

import java.io.Serializable;
import java.util.Map;

import com.arrow.selene.databus.Databus;

public class SeleneProperties implements Serializable {
    private static final long serialVersionUID = 5955242302793786864L;

    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;
    private String jdbcDriver;
    private String databus = Databus.FILE;
    private String fileDatabusDirectory = SeleneConstants.DEFAULT_FILE_DATABUS_DIRECTORY;
    private String mqttDatabusUrl = SeleneConstants.DEFAULT_MQTT_DATABUS_URL;
    private String mqttDatabusUsername;
    private String mqttDatabusPassword;
    private long databusMaxBuffer = SeleneConstants.DEFAULT_DATA_BUS_MAX_BUFFER;
    private long databusPeekInterval = SeleneConstants.DEFAULT_DATA_BUS_POLLING_INTERVAL;

    public SeleneProperties populateFrom(Map<String, String> map) {
        jdbcUrl = map.getOrDefault("jdbcUrl", jdbcUrl);
        jdbcUser = map.getOrDefault("jdbcUser", jdbcUser);
        jdbcPassword = map.getOrDefault("jdbcPassword", jdbcPassword);
        jdbcDriver = map.getOrDefault("jdbcDriver", jdbcDriver);
        databus = map.getOrDefault("databus", databus);
        fileDatabusDirectory = map.getOrDefault("fileDatabusDirectory", fileDatabusDirectory);
        mqttDatabusUrl = map.getOrDefault("mqttDatabusUrl", mqttDatabusUrl);
        mqttDatabusUsername = map.getOrDefault("mqttDatabusUsername", mqttDatabusUsername);
        mqttDatabusPassword = map.getOrDefault("mqttDatabusPassword", mqttDatabusPassword);
        databusMaxBuffer = Long.parseLong(map.getOrDefault("databusMaxBuffer", "" + databusMaxBuffer));
        databusPeekInterval = Long.parseLong(map.getOrDefault("databusPeekInterval", "" + databusPeekInterval));
        return this;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUser() {
        return jdbcUser;
    }

    public void setJdbcUser(String jdbcUser) {
        this.jdbcUser = jdbcUser;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public String getDatabus() {
        return databus;
    }

    public void setDatabus(String databus) {
        this.databus = databus;
    }

    public String getFileDatabusDirectory() {
        return fileDatabusDirectory;
    }

    public void setFileDatabusDirectory(String fileDatabusDirectory) {
        this.fileDatabusDirectory = fileDatabusDirectory;
    }

    public String getMqttDatabusUrl() {
        return mqttDatabusUrl;
    }

    public void setMqttDatabusUrl(String mqttDatabusUrl) {
        this.mqttDatabusUrl = mqttDatabusUrl;
    }

    public String getMqttDatabusUsername() {
        return mqttDatabusUsername;
    }

    public void setMqttDatabusUsername(String mqttDatabusUsername) {
        this.mqttDatabusUsername = mqttDatabusUsername;
    }

    public String getMqttDatabusPassword() {
        return mqttDatabusPassword;
    }

    public void setMqttDatabusPassword(String mqttDatabusPassword) {
        this.mqttDatabusPassword = mqttDatabusPassword;
    }

    public long getDatabusMaxBuffer() {
        return databusMaxBuffer;
    }

    public void setDatabusMaxBuffer(long databusMaxBuffer) {
        this.databusMaxBuffer = databusMaxBuffer;
    }

    public long getDatabusPeekInterval() {
        return databusPeekInterval;
    }

    public void setDatabusPeekInterval(long databusPeekInterval) {
        this.databusPeekInterval = databusPeekInterval;
    }
}
