package com.arrow.pegasus.web;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class PegasusWebVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(PegasusWebVersion.class);

	public final static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
