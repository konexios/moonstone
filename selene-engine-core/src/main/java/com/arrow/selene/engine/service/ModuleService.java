package com.arrow.selene.engine.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.device.self.SelfModule;
import com.arrow.selene.engine.DeviceModule;
import com.arrow.selene.engine.Module;
import com.arrow.selene.engine.ModuleState;
import com.arrow.selene.engine.state.State;
import com.arrow.selene.service.ServiceAbstract;

public class ModuleService extends ServiceAbstract {
	private static final String MODULE_IS_NULL = "module is null";
	private static final String DEVICE_IS_NULL = "device is null";

	private List<Module> modules = new ArrayList<>();

	private static class SingletonHolder {
		static final ModuleService SINGLETON = new ModuleService();
	}

	public static ModuleService getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private ModuleService() {
	}

	public synchronized void registerModule(Module module) {
		Validate.notNull(module, MODULE_IS_NULL);
		modules.add(module);
	}

	public synchronized void deregisterModule(Module module) {
		Validate.notNull(module, MODULE_IS_NULL);
		if (modules.contains(module)) {
			modules.remove(module);
			logInfo("deregisterModule", "deleting module %s ...", module.getName());
		}
	}

	public synchronized List<Module> getModules() {
		return Collections.unmodifiableList(modules);
	}

	public synchronized boolean startAllModules() {
		boolean result = true;
		for (Module module : modules) {
			if (module.getClass() != SelfModule.class)
				result &= startModule(module);
		}
		return result;
	}

	public synchronized boolean stopAllModules() {
		boolean result = true;
		for (Module module : modules) {
			result &= stopModule(module);
		}
		return result;
	}

	public synchronized boolean startModule(Module module) {
		String method = "startModule";
		Validate.notNull(module, MODULE_IS_NULL);
		boolean result = checkRegistered(module)
				&& checkState(module, EnumSet.of(ModuleState.CREATED, ModuleState.STOPPED));
		if (result) {
			try {
				logInfo(method, "starting %s ...", module.getName());
				module.setState(ModuleState.STARTING);
				module.start();
			} catch (Throwable t) {
				logError(method, t);
				module.setErrorState(t.getCause().getMessage());
				result = false;
			}
		}
		return result;
	}

	public synchronized boolean stopModule(Module module) {
		String method = "stopModule";
		Validate.notNull(module, MODULE_IS_NULL);
		boolean result = checkRegistered(module)
				&& checkState(module, EnumSet.of(ModuleState.STARTING, ModuleState.STARTED));
		if (result) {
			try {
				logInfo(method, "stopping %s ...", module.getName());
				module.setState(ModuleState.STOPPING);
				module.stop();
				module.setState(ModuleState.STOPPED);
			} catch (Throwable t) {
				logError(method, t);
				module.setErrorState(t.getCause().getMessage());
				result = false;
			}
		}
		return result;
	}

	public synchronized boolean performCommand(DeviceModule<?, ?, ?, ?> device, Map<String, String> properties) {
	
		String method = "performCommand";
		Validate.notNull(device, DEVICE_IS_NULL);
		boolean result = checkRegistered(device) && !checkState(device, EnumSet.of(ModuleState.ERROR));

		StringBuffer command = new StringBuffer();
		for (String property : properties.keySet()) {
			command.append("\t" + property + ":" + properties.get(property) + "\n");
		}
		
		logInfo(method, "\n\t Device: %s, \n\t Command: %s \n", device.getName(), command.toString());
		
		if (result) {
			try {
				logInfo(method, "execution of command %s ...", device.getName());
				device.performCommand(JsonUtils.toJsonBytes(properties));
			} catch (Throwable t) {
				t.printStackTrace();
				logError(method, t);
				device.setErrorState(t.getMessage());
				result = false;
			}
		}
		return result;
	}

	public synchronized boolean changeProperties(DeviceModule<?, ?, ?, ?> device, Map<String, String> properties) {
		String method = "changeProperties";
		Validate.notNull(device, DEVICE_IS_NULL);
		boolean result = checkRegistered(device) && !checkState(device, EnumSet.of(ModuleState.ERROR));
		if (result) {
			logInfo(method, "notifying %s about properties changing...", device.getName());
			device.notifyPropertiesChanged(properties);
		}
		return result;
	}

	public synchronized boolean changeSensorProperties(DeviceModule<?, ?, ?, ?> device,
			Map<String, String> properties) {
		String method = "changeSensorProperties";
		Validate.notNull(device, DEVICE_IS_NULL);
		boolean result = checkRegistered(device) && !checkState(device, EnumSet.of(ModuleState.ERROR));
		if (result) {
			logInfo(method, "notifying %s about sensor's properties changing...", device.getName());
			device.notifyPropertiesChanged(properties);
		}
		return result;
	}

	public synchronized boolean changeSensorTelemetry(DeviceModule<?, ?, ?, ?> device, Map<String, String> properties) {
		String method = "changeSensorTelemetry";
		Validate.notNull(device, DEVICE_IS_NULL);
		boolean result = checkRegistered(device) && !checkState(device, EnumSet.of(ModuleState.ERROR));
		if (result) {
			logInfo(method, "notifying %s about sensor telemetry changing...", device.getName());
			device.notifyTelemetryChanged(properties);
		}
		return result;
	}

	public synchronized boolean updateStates(DeviceModule<?, ?, ?, ?> device, Map<String, State> states) {
		String method = "updateStates";
		Validate.notNull(device, DEVICE_IS_NULL);
		boolean result = checkRegistered(device) && !checkState(device, EnumSet.of(ModuleState.ERROR));
		if (result) {
			logInfo(method, "notifying %s about state changing...", device.getName());
			result = device.notifyStatesChanged(states);
		}
		return result;
	}

	public synchronized DeviceModule<?, ?, ?, ?> findDevice(String deviceHid) {
		String method = "findDevice";
		for (Module module : modules) {
			if (DeviceModule.class.isAssignableFrom(module.getClass())) {
				DeviceModule<?, ?, ?, ?> device = (DeviceModule<?, ?, ?, ?>) module;
				if (device.getDevice() != null && Objects.equals(device.getDevice().getHid(), deviceHid)) {
					logInfo(method, "found device %s", device.getName());
					return device;
				}
			}
		}
		return null;
	}

	private synchronized boolean checkRegistered(Module module) {
		for (Module m : modules) {
			if (m == module) {
				return true;
			}
		}
		return false;
	}

	private synchronized boolean checkState(Module module, Collection<ModuleState> validStates) {
		return validStates.contains(module.getState());
	}
}
