package moonstone.selene.engine.cloud;

import moonstone.acn.client.cloud.IotConnectConnector;
import moonstone.acn.client.model.CloudPlatform;
import moonstone.selene.device.self.SelfModule;

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
