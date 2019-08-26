package com.arrow.kronos.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerMapping;

import com.arrow.kronos.KronosAuditLog.Api;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestProcedureStep;
import com.arrow.kronos.data.TestResult;
import com.arrow.kronos.data.TestResultStep;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.DeviceTypeService;
import com.arrow.kronos.service.GatewayService;
import com.arrow.kronos.service.KronosCache;
import com.arrow.kronos.service.KronosHttpRequestCache;
import com.arrow.kronos.service.SoftwareReleaseService;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.api.ApiAbstract;
import com.arrow.pegasus.client.api.ClientUserApi;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.DocumentAbstract;
import com.arrow.pegasus.data.TsDocumentAbstract;
import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.AuditLogService;
import com.arrow.rhea.client.api.ClientRTURequestApi;
import com.arrow.rhea.client.api.ClientSoftwareReleaseApi;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;

import moonstone.acn.client.model.AcnDeviceCategory;
import moonstone.acn.client.model.AuditLogModel;
import moonstone.acn.client.model.AvailableFirmwareModel;
import moonstone.acn.client.model.ConfigBackupModel;
import moonstone.acn.client.model.DeviceModel;
import moonstone.acn.client.model.LastLocationModel;
import moonstone.acn.client.model.RightToUseType;
import moonstone.acn.client.model.TestProcedureStepModel;
import moonstone.acn.client.model.TestResultModel;
import moonstone.acn.client.model.TestResultStepModel;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.AuditableDocumentModelAbstract;
import moonstone.acs.client.model.DefinitionModelAbstract;
import moonstone.acs.client.model.ModelAbstract;
import moonstone.acs.client.model.TsModelAbstract;

public abstract class BaseApiAbstract extends ApiAbstract {

	protected final static String SOFTWARE_RELEASE_NAME_FORMAT = "%s %s";

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private ClientUserApi clientUserApi;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private DeviceTypeService deviceTypeService;
	@Autowired
	private KronosCache kronosCache;
	@Autowired
	private KronosHttpRequestCache requestCache;
	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private ClientSoftwareReleaseApi clientSoftwareReleaseApi;
	@Autowired
	private ClientRTURequestApi clientRTURequestApi;
	@Autowired
	private SoftwareReleaseService softwareReleaseService;

	protected AuditLogService getAuditLogService() {
		return auditLogService;
	}

	public ClientUserApi getClientUserApi() {
		return clientUserApi;
	}

	protected DeviceService getDeviceService() {
		return deviceService;
	}

	protected KronosCache getKronosCache() {
		return kronosCache;
	}

	protected DeviceTypeService getDeviceTypeService() {
		return deviceTypeService;
	}

	protected GatewayService getGatewayService() {
		return gatewayService;
	}

	protected String getProductSystemName() {
		return ProductSystemNames.KRONOS;
	}

	protected KronosHttpRequestCache getRequestCache() {
		return requestCache;
	}

	protected ClientSoftwareReleaseApi getClientSoftwareReleaseApi() {
		return clientSoftwareReleaseApi;
	}

	protected SoftwareReleaseService getSoftwareReleaseService() {
		return softwareReleaseService;
	}

