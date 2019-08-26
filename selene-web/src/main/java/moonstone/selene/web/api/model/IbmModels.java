package moonstone.selene.web.api.model;

import java.io.Serializable;
import java.time.Instant;

import moonstone.selene.data.Ibm;

public class IbmModels {

	public static class IbmModel extends BaseModels.BaseEntity {
		private static final long serialVersionUID = 466604688373581312L;

		private String organizationId;
		private String gatewayType;
		private String gatewayId;
		private String authMethod;
		private String authToken;

		public IbmModel(Ibm ibm) {
			super(ibm.getId(), ibm.isEnabled(), Instant.ofEpochMilli(ibm.getCreatedTs()),
					Instant.ofEpochMilli(ibm.getModifiedTs()));
			this.organizationId = ibm.getOrganizationId();
			this.gatewayType = ibm.getGatewayType();
			this.gatewayId = ibm.getGatewayId();
			this.authMethod = ibm.getAuthMethod();
			this.authToken = ibm.getAuthToken();
		}

		public String getOrganizationId() {
			return organizationId;
		}

		public String getGatewayType() {
			return gatewayType;
		}

		public String getGatewayId() {
			return gatewayId;
		}

		public String getAuthMethod() {
			return authMethod;
		}

		public String getAuthToken() {
			return authToken;
		}
	}

	public static class IbmUpsert implements Serializable {
		private static final long serialVersionUID = -8391447392155963433L;

		private IbmModel ibm;

		public IbmUpsert(IbmModel ibm) {
			this.ibm = ibm;
		}

		public IbmModel getIbm() {
			return ibm;
		}
	}

}
