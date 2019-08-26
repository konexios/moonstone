package com.arrow.kronos;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class KronosApiVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(KronosApiVersion.class);

	public static VersionModel get() {
		return VERSION;
	}
}
