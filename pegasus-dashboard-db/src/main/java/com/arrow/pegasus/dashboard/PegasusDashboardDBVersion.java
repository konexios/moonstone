package com.arrow.pegasus.dashboard;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class PegasusDashboardDBVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(PegasusDashboardDBVersion.class);

	public static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
