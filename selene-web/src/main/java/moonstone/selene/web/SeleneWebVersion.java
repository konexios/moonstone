package moonstone.selene.web;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class SeleneWebVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(SeleneWebVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
