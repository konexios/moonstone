package com.arrow.rhea;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public class RheaPrivateApiVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(RheaPrivateApiVersion.class);

	public final static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
