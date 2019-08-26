package com.arrow.kronos;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class KronosCronVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(KronosCronVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
