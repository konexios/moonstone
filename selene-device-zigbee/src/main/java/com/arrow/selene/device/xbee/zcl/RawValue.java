package com.arrow.selene.device.xbee.zcl;

public class RawValue {
    byte[] value;
    boolean valid;

    public RawValue(byte[] value, boolean valid) {
        this.value = value;
        this.valid = valid;
    }

    public byte[] getValue() {
        return value;
    }

    public boolean isValid() {
        return valid;
    }
}
