package com.arrow.selene.engine;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class SeleneEngineVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(SeleneEngineVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
