package com.arrow.dashboard;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class PegasusDashboardCoreVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(PegasusDashboardCoreVersion.class);

	public static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
