package com.arrow.pegasus.repo;

import java.util.List;

import com.arrow.pegasus.data.profile.Zone;

public interface ZoneRepositoryExtension extends RepositoryExtension<Zone> {
	public List<Zone> findZones(ZoneSearchParams params);
}
