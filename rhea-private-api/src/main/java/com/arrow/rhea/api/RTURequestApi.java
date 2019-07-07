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

import com.arrow.acn.client.model.RightToUseStatus;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.rhea.client.model.RTURequestModel;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.repo.RTURequestSearchParams;

@RestController(value = "privateRheaRTURequestApi")
@RequestMapping("/api/v1/private/rhea/rtu-requests")
public class RTURequestApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.POST)
	public RTURequest create(@RequestBody(required = false) RTURequestModel body) {
		RTURequestModel model = JsonUtils.fromJson(getApiPayload(), RTURequestModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "RTURequest is null");
		Assert.hasText(model.getModel().getCompanyId(), "companyId is empty");
		Assert.hasText(model.getModel().getFirmwareVersion(), "firmwareVersion is null");
		Assert.hasText(model.getModel().getSoftwareReleaseId(), "softwareReleaseId is null");

		// check, if there is already rtu request with
		// companyId+softwareReleaseId combination
		RTURequest checkRequest = getRTURequestService().getRTURequestRepository().findByCompanyIdAndSoftwareReleaseId(
				model.getModel().getCompanyId(), model.getModel().getSoftwareReleaseId());
		if (checkRequest != null)
			return null;

		model.getModel().setStatus(RightToUseStatus.Requested);
		return getRTURequestService().create(model.getModel(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public RTURequest update(@RequestBody(required = false) RTURequestModel body) {
		RTURequestModel model = JsonUtils.fromJson(getApiPayload(), RTURequestModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		RTURequest rtuRequest = model.getModel();
		Assert.notNull(rtuRequest, "RTURequest is null");
		Assert.hasText(rtuRequest.getCompanyId(), "companyId is empty");
		Assert.hasText(rtuRequest.getSoftwareReleaseId(), "softwareReleaseId is empty");
		Assert.notNull(rtuRequest.getFirmwareVersion(), "firmwareVersion is null");
		Assert.notNull(model.getModel().getStatus(), "status is null");
		Assert.notNull(rtuRequest.getId(), "id is null");

		RTURequest request = getRTURequestService().getRTURequestRepository().findById(rtuRequest.getId()).orElse(null);
		Assert.notNull(request, "rtuRequest is not found");
		request.setCompanyId(rtuRequest.getCompanyId());
		request.setFirmwareVersion(rtuRequest.getFirmwareVersion());
		request.setSoftwareReleaseId(rtuRequest.getSoftwareReleaseId());
		request.setStatus(rtuRequest.getStatus());

		return getRTURequestService().update(request, model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<RTURequest> findAllBy(@RequestParam(name = "companyIds", required = false) Set<String> companyIds,
			@RequestParam(name = "statuses", required = false) Set<String> statuses,
			@RequestParam(name = "softwareReleaseIds", required = false) Set<String> softwareReleaseIds) {
		RTURequestSearchParams params = new RTURequestSearchParams();
		if (companyIds != null) {
			companyIds.forEach(params::addCompanyIds);
		}
		if (statuses != null) {
			EnumSet<RightToUseStatus> rtuStatuses = EnumSet.noneOf(RightToUseStatus.class);
			for (String rtuStatus : statuses) {
				rtuStatuses.add(RightToUseStatus.valueOf(rtuStatus));
			}
			params.setStatuses(rtuStatuses);
		}
		if (softwareReleaseIds != null) {
			softwareReleaseIds.forEach(params::addSoftwareReleaseIds);
		}
		return getRTURequestService().getRTURequestRepository().findRTURequests(params);
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public RTURequest findById(@PathVariable(name = "id", required = true) String id) {
		return getRTURequestService().getRTURequestRepository().findById(id).orElse(null);
	}

	@RequestMapping(path = "/pages", method = RequestMethod.GET)
	public PagingResultModel<RTURequest> findAllBy(
			@RequestParam(name = "companyIds", required = false) Set<String> companyIds,
			@RequestParam(name = "statuses", required = false) Set<String> statuses,
			@RequestParam(name = "softwareReleaseIds", required = false) Set<String> softwareReleaseIds,
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
		RTURequestSearchParams params = new RTURequestSearchParams();
		if (companyIds != null) {
			companyIds.forEach(params::addCompanyIds);
		}
		if (softwareReleaseIds != null) {
			softwareReleaseIds.forEach(params::addSoftwareReleaseIds);
		}
		if (statuses != null) {
			EnumSet<RightToUseStatus> rtuStatuses = EnumSet.noneOf(RightToUseStatus.class);
			for (String rtuStatus : statuses) {
				rtuStatuses.add(RightToUseStatus.valueOf(rtuStatus));
			}
			params.setStatuses(rtuStatuses);
		}
		Page<RTURequest> rtuRequests = getRTURequestService().getRTURequestRepository().findRTURequests(pageRequest,
				params);
		PagingResultModel<RTURequest> result = new PagingResultModel<RTURequest>().withPage(rtuRequests.getNumber())
				.withTotalPages(rtuRequests.getTotalPages()).withTotalSize(rtuRequests.getTotalElements());
		result.withData(rtuRequests.getContent()).withSize(rtuRequests.getSize());
		return result;
	}
}
