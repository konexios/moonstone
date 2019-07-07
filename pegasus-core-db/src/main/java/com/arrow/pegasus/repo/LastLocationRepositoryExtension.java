package com.arrow.pegasus.repo;

import java.util.List;

import com.arrow.pegasus.data.location.LastLocation;

public interface LastLocationRepositoryExtension extends RepositoryExtension<LastLocation> {
	public List<LastLocation> findLastLocations(LastLocationSearchParams params);
}
