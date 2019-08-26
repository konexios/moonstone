package moonstone.selene.web.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acn.client.model.CloudPlatform;
import moonstone.acs.JsonUtils;
import moonstone.selene.SeleneConstants;
import moonstone.selene.SeleneException;
import moonstone.selene.data.Aws;
import moonstone.selene.data.Azure;
import moonstone.selene.data.Device;
import moonstone.selene.data.Gateway;
import moonstone.selene.data.Ibm;
import moonstone.selene.model.StatusModel;
import moonstone.selene.web.GatewayProperties;
import moonstone.selene.web.WebConstants;
import moonstone.selene.web.WebProperties;
import moonstone.selene.web.api.data.IotConnect;
import moonstone.selene.web.api.data.OsModel;
import moonstone.selene.web.api.model.AwsModels;
import moonstone.selene.web.api.model.AzureModels;
import moonstone.selene.web.api.model.GatewayModels;
import moonstone.selene.web.api.model.IbmModels;
import moonstone.selene.web.api.model.IotConnectModels;
import moonstone.selene.web.common.Utils;

@RestController
@RequestMapping("/api/selene/gateways")
public class GatewayApi extends BaseApi {

	public static final String SELENE_ENGINE_JAR = "selene-engine.jar";
	private Process gatewayProcess;
	private static final TypeReference<Map<String, String>> MAP_TYPE_REF = new TypeReference<Map<String, String>>() {
	};

	@RequestMapping(value = "/getseleneproperties", method = RequestMethod.GET)
	public WebProperties getSeleneProperties() {

		String method = "getSeleneProperties";
		logInfo(method, "fetching selene properties....");

		// If configuration is already loaded.
		if (getConfigService() != null) {
			return getConfigService().getWebProperties();
		}

		WebProperties webProperties = new WebProperties();
		// Loading configuration from standardized path, if it is present.
		String defaultFilePath = OsModel.getHomeDirPath() + WebConstants.DEFAULT_CONFIG_DIRECTORY
		        + WebConstants.DEFAULT_SELENE_PROPERTIES;
		File tempFile = new File(defaultFilePath);
		if (tempFile.exists()) {
			try (InputStream is = new FileInputStream(defaultFilePath)) {
				Properties properties = new Properties();
				properties.load(is);
				Map<String, String> config = new HashMap<>();
				properties.stringPropertyNames().forEach(name -> config.put(name, properties.getProperty(name)));
				BeanUtils.populate(webProperties, config);
				createConfigService();
			} catch (FileNotFoundException e) {
				throw new SeleneException("Error! file path does not exists");
			} catch (IOException e) {
				throw new SeleneException("Error! unable to fetch data from properties file");
			} catch (Exception e) {
				throw new SeleneException("Failure");
			}
			// Setting up environment variable
			System.setProperty(SeleneConstants.ENV_SELENE_CONFIG, defaultFilePath);
		} else {
			// If file doesn't exists at standardized path then providing
			// default values.
			webProperties.setHomeDirectory(OsModel.getHomeDirPath());
			webProperties.isPropertiesFilePresent(false);
		}
		return webProperties;
	}

