package com.arrow.kronos.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.GlobalTag;
import com.arrow.kronos.data.GlobalTagType;
import com.arrow.kronos.repo.GlobalTagRepositoryParams;
import com.arrow.kronos.service.GlobalTagService;
import com.arrow.kronos.util.ApiUtil;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.GlobalTagModel;
import moonstone.acn.client.model.GlobalTagRegistrationModel;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.ListResultModel;
import moonstone.acs.client.model.PagingResultModel;

@RestController
@RequestMapping("/api/v1/kronos/global-tags")
public class GlobalTagApi extends BaseApiAbstract {

	@Autowired
	private GlobalTagService globalTagService;
	@Autowired
	private ApiUtil apiUtil;

	@ApiOperation(value = "find global tag by hid", hidden = true)
	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public GlobalTagModel findByHid(
	        @ApiParam(value = "global hid", required = true) @PathVariable(name = "hid", required = true) String hid) {

		getValidatedAccessKey(ProductSystemNames.KRONOS);
		GlobalTag globalTag = globalTagService.getGlobalTagRepository().doFindByHid(hid);
		return buildGlobalTagModel(globalTag);
	}

	@ApiOperation(value = "find global tags", hidden = true)
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ListResultModel<GlobalTagModel> findBy(
	        @ApiParam(value = "global tag name", required = false) @RequestParam(value = "name", required = false) String name,
	        @ApiParam(value = "object types", required = false) @RequestParam(value = "objectType", required = false) Set<String> objectTypes,
	        @ApiParam(value = "createdDateFrom") @RequestParam(name = "createdDateFrom", required = false) String createdDateFrom,
	        @ApiParam(value = "createdDateTo") @RequestParam(name = "createdDateTo", required = false) String createdDateTo,
	        @ApiParam(value = "sort field") @RequestParam(name = "sortField", required = false, defaultValue = "name") String sortField,
	        @ApiParam(value = "sort direction") @RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection) {

		getValidatedAccessKey(ProductSystemNames.KRONOS);

		Instant from = apiUtil.parseDateParam(createdDateFrom);
		Instant to = apiUtil.parseDateParam(createdDateTo);
		if (from != null && to != null && from.isAfter(to)) {
			throw new AcsLogicalException("createdDateFrom is after createdDateTo");
		}

		GlobalTagRepositoryParams params = new GlobalTagRepositoryParams().addName(name).addCreatedDateFrom(from)
		        .addCreatedDateTo(to);
		if (objectTypes != null) {
			objectTypes.forEach(params::addObjectTypes);
		}
		Sort sort = null;
		if (StringUtils.hasText(sortField)) {
			Direction direction = sortDirection != null ? Direction.valueOf(sortDirection) : Direction.ASC;
			sort = new Sort(direction, sortField);
		}
		List<GlobalTagModel> data = globalTagService.getGlobalTagRepository().findGlobalTags(params, sort).stream()
		        .map(this::buildGlobalTagModel).collect(Collectors.toCollection(ArrayList::new));

		return new ListResultModel<GlobalTagModel>().withData(data).withSize(data.size());
	}

