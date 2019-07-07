package com.arrow.selene.engine.cloud;

import com.arrow.acn.client.cloud.AzureConnector;
import com.arrow.acn.client.model.AzureConfigModel;
import com.arrow.acn.client.model.CloudPlatform;
import com.arrow.selene.data.Azure;
import com.arrow.selene.device.self.SelfModule;
import com.arrow.selene.engine.service.AzureService;
import com.arrow.selene.service.CryptoService;

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
