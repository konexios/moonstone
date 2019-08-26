package moonstone.selene.engine.cloud;

import moonstone.acn.client.cloud.AzureConnector;
import moonstone.acn.client.model.AzureConfigModel;
import moonstone.acn.client.model.CloudPlatform;
import moonstone.selene.data.Azure;
import moonstone.selene.device.self.SelfModule;
import moonstone.selene.engine.cloud.CloudModuleAbstract;
import moonstone.selene.engine.service.AzureService;
import moonstone.selene.service.CryptoService;

public class AzureModule extends CloudModuleAbstract {
	public AzureModule() {
		super(CloudPlatform.Azure);
	}

	@Override
	protected void startClient() {
		String method = "startClient";
		logInfo(method, "...");
		Azure azure = AzureService.getInstance().findOne();
		if (azure == null) {
			logInfo(method, "Azure profile not found");
		} else {
			if (azure.isEnabled()) {
				AzureConfigModel model = new AzureConfigModel();
				model.setHost(azure.getHost());
				model.setAccessKey(CryptoService.getInstance().decrypt(azure.getAccessKey()));
				logInfo(method, "starting azure connector ...");
				connector = new AzureConnector(model, SelfModule.getInstance().getGateway().getUid(),
						SelfModule.getInstance().getAcnClient());
				connector.start();
			} else {
				logInfo(method, "WARNING: Azure profile is disabled");
			}
		}
		super.startClient();
		logInfo(method, "ready!");
	}

	@Override
	public void stop() {
		super.stop();
		if (connector != null) {
			connector.stop();
		}
	}
}
