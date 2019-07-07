package com.arrow.pegasus;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class PegasusCoreWebVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(PegasusCoreWebVersion.class);

	public final static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
