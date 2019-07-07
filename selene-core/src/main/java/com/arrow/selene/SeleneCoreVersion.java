package com.arrow.selene;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class SeleneCoreVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(SeleneCoreVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
