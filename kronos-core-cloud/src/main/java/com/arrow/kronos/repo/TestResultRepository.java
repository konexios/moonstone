package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.TestResult;

import moonstone.acn.client.model.AcnDeviceCategory;

@Repository
public interface TestResultRepository extends MongoRepository<TestResult, String>, TestResultRepositoryExtension {

	List<TestResult> findByApplicationId(String applicationId);

	TestResult findByTestProcedureId(String testProcedureId);

	TestResult findByObjectId(String objectId);

	Long deleteByDeviceCategoryAndObjectId(AcnDeviceCategory deviceCategory, String objectId);

	List<TestResult> findAllByDeviceCategoryAndObjectId(AcnDeviceCategory deviceCategory, String objectId);

	Long deleteByApplicationId(String applicationId);
}