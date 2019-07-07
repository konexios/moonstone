package com.arrow.selene.engine.cloud;

import com.arrow.acn.client.cloud.IbmConnector;
import com.arrow.acn.client.model.CloudPlatform;
import com.arrow.acn.client.model.IbmConfigModel;
import com.arrow.selene.data.Ibm;
import com.arrow.selene.device.self.SelfModule;
import com.arrow.selene.engine.service.IbmService;

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
