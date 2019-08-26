package moonstone.selene.device.zigbee.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import moonstone.acs.JsonUtils;

public class AddressesHolder implements Serializable {
	private static final long serialVersionUID = 511694967439110361L;

	private Set<String> values = new HashSet<>();

	public Set<String> getValues() {
		return values;
	}

	public void setValues(Set<String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
