package com.arrow.pegasus.dashboard.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.dashboard.data.Container;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class ContainerRepositoryImpl extends RepositoryExtensionAbstract<Container>
        implements ContainerRepositoryExtension {

	public ContainerRepositoryImpl() {
		super(Container.class);
	}

	@Override
	public Page<Container> findContainers(Pageable pageable, ContainerSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<Container> findContainers(ContainerSearchParams params) {
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(ContainerSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "boardId", params.getBoardIds());
		}
		return criteria;
	}
}
