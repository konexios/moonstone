package com.arrow.kronos.web.controller;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.iot.model.ListThingsRequest;
import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosCloudConstants.Ibm;
import com.arrow.kronos.data.AwsAccount;
import com.arrow.kronos.data.AzureAccount;
import com.arrow.kronos.data.IbmAccount;
import com.arrow.kronos.data.IoTProvider;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.data.KronosUser;
import com.arrow.kronos.service.AwsAccountService;
import com.arrow.kronos.service.AzureAccountService;
import com.arrow.kronos.service.IbmAccountService;
import com.arrow.kronos.service.KronosApplicationProvisioningService;
import com.arrow.kronos.service.KronosApplicationService;
import com.arrow.kronos.service.KronosUserService;
import com.arrow.kronos.web.model.IoTProviderModels;
import com.arrow.kronos.web.model.IoTProviderModels.AwsAccountModel;
import com.arrow.kronos.web.model.IoTProviderModels.IoTConfigModel;
import com.arrow.pegasus.data.profile.Application;
import com.ibm.iotf.client.app.ApplicationClient;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.PartitionReceiver;
import com.microsoft.azure.sdk.iot.service.RegistryManager;

@RestController
@RequestMapping("/api/kronos/iot")
public class IoTProviderController extends BaseControllerAbstract {

	@Autowired
	private KronosApplicationService kronosApplicationService;
	@Autowired
	private KronosApplicationProvisioningService kronosApplicationProvisioningService;
	@Autowired
	private AwsAccountService awsAccountService;
	@Autowired
	private IbmAccountService ibmAccountService;
	@Autowired
	private AzureAccountService azureAccountService;
	@Autowired
	private KronosUserService kronosUserService;

