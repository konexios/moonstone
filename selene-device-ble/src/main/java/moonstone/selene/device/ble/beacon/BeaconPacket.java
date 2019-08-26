package moonstone.selene.device.ble.beacon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BeaconPacket implements Serializable {
    private static final long serialVersionUID = -3532053484280413959L;

    public static final byte ADV_DATA_TYPE_MANUFACTURER = (byte) 0xFF;

    private byte preamble;
    private byte[] accessAddress = new byte[4];
    private byte[] payloadHeader = new byte[2];
    private String broadcastAddress;
    private int advDataLength;
    private List<BeaconData> data = new ArrayList<>();

    public byte getPreamble() {
        return preamble;
    }

    public void setPreamble(byte preamble) {
        this.preamble = preamble;
    }

    public byte[] getAccessAddress() {
        return accessAddress;
    }

    public void setAccessAddress(byte[] accessAddress) {
        this.accessAddress = accessAddress;
    }

    public byte[] getPayloadHeader() {
        return payloadHeader;
    }

    public void setPayloadHeader(byte[] payloadHeader) {
        this.payloadHeader = payloadHeader;
    }

    public String getBroadcastAddress() {
        return broadcastAddress;
    }

    public void setBroadcastAddress(String broadcastAddress) {
        this.broadcastAddress = broadcastAddress;
    }

    public int getAdvDataLength() {
        return advDataLength;
    }

    public void setAdvDataLength(int advDataLength) {
        this.advDataLength = advDataLength;
    }

    public List<BeaconData> getData() {
        return data;
    }

    public void setData(List<BeaconData> data) {
        this.data = data;
    }

    public BeaconData findBeaconData(byte type) {
        for (BeaconData beaconData : data) {
            if (type == beaconData.getType()) {
                return beaconData;
            }
        }
        return null;
    }

    public static class BeaconData {
        private int length;
        private byte type;
        private byte[] data;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }
    }
}
