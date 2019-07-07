package com.arrow.pegasus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.arrow.acs.AcsSystemException;
import com.arrow.acs.AcsUtils;
import com.arrow.pegasus.data.PlatformConfig;
import com.arrow.pegasus.repo.PlatformConfigRepository;

@Service
public class PlatformConfigService extends ServiceAbstract {
	@Autowired
	private PlatformConfigRepository platformConfigRepository;
	@Autowired
	private CoreCacheService coreCacheService;

	@Caching(put = { @CachePut(value = PlatformConfig.COLLECTION_NAME, condition = "#result != null") })
	public PlatformConfig getConfig() {
		List<PlatformConfig> all = platformConfigRepository.findAll();
		if (all.isEmpty()) {
			throw new AcsSystemException("PlatformConfig is not found in the database");
		}
		if (all.size() > 1) {
			throw new AcsSystemException("More than one PlatformConfig records are found in the database!");
		}
		PlatformConfig result = all.get(0);
		result.setRefZone(coreCacheService.findZoneBySystemName(result.getZoneSystemName()));
		AcsUtils.notNull(result.getRefZone(), "Invalid zoneSystemName: %s", result.getZoneSystemName());
		return result;
	}

	public PlatformConfigRepository getPlatformConfigRepository() {
		return platformConfigRepository;
	}
}
