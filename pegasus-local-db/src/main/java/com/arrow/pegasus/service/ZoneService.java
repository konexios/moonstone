package com.arrow.pegasus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.repo.ZoneRepository;

import moonstone.acs.AcsLogicalException;

@Service
public class ZoneService extends BaseServiceAbstract {

	@Autowired
	private ZoneRepository zoneRepository;

	public ZoneRepository getZoneRepository() {
		return zoneRepository;
	}

	public Zone populateRefs(Zone zone) {

		if (zone == null)
			return zone;

		if (!StringUtils.isEmpty(zone.getRegionId()))
			getCoreCacheService().findRegionById(zone.getRegionId());

		return zone;
	}

	public Zone create(Zone zone, String who) {
		String method = "create";

		// logical checks
		if (zone == null) {
			logInfo(method, "zone is null");
			throw new AcsLogicalException("zone is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// persist
		zone = zoneRepository.doInsert(zone, who);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Zone.CREATE_ZONE)
		        .productName(ProductSystemNames.PEGASUS).objectId(zone.getId()).by(who)
		        .parameter("name", zone.getName()).parameter("regionId", zone.getRegionId()));

		return zone;
	}

	public Zone update(Zone zone, String who) {
		String method = "update";

		// logical checks
		if (zone == null) {
			logInfo(method, "zone is null");
			throw new AcsLogicalException("zone is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		Zone result = zoneRepository.doSave(zone, who);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Region.UPDATE_REGION)
		        .productName(ProductSystemNames.PEGASUS).objectId(zone.getId()).by(who)
		        .parameter("name", zone.getName()).parameter("regionId", zone.getRegionId()));

		// clear cache
		Zone cachedZone = getCoreCacheService().findZoneById(zone.getId());
		if (cachedZone != null) {
			getCoreCacheService().clearZone(cachedZone);
		}

		return result;
	}
}
