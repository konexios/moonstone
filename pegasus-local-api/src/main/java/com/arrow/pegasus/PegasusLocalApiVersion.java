package com.arrow.pegasus;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class PegasusLocalApiVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(PegasusLocalApiVersion.class);

	public static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
