package com.arrow.dashboard;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public final class PegasusDashboardApiVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(PegasusDashboardApiVersion.class);

	public static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
