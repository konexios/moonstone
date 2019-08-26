package com.arrow.pegasus.web;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class PegasusWebVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(PegasusWebVersion.class);

	public final static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
