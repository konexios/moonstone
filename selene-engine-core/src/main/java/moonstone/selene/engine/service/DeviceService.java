package moonstone.selene.engine.service;

import java.util.List;

import moonstone.acs.JsonUtils;
import moonstone.selene.data.Device;
import moonstone.selene.data.Gateway;
import moonstone.selene.device.self.SelfModule;
import moonstone.selene.engine.DeviceInfo;
import moonstone.selene.engine.DeviceModule;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.ModuleState;

public class DeviceService extends moonstone.selene.service.DeviceService {
    private static class SingletonHolder {
        static final DeviceService SINGLETON = new DeviceService();
    }

    public static DeviceService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    public Device find(DeviceInfo info) {
        return getDeviceDao().findByTypeAndUid(info.getType(), info.getUid());
    }

    public Device findByTypeAndUid(String type, String uid) {
        return getDeviceDao().findByTypeAndUid(type, uid);
    }

    public Device findByUid(String uid) {
        return getDeviceDao().findByUid(uid);
    }

    public List<Device> find(String type) {
        return getDeviceDao().findByType(type);
    }

    public Device checkAndCreateDevice(DeviceModule<?, ?, ?, ?> module) {
        String method = "checkAndCreateDevice";
        DeviceInfo info = module.getInfo();
        Device device = find(info);
        if (device == null) {
            device = new Device(info.getName(), info.getType(), info.getUid());
            Gateway gateway = SelfModule.getInstance().getGateway();
            if (gateway != null) {
                device.setGatewayId(gateway.getId());
            }
            device.setStatus(ModuleState.CREATED.name());
            device.setInfo(JsonUtils.toJson(info.exportInfo()));
            device.setProperties(JsonUtils.toJson(module.exportProperties()));
            device.setStates(JsonUtils.toJson(module.getStates().exportStates()));
            getDeviceDao().insert(device);
            logInfo(method, "created new device --> id: %d, uid: %s", device.getId(), device.getUid());
        } else {
            logInfo(method, "found id: %d, hid: %s, uid: %s", device.getId(), device.getHid(), device.getUid());
        }
        return device;
    }

    public void loadDeviceProperties(DeviceModule<?, ?, ?, ?> module, Device device) {
        String method = "loadDeviceProperties";
        DeviceInfo info = module.getInfo();
        info.setName(device.getName());
        info.setType(device.getType());
        info.setUid(device.getUid());

        info.importInfo(JsonUtils.fromJson(device.getInfo(), EngineConstants.MAP_TYPE_REF));
        module.importProperties(JsonUtils.fromJson(device.getProperties(), EngineConstants.MAP_TYPE_REF));
        //		module.getStates().importStates(JsonUtils.fromJson(device.getStates(), EngineConstants.MAP_TYPE_REF));

        logInfo(method, "information was loaded from database for device: %s", device.getName());
    }
}
