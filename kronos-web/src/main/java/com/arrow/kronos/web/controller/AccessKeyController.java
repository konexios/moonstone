package com.arrow.kronos.web.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.GatewayService;
import com.arrow.kronos.service.NodeService;
import com.arrow.kronos.web.model.AccessKeyModels;
import com.arrow.kronos.web.model.DeviceModels;
import com.arrow.kronos.web.model.GatewayModels;
import com.arrow.kronos.web.model.NodeModels;
import com.arrow.kronos.web.model.SearchFilterModels;
import com.arrow.kronos.web.model.SearchFilterModels.AccessKeySearchFilterModel;
import com.arrow.kronos.web.model.SearchResultModels;
import com.arrow.pegasus.client.api.ClientAccessKeyApi;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege;
import com.arrow.pegasus.data.AccessPrivilege.AccessLevel;
import com.arrow.pegasus.data.PriInfo;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.repo.params.AccessKeySearchParams;
import com.arrow.pegasus.security.CryptoClient128Impl;
import com.arrow.pegasus.security.CryptoClientImpl;

@RestController
@RequestMapping("/api/kronos/accesskey")
public class AccessKeyController extends BaseControllerAbstract {

	@Autowired
	private ClientAccessKeyApi clientAccessKeyApi;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private NodeService nodeService;

