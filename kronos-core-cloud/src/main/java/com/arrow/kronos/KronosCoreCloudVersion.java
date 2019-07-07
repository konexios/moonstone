package com.arrow.kronos;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class KronosCoreCloudVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(KronosCoreCloudVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
