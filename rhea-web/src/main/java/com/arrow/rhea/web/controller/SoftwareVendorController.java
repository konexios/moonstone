package com.arrow.rhea.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.arrow.rhea.data.SoftwareVendor;
import com.arrow.rhea.repo.SoftwareVendorSearchParams;
import com.arrow.rhea.service.SoftwareVendorService;
import com.arrow.rhea.web.model.SearchFilterModels.SoftwareVendorSearchFilterModel;
import com.arrow.rhea.web.model.SearchResultModels.SoftwareVendorSearchResultModel;
import com.arrow.rhea.web.model.SoftwareVendorModels.SoftwareVendorModel;

@RestController
@RequestMapping("/api/rhea/software-vendors")
public class SoftwareVendorController extends ControllerAbstract {

	@Autowired
	private SoftwareVendorService softwareVendorService;

	@PreAuthorize("hasAuthority('RHEA_READ_SOFTWARE_VENDORS')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SoftwareVendorSearchResultModel find(@RequestBody SoftwareVendorSearchFilterModel searchFilter) {

		// sorting & paging
		PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
		        new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		SoftwareVendorSearchParams params = new SoftwareVendorSearchParams();
		// implied/enforced filter
		params.addCompanyIds(getAuthenticatedUser().getCompanyId());
		// user defined filter
		params.setEnabled(searchFilter.isEnabled());
		params.setEditable(searchFilter.isEditable());

		Page<SoftwareVendor> softwareVendors = softwareVendorService.getSoftwareVendorRepository()
		        .findSoftwareVendors(pageRequest, params);

		// convert to visual model
		List<SoftwareVendorModel> softwareVendorModels = new ArrayList<>();
		for (SoftwareVendor softwareVendor : softwareVendors) {
			softwareVendorModels.add(new SoftwareVendorModel(softwareVendor));
		}

		Page<SoftwareVendorModel> result = new PageImpl<>(softwareVendorModels, pageRequest,
		        softwareVendors.getTotalElements());

		return new SoftwareVendorSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('RHEA_CREATE_SOFTWARE_VENDOR')")
	@RequestMapping(method = RequestMethod.POST)
	public SoftwareVendorModel create(@RequestBody SoftwareVendorModel model) {
		Assert.notNull(model, "model is null");

		SoftwareVendor softwareVendor = new SoftwareVendor();
		softwareVendor.setCompanyId(getAuthenticatedUser().getCompanyId());
		softwareVendor.setName(model.getName());
		softwareVendor.setDescription(model.getDescription());
		softwareVendor.setEnabled(model.isEnabled());
		softwareVendor.setEditable(model.isEditable());

		softwareVendor = softwareVendorService.create(softwareVendor, getUserId());

		return new SoftwareVendorModel(softwareVendor);
	}

	@PreAuthorize("hasAuthority('RHEA_UPDATE_SOFTWARE_VENDOR')")
	@RequestMapping(value = "/{softwareVendorId}", method = RequestMethod.PUT)
	public SoftwareVendorModel update(@PathVariable String softwareVendorId, @RequestBody SoftwareVendorModel model) {
		Assert.hasText(softwareVendorId, "softwareVendorId is empty");
		Assert.notNull(model, "model is null");

		SoftwareVendor softwareVendor = getRheaCacheService().findSoftwareVendorById(softwareVendorId);
		Assert.notNull(softwareVendor, "softwareVendor is null");
		Assert.isTrue(getAuthenticatedUser().getCompanyId().equals(softwareVendor.getCompanyId()),
		        "user and softwareVendor must have the same companyId");

		softwareVendor.setName(model.getName());
		softwareVendor.setDescription(model.getDescription());
		softwareVendor.setEnabled(model.isEnabled());
		softwareVendor.setEditable(model.isEditable());

		softwareVendor = softwareVendorService.update(softwareVendor, getUserId());

		return new SoftwareVendorModel(softwareVendor);
	}
}
