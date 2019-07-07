package com.arrow.kronos;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class KronosApiVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(KronosApiVersion.class);

	public static VersionModel get() {
		return VERSION;
	}
}
