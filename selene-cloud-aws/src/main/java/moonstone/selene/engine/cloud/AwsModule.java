package moonstone.selene.engine.cloud;

import moonstone.acn.client.cloud.AwsConnector;
import moonstone.acn.client.model.AwsConfigModel;
import moonstone.acn.client.model.CloudPlatform;
import moonstone.selene.data.Aws;
import moonstone.selene.device.self.SelfModule;
import moonstone.selene.engine.cloud.CloudModuleAbstract;
import moonstone.selene.engine.service.AwsService;
import moonstone.selene.service.CryptoService;

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
