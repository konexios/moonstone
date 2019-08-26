package moonstone.selene.web;

import java.io.Serializable;

public class WebProperties implements Serializable {

	private static final long serialVersionUID = 4865132292034985111L;
	
	//Default properties values for selene.properties.
	private String jdbcUrl = "jdbc:h2:file:/opt/selene/db/selene;AUTO_SERVER=TRUE";
	private String jdbcUser = "";
	private String jdbcPassword = "";
	private String databus = "mqtt";
	private String mqttDatabusUrl = "tcp://localhost:1883";
	private String mqttDatabusUsername = "";
	private String mqttDatabusPassword = "";
	private long databusMaxBuffer = 10000L;
	private long databusPeekInterval = 5000L;
	private Boolean enabled = true;
	private String moduleStrategy = "PROPERTIES";
	private String scriptingEngine = "nashorn";
	private String restartScriptFilename = "restart.sh";
	private Boolean propertiesFilePresent = true;
	
	private String homeDirectory;
	private String configDirectory = WebConstants.DEFAULT_CONFIG_DIRECTORY;
	private String deviceDirectory = WebConstants.DEFAULT_DEVICE_DIRECTORY;
	private String downloadDirectory = WebConstants.DEFAULT_DOWNLOAD_DIRECTORY;
	private String backupDirectory = WebConstants.DEFAULT_BACKUP_DIRECTORY;
	private String engineJarPath = WebConstants.DEFAULT_LIB_DIRECTORY;
	private String engineLog4jPath = WebConstants.DEFAULT_CONFIG_DIRECTORY;
	private String transponsePath = WebConstants.DEFAULT_TRANSPOSE_SCRIPT_DIRECTORY;
	private String centralKnowledgeBankPath = WebConstants.DEFAULT_CENTRAL_KNOWLEDGE_BANK_DIRECTORY;
	
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	
	public String getJdbcUser() {
		return jdbcUser;
	}
	
	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser  = jdbcUser;
	}
	
	public String getJdbcPassword() {
		return jdbcPassword;
	}
	
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword  = jdbcPassword;
	}
	
	public String getDatabus() {
		return databus;
	}
	
	public void setDatabus(String databus) {
		this.databus  = databus;
	}
	
	public String getMqttDatabusUrl() {
		return mqttDatabusUrl;
	}
	
	public void setMqttDatabusUrl(String mqttDatabusUrl) {
		this.mqttDatabusUrl  = mqttDatabusUrl;
	}
	
	public String getMqttDatabusUsername() {
		return mqttDatabusUsername;
	}
	
	public void setMqttDatabusUsername(String mqttDatabusUsername) {
		this.mqttDatabusUsername  = mqttDatabusUsername;
	}
	
	public String getMqttDatabusPassword() {
		return mqttDatabusPassword;
	}
	
	public void setMqttDatabusPassword(String mqttDatabusPassword) {
		this.mqttDatabusPassword  = mqttDatabusPassword;
	}
	
	public long getDatabusMaxBuffer() {
		return databusMaxBuffer;
	}
	
	public void setDatabusMaxBuffer(long databusMaxBuffer) {
		this.databusMaxBuffer = databusMaxBuffer;
	}
	
	public long getDatabusPeekInterval() {
		return databusPeekInterval;
	}
	
	public void setDatabusMaxInterval(long databusPeekInterval) {
		this.databusPeekInterval = databusPeekInterval;
	}
	
	public Boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getModuleStrategy() {
		return moduleStrategy;
	}
	
	public void setModuleStrategy(String moduleStrategy) {
		this.moduleStrategy = moduleStrategy;
	}
	
	public String getScriptingEngine() {
		return scriptingEngine;
	}

	public void setScriptingEngine(String scriptingEngine) {
		this.scriptingEngine = scriptingEngine;
	}
	
	public String getRestartScriptFilename() {
		return restartScriptFilename;
	}

	public void setRestartScriptFilename(String restartScriptFilename) {
		this.restartScriptFilename = restartScriptFilename;
	}
	
	public Boolean isPropertiesFilePresent() {
		return propertiesFilePresent;
	}
	
	public void isPropertiesFilePresent(Boolean propertiesFilePresent) {
		this.propertiesFilePresent  = propertiesFilePresent;
	}
	
	public String getHomeDirectory() {
		return homeDirectory;
	}
	
	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}
	
	public String getDownloadDirectory() {
		return downloadDirectory;
	}
	
	public void setDownloadDirectory(String downloadDirectory) {
		this.downloadDirectory = downloadDirectory;
	}
	
	public String getBackupDirectory() {
		return backupDirectory;
	}
	
	public void setBackupDirectory(String backupDirectory) {
		this.backupDirectory = backupDirectory;
	}
	
	public String getEngineJarPath() {
		return engineJarPath;
	}

	public void setEngineJarPath(String engineJarPath) {
		this.engineJarPath = engineJarPath;
	}

	public String getConfigDirectory() {
		return configDirectory;
	}

	public void setConfigDirectory(String configDirectory) {
		this.configDirectory = configDirectory;
	}

	public String getEngineLog4jPath() {
		return engineLog4jPath;
	}

	public void setEngineLog4jPath(String engineLog4jPath) {
		this.engineLog4jPath = engineLog4jPath;
	}

	public String getTransponsePath() {
		return transponsePath;
	}

	public void setTransponsePath(String transponsePath) {
		this.transponsePath = transponsePath;
	}

	public String getCentralKnowledgeBankPath() {
		return centralKnowledgeBankPath;
	}

	public void setCentralKnowledgeBankPath(String centralKnowledgeBankPath) {
		this.centralKnowledgeBankPath = centralKnowledgeBankPath;
	}

	public String getDeviceDirectory() {
		return deviceDirectory;
	}

	public void setDeviceDirectory(String deviceDirectory) {
		this.deviceDirectory = deviceDirectory;
	}
}
