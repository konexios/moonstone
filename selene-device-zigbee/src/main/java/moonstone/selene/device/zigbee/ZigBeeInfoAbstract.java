package moonstone.selene.device.zigbee;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import moonstone.acs.JsonUtils;
import moonstone.selene.device.zigbee.data.ZigBeeInfoHolder;
import moonstone.selene.engine.DeviceInfo;

public abstract class ZigBeeInfoAbstract extends DeviceInfo {
    private static final long serialVersionUID = 2357478237648360228L;

    private String address;
    private boolean router;
    private int manufacturerCode;
    private ZigBeeInfoHolder zigbee = new ZigBeeInfoHolder();

    @Override
    public ZigBeeInfoAbstract populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        address = map.getOrDefault("address", address);
        router = Boolean.parseBoolean(map.getOrDefault("router", Boolean.toString(router)));
        manufacturerCode = Integer.parseInt(map.getOrDefault("manufacturerCode", Integer.toString(manufacturerCode)));
        String value = map.get("zigbee");
        if (StringUtils.isNotEmpty(value))
            zigbee = JsonUtils.fromJson(value, ZigBeeInfoHolder.class);
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isRouter() {
        return router;
    }

    public void setRouter(boolean router) {
        this.router = router;
    }

    public int getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(int manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public ZigBeeInfoHolder getZigbee() {
        return zigbee;
    }

    public void setZigbee(ZigBeeInfoHolder zigbee) {
        this.zigbee = zigbee;
    }
}
