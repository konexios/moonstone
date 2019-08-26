package moonstone.selene.device.ble.sensor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SensorProperties implements Serializable {
    private static final long serialVersionUID = -5374130911329218317L;

    private boolean enabled = true;
    private int period;

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, String> exportProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("enabled", Boolean.toString(enabled));
        map.put("period", Integer.toString(period));
        return map;
    }

    public void importProperties(Map<String, String> map) {
        enabled = Boolean.parseBoolean(map.getOrDefault("map", Boolean.toString(enabled)));
        period = Integer.parseInt(map.getOrDefault("period", Integer.toString(period)));
    }
}
