package moonstone.selene.engine;

import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import moonstone.acn.client.ClientConstants;
import moonstone.acs.client.model.VersionModel;
import moonstone.selene.Loggable;
import moonstone.selene.SeleneCoreVersion;
import moonstone.selene.SeleneException;
import moonstone.selene.dao.DaoManager;
import moonstone.selene.device.self.SelfInfo;
import moonstone.selene.device.self.SelfModule;
import moonstone.selene.engine.DirectoryManager;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.Module;
import moonstone.selene.engine.ShutdownListener;
import moonstone.selene.engine.Utils;
import moonstone.selene.engine.cloud.EdgeMqttModule;
import moonstone.selene.engine.cloud.IotConnectModule;
import moonstone.selene.engine.service.DbusService;
import moonstone.selene.engine.service.DeviceService;
import moonstone.selene.engine.service.ModuleService;

public class Engine extends Loggable implements ShutdownListener {
	private final static String AWS_MODULE_CLASS = "moonstone.selene.engine.cloud.AwsModule";
	private final static String AZURE_MODULE_CLASS = "moonstone.selene.engine.cloud.AzureModule";
	private final static String IBM_MODULE_CLASS = "moonstone.selene.engine.cloud.IbmModule";

	private Thread shutdownThread;
	private boolean shuttingDown;
	private ModuleService moduleService = ModuleService.getInstance();

	private static class SingletonHolder {
		static final Engine SINGLETON = new Engine();
	}

	public static Engine getInstance() {
		return SingletonHolder.SINGLETON;
	}

	public void shutdown() {
		String method = "shutdown";
		shuttingDown = true;
		moduleService.stopAllModules();
		DbusService.getInstance().disconnect();
		logInfo(method, "exiting ...");
	}

	private void start() throws Exception {
		String method = "start";
		SelfModule self = SelfModule.getInstance();
		try {
			logInfo(method, "SeleneCoreVersion: %s", printVersion(SeleneCoreVersion.get()));
			logInfo(method, "SeleneEngineVersion: %s", printVersion(SeleneEngineVersion.get()));

			boolean selfModuleFound = false;
			if (!DaoManager.getInstance().isFreshDatabase()) {
				selfModuleFound = initSelfModule(DeviceService.getInstance().find(SelfInfo.DEFAULT_DEVICE_TYPE).stream()
						.map(Utils::getProperties).collect(Collectors.toSet()));
			} else {
				selfModuleFound = initSelfModule(
						self.loadPropsFromFiles(DirectoryManager.getInstance().getDevices().getPath()));

			}
			if (!selfModuleFound) {
				logError(method, "could not load SelfModule");
				throw new SeleneException("Fatal Error");
			}
		} catch (Exception e) {
			logError(method, "error scanning devices folder", e);
			throw new SeleneException("Fatal Error");
		}

		// register shutdown
		self.addShutdownListener(this);

		// start self module first
		moduleService.startModule(self);

		logInfo(method, "waiting for system ready ...");
		self.waitForSystemReady();

		boolean cloudTelemetryPublishing = self.getProperties().isCloudTelemetryPublishing();
		logInfo(method, "registering cloud modules, cloudTelemetryPublishing: %s", cloudTelemetryPublishing);

		// regardless of cloudTelemetryPublishing
		moduleService.registerModule(IotConnectModule.getInstance());

		if (cloudTelemetryPublishing) {
			switch (self.getGateway().getCloudPlatform()) {
			case Aws:
				moduleService.registerModule((Module) Class.forName(AWS_MODULE_CLASS).newInstance());
				break;
			case Azure:
				moduleService.registerModule((Module) Class.forName(AZURE_MODULE_CLASS).newInstance());
				break;
			case Ibm:
				moduleService.registerModule((Module) Class.forName(IBM_MODULE_CLASS).newInstance());
				break;
			case IotConnect:
				break;
			}
		}

		boolean edgeMqttEnabled = self.getProperties().isEdgeMqttEnabled();
		logInfo(method, "edgeMqttEnabled: %s", edgeMqttEnabled);
		if (edgeMqttEnabled) {
			moduleService.registerModule(EdgeMqttModule.getInstance());
		}

		logInfo(method, "starting all modules ...");
		moduleService.startAllModules();
	}

	private boolean initSelfModule(Set<Properties> properties) {
		String method = "initSelfModule";
		logInfo(method, "...");
		boolean result = false;
		for (Properties props : properties) {
			try {
				String deviceClass = Utils.getRequiredProperty(props, EngineConstants.DEVICE_CLASS);
				if (Objects.equals(deviceClass, SelfModule.class.getName())) {
					logInfo(method, "instantiating SelfModule: %s", deviceClass);
					SelfModule.getInstance().init(props);
					moduleService.registerModule(SelfModule.getInstance());
					result = true;
					break;
				}
			} catch (RuntimeException e) {
				logError(method, "error initializing SelModule", e);
			}
		}
		return result;
	}

	private void hookShutdown() {
		String method = "hookShutdown";
		logInfo(method, "installing hookShutdown ...");
		shutdownThread = new Thread(() -> {
			try {
				logInfo(method, "hookShutdown called ...");
				shutdown();
			} catch (Exception e) {
			}
		}, "shutdown thread");
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}

	private void waitForExit() {
		String method = "waitForExit";
		logInfo(method, "...");
		// infinite loop waiting for shutdown
		while (!shuttingDown) {
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
		}

		if (shutdownThread != null && shutdownThread.isAlive()) {
			try {
				shutdownThread.join(ClientConstants.DEFAULT_SHUTDOWN_WAITING_MS);
			} catch (Exception e) {
			}
			shutdownThread = null;
		}
		logInfo(method, "terminated!");
	}

	private String printVersion(VersionModel model) {
		StringBuilder sb = new StringBuilder();
		sb.append(model.getMajor() != null ? model.getMajor() : "?");
		sb.append(".");
		sb.append(model.getMinor() != null ? model.getMinor() : "?");
		sb.append(".");
		sb.append(model.getBuild() != null ? model.getBuild() : "0");
		return sb.toString();
	}

	public static void main(String[] args) {
		try {
			DirectoryManager.getInstance().init();
			Engine engine = getInstance();
			engine.start();
			engine.hookShutdown();
			engine.waitForExit();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(0);
	}
}
