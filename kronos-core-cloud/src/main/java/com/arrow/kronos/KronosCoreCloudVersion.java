package com.arrow.kronos;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class KronosCoreCloudVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(KronosCoreCloudVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