	@PreAuthorize("hasAuthority('KRONOS_EDIT_IOT_CONFIGURATION')")
	@RequestMapping(value = "/configuration", method = RequestMethod.GET)
	public IoTConfigModel getIoTConfiguration(HttpSession session) {
		Application application = getApplication(session);

		KronosApplication kronosApplication = kronosApplicationService.getKronosApplicationRepository()
				.findByApplicationId(application.getId());

		// ArrowConnect is the default provider for application-level config
		IoTConfigModel iotConfigModel = new IoTConfigModel(
				kronosApplication != null ? kronosApplication.getIotProvider() : IoTProvider.ArrowConnect);

		findAwsAccount(iotConfigModel, application, null);
		findIbmAccount(iotConfigModel, application, null);
		findAzureAccount(iotConfigModel, application, null);

		return iotConfigModel;
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_IOT_CONFIGURATION')")
	@RequestMapping(value = "/configuration", method = RequestMethod.PUT)
	public IoTConfigModel saveIoTConfiguration(@RequestBody IoTConfigModel iotConfigModel, HttpSession session) {
		Application application = getApplication(session);

		KronosApplication kronosApplication = kronosApplicationService.getKronosApplicationRepository()
				.findByApplicationId(application.getId());
		if (kronosApplication == null) {
			// create kronos application if it does not exist
			kronosApplication = kronosApplicationProvisioningService.defaultKronosApplication(application.getId(), true,
					getUserId());
		}
		Assert.notNull(kronosApplication, "kronosApplication is null");
		kronosApplication.setIotProvider(iotConfigModel.getIotProvider());
		kronosApplication = kronosApplicationService.update(kronosApplication, getUserId());

		iotConfigModel.setIotProvider(kronosApplication.getIotProvider());

		return updateIoTConfigurations(iotConfigModel, application, null);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_USER_IOT_CONFIGURATION')")
	@RequestMapping(value = "/user-configuration", method = RequestMethod.GET)
	public IoTConfigModel getIoTUserConfiguration(HttpSession session) {
		Application application = getApplication(session);
		String userId = getUserId();

		KronosUser kronosUser = kronosUserService.getKronosUserRepository().findByUserIdAndApplicationId(userId,
				application.getId());

		// null is the default IoT provider meaning Inherit
		IoTConfigModel iotConfigModel = new IoTConfigModel(kronosUser != null ? kronosUser.getIotProvider() : null);

		findAwsAccount(iotConfigModel, application, userId);
		findIbmAccount(iotConfigModel, application, userId);
		findAzureAccount(iotConfigModel, application, userId);

		return iotConfigModel;
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_USER_IOT_CONFIGURATION')")
	@RequestMapping(value = "/user-configuration", method = RequestMethod.PUT)
	public IoTConfigModel saveIoTUserConfiguration(@RequestBody IoTConfigModel iotConfigModel, HttpSession session) {
		Application application = getApplication(session);
		String userId = getUserId();

		KronosUser kronosUser = kronosUserService.getKronosUserRepository().findByUserIdAndApplicationId(userId,
				application.getId());
		if (kronosUser == null) {
			// create kronosUser if it is not created yet
			kronosUser = new KronosUser();
			kronosUser.setUserId(userId);
			kronosUser.setApplicationId(application.getId());
			kronosUser.setIotProvider(iotConfigModel.getIotProvider());
			kronosUser = kronosUserService.create(kronosUser, userId);
		} else {
			kronosUser.setIotProvider(iotConfigModel.getIotProvider());
			kronosUser = kronosUserService.update(kronosUser, userId);
		}
		Assert.notNull(kronosUser, "kronosUser is null");
		iotConfigModel.setIotProvider(kronosUser.getIotProvider());

		return updateIoTConfigurations(iotConfigModel, application, userId);
	}

	private void findAwsAccount(IoTConfigModel iotConfigModel, Application application, String userId) {
		// for application-wide configuration pass userId=null
		List<AwsAccount> awsAccounts = awsAccountService.getAwsAccountRepository()
				.findAllByApplicationIdAndUserId(application.getId(), userId);

		AwsAccount awsAccount = null;
		if (awsAccounts != null && awsAccounts.size() > 0) {
			awsAccount = awsAccounts.get(0);
		}
		addAwsAccount(iotConfigModel, awsAccount, application);
	}

	private void findIbmAccount(IoTConfigModel iotConfigModel, Application application, String userId) {
		// for application-wide configuration pass userId=null
		List<IbmAccount> ibmAccounts = ibmAccountService.getIbmAccountRepository()
				.findAllByApplicationIdAndUserId(application.getId(), userId);

		IbmAccount ibmAccount = null;
		if (ibmAccounts != null && ibmAccounts.size() > 0) {
			ibmAccount = ibmAccounts.get(0);
		}
		addIbmAccount(iotConfigModel, ibmAccount, application);
	}

	private void findAzureAccount(IoTConfigModel iotConfigModel, Application application, String userId) {
		// for application-wide configuration pass userId=null
		List<AzureAccount> azureAccounts = azureAccountService.getAzureAccountRepository()
				.findAllByApplicationIdAndUserId(application.getId(), userId);

		AzureAccount azureAccount = null;
		if (azureAccounts != null && azureAccounts.size() > 0) {
			azureAccount = azureAccounts.get(0);
		}
		addAzureAccount(iotConfigModel, azureAccount, application);
	}

	private void addAwsAccount(IoTConfigModel iotConfigModel, AwsAccount awsAccount, Application application) {
		AwsAccountModel awsAccountModel;
		if (awsAccount != null) {
			awsAccountModel = new AwsAccountModel(awsAccount);
			// decrypt encrypted fields
			awsAccountModel.setLogin(getCryptoService().decrypt(application.getId(), awsAccount.getLogin()));
			awsAccountModel.setAccessKey(getCryptoService().decrypt(application.getId(), awsAccount.getAccessKey()));
			awsAccountModel.setSecretKey(getCryptoService().decrypt(application.getId(), awsAccount.getSecretKey()));
		} else {
			awsAccountModel = new AwsAccountModel();
		}

		iotConfigModel.setAws(awsAccountModel);
	}

	private void addIbmAccount(IoTConfigModel iotConfigModel, IbmAccount ibmAccount, Application application) {
		IoTProviderModels.IbmAccountModel ibmAccountModel;

		if (ibmAccount != null) {
			ibmAccountModel = new IoTProviderModels.IbmAccountModel(ibmAccount);
			// decrypt encrypted fields
			ibmAccountModel.setApiKey(getCryptoService().decrypt(application.getId(), ibmAccount.getApiKey()));
			ibmAccountModel.setAuthToken(getCryptoService().decrypt(application.getId(), ibmAccount.getAuthToken()));
		} else {
			ibmAccountModel = new IoTProviderModels.IbmAccountModel();
		}

		iotConfigModel.setIbm(ibmAccountModel);
	}

	private void addAzureAccount(IoTConfigModel iotConfigModel, AzureAccount azureAccount, Application application) {
		IoTProviderModels.AzureAccountModel azureAccountModel;

		if (azureAccount != null) {
			azureAccountModel = new IoTProviderModels.AzureAccountModel(azureAccount);
			// decrypt encrypted fields
			azureAccountModel.setAccessKey(getCryptoService().decrypt(application.getId(), azureAccount.getAccessKey()));
		} else {
			azureAccountModel = new IoTProviderModels.AzureAccountModel();
		}

		iotConfigModel.setAzure(azureAccountModel);
	}

	private IoTConfigModel updateIoTConfigurations(IoTConfigModel iotConfigModel, Application application,
			String userId) {
		// for application-wide configuration pass userId=null

		AwsAccount awsAccount = null;
		if (iotConfigModel.getAws() != null && !StringUtils.isEmpty(iotConfigModel.getAws().getId())) {
			awsAccount = getKronosCache().findAwsAccountById(iotConfigModel.getAws().getId());
			Assert.notNull(awsAccount, "aws account is null");
			Assert.isTrue(StringUtils.equals(awsAccount.getApplicationId(), application.getId()),
					"user and aws account must have the same application id");
			Assert.isTrue(StringUtils.equals(awsAccount.getUserId(), userId),
					userId != null ? "aws account must be owned by user"
							: "user id must be not defined for the application-wide aws configuration");
		}

		IbmAccount ibmAccount = null;
		if (iotConfigModel.getIbm() != null && !StringUtils.isEmpty(iotConfigModel.getIbm().getId())) {
			ibmAccount = getKronosCache().findIbmAccountById(iotConfigModel.getIbm().getId());
			Assert.notNull(ibmAccount, "ibm account is null");
			Assert.isTrue(StringUtils.equals(ibmAccount.getApplicationId(), application.getId()),
					"user and ibm account must have the same application id");
			Assert.isTrue(StringUtils.equals(ibmAccount.getUserId(), userId),
					userId != null ? "ibm account must be owned by user"
							: "user id must be not defined for the application-wide ibm configuration");
		}

		AzureAccount azureAccount = null;
		if (iotConfigModel.getAzure() != null && !StringUtils.isEmpty(iotConfigModel.getAzure().getId())) {
			azureAccount = getKronosCache().findAzureAccountById(iotConfigModel.getAzure().getId());
			Assert.notNull(azureAccount, "azure account is null");
			Assert.isTrue(StringUtils.equals(azureAccount.getApplicationId(), application.getId()),
					"user and azure account must have the same application id");
			Assert.isTrue(StringUtils.equals(azureAccount.getUserId(), userId),
					userId != null ? "azure account must be owned by user"
							: "user id must be not defined for the application-wide azure configuration");
		}

		if (iotConfigModel.getIotProvider() == null || iotConfigModel.getIotProvider() == IoTProvider.ArrowConnect) {
			// null means Inherit for user IoT configuration
			if (awsAccount != null) {
				awsAccount.setEnabled(false);
			}
			if (ibmAccount != null) {
				ibmAccount.setEnabled(false);
			}
			if (azureAccount != null) {
				azureAccount.setEnabled(false);
			}
		} else {
			switch (iotConfigModel.getIotProvider()) {
			case AWS:
				if (ibmAccount != null) {
					ibmAccount.setEnabled(false);
				}
				if (azureAccount != null) {
					azureAccount.setEnabled(false);
				}
				if (awsAccount == null) {
					// create a new one
					awsAccount = new AwsAccount();
					awsAccount.setApplicationId(application.getId());
					awsAccount.setUserId(userId);
				}
				awsAccount.setEnabled(true);
				awsAccount.setRegion(iotConfigModel.getAws().getRegion());
				awsAccount.setLogin(getCryptoService().encrypt(application.getId(), iotConfigModel.getAws().getLogin()));
				awsAccount.setAccessKey(
						getCryptoService().encrypt(application.getId(), iotConfigModel.getAws().getAccessKey()));
				awsAccount.setSecretKey(
						getCryptoService().encrypt(application.getId(), iotConfigModel.getAws().getSecretKey()));
				awsAccount.setDefaultPolicyArn(iotConfigModel.getAws().getDefaultPolicyArn());
				awsAccount.setDefaultPolicyName(iotConfigModel.getAws().getDefaultPolicyName());
				break;

			case IBM:
				if (awsAccount != null) {
					awsAccount.setEnabled(false);
				}
				if (azureAccount != null) {
					azureAccount.setEnabled(false);
				}
				if (ibmAccount == null) {
					// create a new one
					ibmAccount = new IbmAccount();
					ibmAccount.setApplicationId(application.getId());
					ibmAccount.setUserId(userId);
				}
				ibmAccount.setEnabled(true);
				ibmAccount.setOrganizationId(iotConfigModel.getIbm().getOrganizationId());
				ibmAccount.setApiKey(getCryptoService().encrypt(application.getId(), iotConfigModel.getIbm().getApiKey()));
				ibmAccount.setAuthToken(
						getCryptoService().encrypt(application.getId(), iotConfigModel.getIbm().getAuthToken()));
				break;

			case AZURE:
				if (awsAccount != null) {
					awsAccount.setEnabled(false);
				}
				if (ibmAccount != null) {
					ibmAccount.setEnabled(false);
				}
				if (azureAccount == null) {
					// create a new one
					azureAccount = new AzureAccount();
					azureAccount.setApplicationId(application.getId());
					azureAccount.setUserId(userId);
				}
				azureAccount.setEnabled(true);
				azureAccount.setHostName(iotConfigModel.getAzure().getHostName());
				azureAccount.setAccessKeyName(iotConfigModel.getAzure().getAccessKeyName());
				azureAccount.setAccessKey(
						getCryptoService().encrypt(application.getId(), iotConfigModel.getAzure().getAccessKey()));
				azureAccount.setEventHubName(iotConfigModel.getAzure().getEventHubName());
				azureAccount.setEventHubEndpoint(iotConfigModel.getAzure().getEventHubEndpoint());
				azureAccount.setNumPartitions(iotConfigModel.getAzure().getNumPartitions());
				break;

			default:
				// not supported
				throw new AcsLogicalException("This IoT Provider is not supported currently");
			}
		}

		// TODO need some kind of transaction to make sure all objects have
		// been persisted
		if (awsAccount != null) {
			awsAccount = awsAccountService.upsert(awsAccount, getUserId());
		}
		if (ibmAccount != null) {
			ibmAccount = ibmAccountService.upsert(ibmAccount, getUserId());
		}
		if (azureAccount != null) {
			azureAccount = azureAccountService.upsert(azureAccount, getUserId());
		}

		addAwsAccount(iotConfigModel, awsAccount, application);
		addIbmAccount(iotConfigModel, ibmAccount, application);
		addAzureAccount(iotConfigModel, azureAccount, application);

		return iotConfigModel;
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_USER_IOT_CONFIGURATION') or hasAuthority('KRONOS_EDIT_IOT_CONFIGURATION')")
	@RequestMapping(value = "/aws/connect", method = RequestMethod.POST)
	public boolean testAWSConnection(@RequestBody AwsAccountModel awsAccountModel, HttpSession session) {
		String method = "testAWSConnection";
		try {
			AWSCredentials creds = new BasicAWSCredentials(awsAccountModel.getAccessKey(),
					awsAccountModel.getSecretKey());
			AWSIot client = AWSIotClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
					.withRegion(awsAccountModel.getRegion()).build();
			client.listThings(new ListThingsRequest());
		} catch (Exception e) {
			logError(method, e);
			return false;
		}
		return true;
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_USER_IOT_CONFIGURATION') or hasAuthority('KRONOS_EDIT_IOT_CONFIGURATION')")
	@RequestMapping(value = "/ibm/connect", method = RequestMethod.POST)
	public boolean testIBMConnection(@RequestBody IoTProviderModels.IbmAccountModel ibmAccountModel,
			HttpSession session) {
		String method = "testIBMConnection";
		try {
			Properties props = new Properties();
			props.setProperty(Ibm.HEADER_ORGANIZATION_ID, ibmAccountModel.getOrganizationId());
			props.setProperty(Ibm.HEADER_CLIENT_ID, ibmAccountModel.getOrganizationId());
			props.setProperty(Ibm.HEADER_AUTHENTICATION_MODE, Ibm.HEADER_AUTHENTICATION_MODE_API_KEY);
			props.setProperty(Ibm.HEADER_API_KEY, ibmAccountModel.getApiKey());
			props.setProperty(Ibm.HEADER_AUTHENTICATION_TOKEN, ibmAccountModel.getAuthToken());
			ApplicationClient client = new ApplicationClient(props);
			client.api().getAllDeviceTypes(null);
		} catch (Exception e) {
			logError(method, e);
			return false;
		}
		return true;
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_USER_IOT_CONFIGURATION') or hasAuthority('KRONOS_EDIT_IOT_CONFIGURATION')")
	@RequestMapping(value = "/azure/connect", method = RequestMethod.POST)
	public boolean testAzureConnection(@RequestBody IoTProviderModels.AzureAccountModel azureAccountModel,
			HttpSession session) {
		String method = "testAzureConnection";
		try {
			Application application = getApplication(session);
			AzureAccount azureAccount = new AzureAccount();
			azureAccount.setApplicationId(application.getId());
			azureAccount.setHostName(azureAccountModel.getHostName());
			azureAccount.setAccessKeyName(azureAccountModel.getAccessKeyName());
			azureAccount.setAccessKey(getCryptoService().encrypt(application.getId(), azureAccountModel.getAccessKey()));
			azureAccount.setEventHubName(azureAccountModel.getEventHubName());
			azureAccount.setEventHubEndpoint(azureAccountModel.getEventHubEndpoint());

			// try to get at least one device
			String connectionString = azureAccountService.buildIotHubConnectionString(azureAccount);
			RegistryManager registryManager = RegistryManager.createFromConnectionString(connectionString);
			registryManager.getDevices(1);

			// try event hub client
			EventHubClient client = EventHubClient
					.createFromConnectionStringSync(azureAccountService.buildEventHubConnectionString(azureAccount));
			String partitionId = "0";
			PartitionReceiver receiver = client.createReceiver(EventHubClient.DEFAULT_CONSUMER_GROUP_NAME, partitionId,
					PartitionReceiver.START_OF_STREAM, false).get();
			receiver.setReceiveTimeout(Duration.ofSeconds(5));
			receiver.close();
			client.close();
		} catch (Exception e) {
			logError(method, e);
			return false;
		}
		return true;
	}
}
