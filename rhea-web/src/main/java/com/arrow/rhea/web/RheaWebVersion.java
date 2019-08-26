package com.arrow.rhea.web;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class RheaWebVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(RheaWebVersion.class);

	public final static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
