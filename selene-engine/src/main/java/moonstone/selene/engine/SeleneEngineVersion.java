package moonstone.selene.engine;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class SeleneEngineVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(SeleneEngineVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
