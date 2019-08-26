package moonstone.selene.engine;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import moonstone.acs.Loggable;
import moonstone.selene.SeleneException;
import moonstone.selene.engine.service.ConfigService;

public class DirectoryManager extends Loggable {
	private static class SingletonHolder {
		static final DirectoryManager SINGLETON = new DirectoryManager();
	}

	public static DirectoryManager getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private File home;
	private File lib;
	private File bin;
	private File config;
	private File devices;
	private File download;
	private File backup;

	private DirectoryManager() {
	}

	public void init() {
		String method = "init";
		logDebug(method, "...");
		EngineProperties props = ConfigService.getInstance().getEngineProperties();
		String homeDir = props.getHomeDirectory();
		if (StringUtils.isEmpty(homeDir)) {
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.contains("win")) {
				homeDir = EngineConstants.DEFAULT_HOME_DIRECTORY_WINDOWS;
			} else {
				homeDir = EngineConstants.DEFAULT_HOME_DIRECTORY_LINUX;
			}
			logWarn(method, "home directory is not defined in properties file, default to: %s", homeDir);
		} else {
			logInfo(method, "found home directory in properties file: %s", homeDir);
		}

		home = new File(homeDir);
		if (!home.exists() || !home.isDirectory()) {
			throw new SeleneException("invalid home directory: " + homeDir);
		}

		lib = new File(home, EngineConstants.DEFAULT_LIB_DIRECTORY);
		if (!lib.exists() || !lib.isDirectory()) {
			throw new SeleneException("lib directory not found: " + lib.getPath());
		}

		config = new File(home, EngineConstants.DEFAULT_CONFIG_DIRECTORY);
		if (!config.exists() || !config.isDirectory()) {
			throw new SeleneException("config directory not found: " + config.getPath());
		}

		devices = new File(config, EngineConstants.DEFAULT_DEVICE_DIRECTORY);
		if (!devices.exists() || !devices.isDirectory()) {
			throw new SeleneException("devices directory not found: " + devices.getPath());
		}

		bin = new File(home, EngineConstants.DEFAULT_BIN_DIRECTORY);
		if (!bin.exists() && !bin.mkdirs()) {
			throw new SeleneException("bin directory not found: " + bin.getPath());
		}

		download = new File(new File(home, EngineConstants.DEFAULT_SOFTWARE_UPDATE_DIRECTORY),
		        EngineConstants.DEFAULT_SOFTWARE_DOWNLOAD_DIRECTORY);
		if (!download.exists() && !download.mkdirs()) {
			throw new SeleneException("unable to create download directory: " + download.getPath());
		}

		backup = new File(new File(home, EngineConstants.DEFAULT_SOFTWARE_UPDATE_DIRECTORY),
		        EngineConstants.DEFAULT_SOFTWARE_BACKUP_DIRECTORY);
		if (!backup.exists() && !backup.mkdirs()) {
			throw new SeleneException("unable to create backup directory: " + backup.getPath());
		}
		logDebug(method, "done");
	}

	public File getHome() {
		return home;
	}

	public File getLib() {
		return lib;
	}

	public File getBin() {
		return bin;
	}

	public File getConfig() {
		return config;
	}

	public File getDevices() {
		return devices;
	}

	public File getDownload() {
		return download;
	}

	public File getBackup() {
		return backup;
	}
}
