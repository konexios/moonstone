package moonstone.selene.device.monnit;

import java.util.Map;

import com.monnit.mine.MonnitMineAPI.Sensor;

import moonstone.selene.engine.state.DeviceStates;
import moonstone.selene.engine.state.State;

public class GenericSensorStates extends DeviceStates {
    private static final long serialVersionUID = 6439286763011952607L;

    private int linkInterval;
    private int recoveryCount;
    private double reportInterval;
    private int retryCount;

    @Override
    public Map<String, String> exportStates() {
        Map<String, String> map = super.exportStates();
        map.put("linkInterval", Integer.toString(linkInterval));
        map.put("recoveryCount", Integer.toString(recoveryCount));
        map.put("reportInterval", Double.toString(reportInterval));
        map.put("retryCount", Integer.toString(retryCount));
        return map;
    }

    @Override
    public Map<String, String> importStates(Map<String, State> states) {
        Map<String, String> map = super.importStates(states);
        State state = states.get("linkInterval");
        if (state != null)
            linkInterval = Integer.parseInt(state.getValue());
        state = states.get("recoveryCount");
        if (state != null)
            recoveryCount = Integer.parseInt(state.getValue());
        state = states.get("reportInterval");
        if (state != null)
            reportInterval = Double.parseDouble(state.getValue());
        state = states.get("retryCount");
        if (state != null)
            retryCount = Integer.parseInt(state.getValue());
        return map;
    }

    public boolean populateFrom(Sensor sensor) {
        boolean updated = false;
        if (linkInterval != sensor.getTransmitIntervalLink()) {
            setLinkInterval(sensor.getTransmitIntervalLink());
            updated = true;
        }
        if (recoveryCount != sensor.getRecovery()) {
            setRecoveryCount(sensor.getRecovery());
            updated = true;
        }
        if (reportInterval != sensor.getReportInterval()) {
            setReportInterval(sensor.getReportInterval());
            updated = true;
        }
        if (retryCount != sensor.getRetryCount()) {
            setRetryCount(sensor.getRetryCount());
            updated = true;
        }
        return updated;
    }

    public int getLinkInterval() {
        return linkInterval;
    }

    public void setLinkInterval(int linkInterval) {
        this.linkInterval = linkInterval;
    }

    public int getRecoveryCount() {
        return recoveryCount;
    }

    public void setRecoveryCount(int recoveryCount) {
        this.recoveryCount = recoveryCount;
    }

    public double getReportInterval() {
        return reportInterval;
    }

    public void setReportInterval(double reportInterval) {
        this.reportInterval = reportInterval;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
