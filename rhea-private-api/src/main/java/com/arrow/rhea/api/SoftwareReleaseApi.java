package com.arrow.rhea.api;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.client.model.RightToUseType;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.rhea.client.model.SoftwareReleaseModel;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.repo.SoftwareReleaseSearchParams;

@RestController(value = "privateRheaSoftwareReleaseApi")
@RequestMapping("/api/v1/private/rhea/software-releases")
public class SoftwareReleaseApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.POST)
	public SoftwareRelease create(@RequestBody(required = false) SoftwareReleaseModel body) {
		SoftwareReleaseModel model = JsonUtils.fromJson(getApiPayload(), SoftwareReleaseModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "SoftwareRelease is null");
		Assert.hasText(model.getModel().getCompanyId(), "companyId is empty");
		Assert.hasText(model.getModel().getSoftwareProductId(), "softwareProductId is empty");
		Assert.notNull(model.getModel().getMinor(), "minor is null");
		Assert.notNull(model.getModel().getMajor(), "major is null");
		Assert.notNull(model.getModel().getRtuType(), "rtuType is null");

		return getSoftwareReleaseService().create(model.getModel(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public SoftwareRelease update(@RequestBody(required = false) SoftwareReleaseModel body) {
		SoftwareReleaseModel model = JsonUtils.fromJson(getApiPayload(), SoftwareReleaseModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		SoftwareRelease release = model.getModel();
		Assert.notNull(release, "SoftwareRelease is null");
		Assert.hasText(release.getCompanyId(), "companyId is empty");
		Assert.hasText(release.getSoftwareProductId(), "softwareProductId is empty");
		Assert.notNull(release.getMinor(), "minor is null");
		Assert.notNull(release.getMajor(), "major is null");
		Assert.notNull(model.getModel().getRtuType(), "rtuType is null");
		Assert.notNull(release.getId(), "id is null");

		SoftwareRelease softwareRelease = getSoftwareReleaseService().getSoftwareReleaseRepository()
				.findById(release.getId()).orElse(null);
		Assert.notNull(softwareRelease, "softwareRelease is not found");
		softwareRelease.setBuild(release.getBuild());
		softwareRelease.setCompanyId(release.getCompanyId());
		softwareRelease.setDeviceTypeIds(release.getDeviceTypeIds());
		softwareRelease.setEnabled(release.isEnabled());
		softwareRelease.setMajor(release.getMajor());
		softwareRelease.setMinor(release.getMinor());
		softwareRelease.setNoLongerSupported(release.isNoLongerSupported());
		softwareRelease.setSoftwareProductId(release.getSoftwareProductId());
		softwareRelease.setUpgradeableFromIds(release.getUpgradeableFromIds());
		softwareRelease.setUrl(release.getUrl());
		softwareRelease.setOwnerEmail(release.getOwnerEmail());
		softwareRelease.setOwnerName(release.getOwnerName());

		return getSoftwareReleaseService().update(softwareRelease, model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<SoftwareRelease> findAllBy(@RequestParam(name = "companyId", required = false) Set<String> companyIds,
			@RequestParam(name = "softwareProductId", required = false) Set<String> softwareProductIds,
			@RequestParam(name = "deviceTypeId", required = false) Set<String> deviceTypeIds,
			@RequestParam(name = "rightToUseTypes", required = false) Set<String> rightToUseTypes,
			@RequestParam(name = "enabled", required = false) Boolean enabled,
			@RequestParam(name = "noLongerSupported", required = false) Boolean noLongerSupported,
			@RequestParam(name = "upgradeableFromId", required = false) Set<String> upgradeableFromIds) {
		SoftwareReleaseSearchParams params = new SoftwareReleaseSearchParams();
		if (companyIds != null) {
			companyIds.forEach(params::addCompanyIds);
		}
		if (softwareProductIds != null) {
			softwareProductIds.forEach(params::addSoftwareProductIds);
		}
		if (deviceTypeIds != null) {
			deviceTypeIds.forEach(params::addDeviceTypeIds);
		}
		if (rightToUseTypes != null) {
			EnumSet<RightToUseType> rtuTypes = EnumSet.noneOf(RightToUseType.class);
			for (String rightToUseType : rightToUseTypes) {
				rtuTypes.add(RightToUseType.valueOf(rightToUseType));
			}
			params.setRightToUseTypes(rtuTypes);
		}
		params.setEnabled(enabled);
		params.setNoLongerSupported(noLongerSupported);
		if (upgradeableFromIds != null) {
			upgradeableFromIds.forEach(params::addUpgradeableFromIds);
		}
		return getSoftwareReleaseService().getSoftwareReleaseRepository().findSoftwareReleases(params);
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public SoftwareRelease findById(@PathVariable(name = "id", required = true) String id) {
		return getSoftwareReleaseService().getSoftwareReleaseRepository().findById(id).orElse(null);
	}

	@RequestMapping(path = "/pages", method = RequestMethod.GET)
	public PagingResultModel<SoftwareRelease> findAllBy(
			@RequestParam(name = "companyId", required = false) Set<String> companyIds,
			@RequestParam(name = "softwareProductId", required = false) Set<String> softwareProductIds,
			@RequestParam(name = "deviceTypeId", required = false) Set<String> deviceTypeIds,
			@RequestParam(name = "rightToUseTypes", required = false) Set<String> rightToUseTypes,
			@RequestParam(name = "enabled", required = false) Boolean enabled,
			@RequestParam(name = "noLongerSupported", required = false) Boolean noLongerSupported,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "sortDirection", required = false) String[] sortDirection,
			@RequestParam(name = "sortProperty", required = false) String[] sortProperty) {
		Assert.isTrue(
				sortDirection == null && sortProperty == null
						|| sortDirection != null && sortProperty != null && sortDirection.length == sortProperty.length,
				"invalid sort options");
		PageRequest pageRequest = null;
		if (sortDirection != null) {
			List<Order> orders = new ArrayList<>();
			for (int i = 0; i < sortDirection.length; i++) {
				orders.add(new Order(Direction.fromString(sortDirection[i]), sortProperty[i]));
			}
			pageRequest = PageRequest.of(page, size, Sort.by(orders));
		} else {
			pageRequest = PageRequest.of(page, size);
		}
		SoftwareReleaseSearchParams params = new SoftwareReleaseSearchParams();
		if (companyIds != null) {
			companyIds.forEach(params::addCompanyIds);
		}
		if (softwareProductIds != null) {
			softwareProductIds.forEach(params::addSoftwareProductIds);
		}
		if (deviceTypeIds != null) {
			deviceTypeIds.forEach(params::addDeviceTypeIds);
		}
		if (rightToUseTypes != null) {
			EnumSet<RightToUseType> rtuTypes = EnumSet.noneOf(RightToUseType.class);
			for (String rightToUseType : rightToUseTypes) {
				rtuTypes.add(RightToUseType.valueOf(rightToUseType));
			}
			params.setRightToUseTypes(rtuTypes);
		}
		params.setEnabled(enabled);
		params.setNoLongerSupported(noLongerSupported);
		Page<SoftwareRelease> softwareReleases = getSoftwareReleaseService().getSoftwareReleaseRepository()
				.findSoftwareReleases(pageRequest, params);
		PagingResultModel<SoftwareRelease> result = new PagingResultModel<SoftwareRelease>()
				.withPage(softwareReleases.getNumber()).withTotalPages(softwareReleases.getTotalPages())
				.withTotalSize(softwareReleases.getTotalElements());
		result.withData(softwareReleases.getContent()).withSize(softwareReleases.getSize());
		return result;
	}
}
