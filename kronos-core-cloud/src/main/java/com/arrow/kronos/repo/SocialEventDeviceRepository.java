package com.arrow.kronos.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.arrow.kronos.data.SocialEventDevice;

public interface SocialEventDeviceRepository
        extends MongoRepository<SocialEventDevice, String>, SocialEventDeviceRepositoryExtension {

	SocialEventDevice findByDeviceTypeIdAndMacAddressAndPinCode(String deviceTypeId, String macAddress, String pinCode);

}
