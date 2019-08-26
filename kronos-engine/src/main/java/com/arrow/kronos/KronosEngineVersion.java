package com.arrow.kronos;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class KronosEngineVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(KronosEngineVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
