package moonstone.selene.device.harting.rfid.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ConfigParameter<T extends ConfigParameter<?>> extends Serializable {
	byte[] build();

	T parse(byte... payload);

	int getId();

	boolean updateState(String name, String value);

	Map<String, String> getStates();

	List<String> getParams();
}
