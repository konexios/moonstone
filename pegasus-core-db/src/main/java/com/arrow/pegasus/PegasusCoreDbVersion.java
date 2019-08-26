package com.arrow.pegasus;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class PegasusCoreDbVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(PegasusCoreDbVersion.class);

	public static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
