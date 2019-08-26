package moonstone.selene.databus;

import moonstone.acs.Loggable;

public abstract class DatabusListenerAbstract extends Loggable implements DatabusListener {
	private final String name;

	public DatabusListenerAbstract(String name) {
		this.name = name;

	}

	@Override
	public String getName() {
		return name;
	}
}
