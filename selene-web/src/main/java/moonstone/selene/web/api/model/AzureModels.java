package moonstone.selene.web.api.model;

import java.io.Serializable;
import java.time.Instant;

import moonstone.selene.data.Azure;

public class AzureModels {

	public static class AzureModel extends BaseModels.BaseEntity {
		private static final long serialVersionUID = -888701507548933247L;

		private String host;
		private String accessKey;

		public AzureModel(Azure azure) {
			super(azure.getId(), azure.isEnabled(), Instant.ofEpochMilli(azure.getCreatedTs()),
					Instant.ofEpochMilli(azure.getModifiedTs()));
			this.host = azure.getHost();
			this.accessKey = azure.getAccessKey();
		}

		public String getHost() {
			return host;
		}

		public String getAccessKey() {
			return accessKey;
		}
	}

	public static class AzureUpsert implements Serializable {
		private static final long serialVersionUID = -3452136336990260978L;

		private AzureModel azure;

		public AzureUpsert(AzureModel azure) {
			this.azure = azure;
		}

		public AzureModel getAzure() {
			return azure;
		}
	}

}
