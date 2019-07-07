package com.arrow.pegasus.repo;

import java.util.List;

import com.arrow.pegasus.data.ApplicationEngine;

public interface ApplicationEngineRepositoryExtension extends RepositoryExtension<ApplicationEngine> {
	public List<ApplicationEngine> findApplicationEngines(ApplicationEngineSearchParams params);
}
