package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.TestProcedure;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class TestProcedureRepositoryExtensionImpl extends RepositoryExtensionAbstract<TestProcedure>
        implements TestProcedureRepositoryExtension {

	public TestProcedureRepositoryExtensionImpl() {
		super(TestProcedure.class);
	}

	@Override
	public Page<TestProcedure> findTestProcedure(Pageable pageable, TestProcedureSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}
	
	private List<Criteria> buildCriteria(TestProcedureSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "deviceTypeId", params.getDeviceTypeIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
		}
		return criteria;
	}
}
