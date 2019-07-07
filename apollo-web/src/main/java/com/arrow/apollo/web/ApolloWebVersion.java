package com.arrow.apollo.web;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public class ApolloWebVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(ApolloWebVersion.class);

	public final static VersionModel get() {
		return VERSION;
	}
}
