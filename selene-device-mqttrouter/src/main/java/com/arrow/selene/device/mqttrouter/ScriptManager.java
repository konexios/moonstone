package com.arrow.selene.device.mqttrouter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.SeleneException;
import com.arrow.selene.engine.state.State;

public class ScriptManager {

	private ScriptEngine scriptEngine;

	private static final class SingletonHolder {
		static final ScriptManager SINGLETON = new ScriptManager();
	}

	public static ScriptManager getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private ScriptManager() {
		startScriptManager();
	}

	protected void startScriptManager() {
		ScriptEngineManager manager = new ScriptEngineManager();
		scriptEngine = manager.getEngineByName("js");
		scriptEngine.getBindings(ScriptContext.GLOBAL_SCOPE);
	}

	protected String runStateScript(String scriptPath, Map<String, State> data, String deviceUid, String deviceName) {

		ArrayList<Map<String, String>> convertedData = new ArrayList<>();
		for (Map.Entry<String, State> stateData : data.entrySet()) {

			Map<String, String> deviceStateData = new HashMap<>();
			deviceStateData.put("propertyName", stateData.getKey());
			deviceStateData.put("propertyValue", stateData.getValue().getValue());
			deviceStateData.put("timestamp", stateData.getValue().getTimestamp().toString());

			convertedData.add(deviceStateData);
		}
		return runScript(scriptPath, JsonUtils.toJson(convertedData), deviceUid, deviceName);
	}

	protected String runRegistrationScript(String scriptPath, String registrationPayload) {
		return runScript(scriptPath, registrationPayload);
	}

	protected String runTelemetryScript(String scriptPath, String registrationPayload) {
		return runScript(scriptPath, registrationPayload);
	}

	protected String runScript(String scriptPath, String devicePayload) {
		return runScript(scriptPath, devicePayload, "", "");
	}

	protected String runScript(String scriptPath, String devicePayload, String deviceUid, String deviceName) {
		String outputPayload;

		try {
			scriptEngine.eval(new FileReader(scriptPath));
			Invocable invocableFunction = (Invocable) scriptEngine;
			if (deviceUid.isEmpty()) {
				outputPayload = (String) invocableFunction.invokeFunction("transpose", devicePayload);
			} else {
				outputPayload = (String) invocableFunction.invokeFunction("transpose", deviceUid, deviceName,
				        devicePayload);
			}
		} catch (SeleneException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw new SeleneException("wrong device registration payload", e);
		} catch (Exception e) {
			throw new SeleneException("error to parse data through script file", e);
		}

		return outputPayload;
	}

}
