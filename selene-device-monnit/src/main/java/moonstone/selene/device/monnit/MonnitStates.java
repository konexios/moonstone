package moonstone.selene.device.monnit;

import moonstone.selene.engine.state.DeviceStates;
import moonstone.selene.engine.state.State;

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
