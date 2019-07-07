package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege.AccessLevel;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class AccessKeyModels {
	public static class AccessKeyList extends CoreDocumentModel {
		private static final long serialVersionUID = 7539467030790229876L;
		private String name;
		private String rawApiKey;
		private long expiration;
		private String ownerDisplayName;

		public AccessKeyList(AccessKey accessKey, String ownerDisplayName, String rawApiKey) {
			super(accessKey.getId(), accessKey.getHid());

			this.name = accessKey.getName();
			this.rawApiKey = rawApiKey;
			this.expiration = accessKey.getExpiration().toEpochMilli();
			this.ownerDisplayName = ownerDisplayName;
		}

		public String getName() {
			return name;
		}

		public String getRawApiKey() {
			return rawApiKey;
		}

		public long getExpiration() {
			return expiration;
		}

		public String getOwnerDisplayName() {
			return ownerDisplayName;
		}
	}

	public static class AccessKeyModel extends CoreDocumentModel {
		private static final long serialVersionUID = -1538128732176919242L;

		private String name;
		private long expiration;
		private String rawApiKey;
		private String rawSecretKey;
		private String aes256ApiKey;
		private String aes256SecretKey;
		private String aes128ApiKey;
		private String aes128SecretKey;
		private List<AccessPrivilegeModel> privileges;

		public AccessKeyModel() {
			super(null, null);
			this.privileges = new ArrayList<>();
		}

		public AccessKeyModel(AccessKey accessKey) {
			super(accessKey.getId(), accessKey.getHid());

			this.name = accessKey.getName();
			this.expiration = accessKey.getExpiration().toEpochMilli();

			this.privileges = new ArrayList<>(accessKey.getPrivileges().size());
		}

		public String getName() {
			return name;
		}

		public long getExpiration() {
			return expiration;
		}

		public String getRawApiKey() {
			return rawApiKey;
		}

		public void setRawApiKey(String rawApiKey) {
			this.rawApiKey = rawApiKey;
		}

		public String getRawSecretKey() {
			return rawSecretKey;
		}

		public void setRawSecretKey(String rawSecretKey) {
			this.rawSecretKey = rawSecretKey;
		}

		public String getAes256ApiKey() {
			return aes256ApiKey;
		}

		public void setAes256ApiKey(String aes256ApiKey) {
			this.aes256ApiKey = aes256ApiKey;
		}

		public String getAes256SecretKey() {
			return aes256SecretKey;
		}

		public void setAes256SecretKey(String aes256SecretKey) {
			this.aes256SecretKey = aes256SecretKey;
		}

		public String getAes128ApiKey() {
			return aes128ApiKey;
		}

		public void setAes128ApiKey(String aes128ApiKey) {
			this.aes128ApiKey = aes128ApiKey;
		}

		public String getAes128SecretKey() {
			return aes128SecretKey;
		}

		public void setAes128SecretKey(String aes128SecretKey) {
			this.aes128SecretKey = aes128SecretKey;
		}

		public List<AccessPrivilegeModel> getPrivileges() {
			return privileges;
		}
	}

	public static class AccessPrivilegeModel implements Serializable {
		private static final long serialVersionUID = 165982751854552357L;

		private AccessLevel level;
		private String pri;
		private DeviceModels.DeviceOption device;
		private GatewayModels.GatewayOption gateway;
		private NodeModels.NodeOption node;

		public AccessPrivilegeModel() {
		}

		public AccessPrivilegeModel(AccessLevel level, String pri) {
			this.level = level;
			this.pri = pri;
		}

		public AccessLevel getLevel() {
			return level;
		}

		public void setLevel(AccessLevel level) {
			this.level = level;
		}

		public DeviceModels.DeviceOption getDevice() {
			return device;
		}

		public void setDevice(DeviceModels.DeviceOption device) {
			this.device = device;
		}

		public GatewayModels.GatewayOption getGateway() {
			return gateway;
		}

		public void setGateway(GatewayModels.GatewayOption gateway) {
			this.gateway = gateway;
		}

		public NodeModels.NodeOption getNode() {
			return node;
		}

		public void setNode(NodeModels.NodeOption node) {
			this.node = node;
		}

		public String getPri() {
			return pri;
		}

		public void setPri(String pri) {
			this.pri = pri;
		}
	}
}
