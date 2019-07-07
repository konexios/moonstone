package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.dashboard.data.Container;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface ContainerRepositoryExtension extends RepositoryExtension<Container> {

	public Page<Container> findContainers(Pageable pageable, ContainerSearchParams params);

	public List<Container> findContainers(ContainerSearchParams params);
}
