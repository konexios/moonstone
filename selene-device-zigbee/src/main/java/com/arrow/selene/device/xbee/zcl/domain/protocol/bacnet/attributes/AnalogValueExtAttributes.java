package com.arrow.selene.device.xbee.zcl.domain.protocol.bacnet.attributes;

public interface AnalogValueExtAttributes {
    short ACKED_TRANSACTIONS_ATTRIBUTE_ID = 0x0000;
    short NOTIFICATION_CLASS_ATTRIBUTE_ID = 0x0011;
    short DEADBAND_ATTRIBUTE_ID = 0x0019;
    short EVENT_ENABLE_ATTRIBUTE_ID = 0x0023;
    short EVENT_STATE_ATTRIBUTE_ID = 0x0024;
    short HIGH_LIMIT_ATTRIBUTE_ID = 0x002d;
    short LIMIT_ENABLE_ATTRIBUTE_ID = 0x0034;
    short LOW_LIMIT_ATTRIBUTE_ID = 0x003b;
    short NOTIFY_TYPE_ATTRIBUTE_ID = 0x0048;
    short TIME_DELAY_ATTRIBUTE_ID = 0x0071;
    short EVENT_TIMESTAMPTS_ATTRIBUTE_ID = 0x0082;
}
