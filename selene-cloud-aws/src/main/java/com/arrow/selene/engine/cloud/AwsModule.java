package com.arrow.selene.engine.cloud;

import com.arrow.acn.client.cloud.AwsConnector;
import com.arrow.acn.client.model.AwsConfigModel;
import com.arrow.acn.client.model.CloudPlatform;
import com.arrow.selene.data.Aws;
import com.arrow.selene.device.self.SelfModule;
import com.arrow.selene.engine.service.AwsService;
import com.arrow.selene.service.CryptoService;

public class AwsModule extends CloudModuleAbstract {
	public AwsModule() {
		super(CloudPlatform.Aws);
	}

	@Override
	protected void startClient() {
		String method = "startClient";
		Aws aws = AwsService.getInstance().findOne();
		if (aws == null) {
			logInfo(method, "AWS profile not found");
		} else {
			logInfo(method, "found AWS module, host: %s", aws.getHost());
			if (aws.isEnabled()) {
				AwsConfigModel model = new AwsConfigModel();
				CryptoService crypto = CryptoService.getInstance();
				model.setCaCert(crypto.decrypt(aws.getRootCert()));
				model.setClientCert(crypto.decrypt(aws.getClientCert()));
				model.setPrivateKey(crypto.decrypt(aws.getPrivateKey()));
				model.setHost(aws.getHost());
				logInfo(method, "instantiating AWSConnector ...");
				connector = new AwsConnector(model, SelfModule.getInstance().getAcnClient());
				connector.start();
				logInfo(method, "AWSConnector started!");
			} else {
				logInfo(method, "WARNING: AWS profile is disabled");
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

	@Override
	protected boolean isBatchSendingMode() {
		return false;
	}
}
