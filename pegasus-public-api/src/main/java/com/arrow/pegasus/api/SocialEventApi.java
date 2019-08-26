package com.arrow.pegasus.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.SocialEvent;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.repo.SocialEventSearchParams;
import com.arrow.pegasus.service.SocialEventService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acs.client.model.ListResultModel;
import moonstone.acs.client.model.SocialEventModel;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController(value = "pegasusSocialEventApi")
@RequestMapping("/api/v1/pegasus/social-events")
public class SocialEventApi extends BaseApiAbstract {

	@Autowired
	private SocialEventService socialEventService;

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	@ApiOperation(value = "find social event by hid", hidden = true)
	public SocialEventModel findByHid(
	        @ApiParam(value = "social event hid", required = true) @PathVariable(name = "hid", required = true) String hid) {

		getValidatedAccessKey(getProductSystemName());

		SocialEvent socialEvent = socialEventService.getSocialEventRepository().doFindByHid(hid);
		return buildSocialEventModel(socialEvent);
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	@ApiOperation(value = "find social events", hidden = true)
	public ListResultModel<SocialEventModel> findBy(
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "startDateFrom", required = false) String startDateFrom,
	        @RequestParam(name = "startDateTo", required = false) String startDateTo,
	        @RequestParam(name = "endDateFrom", required = false) String endDateFrom,
	        @RequestParam(name = "endDateTo", required = false) String endDateTo,
	        @RequestParam(name = "zoneHid", required = false) Set<String> zoneHids,
	        @RequestParam(name = "sortField", required = false, defaultValue = "name") String sortField,
	        @RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection) {

		// TODO revisit, need to solution what keys need to be used
		//getValidatedAccessKey(getProductSystemName());

		SocialEventSearchParams params = new SocialEventSearchParams();
		if (StringUtils.hasText(name)) {
			params.setName(name);
		}
		if (StringUtils.hasText(startDateFrom)) {
			params.setStartDateFrom(Instant.parse(startDateFrom));
		}
		if (StringUtils.hasText(startDateTo)) {
			params.setStartDateTo(Instant.parse(startDateTo));
		}
		if (StringUtils.hasText(endDateFrom)) {
			params.setEndDateFrom(Instant.parse(endDateFrom));
		}
		if (StringUtils.hasText(endDateTo)) {
			params.setEndDateTo(Instant.parse(endDateTo));
		}
		if (zoneHids != null) {
			zoneHids.forEach(hid -> {
				Zone zone = getCoreCacheService().findZoneByHid(hid);
				Assert.notNull(zone, "zone is not found");
				params.addZoneIds(zone.getId());
			});
		}
		Sort sort = null;
		if (StringUtils.hasText(sortField)) {
			Direction direction = sortDirection != null ? Direction.valueOf(sortDirection) : Direction.ASC;
			sort = new Sort(direction, sortField);
		}
		List<SocialEventModel> data = socialEventService.getSocialEventRepository().findSocialEvents(params, sort).stream()
		        .map(this::buildSocialEventModel).collect(Collectors.toCollection(ArrayList::new));
		return new ListResultModel<SocialEventModel>().withSize(data.size()).withData(data);
	}

	private SocialEventModel buildSocialEventModel(SocialEvent socialEvent) {
		Assert.notNull(socialEvent, "socialEvent is null");
		SocialEventModel result = new SocialEventModel();
		result.setHid(socialEvent.getHid());
		result.setEndDate(socialEvent.getEndDate().toString());
		result.setLatitude(socialEvent.getLatitude());
		result.setLongitude(socialEvent.getLongitude());
		result.setName(socialEvent.getName());
		result.setStartDate(socialEvent.getStartDate().toString());
		Zone zone = getCoreCacheService().findZoneById(socialEvent.getZoneId());
		Assert.notNull(zone, "zone is not found");
		result.setZoneHid(zone.getHid());
		result.setZoneSystemName(zone.getSystemName());
		result.setZoomLevel(socialEvent.getZoomLevel());
		return result;
	}
}
