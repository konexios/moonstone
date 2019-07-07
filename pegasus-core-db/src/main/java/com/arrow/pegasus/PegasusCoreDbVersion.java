package com.arrow.pegasus;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class PegasusCoreDbVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(PegasusCoreDbVersion.class);

	public static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
