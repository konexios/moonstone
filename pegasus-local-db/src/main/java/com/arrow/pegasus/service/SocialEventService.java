package com.arrow.pegasus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.SocialEvent;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.repo.SocialEventRepository;

@Service
public class SocialEventService extends BaseServiceAbstract {

    @Autowired
    private SocialEventRepository socialEventRepository;

    public SocialEventRepository getSocialEventRepository() {
        return socialEventRepository;
    }

    public SocialEvent create(SocialEvent socialEvent, String who) {
        Assert.hasText(who, "who is empty");
        Assert.notNull(socialEvent, "socialEvent is null");
        validateSocialEvent(socialEvent);

        SocialEvent event = socialEventRepository.findByName(socialEvent.getName());
        Assert.isNull(event, "name already exists");

        socialEvent = socialEventRepository.doInsert(socialEvent, who);

        getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.SocialEvent.CREATE_SOCIAL_EVENT)
                .productName(ProductSystemNames.PEGASUS).objectId(socialEvent.getId()).by(who)
                .parameter("name", socialEvent.getName()).parameter("startDate", socialEvent.getStartDate().toString())
                .parameter("endDate", socialEvent.getEndDate().toString())
                .parameter("zoneId", socialEvent.getZoneId()));

        return socialEvent;
    }

    public SocialEvent update(SocialEvent socialEvent, String who) {
        Assert.hasText(who, "who is empty");
        Assert.notNull(socialEvent, "socialEvent is null");
        validateSocialEvent(socialEvent);
        Assert.hasText(socialEvent.getId(), "socialEventId is empty");

        SocialEvent event = socialEventRepository.findById(socialEvent.getId()).orElse(null);
        Assert.notNull(event, "social event does not exist");
        if (!event.getName().equals(socialEvent.getName())) {
            event = socialEventRepository.findByName(socialEvent.getName());
            Assert.isNull(event, "name already exists");
        }

        socialEvent = socialEventRepository.doSave(socialEvent, who);

        getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.SocialEvent.UPDATE_SOCIAL_EVENT)
                .productName(ProductSystemNames.PEGASUS).objectId(socialEvent.getId()).by(who)
                .parameter("name", socialEvent.getName()).parameter("startDate", socialEvent.getStartDate().toString())
                .parameter("endDate", socialEvent.getEndDate().toString())
                .parameter("zoneId", socialEvent.getZoneId()));

        return socialEvent;
    }

    private SocialEvent validateSocialEvent(SocialEvent event) {
        // name
        Assert.hasText(event.getName(), "name is empty");
        // startDate & endDate
        Assert.notNull(event.getStartDate(), "startDate is null");
        Assert.notNull(event.getEndDate(), "endDate is null");
        Assert.isTrue(event.getStartDate().isBefore(event.getEndDate()), "startDate is after endDate");
        // zoneId
        Assert.hasText(event.getZoneId(), "zoneId is null");
        Zone zone = getCoreCacheService().findZoneById(event.getZoneId());
        Assert.notNull(zone, "zone is not found");
        // latitude
        if (StringUtils.hasText(event.getLatitude())) {
            try {
                double latitude = Double.valueOf(event.getLatitude());
                Assert.isTrue(latitude >= -90 && latitude <= 90, "invalid latitude");
            } catch (Exception e) {
                throw new AcsLogicalException("invalid latitude");
            }
        }
        // longitude
        if (StringUtils.hasText(event.getLongitude())) {
            try {
                double longitude = Double.valueOf(event.getLongitude());
                Assert.isTrue(longitude >= -180 && longitude <= 180, "invalid longitude");
            } catch (Exception e) {
                throw new AcsLogicalException("invalid longitude");
            }
        }
        return event;
    }
}
