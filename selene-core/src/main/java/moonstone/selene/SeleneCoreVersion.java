package moonstone.selene;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class SeleneCoreVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(SeleneCoreVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
