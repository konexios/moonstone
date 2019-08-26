package moonstone.selene.device.ble.puck;

import moonstone.selene.device.ble.BleInfo;

public class PuckInfo extends BleInfo {
    private static final long serialVersionUID = -246957266416841557L;

    public static final String DEFAULT_DEVICE_TYPE = "silabs-sensor-puck";

    private String companyId;

    public PuckInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
