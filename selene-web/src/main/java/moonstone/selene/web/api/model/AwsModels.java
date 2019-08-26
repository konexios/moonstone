package moonstone.selene.web.api.model;

import java.io.Serializable;
import java.time.Instant;

import moonstone.selene.data.Aws;

public class AwsModels {

	public static class AwsModel extends BaseModels.BaseEntity {
		private static final long serialVersionUID = 5684847779558275692L;

		private String host;
		private int port;
		private String rootCert;
		private String clientCert;
		private String privateKey;

		public AwsModel(Aws aws) {
			super(aws.getId(), aws.isEnabled(), Instant.ofEpochMilli(aws.getCreatedTs()),
			        Instant.ofEpochMilli(aws.getModifiedTs()));
			this.host = aws.getHost();
			this.port = aws.getPort();
			this.rootCert = aws.getRootCert();
			this.clientCert = aws.getClientCert();
			this.privateKey = aws.getPrivateKey();
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		public String getRootCert() {
			return rootCert;
		}

		public String getClientCert() {
			return clientCert;
		}

		public String getPrivateKey() {
			return privateKey;
		}
	}

	public static class AwsUpsert implements Serializable {
		private static final long serialVersionUID = 3828836890163987841L;

		private AwsModel aws;

		public AwsUpsert(AwsModel aws) {
			this.aws = aws;
		}

		public AwsModel getAws() {
			return aws;
		}
	}
}
