package com.arrow.selene.device.ble.gatt;

import com.arrow.selene.device.ble.BleModule;

public class GattFactory {
    public static Gatt initGatt(BleModule<?, ?, ?, ?> module, boolean useDbus) {
        if (useDbus) {
            return initDbusGatt(module);
        } else {
            return initKuraGatt();
        }
    }

    public static Gatt initKuraGatt() {
        return new KuraGatt();
    }

    public static Gatt initDbusGatt(BleModule<?, ?, ?, ?> module) {
        return new DbusGatt(module);
    }
}