	protected AccessKey validateCanReadDevice(String hid) {
		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device not found");
		Gateway gateway = kronosCache.findGatewayById(device.getGatewayId());
		Assert.notNull(gateway, "gateway not found");
		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		Assert.notNull(application, "application not found");

		if (!accessKey.getApplicationId().equals(application.getId())) {
			throw new AcsLogicalException("applicationId mismatched!");
		}
		if (!accessKey.canRead(application) && !accessKey.canRead(gateway) && !accessKey.canRead(device)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateCanUpdateDevice(String hid) {
		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device not found");
		Gateway gateway = kronosCache.findGatewayById(device.getGatewayId());
		Assert.notNull(gateway, "gateway not found");
		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		Assert.notNull(application, "application not found");

		if (!accessKey.getApplicationId().equals(application.getId())) {
			throw new AcsLogicalException("applicationId mismatched!");
		}
		if (!accessKey.canWrite(application) && !accessKey.canWrite(gateway) && !accessKey.canWrite(device)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateCanDeleteDevice(String hid) {
		return validateCanUpdateDevice(hid);
	}

	protected AccessKey validateCanWriteGateway(String hid) {
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());

		Gateway gateway = kronosCache.findGatewayByHid(hid);
		if (gateway == null) {
			throw new AcsLogicalException("invalid gateway hid");
		}
		if (!accessKey.getApplicationId().equals(gateway.getApplicationId())) {
			throw new AcsLogicalException("applicationId mismatched!");
		}
		if (!accessKey.canWrite(accessKey.getRefApplication()) && !accessKey.canWrite(gateway)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateCanReadGateway(String hid) {
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());

		Gateway gateway = kronosCache.findGatewayByHid(hid);
		if (gateway == null) {
			throw new AcsLogicalException("invalid gateway hid");
		}
		if (!accessKey.getApplicationId().equals(gateway.getApplicationId())) {
			throw new AcsLogicalException("applicationId mismatched!");
		}
		if (!accessKey.canRead(accessKey.getRefApplication()) && !accessKey.canRead(gateway)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateCanReadNode(String hid) {
		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);

		Node node = getKronosCache().findNodeByHid(hid);
		Assert.notNull(node, "node not found");
		Application application = getCoreCacheService().findApplicationById(node.getApplicationId());
		Assert.notNull(application, "application not found");

		if (!accessKey.getApplicationId().equals(application.getId())) {
			throw new AcsLogicalException("applicationId mismatched!");
		}
		if (!accessKey.canRead(application) && !accessKey.canRead(node)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateCanWriteNode(String hid) {
		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);

		Node node = getKronosCache().findNodeByHid(hid);
		Assert.notNull(node, "node not found");
		Application application = getCoreCacheService().findApplicationById(node.getApplicationId());
		Assert.notNull(application, "application not found");

		if (!accessKey.getApplicationId().equals(application.getId())) {
			throw new AcsLogicalException("applicationId mismatched!");
		}
		if (!accessKey.canWrite(application) && !accessKey.canWrite(node)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateApplicationOwner() {
		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);
		if (!accessKey.isOwner(accessKey.getRefApplication())) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateCanReadTestProcedure(String hid) {
		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);

		TestProcedure testProcedure = getKronosCache().findTestProcedureByHid(hid);
		Assert.notNull(testProcedure, "testProcedure not found");
		Application application = getCoreCacheService().findApplicationById(testProcedure.getApplicationId());
		Assert.notNull(application, "application not found");

		if (!accessKey.getApplicationId().equals(application.getId())) {
			throw new AcsLogicalException("applicationId mismatched!");
		}
		if (!accessKey.canRead(application) && !accessKey.canRead(testProcedure)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateCanDeleteGateway(String hid) {
		return validateCanWriteGateway(hid);
	}

	protected AccessKey validateCanCreateGateway() {
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		if (!accessKey.canWrite(accessKey.getRefApplication())) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateRootAccess() {
		AccessKey accessKey = getAccessKey();
		Company company = accessKey.getRefCompany();
		if (!accessKey.isOwner(company) || StringUtils.isNotEmpty(company.getParentCompanyId())) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AuditLog auditLog(String method, String accessKeyId, HttpServletRequest request) {
		return auditLog(method, null, null, accessKeyId, request);
	}

	protected AuditLog auditLog(String method, String applicationId, String objectId, String accessKeyId,
			HttpServletRequest request) {
		return auditLog(method, applicationId, objectId, accessKeyId, getApiPayload(), request);
	}

	protected AuditLog auditLog(String method, String applicationId, String objectId, String accessKeyId,
			String requestBody, HttpServletRequest request) {
		AuditLogBuilder builder = AuditLogBuilder.create().type(Api.ApiMethod).productName(ProductSystemNames.KRONOS)
				.by(accessKeyId).parameter(Api.Parameter.accessKey, accessKeyId)
				.parameter(Api.Parameter.apiName, getClass().getSimpleName())
				.parameter(Api.Parameter.apiMethodName, method).parameter(Api.Parameter.httpMethod, request.getMethod())
				.parameter(Api.Parameter.requestUri, request.getRequestURI())
				.parameter(Api.Parameter.requestParams, JsonUtils.toJson(request.getParameterMap()));

		if (applicationId != null)
			builder.applicationId(applicationId);

		if (objectId != null)
			builder.objectId(objectId);

		if (requestBody != null)
			builder.parameter(Api.Parameter.requestBody, requestBody);

		Object requestMappingPath = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		if (requestMappingPath != null)
			builder.parameter(Api.Parameter.requestMappingPath, requestMappingPath.toString());

		Object urlParams = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (urlParams != null)
			builder.parameter(Api.Parameter.urlParams, JsonUtils.toJson((urlParams)));

		return getAuditLogService().create(builder);
	}

	@Override
	protected AccessKey getValidatedAccessKey(String productSystemName) {
		AccessKey accessKey = super.getValidatedAccessKey(productSystemName);

		Application application = accessKey.getRefApplication();
		if (application != null) {
			Subscription subscription = application.getRefSubscription();
			if (subscription != null && Instant.now().isAfter(subscription.getEndDate())) {
				throw new AcsLogicalException("Your subscription has expired. Please contact Arrow Connect Support");
			}
		}
		return accessKey;
	}

	protected String getSoftwareReleaseName(String softwareReleaseId) {
		String softwareReleaseName = null;
		if (!StringUtils.isEmpty(softwareReleaseId)) {
			SoftwareRelease softwareRelease = requestCache.findSoftwareReleaseById(softwareReleaseId);
			if (softwareRelease != null && !StringUtils.isEmpty(softwareRelease.getSoftwareProductId())) {
				SoftwareProduct softwareProduct = requestCache
						.findSoftwareProductById(softwareRelease.getSoftwareProductId());
				if (softwareProduct != null) {
					softwareReleaseName = String.format(SOFTWARE_RELEASE_NAME_FORMAT, softwareProduct.getName(),
							softwareReleaseService.buildSoftwareVersion(softwareRelease));
				}
			}
		}
		return softwareReleaseName;
	}

	protected List<AvailableFirmwareModel> findAvailableSoftwarereleases(String hid, AcnDeviceCategory deviceCategory) {
		Assert.hasText(hid, "hid is empty");
		// 1. check is asset enabled and is softwareReleaseId defined
		String deviceTypeId = null;
		String fromSoftwareReleaseId = null;
		switch (deviceCategory) {
		case GATEWAY: {
			Gateway gateway = getKronosCache().findGatewayByHid(hid);
			Assert.notNull(gateway, "gateway was not found");
			Assert.isTrue(gateway.isEnabled(), "gateway is disabled");
			Assert.hasText(gateway.getSoftwareReleaseId(), "softwareReleaseId is not defined");
			deviceTypeId = gateway.getDeviceTypeId();
			fromSoftwareReleaseId = gateway.getSoftwareReleaseId();
			break;
		}
		case DEVICE: {
			Device device = getKronosCache().findDeviceByHid(hid);
			Assert.notNull(device, "device was not found");
			Assert.isTrue(device.isEnabled(), "device is disabled");
			Assert.hasText(device.getSoftwareReleaseId(), "softwareReleaseId is not defined");
			deviceTypeId = device.getDeviceTypeId();
			fromSoftwareReleaseId = device.getSoftwareReleaseId();
			break;
		}
		default:
			break;
		}

		// 2. check is deviceType enabled and is rheaDeviceType defined
		DeviceType deviceType = getKronosCache().findDeviceTypeById(deviceTypeId);
		Assert.notNull(deviceType, "deviceType was not found");
		Assert.isTrue(deviceType.isEnabled(), "deviceType is disabled");
		Assert.hasText(deviceType.getRheaDeviceTypeId(), "rheaDeviceTypeId is not defined");

		// 3. check is FM feature enabled
		Application application = getRequestCache().findApplicationById(deviceType.getApplicationId(), false);
		boolean hasFirmwareManagementFeature = false;
		if (application.getProductFeatures() != null && !application.getProductFeatures().isEmpty()
				&& application.getProductFeatures().contains(KronosConstants.ProductFeatures.FIRMWARE_MANAGEMENT))
			hasFirmwareManagementFeature = true;

		// 4. find software releases
		EnumSet<RightToUseType> rtuTypes = EnumSet.of(RightToUseType.Unrestricted);
		if (hasFirmwareManagementFeature) {
			rtuTypes.add(RightToUseType.Public);
		}
		String[] rtuTypeNames = new String[rtuTypes.size()];
		int rightToUseNameCount = 0;
		for (RightToUseType rtut : rtuTypes) {
			rtuTypeNames[rightToUseNameCount++] = rtut.name();
		}
		List<SoftwareRelease> softwareReleases = clientSoftwareReleaseApi.findAll(
				new String[] { deviceType.getRheaDeviceTypeId() }, rtuTypeNames, true,
				new String[] { fromSoftwareReleaseId });
		if (hasFirmwareManagementFeature) {
			softwareReleases.addAll(getPrivateApprovedSwReleases(application.getCompanyId(),
					new String[] { deviceType.getRheaDeviceTypeId() }, new String[] { fromSoftwareReleaseId }));
		}

		// 5. build output model
		List<AvailableFirmwareModel> result = new ArrayList<>();
		softwareReleases.forEach(softwareRelease -> {
			result.add(new AvailableFirmwareModel().withSoftwareReleaseHid(softwareRelease.getHid())
					.withSoftwareReleaseName(getSoftwareReleaseName(softwareRelease.getId())));
		});
		result.sort(
				Comparator.comparing(AvailableFirmwareModel::getSoftwareReleaseName, String.CASE_INSENSITIVE_ORDER));
		return result;
	}

	public List<SoftwareRelease> getPrivateApprovedSwReleases(String companyId, String[] deviceTypesId,
			String[] fromSoftwareReleaseId) {
		List<SoftwareRelease> result = new ArrayList<>();
		List<String> swReleaseApprovedIds = new ArrayList<>();
		List<RTURequest> rtuRequests = clientRTURequestApi.findAll(new String[] { companyId },
				new String[] { "Approved" }, null);
		if (rtuRequests != null && !rtuRequests.isEmpty()) {
			swReleaseApprovedIds = rtuRequests.stream().map(rtu -> rtu.getSoftwareReleaseId())
					.collect(Collectors.toList());
			if (!swReleaseApprovedIds.isEmpty()) {
				List<SoftwareRelease> softwareReleases = clientSoftwareReleaseApi.findAll(deviceTypesId,
						new String[] { RightToUseType.Private.name() }, true, fromSoftwareReleaseId);
				for (SoftwareRelease softwareRelease : softwareReleases) {
					if (swReleaseApprovedIds.contains(softwareRelease.getId()))
						result.add(softwareRelease);
				}
			}
		}
		return result;
	}

	protected <T extends DefinitionModelAbstract<T>> T buildModel(T model, DefinitionCollectionAbstract document) {
		model = buildModel(model, (AuditableDocumentAbstract) document);
		model.setDescription(document.getDescription());
		model.setEnabled(document.isEnabled());
		model.setName(document.getName());
		return model;
	}

	protected <T extends AuditableDocumentModelAbstract<T>> T buildModel(T model, AuditableDocumentAbstract document) {
		model = buildModel(model, (TsDocumentAbstract) document);
		// TODO
		// model.setLastModifiedBy(document.getLastModifiedBy());
		model.setLastModifiedDate(document.getLastModifiedDate());
		return model;
	}

	protected <T extends TsModelAbstract<T>> T buildModel(T model, TsDocumentAbstract document) {
		model = buildModel(model, (DocumentAbstract) document);
		// TODO
		// model.setCreatedBy(document.getCreatedBy());
		model.setCreatedDate(document.getCreatedDate());
		return model;
	}

	protected <T extends ModelAbstract<T>> T buildModel(T model, DocumentAbstract document) {
		if (model == null || document == null) {
			return null;
		}
		model.setHid(document.getHid());
		model.setPri(document.getPri());
		return model;
	}

	protected DeviceModel buildDeviceModel(Device device) {
		Assert.notNull(device, "device is null");
		DeviceModel model = buildModel(new DeviceModel(), device);
		device = deviceService.populate(device);
		if (device.getRefDeviceType() != null) {
			model.setType(device.getRefDeviceType().getName());
		} else {
			model.setType("ERROR");
		}
		if (StringUtils.isNotEmpty(device.getGatewayId())) {
			Gateway gateway = kronosCache.findGatewayById(device.getGatewayId());
			if (gateway != null) {
				model.setGatewayHid(gateway.getHid());
			} else {
				model.setGatewayHid("ERROR");
			}
		}
		if (StringUtils.isNoneEmpty(device.getUserId())) {
			User user = getCoreCacheService().findUserById(device.getUserId());
			if (user != null) {
				model.setUserHid(user.getHid());
			} else {
				model.setUserHid("ERROR");
			}
		}
		model.setEnabled(device.isEnabled());
		model.setName(device.getName());
		model.setUid(device.getUid());
		model.setInfo(device.getInfo());
		model.setProperties(device.getProperties());
		if (StringUtils.isNotEmpty(device.getSoftwareReleaseId())) {
			SoftwareRelease softwareRelease = requestCache.findSoftwareReleaseById(device.getSoftwareReleaseId());
			if (softwareRelease != null) {
				model.setSoftwareReleaseHid(softwareRelease.getHid());
				model.setSoftwareReleaseVersion(softwareReleaseService.buildSoftwareVersion(softwareRelease));
				SoftwareProduct softwareProduct = requestCache
						.findSoftwareProductById(softwareRelease.getSoftwareProductId());
				if (softwareProduct != null) {
					model.setSoftwareReleaseName(softwareProduct.getName());
				}
			}
		}
		model.setSoftwareName(device.getSoftwareName());
		model.setSoftwareVersion(device.getSoftwareVersion());
		if (StringUtils.isNotEmpty(device.getNodeId())) {
			Node node = getRequestCache().findNodeById(device.getNodeId());
			if (node != null) {
				model.setNodeHid(node.getHid());
			}
		}
		return model;
	}

	protected LastLocationModel buildLastLocationModel(LastLocation lastLocation, String hid) {
		Assert.notNull(lastLocation, "lastLocation is null");
		LastLocationModel result = buildModel(new LastLocationModel(), lastLocation);
		result.setObjectType(lastLocation.getObjectType().toString());
		result.setObjectHid(hid);
		result.setLocationType(lastLocation.getLocationType().toString());
		result.setLatitude(lastLocation.getLatitude());
		result.setLongitude(lastLocation.getLongitude());
		result.setTimestamp(lastLocation.getTimestamp() == null ? null : lastLocation.getTimestamp().toString());
		return result;
	}

	protected AuditLogModel buildAuditLogModel(AuditLog log, String hid) {
		Assert.notNull(log, "auditLog is null");
		AuditLogModel model = buildModel(new AuditLogModel(), log);
		model.setObjectHid(hid);
		model.setProductName(log.getProductName());
		model.setType(log.getType());
		Map<String, String> parameters = new HashMap<>();
		parameters.putAll(log.getParameters());
		model.setParameters(parameters);
		return model;
	}

	protected ConfigBackupModel buildConfigBackupModel(ConfigBackup configBackup) {
		Assert.notNull(configBackup, "configBackup is null");
		ConfigBackupModel model = buildModel(new ConfigBackupModel(), configBackup);
		model.setType(ConfigBackupModel.Type.valueOf(configBackup.getType().name()));
		return model;
	}

	protected TestResultModel buildTestResultModel(TestResult testResult) {
		Assert.notNull(testResult, "test result is null");

		TestResultModel model = buildModel(new TestResultModel(), testResult);
		model.withStatus(testResult.getStatus().toString())
				.withObjectHid(populateObjectHidById(testResult.getObjectId()))
				.withTestProcedureHid(populateTestProcedureHidById(testResult));
		if (testResult.getStarted() != null) {
			model.withStarted(testResult.getStarted().toString());
		}
		if (testResult.getEnded() != null) {
			model.withEnded(testResult.getEnded().toString());
		}
		if (testResult.getSteps() != null) {
			List<TestResultStepModel> testResultSteps = new ArrayList<>();
			for (TestResultStep testResultStep : testResult.getSteps()) {
				testResultSteps.add(buildTestProcedureStepModel(testResultStep));
			}
			model.setSteps(testResultSteps);
		}
		return model;
	}

	protected TestResultStepModel buildTestProcedureStepModel(TestResultStep testResultStep) {
		Assert.notNull(testResultStep, "testResultStep is null");
		TestResultStepModel stepModel = new TestResultStepModel();
		stepModel.setComment(testResultStep.getComment());
		stepModel.setError(testResultStep.getError());
		stepModel.setDefinition(buildTestProcedureStepModel(testResultStep.getDefinition()));
		stepModel.setStatus(testResultStep.getStatus().toString());
		if (testResultStep.getStarted() != null) {
			stepModel.setStarted(testResultStep.getStarted().toString());
		}
		if (testResultStep.getEnded() != null) {
			stepModel.setEnded(testResultStep.getEnded().toString());
		}
		return stepModel;
	}

	protected TestProcedureStepModel buildTestProcedureStepModel(TestProcedureStep step) {
		TestProcedureStepModel model = new TestProcedureStepModel();
		model.setId(step.getId());
		model.setDescription(step.getDescription());
		model.setName(step.getName());
		model.setSortOrder(step.getSortOrder());
		return model;
	}

	private String populateObjectHidById(String objectId) {
		Assert.notNull(objectId, "objectId is null");
		String objectHid = null;
		Gateway gateway = getKronosCache().findGatewayById(objectId);
		if (gateway != null) {
			objectHid = gateway.getHid();
		} else {
			Device device = getKronosCache().findDeviceById(objectId);
			if (device != null) {
				objectHid = device.getHid();
			}
		}
		Assert.notNull(objectHid, "object was not found");
		return objectHid;
	}

	private String populateTestProcedureHidById(TestResult testResult) {
		TestProcedure testProcedure = getKronosCache().findTestProcedureById(testResult.getTestProcedureId());
		Assert.notNull(testProcedure, "testProcedure is null");
		return testProcedure.getHid();
	}
}
