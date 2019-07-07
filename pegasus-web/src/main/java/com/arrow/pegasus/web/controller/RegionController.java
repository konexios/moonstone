package com.arrow.pegasus.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.web.model.RegionModels;
import com.arrow.pegasus.web.model.SearchFilterModels;
import com.arrow.pegasus.web.model.SearchResultModels;

@RestController
@RequestMapping("/api/pegasus/regions")
public class RegionController extends PegasusControllerAbstract {

	@PreAuthorize("hasAuthority('PEGASUS_READ_REGION')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.RegionSearchResult find(@RequestBody SearchFilterModels.RegionSearchFilter searchFilter) {

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		// lookup
		Page<Region> regionPage = getRegionService().getRegionRepository().findAll(pageRequest);

		// convert to visual model
		Page<RegionModels.RegionList> result = null;
		List<RegionModels.RegionList> regionModels = new ArrayList<>();
		for (Region region : regionPage) {
			getRegionService().populateRefs(region);
			regionModels.add(new RegionModels.RegionList(region));
		}
		result = new PageImpl<>(regionModels, pageRequest, regionPage.getTotalElements());

		return new SearchResultModels.RegionSearchResult(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_REGION')")
	@RequestMapping(value = "/{id}/region")
	public RegionModels.RegionUpsert region(@PathVariable String id) {
		Assert.hasText(id, "id is null");

		Region region = new Region();

		if (!id.equalsIgnoreCase("new")) {
			region = getRegionService().getRegionRepository().findById(id).orElse(null);
			Assert.notNull(region, "region is null");
		}

		return new RegionModels.RegionUpsert(new RegionModels.RegionModel(region));
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_REGION')")
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public RegionModels.RegionModel create(@RequestBody RegionModels.RegionModel model) {

		Assert.notNull(model, "region is null");

		Region region = getPegasusModelUtil().toRegion(model);

		region = getRegionService().create(region, getUserId());

		return new RegionModels.RegionModel(region);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_REGION')")
	@RequestMapping(path = "/update", method = RequestMethod.PUT)
	public RegionModels.RegionModel update(@RequestBody RegionModels.RegionModel model) {
		Assert.notNull(model, "region is null");

		Region region = getCoreCacheService().findRegionById(model.getId());
		Assert.notNull(region, "region not found :: regionId=[" + model.getId() + "]");

		region = getPegasusModelUtil().toRegion(model, region);

		region = getRegionService().update(region, getUserId());

		return new RegionModels.RegionModel(region);
	}
}
