package com.arrow.kronos.web;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class KronosWebVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(KronosWebVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
