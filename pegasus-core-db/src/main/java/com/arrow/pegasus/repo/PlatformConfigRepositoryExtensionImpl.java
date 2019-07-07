package com.arrow.pegasus.repo;

import com.arrow.pegasus.data.PlatformConfig;

public class PlatformConfigRepositoryExtensionImpl extends RepositoryExtensionAbstract<PlatformConfig>
        implements PlatformConfigRepositoryExtension {

	public PlatformConfigRepositoryExtensionImpl() {
		super(PlatformConfig.class);
	}
}
