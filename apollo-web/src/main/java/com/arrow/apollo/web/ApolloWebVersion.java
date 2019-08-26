package com.arrow.apollo.web;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public class ApolloWebVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(ApolloWebVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
