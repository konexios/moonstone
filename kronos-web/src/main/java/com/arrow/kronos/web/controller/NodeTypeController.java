package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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

import com.arrow.kronos.data.NodeType;
import com.arrow.kronos.repo.KronosDocumentSearchParams;
import com.arrow.kronos.service.NodeTypeService;
import com.arrow.kronos.web.model.NodeTypeModels.DeviceCategoryModel;
import com.arrow.kronos.web.model.NodeTypeModels.NodeTypeModel;
import com.arrow.kronos.web.model.SearchFilterModels.KronosSearchFilterModel;
import com.arrow.kronos.web.model.SearchResultModels.NodeTypeSearchResultModel;
import com.arrow.pegasus.data.profile.User;
import com.arrow.rhea.client.api.ClientDeviceCategoryApi;
import com.arrow.rhea.data.DeviceCategory;

@RestController
@RequestMapping("/api/kronos/nodetype")
public class NodeTypeController extends BaseControllerAbstract {

	@Autowired
	private NodeTypeService nodeTypeService;
	@Autowired
	ClientDeviceCategoryApi clientDeviceCategoryApi;

	@PreAuthorize("hasAuthority('KRONOS_VIEW_NODE_TYPES')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public NodeTypeSearchResultModel list(@RequestBody KronosSearchFilterModel searchFilter, HttpSession session) {

		// sorting & paging
		PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
		        new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		KronosDocumentSearchParams params = new KronosDocumentSearchParams();
		params.addApplicationIds(getApplicationId(session));
		// user defined filter
		params.setEnabled(searchFilter.isEnabled());

		Page<NodeType> nodeTypes = nodeTypeService.getNodeTypeRepository().findNodeTypes(pageRequest, params);

		// convert to visual model
		List<NodeTypeModel> nodeTypeModels = new ArrayList<>();
		for (NodeType nodeType : nodeTypes) {
			NodeTypeModel model = new NodeTypeModel(nodeType);
			User currentUser = getCoreCacheService().findUserById(model.getLastModifiedBy());
			if (currentUser != null) {
				model.setLastModifiedBy(getKronosModelUtil().populateDecryptedLogin(currentUser));
			} else if (!model.getLastModifiedBy().equals("admin")) {
				model.setLastModifiedBy("Unknown");
			}
			// if (nodeType.getDeviceCategoryId() != null) {
			// model.setCategory(buildDeviceCategoryModel(nodeType.getDeviceCategoryId()));
			// }
			nodeTypeModels.add(model);
		}
		Page<NodeTypeModel> result = new PageImpl<>(nodeTypeModels, pageRequest, nodeTypes.getTotalElements());

		return new NodeTypeSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_NODE_TYPE')")
	@RequestMapping(method = RequestMethod.POST)
	public NodeTypeModel add(@RequestBody NodeTypeModel nodeTypeModel, HttpSession session) {

		NodeType nodeType = new NodeType();
		nodeType.setApplicationId(getApplicationId(session)); // mandatory
		nodeType.setName(nodeTypeModel.getName());
		nodeType.setDescription(nodeTypeModel.getDescription());
		// nodeType.setDeviceCategoryId(nodeTypeModel.getCategory().getId());
		nodeType.setEnabled(nodeTypeModel.isEnabled());

		nodeType = nodeTypeService.create(nodeType, getUserId());

		// return new
		// NodeTypeModel(nodeType).withCategory(buildDeviceCategoryModel(nodeType.getDeviceCategoryId()));
		return new NodeTypeModel(nodeType);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_NODE_TYPE')")
	@RequestMapping(value = "/{nodeTypeId}", method = RequestMethod.PUT)
	public NodeTypeModel edit(@PathVariable String nodeTypeId, @RequestBody NodeTypeModel nodeTypeModel,
	        HttpSession session) {

		NodeType nodeType = getKronosCache().findNodeTypeById(nodeTypeId);
		Assert.notNull(nodeType, "Node Type is null");
		// make sure the user and node type have the same application id
		Assert.isTrue(getApplicationId(session).equals(nodeType.getApplicationId()),
		        "user and node type must have the same application id");

		nodeType.setName(nodeTypeModel.getName());
		nodeType.setDescription(nodeTypeModel.getDescription());
		// nodeType.setDeviceCategoryId(nodeTypeModel.getCategory().getId());
		nodeType.setEnabled(nodeTypeModel.isEnabled());

		nodeType = nodeTypeService.update(nodeType, getUserId());

		// return new
		// NodeTypeModel(nodeType).withCategory(buildDeviceCategoryModel(nodeType.getDeviceCategoryId()));
		return new NodeTypeModel(nodeType);
	}

	// private DeviceCategoryModel buildDeviceCategoryModel(String
	// deviceCategoryId) {
	// DeviceCategory category =
	// clientDeviceCategoryApi.findById(deviceCategoryId);
	// Assert.notNull(category, "device category is null");
	// return new DeviceCategoryModel(category);
	// }

	@PreAuthorize("hasAuthority('KRONOS_VIEW_NODE_TYPES')")
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public List<DeviceCategoryModel> options() {
		List<DeviceCategoryModel> categories = new ArrayList<>();
		for (DeviceCategory category : clientDeviceCategoryApi.findAll()) {
			categories.add(new DeviceCategoryModel(category));
		}
		return categories;
	}
}
