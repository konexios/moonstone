package moonstone.selene.device.xbee.zcl;

import java.util.HashMap;
import java.util.Map;

public final class ApplicationProfiles {
	public static final int HOME_AUTOMATION_PROFILE = 0x0104;
	public static final int LIGHT_LINK_PROFILE = 0xc05e;

	private static final Map<Integer, String> ALL = new HashMap<>();

	static {
		ALL.put(HOME_AUTOMATION_PROFILE, "Home Automation");
		ALL.put(LIGHT_LINK_PROFILE, "Light Link");
	}

	public static String getName(Integer id) {
		String name = ALL.get(id);
		return name == null ? "Unknown Application Profile" : name;
	}
}
