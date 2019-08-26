package moonstone.selene.engine.state;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StateUpdate implements Serializable {
	private static final long serialVersionUID = -3142950423994852021L;

	private String hid;
	private Map<String, String> states = new HashMap<>();

	public String getHid() {
		return hid;
	}

	public StateUpdate withHid(String hid) {
		this.hid = hid;
		return this;
	}

	public Map<String, String> getStates() {
		return states;
	}

	public StateUpdate withStates(Map<String, String> states) {
		this.states = states;
		return this;
	}

	@Override
	public String toString() {
		StringBuffer statesString = new StringBuffer();
		for (String state : states.keySet()) {
			statesString.append(state + ":" + states.get(state) + "; ");
		}
		
		return "StateUpdate [hid=" + hid + ", states=" + statesString.toString() + "]";
	}		
}
