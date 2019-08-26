package moonstone.selene.engine.state;

import java.util.Map;

public interface StateChangeHandler<States extends DeviceStates> {
	void handle(States states, Map<String, State> statesMap);
}
