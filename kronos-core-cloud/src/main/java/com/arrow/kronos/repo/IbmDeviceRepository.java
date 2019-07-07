package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.IbmDevice;

@Repository
public interface IbmDeviceRepository extends MongoRepository<IbmDevice, String>, IbmDeviceRepositoryExtension {
	List<IbmDevice> findAllByDeviceIdAndEnabled(String deviceId, boolean enabled);

	IbmDevice findByDeviceIdAndIbmAccountId(String deviceId, String ibmAccountId);

	IbmDevice findByDeviceId(String deviceId);

	Long deleteByDeviceId(String deviceId);

	Long deleteByApplicationId(String applicationId);
}
