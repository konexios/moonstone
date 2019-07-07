package com.arrow.rhea.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acn.client.model.RightToUseType;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.JsonUtils;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.data.SoftwareVendor;
import com.arrow.rhea.repo.DeviceProductSearchParams;
import com.arrow.rhea.repo.DeviceTypeSearchParams;
import com.arrow.rhea.repo.SoftwareProductSearchParams;
import com.arrow.rhea.repo.SoftwareReleaseSearchParams;
import com.arrow.rhea.service.DeviceManufacturerService;
import com.arrow.rhea.service.DeviceProductService;
import com.arrow.rhea.service.DeviceTypeService;
import com.arrow.rhea.service.SoftwareProductService;
import com.arrow.rhea.service.SoftwareReleaseService;
import com.arrow.rhea.service.SoftwareVendorService;
import com.arrow.rhea.web.model.DeviceManufacturerModels.DeviceManufacturerOption;
import com.arrow.rhea.web.model.DeviceProductModels.DeviceProductOption;
import com.arrow.rhea.web.model.DeviceTypeModels.DeviceTypeOption;
import com.arrow.rhea.web.model.SearchFilterModels.SoftwareReleaseSearchFilterModel;
import com.arrow.rhea.web.model.SearchResultModels.SoftwareReleaseSearchResultModel;
import com.arrow.rhea.web.model.SoftwareProductModels.SoftwareProductOption;
import com.arrow.rhea.web.model.SoftwareReleaseModels.SoftwareReleaseFilterOptionsModel;
import com.arrow.rhea.web.model.SoftwareReleaseModels.SoftwareReleaseModel;
import com.arrow.rhea.web.model.SoftwareReleaseModels.SoftwareReleaseOption;
import com.arrow.rhea.web.model.SoftwareReleaseModels.SoftwareReleaseOptionsModel;
import com.arrow.rhea.web.model.SoftwareReleaseModels.SoftwareReleaseSelectionModel;
import com.arrow.rhea.web.model.SoftwareVendorModels.SoftwareVendorOption;

@RestController
@RequestMapping("/api/rhea/software-releases")
public class SoftwareReleaseController extends ControllerAbstract {

	@Autowired
	private SoftwareReleaseService softwareReleaseService;
	@Autowired
	private SoftwareProductService softwareProductService;
	@Autowired
	private DeviceTypeService deviceTypeService;
	@Autowired
	private DeviceManufacturerService deviceManufacturerService;
	@Autowired
	private DeviceProductService deviceProductService;
	@Autowired
	private SoftwareVendorService softwareVendorService;

