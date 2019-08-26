package moonstone.selene.device.udp;

import moonstone.selene.engine.DeviceData;

public interface UdpData extends DeviceData {
    int getSize();

    boolean parseRawData(byte[] data);
}
