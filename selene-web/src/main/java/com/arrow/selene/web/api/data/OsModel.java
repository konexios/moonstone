package com.arrow.selene.web.api.data;
import com.arrow.selene.web.WebConstants;
import com.arrow.selene.web.common.OperatingSystem;
import com.arrow.selene.web.common.Utils;

public class OsModel {

	public OsModel() {
	}

	public static String getHomeDirPath() {
		String path = null;
		if (Utils.getOS() == OperatingSystem.WINDOWS) {
			path = WebConstants.DEFAULT_HOME_DIRECTORY_WINDOWS;
		} else if (Utils.getOS() == OperatingSystem.UNIX) {
			path = WebConstants.DEFAULT_HOME_DIRECTORY_LINUX;
		}
		return path;
	}

	public static String getDeviceConfigPath() {
		String path = null;
		if (Utils.getOS() == OperatingSystem.WINDOWS) {
			path = WebConstants.DEFAULT_HOME_DIRECTORY_WINDOWS + WebConstants.DEFAULT_DEVICE_DIRECTORY;
		} else if (Utils.getOS() == OperatingSystem.UNIX) {
			path = WebConstants.DEFAULT_HOME_DIRECTORY_LINUX + WebConstants.DEFAULT_DEVICE_DIRECTORY;
		}
		return path;
	}

	public static String getJarPath() {
		String path = null;
		if (Utils.getOS() == OperatingSystem.WINDOWS) {
			path = WebConstants.DEFAULT_HOME_DIRECTORY_WINDOWS + WebConstants.DEFAULT_LIB_DIRECTORY;
		} else if (Utils.getOS() == OperatingSystem.UNIX) {
			path = WebConstants.DEFAULT_HOME_DIRECTORY_LINUX + WebConstants.DEFAULT_LIB_DIRECTORY;
		}
		return path;
	}

	public static String getConfigPath() {
		String path = null;
		if (Utils.getOS() == OperatingSystem.WINDOWS) {
			path = WebConstants.DEFAULT_HOME_DIRECTORY_WINDOWS + WebConstants.DEFAULT_CONFIG_DIRECTORY;
		} else if (Utils.getOS() == OperatingSystem.UNIX) {
			path = WebConstants.DEFAULT_HOME_DIRECTORY_LINUX + WebConstants.DEFAULT_CONFIG_DIRECTORY;

		}
		return path;
	}

	public static String getCentralKnowledgeBankPath() {
		String path = null;
		if (Utils.getOS() == OperatingSystem.WINDOWS) {
			path = WebConstants.DEFAULT_HOME_DIRECTORY_WINDOWS + WebConstants.DEFAULT_CENTRAL_KNOWLEDGE_BANK_DIRECTORY;
		} else if (Utils.getOS() == OperatingSystem.UNIX) {
			path = WebConstants.DEFAULT_HOME_DIRECTORY_LINUX + WebConstants.DEFAULT_CENTRAL_KNOWLEDGE_BANK_DIRECTORY;
		}
		return path;
	}
}
