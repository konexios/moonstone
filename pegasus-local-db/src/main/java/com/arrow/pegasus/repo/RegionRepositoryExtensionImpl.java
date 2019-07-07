package com.arrow.pegasus.repo;

import com.arrow.pegasus.data.profile.Region;

public class RegionRepositoryExtensionImpl extends RepositoryExtensionAbstract<Region> implements RegionRepositoryExtension {

	public RegionRepositoryExtensionImpl() {
		super(Region.class);
	}
}
