package com.arrow.selene.web;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class SeleneWebVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(SeleneWebVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
