package com.arrow.rhea.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

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

import com.arrow.acn.client.model.RightToUseChangeStatus;
import com.arrow.acn.client.model.RightToUseStatus;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.webapi.data.CoreCompanyModels.CompanyOption;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.repo.RTURequestSearchParams;
import com.arrow.rhea.repo.SoftwareReleaseSearchParams;
import com.arrow.rhea.service.RTURequestService;
import com.arrow.rhea.service.SoftwareReleaseService;
import com.arrow.rhea.web.model.RTURequestModels.RTURequestFilterOptionsModel;
import com.arrow.rhea.web.model.RTURequestModels.RTURequestModel;
import com.arrow.rhea.web.model.SearchFilterModels.RTURequestSearchFilterModel;
import com.arrow.rhea.web.model.SearchResultModels.RTURequestSearchResultModel;

@RestController
@RequestMapping("/api/rhea/rtu-requests")
public class RTURequestController extends ControllerAbstract {

	@Autowired
	private SoftwareReleaseService softwareReleaseService;
	@Autowired
	private RTURequestService rtuRequestService;

	@PreAuthorize("hasAuthority('RHEA_READ_RTU_REQUEST')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public RTURequestSearchResultModel find(@RequestBody RTURequestSearchFilterModel searchFilter) {

		String method = "find";

		// lookup all software releases for logged in user's tenant (companyId)
		SoftwareReleaseSearchParams softwareReleaseSearchParams = new SoftwareReleaseSearchParams();
		softwareReleaseSearchParams.addCompanyIds(getAuthenticatedUser().getCompanyId());
		List<SoftwareRelease> softwareReleases = softwareReleaseService.getSoftwareReleaseRepository()
		        .findSoftwareReleases(softwareReleaseSearchParams);

		List<String> softwareReleaseIds = new ArrayList<String>();
		for (SoftwareRelease sr : softwareReleases)
			softwareReleaseIds.add(sr.getId());

		logDebug(method, "softwareReleases: %s, softwareReleaseIds: %s", softwareReleases.size(),
		        softwareReleaseIds.size());

		// sorting & paging
		PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
		        new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));
		
		List<RTURequestModel> rtuRequestModels = new ArrayList<>();
		long totalElements = 0;
		
		if (!softwareReleaseIds.isEmpty()) {

			RTURequestSearchParams params = new RTURequestSearchParams();

			params.addCompanyIds(searchFilter.getCompanyIds());
			params.addSoftwareReleaseIds(softwareReleaseIds.toArray(new String[softwareReleaseIds.size()]));
			if (searchFilter.getStatuses() != null && searchFilter.getStatuses().length > 0) {
				EnumSet<RightToUseStatus> rtuStatusSet = EnumSet.noneOf(RightToUseStatus.class);
				for (String name : searchFilter.getStatuses())
					rtuStatusSet.add(RightToUseStatus.valueOf(name));
				params.setStatuses(rtuStatusSet);
			}

			Page<RTURequest> rtuRequests = rtuRequestService.getRTURequestRepository().findRTURequests(pageRequest,
			        params);

			// convert to visual model
			for (RTURequest rtuRequest : rtuRequests) {
				rtuRequestModels.add(new RTURequestModel(rtuRequestService.populateRefs(rtuRequest))
				        .withChangeStatuses(getAvailableChangeStatuses(rtuRequest.getStatus())));
			}
			
			totalElements = rtuRequests.getTotalElements();
		}

		Page<RTURequestModel> result = new PageImpl<RTURequestModel>(rtuRequestModels, pageRequest, totalElements);

		return new RTURequestSearchResultModel(result, searchFilter);
	}

	private EnumSet<RightToUseChangeStatus> getAvailableChangeStatuses(RightToUseStatus status) {
		EnumSet<RightToUseChangeStatus> changeStatuses = EnumSet.noneOf(RightToUseChangeStatus.class);
		switch (status) {
		case Requested:
			changeStatuses.add(RightToUseChangeStatus.Decline);
			changeStatuses.add(RightToUseChangeStatus.Approve);
			break;
		case Approved:
			changeStatuses.add(RightToUseChangeStatus.Revoke);
			break;
		case Declined:
		case Revoked:
			changeStatuses.add(RightToUseChangeStatus.Approve);
			break;
		default:
			break;
		}
		return changeStatuses;
	}

	@PreAuthorize("hasAuthority('RHEA_UPDATE_RTU_REQUEST')")
	@RequestMapping(value = "/{rtuRequestId}", method = RequestMethod.PUT)
	public RTURequestModel update(@PathVariable String rtuRequestId, @RequestBody String changeStatus) {
		Assert.hasText(rtuRequestId, "rtuRequestId is empty");
		Assert.hasText(changeStatus, "changeStatus is null");

		RTURequest rtuRequest = getRheaCacheService().findRTURequestById(rtuRequestId);
		Assert.notNull(rtuRequest, "rtuRequest is null");

		rtuRequest.setStatus(getRTUStatus(changeStatus));

		rtuRequest = rtuRequestService.update(rtuRequest, getUserId());

		return new RTURequestModel(rtuRequest).withChangeStatuses(getAvailableChangeStatuses(rtuRequest.getStatus()));
	}

	private RightToUseStatus getRTUStatus(String changeStatus) {
		RightToUseChangeStatus status = RightToUseChangeStatus.valueOf(changeStatus);
		switch (status) {
		case Approve:
			return RightToUseStatus.Approved;
		case Decline:
			return RightToUseStatus.Declined;
		case Revoke:
			return RightToUseStatus.Revoked;
		default:
			break;
		}
		return null;
	}

	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public RTURequestFilterOptionsModel filterOptions() {

		List<RTURequest> rtuRequests = rtuRequestService.getRTURequestRepository().findAll();

		List<CompanyOption> companyOptions = new ArrayList<>();
		rtuRequests.stream().map(s -> s.getCompanyId()).distinct().forEach(companyId -> {
			Company company = getCoreCacheService().findCompanyById(companyId);
			if (company != null)
				companyOptions.add(new CompanyOption(company));
		});
		companyOptions.sort(Comparator.comparing(CompanyOption::getName, String.CASE_INSENSITIVE_ORDER));

		EnumSet<RightToUseStatus> rtuStatuses = EnumSet.noneOf(RightToUseStatus.class);
		rtuStatuses.addAll(rtuRequests.stream().map(s -> s.getStatus()).distinct().collect(Collectors.toSet()));

		return new RTURequestFilterOptionsModel(companyOptions, rtuStatuses);
	}

}