	@PreAuthorize("hasAuthority('RHEA_READ_SOFTWARE_RELEASES')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SoftwareReleaseSearchResultModel find(@RequestBody SoftwareReleaseSearchFilterModel searchFilter) {
		String method = "find";
		// sorting & paging
		PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
		        new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		SoftwareReleaseSearchParams params = new SoftwareReleaseSearchParams();
		// implied/enforced filter
		params.addCompanyIds(getAuthenticatedUser().getCompanyId());
		// user defined filter
		params.setEnabled(searchFilter.isEnabled());
		params.addSoftwareProductIds(searchFilter.getSoftwareProductIds());
		params.addDeviceTypeIds(searchFilter.getDeviceTypeIds());
		params.addUpgradeableFromIds(searchFilter.getUpgradeableFromIds());

		Page<SoftwareRelease> softwareReleases = softwareReleaseService.getSoftwareReleaseRepository()
		        .findSoftwareReleases(pageRequest, params);

		// convert to visual model
		List<SoftwareReleaseModel> softwareReleaseModels = new ArrayList<>();
		for (SoftwareRelease softwareRelease : softwareReleases) {
			SoftwareProduct softwareProduct = getRheaCacheService()
			        .findSoftwareProductById(softwareRelease.getSoftwareProductId());
			if (softwareProduct != null) {
				softwareReleaseModels.add(new SoftwareReleaseModel(softwareReleaseService.populateRefs(softwareRelease),
				        softwareProduct.getName(), populateSoftwareVendorOption(softwareProduct)));
			}
		}

		Page<SoftwareReleaseModel> result = new PageImpl<>(softwareReleaseModels, pageRequest,
		        softwareReleases.getTotalElements());

		return new SoftwareReleaseSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('RHEA_CREATE_SOFTWARE_RELEASE')")
	@RequestMapping(method = RequestMethod.POST)
	public SoftwareReleaseModel create(@RequestParam(required = false) MultipartFile file, @RequestParam String model) {
		SoftwareReleaseModel inputModel = JsonUtils.fromJson(model, SoftwareReleaseModel.class);
		Assert.notNull(inputModel, "model is null");
		Assert.notNull(inputModel.getSoftwareProduct(), "softwareProduct is null");
		Assert.notNull(inputModel.getMajor(), "major in model is null");
		Assert.notNull(inputModel.getMinor(), "minor in model is null");

		SoftwareProduct softwareProduct = getRheaCacheService()
		        .findSoftwareProductById(inputModel.getSoftwareProduct().getId());
		Assert.notNull(softwareProduct, "softwareProduct is null");
		Assert.isTrue(getAuthenticatedUser().getCompanyId().equals(softwareProduct.getCompanyId()),
		        "user and softwareVendor must have the same companyId");

		String method = "create";

		SoftwareRelease softwareRelease = new SoftwareRelease();
		softwareRelease.setBuild(inputModel.getBuild());
		softwareRelease.setCompanyId(getAuthenticatedUser().getCompanyId());
		softwareRelease.setMajor(inputModel.getMajor());
		softwareRelease.setMinor(inputModel.getMinor());
		softwareRelease.setEnabled(inputModel.isEnabled());
		softwareRelease.setSoftwareProductId(softwareProduct.getId());
		softwareRelease.setNoLongerSupported(inputModel.isNoLongerSupported());
		softwareRelease.setRtuType(inputModel.getRtuType());
		softwareRelease.setDeviceTypeIds(inputModel.getDeviceTypeIds());
		softwareRelease.setUpgradeableFromIds(inputModel.getUpgradeableFromIds());
		softwareRelease.setOwnerEmail(inputModel.getOwnerEmail());
		softwareRelease.setOwnerName(inputModel.getOwnerName());

		// file store
		if (file != null) {
			byte[] bytes = null;
			try {
				bytes = file.getBytes();
			} catch (IOException e) {
				e.printStackTrace();
				logDebug(method, e.getMessage());
				throw new AcsLogicalException("Unable to create software release!");
			}
			softwareRelease = softwareReleaseService.createWithFile(softwareRelease, file.getOriginalFilename(),
			        file.getContentType(), bytes, getUserId());
		} else {
			softwareRelease = softwareReleaseService.create(softwareRelease, getUserId());
		}

		return new SoftwareReleaseModel(softwareRelease, softwareProduct.getName(),
		        populateSoftwareVendorOption(softwareProduct));
	}

	@PreAuthorize("hasAuthority('RHEA_UPDATE_SOFTWARE_RELEASE')")
	@RequestMapping(value = "/{softwareReleaseId}", method = RequestMethod.POST)
	public SoftwareReleaseModel update(@PathVariable String softwareReleaseId,
	        @RequestParam(required = false) MultipartFile file, @RequestParam String model) {
		SoftwareReleaseModel inputModel = JsonUtils.fromJson(model, SoftwareReleaseModel.class);
		Assert.hasText(softwareReleaseId, "softwareReleaseId is empty");
		Assert.notNull(inputModel, "model is null");
		Assert.notNull(inputModel.getSoftwareProduct(), "softwareProduct is null");
		Assert.notNull(inputModel.getMajor(), "major in model is null");
		Assert.notNull(inputModel.getMinor(), "minor in model is null");

		SoftwareProduct softwareProduct = getRheaCacheService()
		        .findSoftwareProductById(inputModel.getSoftwareProduct().getId());
		Assert.notNull(softwareProduct, "softwareProduct is null");
		Assert.isTrue(getAuthenticatedUser().getCompanyId().equals(softwareProduct.getCompanyId()),
		        "user and softwareProduct must have the same companyId");

		SoftwareRelease softwareRelease = getRheaCacheService().findSoftwareReleaseById(softwareReleaseId);
		Assert.notNull(softwareRelease, "softwareRelease is null");
		Assert.isTrue(getAuthenticatedUser().getCompanyId().equals(softwareRelease.getCompanyId()),
		        "user and softwareRelease must have the same companyId");

		String method = "update";

		softwareRelease.setBuild(inputModel.getBuild());
		softwareRelease.setMajor(inputModel.getMajor());
		softwareRelease.setMinor(inputModel.getMinor());
		softwareRelease.setEnabled(inputModel.isEnabled());
		softwareRelease.setSoftwareProductId(softwareProduct.getId());
		softwareRelease.setNoLongerSupported(inputModel.isNoLongerSupported());
		softwareRelease.setRtuType(inputModel.getRtuType());
		softwareRelease.setDeviceTypeIds(inputModel.getDeviceTypeIds());
		softwareRelease.setUpgradeableFromIds(inputModel.getUpgradeableFromIds());
		softwareRelease.setOwnerEmail(inputModel.getOwnerEmail());
		softwareRelease.setOwnerName(inputModel.getOwnerName());

		// file store
		if (file != null) {
			byte[] bytes = null;
			try {
				bytes = file.getBytes();
			} catch (IOException e) {
				e.printStackTrace();
				logDebug(method, e.getMessage());
				throw new AcsLogicalException("Unable to update software release!");
			}
			softwareRelease = softwareReleaseService.updateWithFile(softwareRelease, file.getOriginalFilename(),
			        file.getContentType(), bytes, getUserId());
		} else {
			softwareRelease = softwareReleaseService.update(softwareRelease, getUserId());
		}

		return new SoftwareReleaseModel(softwareRelease, softwareProduct.getName(),
		        populateSoftwareVendorOption(softwareProduct));
	}

	private SoftwareVendorOption populateSoftwareVendorOption(SoftwareProduct softwareProduct) {
		Assert.notNull(softwareProduct, "softwareProduct is null");
		softwareProduct = softwareProductService.populateRefs(softwareProduct);
		if (softwareProduct.getRefSoftwareVendor() != null) {
			return new SoftwareVendorOption(softwareProduct.getRefSoftwareVendor());
		}
		return new SoftwareVendorOption();
	}

	@PreAuthorize("hasAuthority('RHEA_CREATE_SOFTWARE_RELEASE') or hasAuthority('RHEA_UPDATE_SOFTWARE_RELEASE')")
	@RequestMapping(value = "/options", method = RequestMethod.POST)
	public SoftwareReleaseOptionsModel options(@RequestBody SoftwareReleaseSelectionModel selection) {
		SoftwareReleaseOptionsModel options = new SoftwareReleaseOptionsModel();

		// fill missing ids in the selection object if possible
		if (StringUtils.isNotBlank(selection.getSoftwareReleaseId())) {
			SoftwareRelease softwareRelease = getRheaCacheService()
			        .findSoftwareReleaseById(selection.getSoftwareReleaseId());
			selection.setSoftwareProductId(softwareRelease.getSoftwareProductId());
			if (softwareRelease.getDeviceTypeIds().size() > 0) {
				DeviceType deviceType = getRheaCacheService()
				        .findDeviceTypeById(softwareRelease.getDeviceTypeIds().get(0));
				selection.setProductId(deviceType.getDeviceProductId());
			}
			selection.setRtuType(softwareRelease.getRtuType());
			selection.setOwnerEmail(softwareRelease.getOwnerEmail());
			selection.setOwnerName(softwareRelease.getOwnerName());
		}
		if ((StringUtils.isNotBlank(selection.getProductId()))) {
			DeviceProduct deviceProduct = getRheaCacheService().findDeviceProductById(selection.getProductId());
			selection.setManufacturerId(deviceProduct.getDeviceManufacturerId());
			// selection.setCategoryId(deviceProduct.getDeviceCategoryId());
			selection.setDeviceCategory(deviceProduct.getDeviceCategory());
		}

		// get available manufacturers
		List<DeviceManufacturer> manufacturers = deviceManufacturerService.getDeviceManufacturerRepository()
		        .findAllByEnabled(true);
		List<DeviceManufacturerOption> manufacturerModels = new ArrayList<>(manufacturers.size());
		for (DeviceManufacturer manufacturer : manufacturers) {
			manufacturerModels.add(new DeviceManufacturerOption(manufacturer));
		}
		manufacturerModels.sort(Comparator.comparing(DeviceManufacturerOption::getName, String.CASE_INSENSITIVE_ORDER));
		options.setManufacturers(manufacturerModels);

		// asset categories
		options.setDeviceCategories(EnumSet.of(AcnDeviceCategory.GATEWAY, AcnDeviceCategory.DEVICE));

		// get available software vendors
		List<SoftwareVendor> softwareVendors = softwareVendorService.getSoftwareVendorRepository()
		        .findAllByCompanyIdAndEnabled(getAuthenticatedUser().getCompanyId(), true);
		List<SoftwareVendorOption> softwareVendorModels = new ArrayList<>(softwareVendors.size());
		for (SoftwareVendor softwareVendor : softwareVendors) {
			softwareVendorModels.add(new SoftwareVendorOption(softwareVendor));
		}
		softwareVendorModels.sort(Comparator.comparing(SoftwareVendorOption::getName, String.CASE_INSENSITIVE_ORDER));
		options.setSoftwareVendors(softwareVendorModels);

		// get available software products
		if (StringUtils.isNotBlank(selection.getSoftwareVendorId())) {
			SoftwareProductSearchParams params = new SoftwareProductSearchParams();
			params.addCompanyIds(getAuthenticatedUser().getCompanyId());
			params.setEnabled(true);
			params.addSoftwareVendorIds(selection.getSoftwareVendorId());
			List<SoftwareProduct> softwareProducts = softwareProductService.getSoftwareProductRepository()
			        .findSoftwareProducts(params);
			List<SoftwareProductOption> productOptions = new ArrayList<>(softwareProducts.size());
			for (SoftwareProduct softwareProduct : softwareProducts) {
				productOptions.add(new SoftwareProductOption(softwareProduct));
			}
			productOptions.sort(Comparator.comparing(SoftwareProductOption::getName, String.CASE_INSENSITIVE_ORDER));
			options.setSoftwareProducts(productOptions);
		}

		// get available device products
		if (StringUtils.isNotBlank(selection.getManufacturerId()) && selection.getDeviceCategory() != null) {
			DeviceProductSearchParams params = new DeviceProductSearchParams();
			params.addCompanyIds(getAuthenticatedUser().getCompanyId());
			// params.addDeviceCategoryIds(selection.getCategoryId());
			params.setDeviceCategories(EnumSet.of(selection.getDeviceCategory()));
			params.addDeviceManufacturerIds(selection.getManufacturerId());
			params.setEnabled(true);
			List<DeviceProduct> deviceProducts = deviceProductService.getDeviceProductRepository()
			        .findDeviceProducts(params);
			List<DeviceProductOption> deviceProductModels = new ArrayList<>();
			for (DeviceProduct deviceProduct : deviceProducts) {
				deviceProductModels.add(new DeviceProductOption(deviceProduct));
			}
			deviceProductModels.sort(Comparator.comparing(DeviceProductOption::getName, String.CASE_INSENSITIVE_ORDER));
			options.setProducts(deviceProductModels);
		}

		// get available device types
		if (StringUtils.isNotBlank(selection.getProductId())) {
			DeviceTypeSearchParams params = new DeviceTypeSearchParams();
			params.setEnabled(true);
			params.addDeviceProductIds(selection.getProductId());
			params.addCompanyIds(getAuthenticatedUser().getCompanyId());
			List<DeviceType> rheaDeviceTypes = deviceTypeService.getDeviceTypeRepository().findDeviceTypes(params);
			List<DeviceTypeOption> deviceTypeModels = new ArrayList<>();
			for (DeviceType rheaDeviceType : rheaDeviceTypes) {
				deviceTypeModels.add(new DeviceTypeOption(rheaDeviceType));
			}
			deviceTypeModels.sort(Comparator.comparing(DeviceTypeOption::getName, String.CASE_INSENSITIVE_ORDER));
			options.setDeviceTypes(deviceTypeModels);
		}

		// get available software releases
		if (StringUtils.isNotBlank(selection.getSoftwareProductId())) {
			SoftwareReleaseSearchParams params = new SoftwareReleaseSearchParams();
			params.setEnabled(true);
			params.addCompanyIds(getAuthenticatedUser().getCompanyId());
			params.addSoftwareProductIds(selection.getSoftwareProductId());
			List<SoftwareRelease> softwareReleases = softwareReleaseService.getSoftwareReleaseRepository()
			        .findSoftwareReleases(params);
			List<SoftwareReleaseOption> releaseOptions = new ArrayList<>(softwareReleases.size());
			for (SoftwareRelease softwareRelease : softwareReleases) {
				releaseOptions.add(buildSoftwareReleaseOptionModel(softwareRelease));
			}
			options.setSoftwareReleases(releaseOptions);
		}

		if (StringUtils.isNotBlank(selection.getOwnerEmail())) {
			options.setOwnerEmail(selection.getOwnerEmail());
		}

		// get default software owner name
		if (!StringUtils.isNotBlank(selection.getOwnerName())) {
			options.setOwnerName(getAuthenticatedUser().getRefCompany().getName());
		} else {
			options.setOwnerName(selection.getOwnerName());
		}

		// right to use options
		EnumSet<RightToUseType> rightToUseTypeSet = EnumSet.allOf(RightToUseType.class);
		if (!getAuthenticatedUser().isAdmin())
			rightToUseTypeSet.remove(RightToUseType.Unrestricted);

		options.setRightToUseTypes(rightToUseTypeSet);

		// selection
		options.setSelection(selection);

		return options;
	}

	@PreAuthorize("hasAuthority('RHEA_CREATE_SOFTWARE_RELEASE') or hasAuthority('RHEA_UPDATE_SOFTWARE_RELEASE')")
	@RequestMapping(value = "/filter-options", method = RequestMethod.GET)
	public SoftwareReleaseFilterOptionsModel filterOptions() {

		List<SoftwareProduct> softwareProducts = softwareProductService.getSoftwareProductRepository()
		        .findAllByCompanyIdAndEnabled(getAuthenticatedUser().getCompanyId(), true);
		List<SoftwareProductOption> productOptions = new ArrayList<>(softwareProducts.size());
		for (SoftwareProduct softwareProduct : softwareProducts) {
			productOptions.add(new SoftwareProductOption(softwareProduct));
		}
		productOptions.sort(Comparator.comparing(SoftwareProductOption::getName, String.CASE_INSENSITIVE_ORDER));

		List<DeviceType> deviceTypes = deviceTypeService.getDeviceTypeRepository()
		        .findByCompanyIdAndEnabled(getAuthenticatedUser().getCompanyId(), true);

		List<DeviceTypeOption> deviceTypeOptions = new ArrayList<>(deviceTypes.size());
		for (DeviceType deviceType : deviceTypes) {
			deviceTypeOptions.add(new DeviceTypeOption(deviceType));
		}
		deviceTypeOptions.sort(Comparator.comparing(DeviceTypeOption::getName, String.CASE_INSENSITIVE_ORDER));

		List<SoftwareRelease> softwareReleases = softwareReleaseService.getSoftwareReleaseRepository()
		        .findAllByCompanyIdAndEnabled(getAuthenticatedUser().getCompanyId(), true);
		List<SoftwareReleaseOption> releaseOptions = new ArrayList<>(softwareReleases.size());
		for (SoftwareRelease softwareRelease : softwareReleases) {
			releaseOptions.add(buildSoftwareReleaseOptionModel(softwareRelease));
		}

		return new SoftwareReleaseFilterOptionsModel(productOptions, deviceTypeOptions, releaseOptions);
	}

	private SoftwareReleaseOption buildSoftwareReleaseOptionModel(SoftwareRelease softwareRelease) {
		SoftwareProduct softwareProduct = getRheaCacheService()
		        .findSoftwareProductById(softwareRelease.getSoftwareProductId());
		return new SoftwareReleaseOption(softwareRelease, softwareProduct.getName());
	}
}