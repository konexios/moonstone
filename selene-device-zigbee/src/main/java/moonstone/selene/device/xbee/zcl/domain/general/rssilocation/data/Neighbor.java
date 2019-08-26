package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.data;

import java.util.Arrays;

public class Neighbor {
    private byte[] neighborAddress;
    private short coord1;
    private short coord2;
    private short coord3;
    private byte rssi;
    private byte numberRssiMeasurements;

    public byte[] getNeighborAddress() {
        return neighborAddress;
    }

    public Neighbor withNeighborAddress(byte[] neighborAddress) {
        this.neighborAddress = neighborAddress;
        return this;
    }

    public short getCoord1() {
        return coord1;
    }

    public Neighbor withCoord1(short coord1) {
        this.coord1 = coord1;
        return this;
    }

    public short getCoord2() {
        return coord2;
    }

    public Neighbor withCoord2(short coord2) {
        this.coord2 = coord2;
        return this;
    }

    public short getCoord3() {
        return coord3;
    }

    public Neighbor withCoord3(short coord3) {
        this.coord3 = coord3;
        return this;
    }

    public byte getRssi() {
        return rssi;
    }

    public Neighbor withRssi(byte rssi) {
        this.rssi = rssi;
        return this;
    }

    public byte getNumberRssiMeasurements() {
        return numberRssiMeasurements;
    }

    public Neighbor withNumberRssiMeasurements(byte numberRssiMeasurements) {
        this.numberRssiMeasurements = numberRssiMeasurements;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Neighbor neighbor = (Neighbor) o;

        if (coord1 != neighbor.coord1) {
            return false;
        }
        if (coord2 != neighbor.coord2) {
            return false;
        }
        if (coord3 != neighbor.coord3) {
            return false;
        }
        if (rssi != neighbor.rssi) {
            return false;
        }
        if (numberRssiMeasurements != neighbor.numberRssiMeasurements) {
            return false;
        }
        return Arrays.equals(neighborAddress, neighbor.neighborAddress);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(neighborAddress);
        result = 31 * result + (int) coord1;
        result = 31 * result + (int) coord2;
        result = 31 * result + (int) coord3;
        result = 31 * result + (int) rssi;
        result = 31 * result + (int) numberRssiMeasurements;
        return result;
    }
}
