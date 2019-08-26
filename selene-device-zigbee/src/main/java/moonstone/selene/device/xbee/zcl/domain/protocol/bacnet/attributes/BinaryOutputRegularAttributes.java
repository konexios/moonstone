package moonstone.selene.device.xbee.zcl.domain.protocol.bacnet.attributes;

public interface BinaryOutputRegularAttributes {
    short CHANGE_OF_STATE_COUNT_ATTRIBUTE_ID = 0x000f;
    short CHANGE_OF_STATE_TIME_ATTRIBUTE_ID = 0x0010;
    short DEVICE_TYPE_ATTRIBUTE_ID = 0x001f;
    short ELAPSED_ACTIVE_TIME_ATTRIBUTE_ID = 0x0021;
    short FEEDBACK_VALUE_ATTRIBUTE_ID = 0x0028;
    short OBJECT_IDENTIFIER_ATTRIBUTE_ID = 0x004b;
    short OBJECT_NAME_ATTRIBUTE_ID = 0x004d;
    short OBJECT_TYPE_ATTRIBUTE_ID = 0x004f;
    short TIME_OF_AT_RESET_ATTRIBUTE_ID = 0x0072;
    short TIME_OF_SC_RESET_ATTRIBUTE_ID = 0x0073;
    short PROFILE_NAME_ATTRIBUTE_ID = 0x00a8;
}
