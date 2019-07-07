package com.arrow.pegasus.web.controller;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

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

import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;
import com.arrow.pegasus.repo.params.PrivilegeSearchParams;
import com.arrow.pegasus.web.model.PrivilegeModels;
import com.arrow.pegasus.web.model.ProductModels;
import com.arrow.pegasus.web.model.SearchFilterModels;
import com.arrow.pegasus.web.model.SearchResultModels;
import com.arrow.pegasus.webapi.data.KeyValueOption;

@RestController
@RequestMapping("/api/pegasus/privileges")
public class PrivilegeController extends PegasusControllerAbstract {

	// @RequestMapping(value = "/{enabled}/options", method = RequestMethod.GET)
	// public List<PrivilegeModels.PrivilegeOption> options(@PathVariable
	// Boolean enabled) {
	// return getPrivilegeOptions(null, enabled);
	// }

	// @RequestMapping(value = "/{enabled}/{applicationId}/options", method =
	// RequestMethod.GET)
	// public List<PrivilegeModels.RolePrivilegeOption>
	// rolePrivilegeOptions(@PathVariable Boolean enabled,
	// @PathVariable String applicationId) {
	// Assert.hasText(applicationId, "applicationId is null");
	//
	// Application application =
	// getCoreCacheService().findApplicationById(applicationId);
	// Assert.notNull(application, "application is null");
	//
	// return getRolePrivilegeOptions(application.getProductId(), enabled);
	// }

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/filters", method = RequestMethod.GET)
	public PrivilegeModels.PrivilegeFilterOptions filterOptions(HttpSession session) {

		List<ProductModels.ProductOption> productOptions = new ArrayList<>();
		if (getAuthenticatedUser().isAdmin()) {
			// TODO revisit, design approach to get products for a non-admin
			for (Product product : getProductService().getProductRepository().findAll()) {
				productOptions.add(new ProductModels.ProductOption(product));
			}
		}

		// enabled options
		List<KeyValueOption> enabledOptions = new ArrayList<>();
		enabledOptions.add(new KeyValueOption("true", "Yes"));
		enabledOptions.add(new KeyValueOption("false", "No"));
		enabledOptions.add(new KeyValueOption("all", "All"));

		return new PrivilegeModels.PrivilegeFilterOptions().withProductOptions(productOptions)
				.withEnabledOptions(enabledOptions);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_PRIVILEGE')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.PrivilegeSearchResult find(
			@RequestBody SearchFilterModels.PrivilegeSearchFilter searchFilter) {

		User authenticatedUser = getAuthenticatedUser();

		// search params
		PrivilegeSearchParams params = new PrivilegeSearchParams();
		params.setName(searchFilter.getName());
		params.setSystemName(searchFilter.getSystemName());

		if (authenticatedUser.isAdmin()) {
			params.withProductIds(searchFilter.getProductIds());

			if (searchFilter.getEnabled() != null && !searchFilter.getEnabled().equalsIgnoreCase("all"))
				params.setEnabled(Boolean.valueOf(searchFilter.getEnabled()));
		} else {
			// lookup authenticated user's applications and leverage unique list
			// of productIds based on their application instances
			List<String> companyIds = getCompanyIds(authenticatedUser.getCompanyId(), EnumSet.of(CompanyStatus.Active),
					true, false);

			ApplicationSearchParams applicationSearchParams = new ApplicationSearchParams();
			applicationSearchParams.addCompanyIds(companyIds.toArray(new String[companyIds.size()]));
			applicationSearchParams.setEnabled(true);

			List<Application> applications = getApplicationService().getApplicationRepository()
					.findApplications(applicationSearchParams);
			Set<String> productIds = new HashSet<>();
			for (Application application : applications) {
				productIds.add(application.getProductId());
				if (!application.getProductExtensionIds().isEmpty())
					productIds.addAll(application.getProductExtensionIds());
			}

			// ensure authenticated user can't see privileges the tenant does
			// not have an application instance for
			params.withProductIds(productIds.toArray(new String[productIds.size()]));
			// ensure authenticated user can't see disabled privileges
			params.setEnabled(true);
			// ensure authenticated user can't see hidden privileges
			params.setHidden(false);
		}

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		// lookup
		Page<Privilege> privilegePage = getPrivilegeService().getPrivilegeRepository().findPrivileges(pageRequest,
				params);

		// convert to visual model
		Page<PrivilegeModels.PrivilegeList> result = null;
		List<PrivilegeModels.PrivilegeList> privilegeModels = new ArrayList<>();
		for (Privilege privilege : privilegePage) {
			getPrivilegeService().populateRefs(privilege);
			privilegeModels.add(new PrivilegeModels.PrivilegeList(privilege, privilege.getRefProduct()));
		}
		result = new PageImpl<>(privilegeModels, pageRequest, privilegePage.getTotalElements());

		return new SearchResultModels.PrivilegeSearchResult(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_PRIVILEGE')")
	@RequestMapping(value = "/{id}/privilege")
	public PrivilegeModels.PrivilegeUpsert privilege(@PathVariable String id) {
		Assert.hasText(id, "id is null");

		User authenticatedUser = getAuthenticatedUser();

		Privilege privilege = new Privilege();

		if (!id.equalsIgnoreCase("new")) {
			privilege = getPrivilegeService().getPrivilegeRepository().findById(id).orElse(null);
			Assert.notNull(privilege, "privilege is null");
		}

		return new PrivilegeModels.PrivilegeUpsert(new PrivilegeModels.PrivilegeModel(privilege),
				getProductOptions(authenticatedUser.isAdmin()));
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_PRIVILEGE')")
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public PrivilegeModels.PrivilegeModel create(@RequestBody PrivilegeModels.PrivilegeModel model) {

		Assert.notNull(model, "privilege is null");

		Privilege privilege = getPegasusModelUtil().toPrivilege(model);

		privilege = getPrivilegeService().create(privilege, getUserId());

		return new PrivilegeModels.PrivilegeModel(privilege);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_PRIVILEGE')")
	@RequestMapping(path = "/update", method = RequestMethod.PUT)
	public PrivilegeModels.PrivilegeModel update(@RequestBody PrivilegeModels.PrivilegeModel model) {
		Assert.notNull(model, "privilege is null");

		Privilege privilege = getCoreCacheService().findPrivilegeById(model.getId());
		Assert.notNull(privilege, "privilege not found :: privilegeId=[" + model.getId() + "]");

		privilege = getPegasusModelUtil().toPrivilege(model, privilege);

		privilege = getPrivilegeService().update(privilege, getUserId());

		return new PrivilegeModels.PrivilegeModel(privilege);
	}
}
