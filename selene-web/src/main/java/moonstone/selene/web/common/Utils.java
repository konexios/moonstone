package moonstone.selene.web.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import moonstone.selene.Loggable;

public class Utils extends Loggable {
	private final static String OS = System.getProperty("os.name").toLowerCase();
	private static final Loggable LOGGER = new Loggable();

	public static boolean isProcessRunning(String processName) {
		try {
			Process p;
			if (getOS() == OperatingSystem.UNIX) {
				p = Runtime.getRuntime().exec("ps -few");
			} else if (getOS() == OperatingSystem.WINDOWS) {
				p = Runtime.getRuntime().exec("tasklist");
			} else {
				// TODO check for process in other OS
				return false;
			}

			try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				return input.lines().collect(Collectors.joining("\n")).contains(processName);
			}

		} catch (Exception e) {
			LOGGER.logError("isProcessRunning", e);
		}
		return false;
	}

	public static boolean killProcess(String processName) {
		try {
			if (getOS() == OperatingSystem.UNIX) {
				Process p = Runtime.getRuntime().exec("ps -few");

				try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
					input.lines().filter(s -> s.contains(processName)).forEach(s -> {
						String pid = s.split("\\s+", 3)[1];
						try {
							Runtime.getRuntime().exec("kill -9 " + pid);
						} catch (IOException e) {
							LOGGER.logError("killProcess", "error in killing process (%s): %s", processName, e);
						}
					});
				}

			} else if (getOS() == OperatingSystem.WINDOWS) {
				// TODO add process kill in windows
			}
		} catch (Exception e) {
			LOGGER.logError("killProcess", e);
			return false;
		}
		return true;
	}

	public static OperatingSystem getOS() {
		if (OS.contains("win")) {
			return OperatingSystem.WINDOWS;
		} else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
			return OperatingSystem.UNIX;
		} else if (OS.contains("sunos")) {
			return OperatingSystem.SOLARIS;
		} else if (OS.contains("mac")) {
			return OperatingSystem.MAC;
		}
		return OperatingSystem.UNKNOWN;
	}
}
