package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.ConfigBackup.Type;

@Repository
public interface ConfigBackupRepository extends MongoRepository<ConfigBackup, String>, ConfigBackupRepositoryExtension {

	List<ConfigBackup> findByObjectId(String objectId);

	Long deleteByTypeAndObjectId(Type type, String objectId);

	Long deleteByApplicationId(String applicationId);
}