	@RequestMapping(value = "/createseleneproperties", method = RequestMethod.POST)
	public StatusModel createSeleneProperties(@RequestBody String data) {

		String method = "createSeleneProperties";
		logInfo(method, "creating Selene Properties ....");
		try {
			Properties properties = JsonUtils.fromJson(data, Properties.class);
			String seleneHomeDirPath = properties.getProperty("homeDirectory");

			if (StringUtils.isEmpty(seleneHomeDirPath)) {
				return new StatusModel().withStatus("ERROR").withMessage("Error! Selene home directory not found");
			}
			// Based on the provided home directory path, creating standardized
			// paths for selene-web.
			properties.put("configDirectory", seleneHomeDirPath + WebConstants.DEFAULT_CONFIG_DIRECTORY);
			properties.put("deviceDirectory", seleneHomeDirPath + WebConstants.DEFAULT_DEVICE_DIRECTORY);
			properties.put("downloadDirectory", seleneHomeDirPath + WebConstants.DEFAULT_DOWNLOAD_DIRECTORY);
			properties.put("backupDirectory", seleneHomeDirPath + WebConstants.DEFAULT_BACKUP_DIRECTORY);
			properties.put("engineJarPath", seleneHomeDirPath + WebConstants.DEFAULT_LIB_DIRECTORY);
			properties.put("engineLog4jPath", seleneHomeDirPath + WebConstants.DEFAULT_CONFIG_DIRECTORY);
			properties.put("transponsePath", seleneHomeDirPath + WebConstants.DEFAULT_TRANSPOSE_SCRIPT_DIRECTORY);
			properties.put("centralKnowledgeBankPath",
			        seleneHomeDirPath + WebConstants.DEFAULT_CENTRAL_KNOWLEDGE_BANK_DIRECTORY);

			// Storing the file at a specific path.
			FileOutputStream outputStream = new FileOutputStream(
			        seleneHomeDirPath + WebConstants.DEFAULT_CONFIG_DIRECTORY + WebConstants.DEFAULT_SELENE_PROPERTIES);
			properties.store(outputStream, "selene gateway properties");
			outputStream.close();
			// Setting up path specific environment variable.
			System.setProperty(SeleneConstants.ENV_SELENE_CONFIG,
			        seleneHomeDirPath + WebConstants.DEFAULT_CONFIG_DIRECTORY + WebConstants.DEFAULT_SELENE_PROPERTIES);

			// Creating required directory structure.
			File transpose = new File(seleneHomeDirPath + WebConstants.DEFAULT_TRANSPOSE_SCRIPT_DIRECTORY);
			if (!transpose.exists()) {
				transpose.mkdir();
			}
			File centralKnowledgeBank = new File(
			        seleneHomeDirPath + WebConstants.DEFAULT_CENTRAL_KNOWLEDGE_BANK_DIRECTORY);
			if (!centralKnowledgeBank.exists()) {
				centralKnowledgeBank.mkdir();
			}
			// Creating required services.
			if (getConfigService() == null) {
				createConfigService();
			}
			return StatusModel.OK;
		} catch (FileNotFoundException e) {
			return new StatusModel().withStatus("ERROR").withMessage("Error! Unable to create properties file");
		} catch (IOException e) {
			return new StatusModel().withStatus("ERROR").withMessage("Failure");
		}
	}

	@RequestMapping(value = "/loadpersistentseleneproperties", method = RequestMethod.POST)
	public StatusModel loadPersistentSeleneProperties(@RequestBody String data) {

		String method = "loadPersistentSeleneProperties";
		logInfo(method, "loading Existing Selene Properties ....");

		Properties properties = JsonUtils.fromJson(data, Properties.class);
		String seleneHomeDirPath = properties.getProperty("homeDirectory");

		if (StringUtils.isEmpty(seleneHomeDirPath)) {
			return new StatusModel().withStatus("ERROR").withMessage("Error! Selene home directory not found");
		}

		String selenePropertiesPath = seleneHomeDirPath + WebConstants.DEFAULT_CONFIG_DIRECTORY
		        + WebConstants.DEFAULT_SELENE_PROPERTIES;

		File tempFile = new File(selenePropertiesPath);
		if (tempFile.exists()) {
			// Setting up environment variable as per the provided path.
			System.setProperty(SeleneConstants.ENV_SELENE_CONFIG, selenePropertiesPath);
			// Creating required services.
			if (getConfigService() == null) {
				createConfigService();
			}
			return StatusModel.OK;
		} else {
			logInfo(method, "properties file not present at provided path....");
			return new StatusModel().withStatus("ERROR").withMessage("Error! Unable to load properties file");
		}
	}

	@RequestMapping(value = "/creategateway", method = RequestMethod.POST)
	public void createGateway(@RequestBody String data) {
		String method = "createGateway";
		logInfo(method, "creating self.properties....");
		String filePath = "";
		try {
			filePath = getConfigService().getWebProperties().getDeviceDirectory();
		} catch (Exception ignored) {
			throw new SeleneException("Web properties not configured!");
		}

		if (StringUtils.isEmpty(filePath)) {
			filePath = OsModel.getDeviceConfigPath();
		}

		try (FileOutputStream outputStream = new FileOutputStream(new File(filePath, "self.properties"))) {
			Properties properties = JsonUtils.fromJson(data, Properties.class);
			properties.store(outputStream, "selene gateway properties");
		} catch (FileNotFoundException e) {
			throw new SeleneException("File path does not exist!");
		} catch (IOException e) {
			throw new SeleneException("File write error!");
		}
	}

