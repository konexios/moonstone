package moonstone.selene.engine.state;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DeviceStates implements Serializable {
    private static final long serialVersionUID = -3959893515884572620L;

    public Map<String, String> exportStates() {
        return new HashMap<>();
    }

    public Map<String, String> importStates(Map<String, State> states) {
        return new HashMap<>();
    }
}
