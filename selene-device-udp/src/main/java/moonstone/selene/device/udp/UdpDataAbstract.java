package moonstone.selene.device.udp;

import moonstone.selene.engine.DeviceDataAbstract;

public abstract class UdpDataAbstract extends DeviceDataAbstract implements UdpData {

    private byte[] rawData;

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    protected byte[] getRawData() {
        return rawData;
    }
}