	@RequestMapping(value = "/loadgatewayproperties", method = RequestMethod.GET)
	public GatewayProperties loadGatewayProperties() {
		String method = "loadGatewayProperties";
		logInfo(method, "loading self.properties....");
		String filePath = "";
		try {
			filePath = getConfigService().getWebProperties().getDeviceDirectory();
		} catch (Exception ignored) {
			logError(method, "Web properties not configured!");
			return null;
		}

		if (StringUtils.isEmpty(filePath)) {
			filePath = OsModel.getDeviceConfigPath();
		}

		try (InputStream is = new FileInputStream(new File(filePath, "self.properties"))) {
			Properties properties = new Properties();
			properties.load(is);
			logInfo(method, "self.properties loaded successfully");

			// load gateway specific properties
			GatewayProperties gatewayProperties = new GatewayProperties();
			Map<String, String> config = new HashMap<>();
			properties.stringPropertyNames().forEach(name -> config.put(name, properties.getProperty(name)));
			BeanUtils.populate(gatewayProperties, config);

			return gatewayProperties;

		} catch (Exception e) {
			logError(method, "ERROR loading self.properties", e);
		}
		return null;
	}

	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public void start() {
		String method = "start";
		logInfo(method, "starting selene gateway....");

		// Creating required services if not created.
		if (getConfigService() == null) {
			createConfigService();
		}
		if (getTelemetryService() == null) {
			createTelemetryService();
		}

		String jarPath = getConfigService().getWebProperties().getEngineJarPath();
		if (StringUtils.isEmpty(jarPath)) {
			jarPath = OsModel.getJarPath() + SELENE_ENGINE_JAR;
		} else {
			jarPath += SELENE_ENGINE_JAR;
		}

		String propertiesPath = getConfigService().getWebProperties().getConfigDirectory();
		if (StringUtils.isEmpty(propertiesPath)) {
			propertiesPath = OsModel.getConfigPath() + "selene.properties";
		} else {
			propertiesPath += "selene.properties";
		}

		String logPath = getConfigService().getWebProperties().getEngineLog4jPath();
		if (StringUtils.isEmpty(logPath)) {
			logPath = OsModel.getConfigPath() + "log4j2.xml";
		} else {
			logPath += "log4j2.xml";
		}

		logInfo(method, "Jar Path : %s", jarPath);
		logInfo(method, "Properties Path : %s", propertiesPath);
		logInfo(method, "Log Path : %s", logPath);

		if (new File(jarPath).exists() && new File(propertiesPath).exists()) {

			if (!new File(logPath).exists()) {
				logPath = OsModel.getConfigPath() + "log4j2.xml";
			}

			try {
				String[] command = { "java", "-DseleneConfig=" + propertiesPath, "-Dlog4j.configurationFile=" + logPath,
				        "-Dselene.databaseLogging=false", "-jar", jarPath };
				if (gatewayProcess != null && gatewayProcess.isAlive()) {
					gatewayProcess.destroyForcibly();
				}
				gatewayProcess = new ProcessBuilder(command).start();
			} catch (Exception e) {
				throw new SeleneException("Selene Engine jar is not present at location: " + jarPath);
			}
		} else {
			throw new SeleneException("Selene Engine jar is not present at location: " + jarPath);
		}
	}

