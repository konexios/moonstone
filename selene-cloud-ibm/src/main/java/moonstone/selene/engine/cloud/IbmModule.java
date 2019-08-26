package moonstone.selene.engine.cloud;

import moonstone.acn.client.cloud.IbmConnector;
import moonstone.acn.client.model.CloudPlatform;
import moonstone.acn.client.model.IbmConfigModel;
import moonstone.selene.data.Ibm;
import moonstone.selene.device.self.SelfModule;
import moonstone.selene.engine.cloud.CloudModuleAbstract;
import moonstone.selene.engine.service.IbmService;

public class IbmModule extends CloudModuleAbstract {
	public IbmModule() {
		super(CloudPlatform.Ibm);
	}

	@Override
	protected void startClient() {
		String method = "startClient";
		Ibm ibm = IbmService.getInstance().findOne();
		if (ibm == null) {
			logInfo(method, "IBM profile not found");
		} else {
			if (ibm.isEnabled()) {
				connector = new IbmConnector(IbmService.getInstance().toModel(new IbmConfigModel(), ibm),
						SelfModule.getInstance().getAcnClient());
				logInfo(method, "starting client ...");
				connector.start();
			} else {
				logInfo(method, "WARNING: IBM profile is disabled");
			}
		}
		super.startClient();
	}

	@Override
	public void stop() {
		super.stop();
		if (connector != null) {
			connector.stop();
		}
	}
}
