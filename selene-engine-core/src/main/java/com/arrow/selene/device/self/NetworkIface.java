package com.arrow.selene.device.self;

import java.io.Serializable;
import java.util.List;

public class NetworkIface implements Serializable {
    private static final long serialVersionUID = 2427812088573562009L;

    private String displayName;
    private String name;
    private String hardwareAddress;
    private List<String> inetAddresses;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHardwareAddress() {
        return hardwareAddress;
    }

    public void setHardwareAddress(String hardwareAddress) {
        this.hardwareAddress = hardwareAddress;
    }

    public List<String> getInetAddresses() {
        return inetAddresses;
    }

    public void setInetAddresses(List<String> inetAddresses) {
        this.inetAddresses = inetAddresses;
    }
}
