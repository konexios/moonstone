package com.arrow.rhea;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class RheaCoreVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(RheaCoreVersion.class);

	public final static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}