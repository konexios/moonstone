package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.NodeType;
import com.arrow.kronos.service.NodeTypeService;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.NodeTypeModel;
import moonstone.acn.client.model.NodeTypeRegistrationModel;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.ListResultModel;

@RestController
@RequestMapping("/api/v1/kronos/nodes/types")
public class NodeTypeApi extends BaseApiAbstract {

	@Autowired
	private NodeTypeService nodeTypeService;

	@ApiOperation(value = "list existing node types")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ListResultModel<NodeTypeModel> listNodeTypes() {

		AccessKey accessKey = validateCanReadApplication(getProductSystemName());

		List<NodeType> nodeTypes = nodeTypeService.getNodeTypeRepository()
		        .findByApplicationIdAndEnabled(accessKey.getApplicationId(), true);
		List<NodeTypeModel> data = new ArrayList<>();
		if (nodeTypes != null) {
			nodeTypes.forEach(type -> data.add(buildNodeTypeModel(type)));
		}
		return new ListResultModel<NodeTypeModel>().withData(data).withSize(data.size());
	}

	@ApiOperation(value = "create new node type")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel create(
	        @ApiParam(value = "node type model", required = true) @RequestBody(required = false) NodeTypeRegistrationModel body,
	        HttpServletRequest request) {
		String method = "create";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		AuditLog auditLog = auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		NodeTypeRegistrationModel model = JsonUtils.fromJson(getApiPayload(), NodeTypeRegistrationModel.class);
		Assert.notNull(model, "model is null");
		model.trim();
		Assert.hasText(model.getName(), "name is empty");
		Assert.hasText(model.getDescription(), "description is empty");
		// Assert.hasText(model.getDeviceCategoryHid(), "deviceCategoryHid is
		// empty");

		NodeType nodeType = new NodeType();
		nodeType.setApplicationId(accessKey.getApplicationId());
		nodeType.setDescription(model.getDescription());
		nodeType.setName(model.getName());
		nodeType.setEnabled(model.isEnabled());

		// DeviceCategory deviceCategory =
		// clientCacheApi.findDeviceCategoryByHid(model.getDeviceCategoryHid());
		// Assert.notNull(deviceCategory, "deviceCategory is not found");
		//
		// nodeType.setDeviceCategoryId(deviceCategory.getId());
		nodeType = nodeTypeService.create(nodeType, accessKey.getId());

		auditLog.setObjectId(nodeType.getId());
		getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());

		return new HidModel().withHid(nodeType.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update existing node type")
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel update(@ApiParam(value = "node type hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "node type model", required = true) @RequestBody(required = false) NodeTypeRegistrationModel body,
	        HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		NodeType nodeType = nodeTypeService.getNodeTypeRepository().doFindByHid(hid);
		Assert.notNull(nodeType, "node type does not exist");

		auditLog(method, nodeType.getApplicationId(), nodeType.getId(), accessKey.getId(), request);

		Assert.isTrue(nodeType.getApplicationId().equals(accessKey.getApplicationId()), "applicationId mismatched!");

		NodeTypeRegistrationModel model = JsonUtils.fromJson(getApiPayload(), NodeTypeRegistrationModel.class);
		Assert.notNull(model, "model is null");
		model.trim();

		if (StringUtils.hasText(model.getName())) {
			nodeType.setName(model.getName());
		}
		if (StringUtils.hasText(model.getDescription())) {
			nodeType.setDescription(model.getDescription());
		}
		nodeType.setEnabled(model.isEnabled());
		// if (StringUtils.hasText(model.getDeviceCategoryHid())) {
		// DeviceCategory deviceCategory =
		// clientCacheApi.findDeviceCategoryByHid(model.getDeviceCategoryHid());
		// Assert.notNull(deviceCategory, "deviceCategory is not found");
		// nodeType.setDeviceCategoryId(deviceCategory.getId());
		// }

		nodeType = nodeTypeService.update(nodeType, accessKey.getId());

		return new HidModel().withHid(nodeType.getHid()).withMessage("OK");
	}

	private NodeTypeModel buildNodeTypeModel(NodeType type) {
		Assert.notNull(type, "node type is null");
		NodeTypeModel result = buildModel(new NodeTypeModel(), type);
		// if (StringUtils.hasText(type.getDeviceCategoryId())) {
		// DeviceCategory deviceCategory =
		// clientCacheApi.findDeviceCategoryById(type.getDeviceCategoryId());
		// Assert.notNull(deviceCategory, "deviceCategory is not found");
		// result.setDeviceCategoryHid(deviceCategory.getHid());
		// }
		return result;
	}
}