	@RequestMapping(value = "/gateway", method = RequestMethod.GET)
	public ResponseEntity<GatewayModels.GatewayUpsert> gateway() {
		String method = "Gateway";
		logInfo(method, "finding gateway....");

		if (getConfigService() != null) {
			Gateway gateway = getGatewayService().findOne();

			if (gateway != null) {
				List<GatewayModels.CloudPlatformOption> cloudPlatformOptions = new ArrayList<>();
				for (CloudPlatform cp : CloudPlatform.values())
					cloudPlatformOptions.add(new GatewayModels.CloudPlatformOption(cp));
				return new ResponseEntity<GatewayModels.GatewayUpsert>(
				        new GatewayModels.GatewayUpsert(new GatewayModels.GatewayModel(gateway), cloudPlatformOptions),
				        HttpStatus.OK);
			}
		}
		logInfo(method, "no gateway found....");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/checkGatewayStatus", method = RequestMethod.GET)
	public StatusModel checkGatewayStatus() {
		String method = "checkGatewayStatus";

		if ((gatewayProcess != null && gatewayProcess.isAlive() || Utils.isProcessRunning(SELENE_ENGINE_JAR))) {
			logInfo(method, "Gateway is running....");
			return StatusModel.OK;
		} else {
			logInfo(method, "Gateway is not running....");
			return new StatusModel().withStatus("ERROR").withMessage("Gateway is not Running");
		}
	}

	@RequestMapping(path = "/update", method = RequestMethod.PUT)
	public GatewayModels.GatewayModel update(@RequestBody GatewayModels.GatewayModel model) {
		Assert.notNull(model, "model is null");

		Gateway gateway = getGatewayService().findOne();
		Assert.notNull(gateway, "gateway not found");

		gateway.setName(model.getName());
		gateway.setUid(model.getUid());
		gateway.setIotConnectUrl(model.getIotConnectUrl());
		if (model.getTopology() == null) {
			gateway.setTopology("");
		} else {
			gateway.setTopology(model.getTopology());
		}
		gateway.setHeartBeatIntervalMs(model.getHeartBeatIntervalMs());
		gateway.setPurgeTelemetryIntervalDays(model.getPurgeTelemetryIntervalDays());
		gateway.setPurgeMessagesIntervalDays(model.getPurgeMessagesIntervalDays());
		gateway.setCloudPlatform(model.getCloudPlatform());
		gateway.setEnabled(model.isEnabled());
		gateway.setApiKey(model.getApiKey());
		gateway.setSecretKey(model.getSecretKey());
		// TODO revisit, should be a json object
		// gateway.setProperties(model.getProperties());

		gateway = getGatewayService().update(gateway);

		return new GatewayModels.GatewayModel(gateway);
	}

	@RequestMapping(value = "/aws", method = RequestMethod.GET)
	public AwsModels.AwsUpsert aws() {

		Aws aws = getAwsService().findOne();
		if (aws == null) {
			return null;
		}

		return new AwsModels.AwsUpsert(new AwsModels.AwsModel(aws));
	}

	// TODO update aws

	@RequestMapping(value = "/ibm", method = RequestMethod.GET)
	public IbmModels.IbmUpsert ibm() {

		Ibm ibm = getIbmService().findOne();
		if (ibm == null) {
			return null;
		}

		return new IbmModels.IbmUpsert(new IbmModels.IbmModel(ibm));
	}

	// TODO update ibm

	@RequestMapping(value = "/azure", method = RequestMethod.GET)
	public AzureModels.AzureUpsert azure() {

		Azure azure = getAzureService().findOne();
		if (azure == null) {
			return null;
		}

		return new AzureModels.AzureUpsert(new AzureModels.AzureModel(azure));
	}

	// TODO update azure

	@RequestMapping(value = "/iotconnect", method = RequestMethod.GET)
	public IotConnectModels.IotConnectUpsert iotConnect() {
		try {
			// Get gateway device
			Device gatewayDevice = getDeviceService().find(0L);

			Map<String, String> propMap = JsonUtils.fromJson(gatewayDevice.getProperties(), MAP_TYPE_REF);
			IotConnect iotConnect = new IotConnect();
			iotConnect.setIotConnectMqtt(propMap.get("iotConnectMqtt"));
			iotConnect.setIotConnectMqttVHost(propMap.get("iotConnectMqttVHost"));
			return new IotConnectModels.IotConnectUpsert(new IotConnectModels.IotConnectModel(iotConnect));

		} catch (Exception e) {
			logError("iotConnect", "Error fetching iot connect details", e);
			return null;
		}
	}
}
