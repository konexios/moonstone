package com.arrow.kronos.web.model;

import java.io.Serializable;

import com.arrow.kronos.data.AwsAccount;
import com.arrow.kronos.data.AzureAccount;
import com.arrow.kronos.data.IbmAccount;
import com.arrow.kronos.data.IoTProvider;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class IoTProviderModels {

	public static class IoTConfigModel implements Serializable {
		private static final long serialVersionUID = 1723842527441898386L;

		private IoTProvider iotProvider;
		private AwsAccountModel aws;
		private IbmAccountModel ibm;
		private AzureAccountModel azure;

		public IoTConfigModel() {
			this.iotProvider = IoTProvider.ArrowConnect;
		}

		public IoTConfigModel(IoTProvider iotProvider) {
			this.iotProvider = iotProvider;
		}

		public IoTProvider getIotProvider() {
			return iotProvider;
		}

		public void setIotProvider(IoTProvider iotProvider) {
			this.iotProvider = iotProvider;
		}

		public AwsAccountModel getAws() {
			return aws;
		}

		public void setAws(AwsAccountModel aws) {
			this.aws = aws;
		}

		public IbmAccountModel getIbm() {
			return ibm;
		}

		public void setIbm(IbmAccountModel ibm) {
			this.ibm = ibm;
		}

		public AzureAccountModel getAzure() {
			return azure;
		}

		public void setAzure(AzureAccountModel azure) {
			this.azure = azure;
		}
	}

	public static class IbmAccountModel extends CoreDocumentModel {
		private static final long serialVersionUID = -2732156377788813305L;

		private String organizationId;
		private String apiKey;
		private String authToken;

		public IbmAccountModel() {
			super(null, null);
		}

		public IbmAccountModel(IbmAccount ibmAccount) {
			super(ibmAccount.getId(), ibmAccount.getHid());
			this.organizationId = ibmAccount.getOrganizationId();
			this.apiKey = ibmAccount.getApiKey();
			this.authToken = ibmAccount.getAuthToken();
		}

		public String getOrganizationId() {
			return organizationId;
		}

		public void setOrganizationId(String organizationId) {
			this.organizationId = organizationId;
		}

		public String getApiKey() {
			return apiKey;
		}

		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}

		public String getAuthToken() {
			return authToken;
		}

		public void setAuthToken(String authToken) {
			this.authToken = authToken;
		}
	}

	public static class AwsAccountModel extends CoreDocumentModel {
		private static final long serialVersionUID = 7177095668261732425L;

		private String region;
		private String login;
		private String accessKey;
		private String secretKey;
		private String defaultPolicyArn;
		private String defaultPolicyName;

		public AwsAccountModel() {
			super(null, null);
		}

		public AwsAccountModel(AwsAccount awsAccount) {
			super(awsAccount.getId(), awsAccount.getHid());

			this.region = awsAccount.getRegion();
			this.defaultPolicyArn = awsAccount.getDefaultPolicyArn();
			this.defaultPolicyName = awsAccount.getDefaultPolicyName();
			// login, accessKey and secretKey are encrypted and should be set
			// separately
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getAccessKey() {
			return accessKey;
		}

		public void setAccessKey(String accessKey) {
			this.accessKey = accessKey;
		}

		public String getSecretKey() {
			return secretKey;
		}

		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}

		public String getDefaultPolicyArn() {
			return defaultPolicyArn;
		}

		public void setDefaultPolicyArn(String defaultPolicyArn) {
			this.defaultPolicyArn = defaultPolicyArn;
		}

		public String getDefaultPolicyName() {
			return defaultPolicyName;
		}

		public void setDefaultPolicyName(String defaultPolicyName) {
			this.defaultPolicyName = defaultPolicyName;
		}

	}

	public static class AzureAccountModel extends CoreDocumentModel {
		private static final long serialVersionUID = 1524623900312309629L;

		private String hostName;
		private String accessKeyName;
		private String accessKey;
		private String eventHubName;
		private String eventHubEndpoint;
		private int numPartitions;

		public AzureAccountModel() {
			super(null, null);
		}

		public AzureAccountModel(AzureAccount azureAccount) {
			super(azureAccount.getId(), azureAccount.getHid());
			this.hostName = azureAccount.getHostName();
			this.accessKeyName = azureAccount.getAccessKeyName();
			// accessKey is encrypted and will be set separately
			this.eventHubName = azureAccount.getEventHubName();
			this.eventHubEndpoint = azureAccount.getEventHubEndpoint();
			this.numPartitions = azureAccount.getNumPartitions();
		}

		public String getHostName() {
			return hostName;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

		public String getAccessKeyName() {
			return accessKeyName;
		}

		public void setAccessKeyName(String accessKeyName) {
			this.accessKeyName = accessKeyName;
		}

		public String getAccessKey() {
			return accessKey;
		}

		public void setAccessKey(String accessKey) {
			this.accessKey = accessKey;
		}

		public String getEventHubName() {
			return eventHubName;
		}

		public void setEventHubName(String eventHubName) {
			this.eventHubName = eventHubName;
		}

		public String getEventHubEndpoint() {
			return eventHubEndpoint;
		}

		public void setEventHubEndpoint(String eventHubEndpoint) {
			this.eventHubEndpoint = eventHubEndpoint;
		}

		public int getNumPartitions() {
			return numPartitions;
		}

		public void setNumPartitions(int numPartitions) {
			this.numPartitions = numPartitions;
		}
	}
}
