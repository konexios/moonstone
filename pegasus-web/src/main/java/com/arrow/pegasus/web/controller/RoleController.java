package com.arrow.pegasus.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;
import com.arrow.pegasus.repo.params.RoleSearchParams;
import com.arrow.pegasus.web.model.ApplicationModels;
import com.arrow.pegasus.web.model.ProductModels;
import com.arrow.pegasus.web.model.RoleModels;
import com.arrow.pegasus.web.model.SearchFilterModels;
import com.arrow.pegasus.web.model.SearchResultModels;
import com.arrow.pegasus.webapi.data.CoreApplicationModels;
import com.arrow.pegasus.webapi.data.CoreApplicationModels.ApplicationOption;
import com.arrow.pegasus.webapi.data.CoreCompanyModels.CompanyOption;
import com.arrow.pegasus.webapi.data.CorePrivilegeModels;
import com.arrow.pegasus.webapi.data.CoreProductModels.ProductOption;
import com.arrow.pegasus.webapi.data.KeyValueOption;

@RestController
@RequestMapping("/api/pegasus/roles")
public class RoleController extends PegasusControllerAbstract {

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/{enabled}/options", method = RequestMethod.GET)
	public List<RoleModels.RoleOption> options(@PathVariable Boolean enabled) {
		return getRoleOptions(null, enabled);
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/{productId}/applications", method = RequestMethod.GET)
	public List<CoreApplicationModels.ApplicationOption> applicationOptions(@PathVariable String productId) {

		User authenticatedUser = getAuthenticatedUser();

		return getApplicationOptions(productId, authenticatedUser.getCompanyId(), true, authenticatedUser.isAdmin());
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/{productId}/privileges", method = RequestMethod.GET)
	public List<CorePrivilegeModels.PrivilegeOption> privilegeOptions(@PathVariable String productId) {
		return getPrivilegeOptions(productId, getAuthenticatedUser().isAdmin());
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/filters", method = RequestMethod.GET)
	public RoleModels.RoleFilterOptions filterOptions(HttpSession session) {

		User authenticatedUser = getAuthenticatedUser();

		// company options
		List<CompanyOption> companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(),
				EnumSet.of(CompanyStatus.Active), true, authenticatedUser.isAdmin());

		List<ProductModels.ProductOption> productOptions = new ArrayList<>();
		if (authenticatedUser.isAdmin()) {
			// TODO revisit, design approach to get products for a non-admin
			for (Product product : getProductService().getProductRepository().findAll()) {
				productOptions.add(new ProductModels.ProductOption(product));
			}
		}

		// application options
		Set<String> companyIdSet = new HashSet<>();
		for (CompanyOption companyOption : companyOptions)
			companyIdSet.add(companyOption.getId());

		List<Application> applications = getApplicationService().getApplicationRepository()
				.findApplications(new ApplicationSearchParams()
						.withCompanyIds(companyIdSet.toArray(new String[companyIdSet.size()])).withEnabled(true));
		List<ApplicationModels.ApplicationOption> applicationOptions = new ArrayList<>();
		for (Application application : applications) {
			applicationOptions.add(new ApplicationModels.ApplicationOption(application));
		}

		// enabled options
		List<KeyValueOption> enabledOptions = new ArrayList<>();
		enabledOptions.add(new KeyValueOption("true", "Yes"));
		enabledOptions.add(new KeyValueOption("false", "No"));
		enabledOptions.add(new KeyValueOption("all", "All"));

		// editable options
		List<KeyValueOption> editableOptions = new ArrayList<>();
		editableOptions.add(new KeyValueOption("true", "Yes"));
		editableOptions.add(new KeyValueOption("false", "No"));
		editableOptions.add(new KeyValueOption("all", "All"));

		return new RoleModels.RoleFilterOptions().withProductOptions(productOptions)
				.withApplicationOptions(applicationOptions).withCompanyOptions(companyOptions)
				.withEnabledOptions(enabledOptions).withEditableOptions(editableOptions);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_ROLE')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.RoleSearchResult find(@RequestBody SearchFilterModels.RoleSearchFilter searchFilter) {

		User authenticatedUser = getAuthenticatedUser();

		// search params
		RoleSearchParams params = new RoleSearchParams();

		Set<String> applicationIdSet = new HashSet<>();

		if (authenticatedUser.isAdmin()) {
			// products
			params.withProductIds(searchFilter.getProductIds());

			// editable
			if (searchFilter.getEditable() != null && !searchFilter.getEditable().equalsIgnoreCase("all"))
				params.setEditable(Boolean.valueOf(searchFilter.getEditable()));
		} else {
			// ensure authenticated user can't see roles the tenant or it's
			// sub-tenants do not have an application instance
			List<String> companyIds = getCompanyIds(authenticatedUser.getCompanyId(), EnumSet.of(CompanyStatus.Active),
					true, false);
			searchFilter.setCompanyIds(companyIds.toArray(new String[companyIds.size()]));

			// hidden
			params.setHidden(false);
		}

		// companyIds
		if (searchFilter.getCompanyIds() != null && searchFilter.getCompanyIds().length > 0) {

			ApplicationSearchParams applicationSearchParams = new ApplicationSearchParams()
					.withCompanyIds(searchFilter.getCompanyIds()).withEnabled(true);

			List<Application> applications = getApplicationService().getApplicationRepository()
					.findApplications(applicationSearchParams);
			for (Application application : applications)
				applicationIdSet.add(application.getId());
		}

		// editable
		if (searchFilter.getEnabled() != null && !searchFilter.getEnabled().equalsIgnoreCase("all"))
			params.setEnabled(Boolean.valueOf(searchFilter.getEnabled()));

		if (authenticatedUser.isAdmin()) {
			// applicationIds (these are merged with companyId applications if
			// companyId filter is passed in)
			for (String applicationId : searchFilter.getApplicationIds())
				applicationIdSet.add(applicationId);
		}

		if (!applicationIdSet.isEmpty())
			params.withApplicationIds(applicationIdSet.toArray(new String[applicationIdSet.size()]));

		params.setName(searchFilter.getName());

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		// lookup
		Page<Role> rolePage = getRoleService().getRoleRepository().findRoles(pageRequest, params);

		// convert to visual model
		Page<RoleModels.RoleList> result = null;
		List<RoleModels.RoleList> roleModels = new ArrayList<>();
		for (Role role : rolePage) {
			getCoreCacheHelper().populateRole(role);
			getCoreCacheHelper().populateApplication(role.getRefApplication());
			roleModels.add(new RoleModels.RoleList(role, role.getRefApplication()));
		}
		result = new PageImpl<>(roleModels, pageRequest, rolePage.getTotalElements());

		return new SearchResultModels.RoleSearchResult(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_ROLE')")
	@RequestMapping(value = "/{id}/role")
	public RoleModels.RoleUpsert role(@PathVariable String id) {
		Assert.hasText(id, "id is null");

		User authenticatedUser = getAuthenticatedUser();
		List<CoreApplicationModels.ApplicationOption> applicationOptions = new ArrayList<>();
		List<CorePrivilegeModels.PrivilegeOption> privilegeOptions = new ArrayList<>();

		List<ProductOption> productOptions = new ArrayList<>();
		if (authenticatedUser.isAdmin()) {
			productOptions = getProductOptions(true);
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

			for (String productId : productIds) {
				Product product = getCoreCacheService().findProductById(productId);
				Assert.notNull(product, "Product not found! productId=" + productId);
				productOptions.add(new ProductOption(product));
			}
		}

		Role role = new Role();
		if (!id.equalsIgnoreCase("new")) {
			role = getRoleService().getRoleRepository().findById(id).orElse(null);
			role = getRoleService().populate(role);
			Assert.notNull(role, "role is null");

			// application options
			applicationOptions = getApplicationOptions(role.getProductId(), authenticatedUser.getCompanyId(), true,
					authenticatedUser.isAdmin());

			// limit privileges to the assigned productId
			privilegeOptions = getPrivilegeOptions(role.getProductId(), authenticatedUser.isAdmin());

			// ensure product options include the assigned product
			boolean foundAssignedProduct = false;
			for (ProductOption productOption : productOptions) {
				if (productOption.getId().equals(role.getProductId())) {
					foundAssignedProduct = true;
					break;
				}
			}

			if (!foundAssignedProduct)
				productOptions.add(new ProductOption(role.getRefProduct()));

			// ensure application options include the assigned application
			boolean foundAssignedApplication = false;
			for (ApplicationOption applicationOption : applicationOptions) {
				if (applicationOption.getId().equals(role.getApplicationId())) {
					foundAssignedApplication = true;
					break;
				}
			}

			if (!foundAssignedApplication)
				applicationOptions.add(new ApplicationOption(role.getRefApplication()));
		}

		// sort by application name
		applicationOptions.sort(Comparator.comparing(CoreApplicationModels.ApplicationOption::getName));

		// sort by product name
		productOptions.sort(Comparator.comparing(ProductModels.ProductOption::getName));

		return new RoleModels.RoleUpsert(new RoleModels.RoleModel(role), productOptions, applicationOptions,
				privilegeOptions);
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_ROLE')")
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public RoleModels.RoleModel create(@RequestBody RoleModels.RoleModel model) {

		Assert.notNull(model, "role is null");

		Role role = getPegasusModelUtil().toRole(model);

		role = getRoleService().create(role, getUserId());

		return new RoleModels.RoleModel(role);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_ROLE')")
	@RequestMapping(path = "/update", method = RequestMethod.PUT)
	public RoleModels.RoleModel update(@RequestBody RoleModels.RoleModel model) {
		Assert.notNull(model, "role is null");

		Role role = getCoreCacheService().findRoleById(model.getId());
		Assert.notNull(role, "role not found :: roleId=[" + model.getId() + "]");

		role = getPegasusModelUtil().toRole(model, role);

		role = getRoleService().update(role, getUserId());

		return new RoleModels.RoleModel(role);
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_ROLE')")
	@RequestMapping(value = "/{id}/clone")
	public RoleModels.RoleUpsert clone(@PathVariable String id) {
		Assert.hasText(id, "id is null");

		User authenticatedUser = getAuthenticatedUser();

		Role role = getCoreCacheService().findRoleById(id);
		Assert.notNull(role, "role is null");

		List<ApplicationOption> applicationOptions = getApplicationOptions(role.getProductId(),
				authenticatedUser.getCompanyId(), true, authenticatedUser.isAdmin());
		// // limit privileges to the assigned productId and always clone as
		// system admin so privileges
		// List<CorePrivilegeModels.PrivilegeOption> privilegeOptions =
		// getPrivilegeOptions(role.getProductId(), true,
		// false, true);
		// List<ProductOption> productOptions =
		// getProductOptions(authenticatedUser.isAdmin());

		return new RoleModels.RoleUpsert(new RoleModels.RoleModel(role), Collections.emptyList(), applicationOptions,
				Collections.emptyList());
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_ROLE')")
	@RequestMapping(path = "/createClone", method = RequestMethod.POST)
	public RoleModels.RoleModel createClone(@RequestBody RoleModels.RoleModel model) {
		Assert.notNull(model, "role is null");

		Role role = getCoreCacheService().findRoleById(model.getId());
		role.setId(null);
		role.setHid(null);
		role.setName(model.getName());
		role.setDescription(model.getDescription());
		role.setApplicationId(model.getApplicationId());
		role.setEditable(model.isEditable());
		role.setEnabled(model.isEnabled());
		role.setHidden(model.isHidden());

		role = getRoleService().create(role, getUserId());

		return new RoleModels.RoleModel(role);
	}
}
