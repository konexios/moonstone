package com.arrow.pegasus.web.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.webapi.data.CoreApplicationEngineModels;

@RestController
@RequestMapping("/api/pegasus/engines")
public class ApplicationEngineController extends PegasusControllerAbstract {

	// @PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	// @RequestMapping(value = "/{enabled}/options", method = RequestMethod.GET)
	// public List<ZoneModels.ZoneOption> options(@PathVariable Boolean enabled)
	// {
	// return getZoneOptions(null, enabled);
	// }

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/{productId}/{zoneId}/options", method = RequestMethod.GET)
	public List<CoreApplicationEngineModels.ApplicationEngineOption> optionsByProductAndZone(
	        @PathVariable String productId, @PathVariable String zoneId) {
		return getApplicationEngineOptions(productId, zoneId, getAuthenticatedUser().isAdmin());
	}

	// @PreAuthorize("hasAuthority('PEGASUS_READ_ZONE')")
	// @RequestMapping(value = "/find", method = RequestMethod.POST)
	// public SearchResultModels.ZoneSearchResult find(@RequestBody
	// SearchFilterModels.ZoneSearchFilter searchFilter) {
	//
	// // sorting & paging
	// PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(),
	// searchFilter.getItemsPerPage(),
	// new Sort(Direction.valueOf(searchFilter.getSortDirection()),
	// searchFilter.getSortField()));
	//
	// // lookup
	// Page<Zone> zonePage =
	// getZoneService().getZoneRepository().findAll(pageRequest);
	//
	// // convert to visual model
	// Page<ZoneModels.ZoneList> result = null;
	// List<ZoneModels.ZoneList> zoneModels = new ArrayList<>();
	// for (Zone zone : zonePage) {
	// String regionName = "UNKNOWN";
	// if (!StringUtils.isEmpty(zone.getRegionId())) {
	// Region region = getCoreCacheService().findRegionById(zone.getRegionId());
	// if (region != null)
	// regionName = region.getName();
	// }
	//
	// zoneModels.add(new ZoneModels.ZoneList(zone, regionName));
	// }
	// result = new PageImpl<>(zoneModels, pageRequest,
	// zonePage.getTotalElements());
	//
	// return new SearchResultModels.ZoneSearchResult(result, searchFilter);
	// }
	//
	// @PreAuthorize("hasAuthority('PEGASUS_READ_ZONE')")
	// @RequestMapping(value = "/{id}/zone")
	// public ZoneModels.ZoneUpsert zone(@PathVariable String id) {
	// Assert.hasText(id, "id is null");
	//
	// Zone zone = new Zone();
	//
	// if (!id.equalsIgnoreCase("new")) {
	// zone = getZoneService().getZoneRepository().findOne(id);
	// Assert.notNull(zone, "zone is null");
	// }
	//
	// List<ZoneRegionOption> zoneRegionOptions = getZoneRegionOptions(true,
	// true);
	//
	// return new ZoneModels.ZoneUpsert(new ZoneModels.ZoneModel(zone),
	// zoneRegionOptions);
	// }
	//
	// @PreAuthorize("hasAuthority('PEGASUS_CREATE_ZONE')")
	// @RequestMapping(path = "/create", method = RequestMethod.POST)
	// public ZoneModels.ZoneModel create(@RequestBody ZoneModels.ZoneModel
	// model) {
	//
	// Assert.notNull(model, "zone is null");
	//
	// Zone zone = getPegasusModelUtil().populateZone(model);
	//
	// zone = getZoneService().create(zone, getUserId());
	//
	// return new ZoneModels.ZoneModel(zone);
	// }
	//
	// @PreAuthorize("hasAuthority('PEGASUS_UPDATE_ZONE')")
	// @RequestMapping(path = "/update", method = RequestMethod.PUT)
	// public ZoneModels.ZoneModel update(@RequestBody ZoneModels.ZoneModel
	// model) {
	// Assert.notNull(model, "zone is null");
	//
	// Zone zone = getCoreCacheService().findZoneById(model.getId());
	// Assert.notNull(zone, "zone not found :: zoneId=[" + model.getId() + "]");
	//
	// zone = getPegasusModelUtil().populateZone(model, zone);
	//
	// zone = getZoneService().update(zone, getUserId());
	//
	// return new ZoneModels.ZoneModel(zone);
	// }
}