	@ApiOperation(value = "find global tags", hidden = true)
	@RequestMapping(path = "/pages", method = RequestMethod.GET)
	public PagingResultModel<GlobalTagModel> findBy(
	        @ApiParam(value = "global tag name", required = false) @RequestParam(value = "name", required = false) String name,
	        @ApiParam(value = "object types", required = false) @RequestParam(value = "objectType", required = false) Set<String> objectTypes,
	        @ApiParam(value = "createdDateFrom") @RequestParam(name = "createdDateFrom", required = false) String createdDateFrom,
	        @ApiParam(value = "createdDateTo") @RequestParam(name = "createdDateTo", required = false) String createdDateTo,
	        @ApiParam(value = "sort field") @RequestParam(name = "sortField", required = false, defaultValue = "name") String sortField,
	        @ApiParam(value = "sort direction") @RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
	        @ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
	        @ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {

		getValidatedAccessKey(ProductSystemNames.KRONOS);

		size = apiUtil.validateSize(size);
		page = apiUtil.validatePage(page);

		Instant from = apiUtil.parseDateParam(createdDateFrom);
		Instant to = apiUtil.parseDateParam(createdDateTo);
		if (from != null && to != null && from.isAfter(to)) {
			throw new AcsLogicalException("createdDateFrom is after createdDateTo");
		}

		PageRequest pageRequest = apiUtil.buildPageRequest(page, size, sortField, sortDirection);

		GlobalTagRepositoryParams params = new GlobalTagRepositoryParams().addName(name).addCreatedDateFrom(from)
		        .addCreatedDateTo(to);
		if (objectTypes != null) {
			objectTypes.forEach(params::addObjectTypes);
		}
		Page<GlobalTag> globalTags = globalTagService.getGlobalTagRepository().findGlobalTags(pageRequest, params);
		List<GlobalTagModel> data = globalTags.getContent().stream().map(this::buildGlobalTagModel)
		        .collect(Collectors.toCollection(ArrayList::new));

		PagingResultModel<GlobalTagModel> result = new PagingResultModel<>();
		result.setPage(pageRequest.getPageNumber());
		result.setSize(pageRequest.getPageSize());
		result.setTotalPages(globalTags.getTotalPages());
		result.setTotalSize(globalTags.getTotalElements());
		result.setData(data);
		return result;
	}

	@ApiOperation(value = "create global tag", hidden = true)
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel create(
	        @ApiParam(value = "global tag model", required = true) @RequestBody(required = false) GlobalTagRegistrationModel body,
	        HttpServletRequest request) {
		String method = "create";

		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);
		AuditLog auditLog = auditLog(method, accessKey.getId(), request);

		GlobalTagRegistrationModel model = JsonUtils.fromJson(getApiPayload(), GlobalTagRegistrationModel.class);

		GlobalTag globalTag = buildGlobalTag(new GlobalTag(), model);
		globalTag = globalTagService.create(globalTag, accessKey.getId());

		auditLog.setObjectId(globalTag.getId());
		getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());

		return new HidModel().withHid(globalTag.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update global tag", hidden = true)
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel update(
	        @ApiParam(value = "global hid", required = true) @PathVariable(name = "hid", required = true) String hid,
	        @ApiParam(value = "global tag model", required = true) @RequestBody(required = false) GlobalTagRegistrationModel body,
	        HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);

		GlobalTag globalTag = globalTagService.getGlobalTagRepository().doFindByHid(hid);
		Assert.notNull(globalTag, "globalTag is not found");

		GlobalTagRegistrationModel model = JsonUtils.fromJson(getApiPayload(), GlobalTagRegistrationModel.class);
		globalTag = buildGlobalTag(globalTag, model);

		auditLog(method, null, globalTag.getId(), accessKey.getId(), request);

		globalTag = globalTagService.update(globalTag, accessKey.getId());
		return new HidModel().withHid(globalTag.getHid()).withMessage("OK");
	}

	private GlobalTag buildGlobalTag(GlobalTag globalTag, GlobalTagRegistrationModel model) {
		Assert.notNull(model, "model is null");
		if (globalTag == null) {
			globalTag = new GlobalTag();
		}
		Assert.hasText(model.getName(), "name is empty");
		Assert.hasText(model.getTagType(), "tagType is empty");
		// name
		globalTag.setName(model.getName());
		// tagType
		globalTag.setTagType(GlobalTagType.valueOf(model.getTagType()));
		// objectType
		globalTag.setObjectType(model.getObjectType());
		return globalTag;
	}

	private GlobalTagModel buildGlobalTagModel(GlobalTag globalTag) {
		Assert.notNull(globalTag, "globalTag is not found");
		GlobalTagModel result = buildModel(new GlobalTagModel(), globalTag);
		result.setObjectType(globalTag.getObjectType());
		result.setTagType(globalTag.getTagType().name());
		return result;
	}
}
