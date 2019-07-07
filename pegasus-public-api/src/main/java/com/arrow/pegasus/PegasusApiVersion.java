package com.arrow.pegasus;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class PegasusApiVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(PegasusApiVersion.class);

	public static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
