package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.TestProcedure;

@Repository
public interface TestProcedureRepository
        extends MongoRepository<TestProcedure, String>, TestProcedureRepositoryExtension {

	List<TestProcedure> findByApplicationId(String applicationId);

	TestProcedure findByApplicationIdAndDeviceTypeIdAndEnabled(String applicationId, String deviceTypeId, boolean enabled);

	TestProcedure findByApplicationIdAndNameAndEnabled(String applicationId, String name, boolean enabled);

	Long deleteByApplicationId(String applicationId);
}
