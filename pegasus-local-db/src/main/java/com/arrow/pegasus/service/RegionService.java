package com.arrow.pegasus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.repo.RegionRepository;

@Service
public class RegionService extends BaseServiceAbstract {

	@Autowired
	private RegionRepository regionRepository;

	public RegionRepository getRegionRepository() {
		return regionRepository;
	}

	public Region populateRefs(Region region) {

		if (region == null)
			return region;

		return region;
	}

	public Region create(Region region, String who) {
		String method = "create";

		// logical checks
		if (region == null) {
			logInfo(method, "region is null");
			throw new AcsLogicalException("region is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// persist
		region = regionRepository.doInsert(region, who);

		// audit log
		getAuditLogService().save(
		        AuditLogBuilder.create().type(CoreAuditLog.Region.CREATE_REGION).productName(ProductSystemNames.PEGASUS)
		                .objectId(region.getId()).by(who).parameter("name", region.getName()));

		return region;
	}

	public Region update(Region region, String who) {
		String method = "update";

		// logical checks
		if (region == null) {
			logInfo(method, "region is null");
			throw new AcsLogicalException("region is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		Region result = regionRepository.doSave(region, who);

		// audit log
		getAuditLogService().save(
		        AuditLogBuilder.create().type(CoreAuditLog.Region.UPDATE_REGION).productName(ProductSystemNames.PEGASUS)
		                .objectId(region.getId()).by(who).parameter("name", region.getName()));

		// clear cache
		Region cachedRegion = getCoreCacheService().findRegionById(region.getId());
		if (cachedRegion != null) {
			getCoreCacheService().clearRegion(cachedRegion);
		}

		return result;
	}
}
