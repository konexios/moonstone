package com.arrow.selene.device.monnit;

import com.arrow.selene.engine.state.DeviceStates;
import com.arrow.selene.engine.state.State;

public class MonnitStates extends DeviceStates {
    private static final long serialVersionUID = -3001006425674429397L;
    
    private State gatewayReportInterval = new State();
    private State sensorReportInterval = new State();
    
	public State getGatewayReportInterval() {
		return gatewayReportInterval;
	}
	public void setGatewayReportInterval(State gatewayReportInterval) {
		this.gatewayReportInterval = gatewayReportInterval;
	}
	public State getSensorReportInterval() {
		return sensorReportInterval;
	}
	public void setSensorReportInterval(State sensorReportInterval) {
		this.sensorReportInterval = sensorReportInterval;
	}    
}
