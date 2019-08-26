package moonstone.selene.device.ble.beacon.advertising;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;

import moonstone.selene.Loggable;
import moonstone.selene.device.ble.beacon.BeaconControllerModule;
import moonstone.selene.device.ble.beacon.BeaconModule;
import moonstone.selene.device.ble.beacon.BeaconPacket;
import moonstone.selene.engine.service.ModuleService;

public abstract class AdvertisingReader extends Loggable {
	protected BeaconControllerModule controller;
	protected Set<String> ignoredMacSet = new HashSet<>();
	private Map<String, List<BeaconModule>> moduleMap = new HashMap<>();

	protected AdvertisingReader(BeaconControllerModule controller) {
		this.controller = controller;
	}

	protected BeaconModule createBeaconModule(BeaconPacket packet, String moduleClass,
	                                          BeaconControllerModule controller) {
		String method = "createBeaconModule";
		BeaconModule module = null;
		try {
			Object instance = Class.forName(moduleClass).getDeclaredConstructor().newInstance();
			if (instance instanceof BeaconModule) {
				module = (BeaconModule) instance;
				module.init(controller, packet);
				ModuleService.getInstance().registerModule(module);
				ModuleService.getInstance().startModule(module);
				logInfo(method, "new beacon module added: %s / %s", moduleClass, module.getMacAddress());
			} else {
				logError(method, "not a beacon module: %s", instance.getClass().getName());
			}
		} catch (Exception e) {
			logError(method, ExceptionUtils.getStackTrace(e));
		}
		return module;
	}

	public void setShuttingDown(boolean isShuttingDown) {
		// nothing to do
	}

	protected void handlePacket(String macAddress, BeaconPacket packet, String companyId, String moduleClass) {
		BeaconModule module = null;
		List<BeaconModule> modules = moduleMap.computeIfAbsent(companyId, k -> new ArrayList<>());
		for (BeaconModule m : modules) {
			if (Objects.equals(m.getMacAddress(), macAddress)) {
				module = m;
				break;
			}
		}
		if (module == null) {
			module = createBeaconModule(packet, moduleClass, controller);
			modules.add(module);
		}
		if (module == null) {
			ignoredMacSet.add(macAddress);
		} else {
			module.receive(packet);
			ignoredMacSet.remove(macAddress);
		}
	}

	public abstract void startScan(BeaconControllerModule controller);

	public abstract void cleanup();
}
