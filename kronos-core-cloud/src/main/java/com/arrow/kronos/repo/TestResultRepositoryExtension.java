package com.arrow.kronos.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.TestResult;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface TestResultRepositoryExtension extends RepositoryExtension<TestResult> {

	Page<TestResult> findTestResult(Pageable pageable, TestResultSearchParams params);

	List<String> doFindTestProcedureIds(Collection<String> objectIds);
}
