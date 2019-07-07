package com.arrow.kronos.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.SoftwareReleaseSchedule;

@Repository
public interface SoftwareReleaseScheduleRepository
        extends MongoRepository<SoftwareReleaseSchedule, String>, SoftwareReleaseScheduleRepositoryExtension {
	Long deleteByApplicationId(String applicationId);
}