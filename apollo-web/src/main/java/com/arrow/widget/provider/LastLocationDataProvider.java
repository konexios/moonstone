package com.arrow.widget.provider;

import java.util.List;

import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.repo.LastLocationSearchParams;

public interface LastLocationDataProvider {

	List<LastLocation> findLastLocations(LastLocationSearchParams params);
}