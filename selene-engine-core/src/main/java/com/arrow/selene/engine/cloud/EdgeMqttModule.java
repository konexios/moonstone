package com.arrow.selene.engine.cloud;

import com.arrow.acn.client.model.CloudPlatform;
import com.arrow.selene.device.self.SelfModule;

public class EdgeMqttModule extends CloudModuleAbstract {
    private static class SingletonHolder {
        static final EdgeMqttModule SINGLETON = new EdgeMqttModule();
    }

    public static EdgeMqttModule getInstance() {
        return SingletonHolder.SINGLETON;
    }

    private EdgeMqttModule() {
        super(CloudPlatform.IotConnect);
    }

    @Override
    public void start() {
        connector = new EdgeMqttConnector(SelfModule.getInstance().getProperties().getEdgeMqttUrl(),
                SelfModule.getInstance().getGateway().getHid(), SelfModule.getInstance().getAcnClient());
        connector.start();
        super.start();
    }
}
