package com.arrow.pegasus.api;

import java.time.Instant;
import java.util.List;

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
import com.arrow.pegasus.repo.SocialEventSearchParams;
import com.arrow.pegasus.service.SocialEventService;

@RestController(value = "localPegasusSocialEventApi")
@RequestMapping("/api/v1/local/pegasus/social-events")
public class SocialEventApi extends BaseApiAbstract {

	@Autowired
	private SocialEventService socialEventService;

	@RequestMapping(path = "/hids/{hid}", method = RequestMethod.GET)
	public SocialEvent findByHid(@PathVariable(name = "hid", required = true) String hid) {
		SocialEvent result = socialEventService.getSocialEventRepository().doFindByHid(hid);
		Assert.notNull(result, "socialEvent not found");
		return result;
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public SocialEvent findById(@PathVariable(name = "id", required = true) String id) {
		SocialEvent result = socialEventService.getSocialEventRepository().findById(id).orElse(null);
		Assert.notNull(result, "socialEvent not found");
		return result;
	}

	@RequestMapping(path = "/names/{name}", method = RequestMethod.GET)
	public SocialEvent findByName(@PathVariable(name = "name", required = true) String name) {
		SocialEvent result = socialEventService.getSocialEventRepository().findByName(name);
		Assert.notNull(result, "socialEvent not found");
		return result;
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<SocialEvent> findBy(@RequestParam(name = "date", required = false) String date,
			@RequestParam(name = "zoneId", required = false) String zoneId,
			@RequestParam(name = "sortField", required = false, defaultValue = "name") String sortField,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection) {
		SocialEventSearchParams params = new SocialEventSearchParams();
		if (StringUtils.hasText(date)) {
			Instant eventDate = Instant.parse(date);
			params.setStartDateTo(eventDate);
			params.setEndDateFrom(eventDate);
		}
		if (StringUtils.hasText(zoneId)) {
			params.addZoneIds(zoneId);
		}
		Sort sort = null;
		if (StringUtils.hasText(sortField)) {
			Direction direction = sortDirection != null ? Direction.valueOf(sortDirection) : Direction.ASC;
			sort = new Sort(direction, sortField);
		}
		return socialEventService.getSocialEventRepository().findSocialEvents(params, sort);
	}
}
