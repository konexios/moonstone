package moonstone.selene.engine.service;

import java.util.List;

import org.apache.commons.lang3.Validate;

import moonstone.acn.client.AcnClientVersion;
import moonstone.acn.client.model.CreateGatewayModel;
import moonstone.acn.client.model.UpdateGatewayModel;
import moonstone.acn.client.model.CreateGatewayModel.GatewayType;
import moonstone.acs.client.model.VersionModel;
import moonstone.selene.data.Gateway;
import moonstone.selene.device.self.SelfInfo;
import moonstone.selene.device.self.SelfModule;
import moonstone.selene.device.self.SelfProperties;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.SeleneEngineCoreVersion;

public class GatewayService extends moonstone.selene.service.GatewayService {

    private static class SingletonHolder {
        static final GatewayService SINGLETON = new GatewayService();
    }

    public static GatewayService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    public Gateway checkAndCreateDefaultGateway(SelfModule self) {
        String method = "checkAndCreateDefaultGateway";
        List<Gateway> gateways = getGatewayDao().findAll();
        logInfo(method, "gateways size: %d", gateways.size());
        Gateway gateway;
        if (gateways.isEmpty()) {
            gateway = populate(new Gateway(), self);
            getGatewayDao().insert(gateway);
            logInfo(method, "saved default gateway to database --> id: %d, uid: %s", gateway.getId(), gateway.getUid());
        } else {
            // SHOULD BE ONLY 1 RECORD
            gateway = gateways.get(0);
            logInfo(method, "found id: %d, uid: %s, createdTs: %s", gateway.getId(), gateway.getUid(),
                    gateway.getCreatedTs());
        }
        return gateway;
    }

    public UpdateGatewayModel populateModel(Gateway gateway, UpdateGatewayModel model) {
        Validate.notNull(gateway, "gateway is null");
        Validate.notNull(model, "model is null");
        model.setName(gateway.getName());
        model.setType(GatewayType.Local);
        model.setUid(gateway.getUid());
        model.setOsName(String.format("%s, %s, %s", System.getProperty("os.name"), System.getProperty("os.version"),
                System.getProperty("os.arch")));
        model.setDeviceType(EngineConstants.GATEWAY_DEVICE_TYPE);
        VersionModel version = SeleneEngineCoreVersion.get();
        model.setSoftwareName(version.getName());
        model.setSoftwareVersion(String.format("%d.%d.%d", version.getMajor(), version.getMinor(), version.getBuild()));
        version = AcnClientVersion.get();
        model.setSdkVersion(String.format("%d.%d", version.getMajor(), version.getMinor()));
        return model;
    }

    public CreateGatewayModel populateModel(Gateway gateway, CreateGatewayModel model) {
        Validate.notNull(model, "model is null");
        model.setName(gateway.getName());
        model.setType(GatewayType.Local);
        model.setUid(gateway.getUid());
        model.setOsName(String.format("%s, %s, %s", System.getProperty("os.name"),
                System.getProperty("os" + ".version"), System.getProperty("os.arch")));
        model.setDeviceType(EngineConstants.GATEWAY_DEVICE_TYPE);
        VersionModel version = SeleneEngineCoreVersion.get();
        model.setSoftwareName(version.getName());
        model.setSoftwareVersion(String.format("%d.%d.%d", version.getMajor(), version.getMinor(), version.getBuild()));
        version = AcnClientVersion.get();
        model.setSdkVersion(String.format("%d.%d", version.getMajor(), version.getMinor()));
        return model;
    }

    private Gateway populate(Gateway gateway, SelfModule self) {
        SelfInfo info = self.getInfo();
        SelfProperties props = self.getProperties();
        gateway.setName(info.getName());
        gateway.setUid(info.getUid());
        gateway.setIotConnectUrl(props.getIotConnectUrl());
        gateway.setApiKey(props.getApiKey());
        gateway.setSecretKey(props.getSecretKey());
        gateway.setHeartBeatIntervalMs(props.getHeartBeatIntervalMs());
        gateway.setPurgeTelemetryIntervalDays(props.getPurgeTelemetryIntervalDays());
        gateway.setPurgeMessagesIntervalDays(props.getPurgeMessagesIntervalDays());
        return gateway;
    }
}