	@PreAuthorize("hasAuthority('KRONOS_VIEW_ACCESS_KEYS')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.AccessKeySearchResultModel list(@RequestBody AccessKeySearchFilterModel searchFilter,
			HttpSession session) {
		Application application = getApplication(session);

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				Sort.by(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		AccessKeySearchParams params = new AccessKeySearchParams();
		params.addApplicationIds(application.getId());
		if (StringUtils.isNotEmpty(searchFilter.getName())) {
			params.setName(searchFilter.getName());
		}
		params.addAccessLevels(searchFilter.getAccessLevels());
		params.addPri(searchFilter.getPri());
		if (searchFilter.getExpirationDateFrom() != null) {
			params.setExpirationDateFrom(Instant.ofEpochMilli(searchFilter.getExpirationDateFrom()));
		}
		if (searchFilter.getExpirationDateTo() != null) {
			params.setExpirationDateTo(Instant.ofEpochMilli(searchFilter.getExpirationDateTo()));
		}

		// lookup
		Page<AccessKey> accessKeyPage = clientAccessKeyApi.findAccessKeys(pageRequest, params);

		// convert to visual model
		List<AccessKeyModels.AccessKeyList> accessKeyModels = new ArrayList<>();
		// map of nodes to build node path
		Map<String, Node> nodesMap = findAllNodes(application);
		String ownerDisplayName;

		for (AccessKey accessKey : accessKeyPage) {
			ownerDisplayName = "None";
			for (AccessPrivilege privilege : accessKey.getPrivileges()) {
				if (privilege.getLevel() == AccessLevel.OWNER) {
					AccessKeyModels.AccessPrivilegeModel privilegeModel = buildAccessPrivilegeModel(privilege,
							application, nodesMap);
					if (privilegeModel.getDevice() != null) {
						ownerDisplayName = "Device: " + privilegeModel.getDevice().getName();
					} else if (privilegeModel.getGateway() != null) {
						ownerDisplayName = "Gateway: " + privilegeModel.getGateway().getName();
					} else if (privilegeModel.getNode() != null) {
						ownerDisplayName = "Group: " + privilegeModel.getNode().getName();
					} else {
						ownerDisplayName = privilegeModel.getPri();
					}
					break;
				}
			}
			String rawApiKey = getCryptoService().decrypt(accessKey.isOwner(application) ? null : application.getId(),
					accessKey.getEncryptedApiKey());
			AccessKeyModels.AccessKeyList model = new AccessKeyModels.AccessKeyList(accessKey, ownerDisplayName,
					rawApiKey.substring(0, 10 < rawApiKey.length() ? 10 : rawApiKey.length()) + "...");
			accessKeyModels.add(model);
		}
		Page<AccessKeyModels.AccessKeyList> result = new PageImpl<>(accessKeyModels, pageRequest,
				accessKeyPage.getTotalElements());

		return new SearchResultModels.AccessKeySearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_ACCESS_KEYS')")
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public SearchFilterModels.AccessKeyOptions options(HttpSession session) {
		String applicationId = getApplicationId(session);

		List<Device> devices = deviceService.getDeviceRepository().findAllByApplicationIdAndEnabled(applicationId,
				true);
		List<Gateway> gateways = gatewayService.getGatewayRepository().findAllByApplicationIdAndEnabled(applicationId,
				true);
		List<Node> nodes = nodeService.getNodeRepository().findByApplicationIdAndEnabled(applicationId, true);

		return new SearchFilterModels.AccessKeyOptions(devices, gateways, nodes);
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_ACCESS_KEYS')")
	@RequestMapping(value = "/{accessKeyId}", method = RequestMethod.GET)
	public AccessKeyModels.AccessKeyModel findOne(@PathVariable String accessKeyId, HttpSession session) {
		Application application = getApplication(session);

		AccessKey accessKey = getCoreCacheService().findAccessKeyById(accessKeyId);
		Assert.notNull(accessKey, "Access key is not found");
		Assert.isTrue(application.getId().equals(accessKey.getApplicationId()),
				"Access key and user application ids must be the same");

		return buildAccessKeyModel(accessKey, application, findAllNodes(application));
	}

	// Temporarily remove ability to add and modify access key until the logic
	// is re-designed
	/*
	 * @PreAuthorize("hasAuthority('KRONOS_CREATE_ACCESS_KEY')")
	 * 
	 * @RequestMapping(method = RequestMethod.POST) public
	 * AccessKeyModels.AccessKeyModel create(@RequestBody
	 * AccessKeyModels.AccessKeyModel model, HttpSession session) { Application
	 * application = getApplication(session);
	 * 
	 * List<AccessPrivilege> privileges = getPrivilegesFromModel(model,
	 * application);
	 * 
	 * // create and persist AccessKey accessKey =
	 * clientAccessKeyApi.create(application.getCompanyId(), application.getId(),
	 * model.getName(), Instant.ofEpochMilli(model.getExpiration()), privileges,
	 * getUserId());
	 * 
	 * return buildAccessKeyModel(accessKey, application,
	 * findAllNodes(application)); }
	 * 
	 * @PreAuthorize("hasAuthority('KRONOS_EDIT_ACCESS_KEY')")
	 * 
	 * @RequestMapping(value = "/{accessKeyId}", method = RequestMethod.PUT) public
	 * AccessKeyModels.AccessKeyModel save(@PathVariable String accessKeyId,
	 * 
	 * @RequestBody AccessKeyModels.AccessKeyModel model, HttpSession session) {
	 * Application application = getApplication(session);
	 * 
	 * AccessKey accessKey = clientAccessKeyApi.findById(accessKeyId);
	 * Assert.notNull(accessKey, "Access key is not found");
	 * Assert.isTrue(application.getId().equals(accessKey.getApplicationId()),
	 * "Access key and user application ids must be the same");
	 * 
	 * List<AccessPrivilege> privileges = accessKey.getPrivileges();
	 * privileges.removeIf((privilege) -> privilege.getLevel() !=
	 * AccessLevel.OWNER); privileges.addAll(getPrivilegesFromModel(model,
	 * application));
	 * 
	 * accessKey.setName(model.getName());
	 * accessKey.setExpiration(Instant.ofEpochMilli(model.getExpiration()));
	 * accessKey.setPrivileges(privileges);
	 * 
	 * // persist accessKey = clientAccessKeyApi.update(accessKey, getUserId());
	 * 
	 * return buildAccessKeyModel(accessKey, application,
	 * findAllNodes(application)); }
	 * 
	 */
	private AccessKeyModels.AccessKeyModel buildAccessKeyModel(AccessKey accessKey, Application application,
			Map<String, Node> nodesMap) {
		CryptoClientImpl cryptoClient256 = new CryptoClientImpl();
		CryptoClient128Impl cryptoClient128 = new CryptoClient128Impl();

		AccessKeyModels.AccessKeyModel model = new AccessKeyModels.AccessKeyModel(accessKey);

		String rawApiKey = getCryptoService().decrypt(accessKey.isOwner(application) ? null : application.getId(),
				accessKey.getEncryptedApiKey());
		String rawSecretKey = getCryptoService().decrypt(accessKey.isOwner(application) ? null : application.getId(),
				accessKey.getEncryptedSecretKey());
		model.setRawApiKey(rawApiKey);
		model.setRawSecretKey(rawSecretKey);
		model.setAes256ApiKey(cryptoClient256.internalEncrypt(rawApiKey));
		model.setAes256SecretKey(cryptoClient256.internalEncrypt(rawSecretKey));
		model.setAes128ApiKey(cryptoClient128.internalEncrypt(rawApiKey));
		model.setAes128SecretKey(cryptoClient128.internalEncrypt(rawSecretKey));

		for (AccessPrivilege accessPrivilege : accessKey.getPrivileges()) {
			model.getPrivileges().add(buildAccessPrivilegeModel(accessPrivilege, application, nodesMap));
		}
		return model;
	}

	private AccessKeyModels.AccessPrivilegeModel buildAccessPrivilegeModel(AccessPrivilege accessPrivilege,
			Application application, Map<String, Node> nodesMap) {
		String method = "buildAccessPrivilegeModel";

		// objects for testing PRI
		Device deviceSample = new Device();
		Gateway gatewaySample = new Gateway();
		Node nodeSample = new Node();

		AccessKeyModels.AccessPrivilegeModel accessPrivilegeModel = new AccessKeyModels.AccessPrivilegeModel(
				accessPrivilege.getLevel(), accessPrivilege.getPri());
		PriInfo priInfo = PriInfo.parse(accessPrivilege.getPri());
		if (priInfo.isOfType(deviceSample)) {
			// Device
			Device device = getKronosCache().findDeviceByHid(priInfo.getId());
			if (device != null) {
				if (application.getId().equals(device.getApplicationId()) && device.isEnabled()) {
					accessPrivilegeModel.setDevice(new DeviceModels.DeviceOption(device));
				} else {
					logWarn(method, "Device %s is for another application or disabled", device.getId());
				}
			} else {
				logError(method, "Failed to find device by hid %s", priInfo.getId());
			}
		} else if (priInfo.isOfType(gatewaySample)) {
			// Gateway
			Gateway gateway = getKronosCache().findGatewayByHid(priInfo.getId());
			if (gateway != null) {
				if (application.getId().equals(gateway.getApplicationId()) && gateway.isEnabled()) {
					accessPrivilegeModel.setGateway(new GatewayModels.GatewayOption(gateway));
				} else {
					logWarn(method, "Gateway %s is for another application or disabled", gateway.getId());
				}
			} else {
				logError(method, "Failed to find gateway by hid %s", priInfo.getId());
			}
		} else if (priInfo.isOfType(nodeSample)) {
			// Node
			Node node = nodeService.getNodeRepository().doFindByHid(priInfo.getId());
			if (node != null) {
				if (application.getId().equals(node.getApplicationId()) && node.isEnabled()) {
					accessPrivilegeModel.setNode(
							new NodeModels.NodeOption(node, NodeModels.NodeOption.getNodePath(node, nodesMap)));
				} else {
					logWarn(method, "Node %s is for another application or disabled", node.getId());
				}
			} else {
				logError(method, "Failed to find node by hid %s", priInfo.getId());
			}
		} else {
			// application or not supported object
			logWarn(method, "PRI is not supported: " + accessPrivilege.getPri());
		}
		return accessPrivilegeModel;
	}

	List<AccessPrivilege> getPrivilegesFromModel(AccessKeyModels.AccessKeyModel model, Application application) {
		String method = "getPrivilegesFromModel";

		List<AccessPrivilege> privileges = new ArrayList<>();

		for (AccessKeyModels.AccessPrivilegeModel privilegeModel : model.getPrivileges()) {
			// OWNER privileges cannot be set or deleted
			if (privilegeModel.getLevel() != AccessLevel.OWNER) {
				if (privilegeModel.getDevice() != null) {
					Device device = getKronosCache().findDeviceById(privilegeModel.getDevice().getId());
					if (device != null && application.getId().equals(device.getApplicationId()) && device.isEnabled()) {
						privileges.add(new AccessPrivilege(device.getPri(), privilegeModel.getLevel()));
					} else {
						logError(method, "Failed to add access privilege for device %s",
								privilegeModel.getDevice().getId());
					}
				} else if (privilegeModel.getGateway() != null) {
					Gateway gateway = getKronosCache().findGatewayById(privilegeModel.getGateway().getId());
					if (gateway != null && application.getId().equals(gateway.getApplicationId())
							&& gateway.isEnabled()) {
						privileges.add(new AccessPrivilege(gateway.getPri(), privilegeModel.getLevel()));
					} else {
						logError(method, "Failed to add access privilege for gateway %s",
								privilegeModel.getDevice().getId());
					}
				} else if (privilegeModel.getNode() != null) {
					Node node = nodeService.getNodeRepository().findById(privilegeModel.getNode().getId()).orElse(null);
					if (node != null && application.getId().equals(node.getApplicationId()) && node.isEnabled()) {
						privileges.add(new AccessPrivilege(node.getPri(), privilegeModel.getLevel()));
					} else {
						logError(method, "Failed to add access privilege for node %s",
								privilegeModel.getDevice().getId());
					}
				} else if (StringUtils.isNotEmpty(privilegeModel.getPri())) {
					privileges.add(new AccessPrivilege(privilegeModel.getPri(), privilegeModel.getLevel()));
				} else {
					logError(method, "Failed to add access privilege, object and PRI are not defined");
				}
			}
		}
		return privileges;
	}

	private Map<String, Node> findAllNodes(Application application) {
		Map<String, Node> nodesMap = new HashMap<>();
		List<Node> nodes = nodeService.getNodeRepository().findByApplicationIdAndEnabled(application.getId(), true);
		for (Node n : nodes) {
			nodesMap.put(n.getId(), n);
		}
		return nodesMap;
	}
}
