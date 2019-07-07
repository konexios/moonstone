package com.arrow.selene.engine.cloud;

import com.arrow.acn.client.cloud.IotConnectConnector;
import com.arrow.acn.client.model.CloudPlatform;
import com.arrow.selene.device.self.SelfModule;

public class IotConnectModule extends CloudModuleAbstract {
    private static class SingletonHolder {
        static final IotConnectModule SINGLETON = new IotConnectModule();
    }

    public static IotConnectModule getInstance() {
        return SingletonHolder.SINGLETON;
    }

    private IotConnectModule() {
        super(CloudPlatform.IotConnect);
    }

    @Override
    public void start() {
        connector = new IotConnectConnector(SelfModule.getInstance().getProperties().getIotConnectMqtt(),
                SelfModule.getInstance().getProperties().getIotConnectMqttVHost(),
                SelfModule.getInstance().getGateway().getHid(), SelfModule.getInstance().getAcnClient());
        connector.start();
        super.start();
    }
}
