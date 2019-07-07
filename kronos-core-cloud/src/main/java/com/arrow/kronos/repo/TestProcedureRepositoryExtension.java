package com.arrow.kronos.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.TestProcedure;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface TestProcedureRepositoryExtension extends RepositoryExtension<TestProcedure> {
	
	Page<TestProcedure> findTestProcedure(Pageable pageable, TestProcedureSearchParams params);
}
