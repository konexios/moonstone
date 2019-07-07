package com.arrow.selene.service;

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.dao.DeviceDao;
import com.arrow.selene.data.Device;

public class DeviceService extends ServiceAbstract {

    private static class SingletonHolder {
        private static final DeviceService SINGLETON = new DeviceService();
    }

    public static DeviceService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    private final DeviceDao deviceDao;

    protected DeviceService() {
        super();
        deviceDao = DeviceDao.getInstance();
    }

    public Device save(Device device) {
        Validate.notNull(device, "device is null");
        deviceDao.update(device);
        return device;
    }

	public void delete(Device device) {
		Validate.notNull(device, "device is null");
		deviceDao.delete(device.getId());
		logInfo("delete", "Device deleted from database successfully...!");

	}

    public List<Device> findAll() {
        return getDeviceDao().findAll();
    }

    public Device find(long id) {
        return getDeviceDao().findById(id);
    }

    public boolean createDevice(Device device) {
        try {
            getDeviceDao().insert(device);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected DeviceDao getDeviceDao() {
        return deviceDao;
    }

    public boolean sendCommand(long deviceId, String commandJson) {
        DatabusService databus = DatabusService.getInstance();
        if (databus != null && deviceId >= 0) {
            databus.send("selene-command-" + deviceId, commandJson.getBytes());
        } else {
            return false;
        }

        return true;
    }
}
