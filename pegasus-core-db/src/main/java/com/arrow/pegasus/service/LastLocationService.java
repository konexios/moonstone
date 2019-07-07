package com.arrow.pegasus.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.location.LastLocationType;
import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.repo.LastLocationRepository;

@Service
public class LastLocationService extends BaseServiceAbstract {

	@Autowired
	private LastLocationRepository lastLocationRepository;

	public LastLocationRepository getLastLocationRepository() {
		return lastLocationRepository;
	}

	public String update(LocationObjectType type, String objectId, Double latitude, Double longitude,
	        LastLocationType locationType, Instant timestamp) {
		LastLocation lastLocation = new LastLocation();
		lastLocation.setObjectType(type);
		lastLocation.setObjectId(objectId);
		lastLocation.setLatitude(latitude);
		lastLocation.setLongitude(longitude);
		lastLocation.setLocationType(locationType);
		lastLocation.setTimestamp(timestamp);
		lastLocation = update(lastLocation);
		return lastLocation.getId();
	}

	public LastLocation update(LastLocation lastLocation) {
		String method = "update";
		Assert.notNull(lastLocation, "lastLocation is null");
		Assert.notNull(lastLocation.getObjectType(), "objectType is null");
		Assert.notNull(lastLocation.getLocationType(), "locationType is null");
		Assert.hasLength(lastLocation.getObjectId(), "objectId is empty");

		LastLocation existingLastLocation = lastLocationRepository
		        .findByObjectTypeAndObjectId(lastLocation.getObjectType(), lastLocation.getObjectId());
		boolean create = existingLastLocation == null;
		if (create) {
			existingLastLocation = new LastLocation();
			existingLastLocation.setObjectType(lastLocation.getObjectType());
			existingLastLocation.setObjectId(lastLocation.getObjectId());
		}
		existingLastLocation.setLatitude(lastLocation.getLatitude());
		existingLastLocation.setLongitude(lastLocation.getLongitude());
		existingLastLocation.setLocationType(lastLocation.getLocationType());

		if (create) {
			existingLastLocation.setTimestamp(lastLocation.getTimestamp());
			existingLastLocation = lastLocationRepository.doInsert(existingLastLocation, null);
			logDebug(method, "created lastLocation for %s: %s", lastLocation.getObjectType(),
			        lastLocation.getObjectId());
		} else {
			if (existingLastLocation.getTimestamp() == null
			        || lastLocation.getTimestamp().isAfter(existingLastLocation.getTimestamp())) {
				existingLastLocation.setTimestamp(lastLocation.getTimestamp());
				existingLastLocation = lastLocationRepository.doSave(existingLastLocation, null);
				logDebug(method, "updated latitude, longitude, and timestamp for %s: %s", lastLocation.getObjectType(),
				        lastLocation.getObjectId());
			} else {
				logDebug(method, "ignored old location with timestamp: %s", lastLocation.toString());
			}
		}
		return existingLastLocation;
	}

	public void deleteBy(LocationObjectType objectType, String objectId) {
		String method = "deleteBy";
		Assert.notNull(objectType, "objectType is null");
		Assert.hasLength(objectId, "objectId is empty");

		Long numDeleted = lastLocationRepository.deleteByObjectTypeAndObjectId(objectType, objectId);
		logInfo(method, "Last location has been deleted for objectType=" + objectType.name() + ", objectId=" + objectId
		        + ", total " + numDeleted);
	}
}
