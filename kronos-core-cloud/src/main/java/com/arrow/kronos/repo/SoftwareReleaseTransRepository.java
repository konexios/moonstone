package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.SoftwareReleaseTrans;

@Repository
public interface SoftwareReleaseTransRepository
        extends MongoRepository<SoftwareReleaseTrans, String>, SoftwareReleaseTransRepositoryExtension {

	SoftwareReleaseTrans findByHid(String hid);

	Long deleteByApplicationId(String applicationId);

	List<SoftwareReleaseTrans> findByObjectIdAndDeviceCategory(String objectId, String deviceCategory);

	Long deleteByObjectIdAndDeviceCategory(String objectId, String deviceCategory);
}