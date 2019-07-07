package com.arrow.selene.device.monnit.handlers;

import java.util.List;
import java.util.Map;

import com.arrow.acs.Loggable;
import com.arrow.selene.device.monnit.MonnitProperties;
import com.arrow.selene.device.monnit.MonnitStates;
import com.arrow.selene.engine.state.State;
import com.arrow.selene.engine.state.StateChangeHandler;
import com.monnit.mine.MonnitMineAPI.Gateway;
import com.monnit.mine.MonnitMineAPI.MineServer;
import com.monnit.mine.MonnitMineAPI.Sensor;

public class GatewayStateChangeHandler extends Loggable implements StateChangeHandler<MonnitStates> {

	private MineServer monnitServer;
	private Gateway monnitGateway;
	private MonnitProperties properties;

	@Override
	public void handle(MonnitStates currentStates, Map<String, State> newStates) {
		String method = "handle";

		try {
			for (String key : newStates.keySet()) {
				if (key.trim().equalsIgnoreCase("gatewayReportInterval")) {
					double gatewayReportInterval = Double.parseDouble(newStates.get(key).getValue());
					logInfo(method, "New gateway report interval %s", newStates.get(key).getValue());
					if (monnitGateway != null) {
						monnitGateway.UpdateReportInterval(gatewayReportInterval);
						properties.setGatewayReportInterval(gatewayReportInterval);
					}
				} else if (key.trim().equalsIgnoreCase("sensorReportInterval")) {
					double sensorReportInterval = Double.parseDouble(newStates.get(key).getValue());
					logInfo(method, "New sensor report interval %s", newStates.get(key).getValue());
					List<Sensor> sensors = monnitServer.FindSensorsForGateway(monnitGateway.getGatewayID());
					for (Sensor sensor : sensors) {
						sensor.UpdateReportInterval(sensorReportInterval);						
					}
					properties.setSensorReportInterval(sensorReportInterval);
				}
			}
		} catch (Exception e) {
			logError(method, "Exception %s", e.getMessage());
		}
		
	}

	public Gateway getMonnitGateway() {
		return monnitGateway;
	}

	public void setMonnitGateway(Gateway monnitGateway) {
		this.monnitGateway = monnitGateway;
	}

	public MonnitProperties getProperties() {
		return properties;
	}

	public void setProperties(MonnitProperties properties) {
		this.properties = properties;
	}

	public MineServer getMonnitServer() {
		return monnitServer;
	}

	public void setMonnitServer(MineServer monnitServer) {
		this.monnitServer = monnitServer;
	}
	
}
