package com.arrow.kronos.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.AcnEventNames;
import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acn.client.model.AuditLogModel;
import com.arrow.acn.client.model.AvailableFirmwareModel;
import com.arrow.acn.client.model.AwsConfigModel;
import com.arrow.acn.client.model.AzureConfigModel;
import com.arrow.acn.client.model.CloudPlatform;
import com.arrow.acn.client.model.ConfigBackupModel;
import com.arrow.acn.client.model.CreateConfigBackupModel;
import com.arrow.acn.client.model.CreateGatewayModel;
import com.arrow.acn.client.model.DeviceCommandModel;
import com.arrow.acn.client.model.DeviceModel;
import com.arrow.acn.client.model.DevicePropertiesModel;
import com.arrow.acn.client.model.GatewayConfigModel;
import com.arrow.acn.client.model.GatewayModel;
import com.arrow.acn.client.model.IbmConfigModel;
import com.arrow.acn.client.model.LastLocationModel;
import com.arrow.acn.client.model.LastLocationRegistrationModel;
import com.arrow.acn.client.model.SoftwareUpdateModel;
import com.arrow.acn.client.model.TestResultModel;
import com.arrow.acn.client.model.UpdateGatewayModel;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.AcsUtils;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.ExternalHidModel;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.KeyModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.StatusModel;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.KronosCloudConstants.Ibm;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.AwsThing;
import com.arrow.kronos.data.AzureAccount;
import com.arrow.kronos.data.AzureDevice;
import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.IbmAccount;
import com.arrow.kronos.data.IbmGateway;
import com.arrow.kronos.data.IoTProvider;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.data.KronosUser;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestResult;
import com.arrow.kronos.repo.ConfigBackupSearchParams;
import com.arrow.kronos.repo.GatewaySearchParams;
import com.arrow.kronos.repo.TestResultSearchParams;
import com.arrow.kronos.service.AwsAccountService;
import com.arrow.kronos.service.AwsIntegrationService;
import com.arrow.kronos.service.AwsThingService;
import com.arrow.kronos.service.AzureDeviceService;
import com.arrow.kronos.service.AzureIntegrationService;
import com.arrow.kronos.service.ConfigBackupService;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.GatewayCommandService;
import com.arrow.kronos.service.GatewayService;
import com.arrow.kronos.service.IbmAccountService;
import com.arrow.kronos.service.IbmGatewayService;
import com.arrow.kronos.service.IbmIntegrationService;
import com.arrow.kronos.service.KronosApplicationService;
import com.arrow.kronos.service.KronosCache;
import com.arrow.kronos.service.KronosUserService;
import com.arrow.kronos.service.TestResultService;
import com.arrow.kronos.util.ApiUtil;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.GatewayType;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventBuilder;
import com.arrow.pegasus.data.event.EventParameter;
import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;
import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.service.LastLocationService;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/kronos/gateways")
public class GatewayApi extends BaseApiAbstract {

	@Autowired
	private AwsIntegrationService awsIntService;
	@Autowired
	private AwsThingService awsThingService;
	@Autowired
	private AwsAccountService awsAcctService;
	@Autowired
	private IbmGatewayService ibmGatewayService;
	@Autowired
	private IbmAccountService ibmAccountService;
	@Autowired
	private IbmIntegrationService ibmIntService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private GatewayCommandService gatewayCommandService;
	@Autowired
	private ApiUtil apiUtil;
	@Autowired
	private KronosApplicationService kronosApplicationService;
	@Autowired
	private KronosUserService kronosUserService;
	@Autowired
	private KronosCache kronosCache;
	@Autowired
	private LastLocationService lastLocationService;
	@Autowired
	private AzureIntegrationService azureIntService;
	@Autowired
	private AzureDeviceService azureDeviceService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private ConfigBackupService configBackupService;
	@Autowired
	private GatewayService gatewayService;

	private ObjectMapper localObjectMapper;

	@Override
	protected void postConstruct() {
		super.postConstruct();
		String method = "postConstruct";
		localObjectMapper = JsonUtils.getObjectMapper().copy();
		localObjectMapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true);
		logInfo(method, "localObjectMapper is initialized");
	}

	@ApiOperation(value = "register new gateway", response = ExternalHidModel.class)
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ExternalHidModel create(
			@ApiParam(value = "gateway model", required = true) @RequestBody(required = false) CreateGatewayModel body,
			HttpServletRequest request) {
		String method = "create";
		CreateGatewayModel model = JsonUtils.fromJson(getApiPayload(), CreateGatewayModel.class);

		AccessKey accessKey = validateCanCreateGateway();
		Application application = accessKey.getRefApplication();
		AuditLog auditLog = auditLog(method, application.getId(), null, accessKey.getId(), request);

		// special case for mobile app
		if (!StringUtils.isEmpty(model.getApplicationHid())
				&& !StringUtils.equals(application.getHid(), model.getApplicationHid())) {
			validateApplicationIdMismatched(accessKey.getRefApplication(),
					getCoreCacheService().findApplicationByHid(model.getApplicationHid()));
		}

		Gateway gateway = doCreate(model);
		auditLog.setObjectId(gateway.getId());
		getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());
		return (ExternalHidModel) new ExternalHidModel().withExternalId(gateway.getExternalId())
				.withHid(gateway.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update existing gateway", response = HidModel.class)
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel update(@PathVariable(value = "hid") String hid,
			@ApiParam(value = "gateway model", required = true) @RequestBody(required = false) UpdateGatewayModel body,
			HttpServletRequest request) {
		String method = "update";
		UpdateGatewayModel model = JsonUtils.fromJson(getApiPayload(), UpdateGatewayModel.class);
		model.setHid(hid);
		Gateway gateway = doUpdate(model, method, request);
		return new HidModel().withHid(gateway.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "download gateway configuration", response = GatewayConfigModel.class)
	@RequestMapping(path = "/{hid}/config", method = RequestMethod.GET)
	public GatewayConfigModel config(@PathVariable(value = "hid") String hid) {
		String method = "config";

		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());

		Gateway gateway = getKronosCache().findGatewayByHid(hid);
		checkEnabled(gateway, "gateway");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());

		// special case for mobile app
		if (!StringUtils.equals(accessKey.getApplicationId(), application.getId())) {
			validateApplicationIdMismatched(accessKey.getRefApplication(), application);
		} else {
			validateCanReadGateway(hid);
		}

		AccessKey ownerKey = getCoreCacheHelper()
				.populateAccessKey(getCoreCacheService().findOwnerAccessKeyByPri(gateway.getPri()));
		if (ownerKey == null) {
			throw new AcsLogicalException("OwnerKey not found");
		}

		GatewayConfigModel result = new GatewayConfigModel();
		result.withKey(
				new KeyModel().withApiKey(getCryptoService().decrypt(application.getId(), ownerKey.getEncryptedApiKey()))
						.withSecretKey(getCryptoService().decrypt(application.getId(), ownerKey.getEncryptedSecretKey())));

		// default to Arrow Connect
		result.setCloudPlatform(CloudPlatform.IotConnect);

		switch (findGatewayIoTProvider(gateway)) {
		case AWS:
			AwsThing thing = awsThingService.findBy(gateway);
			if (thing != null) {
				checkEnabled(thing, "awsThing");
				logInfo(method, "awsthing arn: %s", thing.getThingArn());

				AwsConfigModel awsModel = new AwsConfigModel().withHost(thing.getHost())
						.withClientCert(getCryptoService().decrypt(application.getId(), thing.getCertPem()))
						.withPrivateKey(getCryptoService().decrypt(application.getId(), thing.getPrivateKey()));
				awsModel.withCaCert(awsAcctService.loadCaCert());
				result.withAws(awsModel);
				result.setCloudPlatform(CloudPlatform.Aws);
			} else {
				logError(method, "AwsThing not found for gateway: %s", gateway.getId());
			}
			break;
		case IBM:
			IbmGateway ibmGateway = ibmGatewayService.findBy(gateway);
			if (ibmGateway != null) {
				checkEnabled(ibmGateway, "ibmGateway");
				IbmAccount account = ibmAccountService.getIbmAccountRepository().findById(ibmGateway.getIbmAccountId())
						.orElse(null);
				checkEnabled(account, "ibmAccount");

				IbmConfigModel ibmModel = new IbmConfigModel().withOrganizationId(account.getOrganizationId())
						.withGatewayType(ibmIntService.buildGatewayDeviceType(gateway.getType()))
						.withGatewayId(gateway.getUid()).withAuthMethod(Ibm.HEADER_AUTHENTICATION_MODE_TOKEN)
						.withAuthToken(getCryptoService().decrypt(application.getId(), ibmGateway.getAuthToken()));
				result.withIbm(ibmModel);
				result.setCloudPlatform(CloudPlatform.Ibm);
			} else {
				logError(method, "ibmGateway not found for gateway: %s", gateway.getId());
			}
			break;
		case AZURE:
			AzureDevice azureDevice = azureDeviceService.findBy(gateway);
			if (azureDevice != null) {
				checkEnabled(azureDevice, "azureDevice");
				logInfo(method, "azureDevice: %s", azureDevice.getPri());

				AzureAccount azureAccount = kronosCache.findAzureAccountById(azureDevice.getAzureAccountId());
				checkEnabled(azureAccount, "azureAccount");

				AzureConfigModel azureModel = new AzureConfigModel().withHost(azureAccount.getHostName())
						.withAccessKey(getCryptoService().decrypt(application.getId(), azureDevice.getPrimaryKey()));
				result.withAzure(azureModel);
				result.setCloudPlatform(CloudPlatform.Azure);
			} else {
				logError(method, "AzureDevice not found for gateway: %s", gateway.getId());
			}
			break;
		case ArrowConnect:
			break;
		default:
			break;

		}

		return result;
	}

	@ApiOperation(value = "list gateway audit logs")
	@RequestMapping(path = "/{hid}/logs", method = RequestMethod.GET)
	public PagingResultModel<AuditLogModel> listGatewayAuditLogs(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "createdDateFrom") @RequestParam(name = "createdDateFrom", required = false) String createdDateFrom,
			@ApiParam(value = "createdDateTo") @RequestParam(name = "createdDateTo", required = false) String createdDateTo,
			@ApiParam(value = "user hids") @RequestParam(name = "userHids", required = false) String[] userHids,
			@ApiParam(value = "types") @RequestParam(name = "types", required = false) String[] types,
			@ApiParam(value = "sortField") @RequestParam(name = "sortField", required = false) String sortField,
			@ApiParam(value = "sortDirection") @RequestParam(name = "sortDirection", required = false) String sortDirection,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {

		AccessKey accessKey = validateCanReadGateway(hid);

		size = apiUtil.validateSize(size);
		page = apiUtil.validatePage(page);

		Instant from = apiUtil.parseDateParam(createdDateFrom);
		Instant to = apiUtil.parseDateParam(createdDateTo);
		if (from != null && to != null && from.isAfter(to)) {
			throw new AcsLogicalException("createdDateFrom is after createdDateTo");
		}

		Gateway gateway = getKronosCache().findGatewayByHid(hid);
		Assert.notNull(gateway, "gateway is null");

		Set<String> userIds = apiUtil.getUserIdsByHids(userHids);

		PageRequest pageRequest = apiUtil.buildPageRequest(page, size, sortField, sortDirection);

		Page<AuditLog> list = getAuditLogService().getAuditLogRepository().findAuditLogs(pageRequest, null,
				new String[] { accessKey.getApplicationId() }, types, new String[] { gateway.getId() }, null, from, to,
				userIds.toArray(new String[userIds.size()]));
		List<AuditLogModel> data = new ArrayList<>();
		list.forEach(item -> data.add(buildAuditLogModel(item, gateway.getHid())));

		PagingResultModel<AuditLogModel> result = new PagingResultModel<>();
		result.setTotalPages(list.getTotalPages());
		result.setTotalSize(list.getTotalElements());
		result.setPage(pageRequest.getPageNumber());
		result.setSize(pageRequest.getPageSize());
		result.setData(data);
		return result;
	}

	@Deprecated
	@ApiOperation(value = "send command and payload to gateway and device")
	@RequestMapping(path = "/{hid}/commands/device-command", method = RequestMethod.POST)
	public HidModel sendDeviceCommand(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "device command model", required = true) @RequestBody(required = false) DeviceCommandModel body,
			HttpServletRequest request) {
		String method = "sendDeviceCommand";
		Event event = doSendDeviceCommand(hid, null, method, request);
		return new HidModel().withHid(event.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "send command and payload to gateway and device")
	@RequestMapping(path = "/{gatewayHid}/devices/{deviceHid}/actions/command", method = RequestMethod.POST)
	public HidModel sendDeviceCommand(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			@ApiParam(value = "device command model", required = true) @RequestBody(required = false) DeviceCommandModel body,
			HttpServletRequest request) {
		String method = "sendDeviceCommand";
		Event event = doSendDeviceCommand(gatewayHid, deviceHid, method, request);
		return new HidModel().withHid(event.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "list gateway devices")
	@RequestMapping(path = "/{hid}/devices", method = RequestMethod.GET)
	public ListResultModel<DeviceModel> listDevices(@PathVariable(value = "hid") String hid) {
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		Gateway gateway = getKronosCache().findGatewayByHid(hid);
		Assert.notNull(gateway, "gateway not found");
		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		Assert.notNull(application, "application not found");
		boolean canReadApplication = accessKey.canRead(application);
		boolean canReadGateway = accessKey.canRead(gateway);
		List<Device> devices = deviceService.getDeviceRepository().findAllByGatewayId(gateway.getId());
		List<DeviceModel> data = new ArrayList<>();
		devices.forEach(device -> {
			if (canReadApplication || canReadGateway || accessKey.canRead(device)) {
				data.add(buildDeviceModel(device));
			}
		});
		ListResultModel<DeviceModel> result = new ListResultModel<>();
		result.setData(data);
		result.setSize(data.size());
		return result;
	}

	@ApiOperation(value = "push software update")
	@RequestMapping(path = "/{gatewayHid}/actions/software-update", method = RequestMethod.POST)
	public HidModel pushSoftwareUpdate(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			@ApiParam(value = "software update model", required = true) @RequestBody(required = false) SoftwareUpdateModel body,
			HttpServletRequest request) {

		String method = "pushSoftwareUpdate";
		AccessKey accessKey = validateCanWriteGateway(gatewayHid);

		SoftwareUpdateModel model = JsonUtils.fromJson(getApiPayload(), SoftwareUpdateModel.class);
		Assert.notNull(model, "software update model is null");
		model.trim();
		UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES + UrlValidator.ALLOW_2_SLASHES);
		Assert.isTrue(urlValidator.isValid(model.getUrl()), "invalid url");

		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);
		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		EventBuilder eventBuilder = EventBuilder.create().applicationId(accessKey.getApplicationId())
				.name(AcnEventNames.ServerToGateway.GATEWAY_SOFTWARE_UPDATE)
				.parameter(EventParameter.InString("url", model.getUrl()));
		Event event = gatewayCommandService.sendEvent(eventBuilder.build(), gateway.getId(), accessKey);
		logDebug(method, "event has been sent to gatewayHid=%s: eventHid=%s", gatewayHid, event.getHid());

		return new HidModel().withHid(event.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "start device")
	@RequestMapping(path = "/{gatewayHid}/devices/{deviceHid}/actions/start", method = RequestMethod.POST)
	public HidModel startDevice(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			HttpServletRequest request) {

		String method = "startDevice";
		Event event = doStartStopDevice(AcnEventNames.ServerToGateway.DEVICE_START, gatewayHid, deviceHid, method,
				request);
		logDebug(method, "event has been sent to deviceHid=%s: eventHid=%s", deviceHid, event.getHid());
		return new HidModel().withHid(event.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "stop device")
	@RequestMapping(path = "/{gatewayHid}/devices/{deviceHid}/actions/stop", method = RequestMethod.POST)
	public HidModel stopDevice(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			HttpServletRequest request) {

		String method = "stopDevice";
		Event event = doStartStopDevice(AcnEventNames.ServerToGateway.DEVICE_STOP, gatewayHid, deviceHid, method,
				request);
		logDebug(method, "event has been sent to deviceHid=%s: eventHid=%s", deviceHid, event.getHid());
		return new HidModel().withHid(event.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update device properties")
	@RequestMapping(path = "/{gatewayHid}/devices/{deviceHid}/actions/properties", method = RequestMethod.POST)
	public HidModel updateDeviceProperties(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			@ApiParam(value = "device properties model", required = true) @RequestBody(required = false) DevicePropertiesModel body,
			HttpServletRequest request) {

		String method = "updateDeviceProperties";
		AccessKey accessKey = validateCanWriteGateway(gatewayHid);

		DevicePropertiesModel model = JsonUtils.fromJson(getApiPayload(), DevicePropertiesModel.class);
		Assert.notNull(model, "device properties model is null");
		Assert.notEmpty(model.getProperties(), "properties are empty");

		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);

		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		Device device = kronosCache.findDeviceByHid(deviceHid);
		Assert.notNull(device, "device not found");
		Assert.isTrue(gateway.getId().equals(device.getGatewayId()), "gateway does not match");

		if (device.getProperties() == null) {
			device.setProperties(new HashMap<String, String>());
		}
		for (String property : model.getProperties().keySet()) {
			device.getProperties().put(property, model.getProperties().get(property));
		}
		device = deviceService.update(device, accessKey.getId());

		EventBuilder builder = EventBuilder.create().applicationId(accessKey.getApplicationId())
				.name(AcnEventNames.ServerToGateway.DEVICE_PROPERTY_CHANGE)
				.parameter(EventParameter.InString("deviceHid", device.getHid()));
		model.getProperties().forEach((key, value) -> builder.parameter(EventParameter.InString(key, value)));
		Event event = gatewayCommandService.sendEvent(builder.build(), device.getGatewayId(), accessKey);
		logDebug(method, "event has been sent to deviceHid=%s: eventHid=%s", device.getHid(), event.getHid());
		return new HidModel().withHid(event.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "get last location")
	@RequestMapping(path = "/{hid}/location", method = RequestMethod.GET)
	public LastLocationModel getLastLocation(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "hid") String hid) {

		validateCanReadGateway(hid);
		Gateway gateway = getKronosCache().findGatewayByHid(hid);

		LastLocation lastLocation = lastLocationService.getLastLocationRepository()
				.findByObjectTypeAndObjectId(LocationObjectType.GATEWAY, gateway.getId());
		Assert.notNull(lastLocation, "last location not found");
		return buildLastLocationModel(lastLocation, gateway.getHid());
	}

	@ApiOperation(value = "set last location")
	@RequestMapping(path = "/{hid}/location", method = RequestMethod.PUT)
	public HidModel setLastLocation(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "last location model", required = true) @RequestBody(required = false) LastLocationRegistrationModel body,
			HttpServletRequest request) {
		String method = "setLastLocation";

		AccessKey accessKey = validateCanWriteGateway(hid);
		Gateway gateway = getKronosCache().findGatewayByHid(hid);
		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		LastLocationRegistrationModel model = JsonUtils.fromJson(getApiPayload(), LastLocationRegistrationModel.class);
		LastLocation lastLocation = apiUtil.updateLastLocation(model, LocationObjectType.GATEWAY, gateway.getId());
		return new HidModel().withHid(lastLocation.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "list gateway test results")
	@RequestMapping(path = "/{hid}/test-results", method = RequestMethod.GET)
	public PagingResultModel<TestResultModel> listTestResults(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "status") @RequestParam(name = "status", required = false) Set<String> statuses,
			@ApiParam(value = "test procedure hid") @RequestParam(name = "testProcedureHid", required = false) String testProcedureHid,
			@ApiParam(value = "createdDateFrom") @RequestParam(name = "createdDateFrom", required = false) String createdDateFrom,
			@ApiParam(value = "createdDateTo") @RequestParam(name = "createdDateTo", required = false) String createdDateTo,
			@ApiParam(value = "sort field") @RequestParam(name = "sortField", required = false) String sortField,
			@ApiParam(value = "sort direction") @RequestParam(name = "sortDirection", required = false) String sortDirection,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {

		validateCanReadGateway(hid);

		PageRequest pageRequest = apiUtil.buildPageRequest(apiUtil.validatePage(page), apiUtil.validateSize(size),
				sortField, sortDirection);

		Instant from = apiUtil.parseDateParam(createdDateFrom);
		Instant to = apiUtil.parseDateParam(createdDateTo);
		if (from != null && to != null && from.isAfter(to)) {
			throw new AcsLogicalException("createdDateFrom is after createdDateTo");
		}

		Gateway gateway = getKronosCache().findGatewayByHid(hid);
		Assert.notNull(gateway, "gateway is not found");

		TestResultSearchParams params = new TestResultSearchParams().addObjectId(gateway.getId()).addCreatedBefore(to)
				.addCreatedAfter(from);
		if (StringUtils.isNotBlank(testProcedureHid)) {
			TestProcedure testProcedure = getKronosCache().findTestProcedureByHid(testProcedureHid);
			Assert.notNull(testProcedure, "testProcedure is not found");
			params.addTestProcedureIds(testProcedure.getId());
		}
		if (statuses != null) {
			statuses.forEach(params::addStatuses);
		}
		Page<TestResult> testResults = testResultService.getTestResultRepository().findTestResult(pageRequest, params);
		List<TestResultModel> data = testResults.getContent().stream().map(this::buildTestResultModel)
				.collect(Collectors.toCollection(ArrayList::new));

		PagingResultModel<TestResultModel> result = new PagingResultModel<>();
		result.setPage(pageRequest.getPageNumber());
		result.setSize(pageRequest.getPageSize());
		result.setTotalPages(testResults.getTotalPages());
		result.setTotalSize(testResults.getTotalElements());
		result.setData(data);
		return result;
	}

	@ApiOperation(value = "request gateway configuration update")
	@RequestMapping(path = "/{gatewayHid}/actions/configuration-update", method = RequestMethod.POST)
	public HidModel requestGatewayConfigurationUpdate(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			HttpServletRequest request) {
		String method = "requestGatewayConfigurationUpdate";
		Assert.hasText(gatewayHid, "gatewayHid is empty");

		AccessKey accessKey = validateCanWriteGateway(gatewayHid);
		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);
		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		Event event = getGatewayService().requestConfigurationUpdate(gateway.getId());
		return new HidModel().withHid(event.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "request device configuration update")
	@RequestMapping(path = "/{gatewayHid}/devices/{deviceHid}/actions/configuration-update", method = RequestMethod.POST)
	public HidModel requestDeviceConfigurationUpdate(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			HttpServletRequest request) {
		String method = "requestDeviceConfigurationUpdate";
		Assert.hasText(gatewayHid, "gatewayHid is empty");
		Assert.hasText(deviceHid, "deviceHid is empty");

		AccessKey accessKey = validateCanWriteGateway(gatewayHid);
		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);
		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		Device device = getKronosCache().findDeviceByHid(deviceHid);
		Assert.notNull(device, "device is not found");
		Assert.isTrue(gateway.getId().equals(device.getGatewayId()), "gateway does not match");

		Event event = deviceService.requestConfigurationUpdate(device.getId());
		return new HidModel().withHid(event.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "backup gateway configuration")
	@RequestMapping(path = "/{hid}/config-backups", method = RequestMethod.POST)
	public HidModel backupConfiguration(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "config backup name", required = true) @RequestBody(required = false) CreateConfigBackupModel body,
			HttpServletRequest request) {
		String method = "backupConfiguration";

		CreateConfigBackupModel model = JsonUtils.fromJson(getApiPayload(), CreateConfigBackupModel.class);
		Assert.notNull(model, "configBackup model is null");
		Assert.hasText(model.getName(), "configBackup name is empty");

		AccessKey accessKey = validateCanWriteGateway(hid);
		Gateway gateway = getKronosCache().findGatewayByHid(hid);
		Assert.notNull(gateway, "gateway is not found");
		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		ConfigBackup configBackup = getGatewayService().backupConfiguration(gateway, model.getName(),
				accessKey.getId());
		return new HidModel().withHid(configBackup.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "restore gateway configuration")
	@RequestMapping(path = "/{hid}/config-backups/{configBackupHid}/restore", method = RequestMethod.POST)
	public StatusModel restoreConfiguration(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "config backup hid", required = true) @PathVariable(value = "configBackupHid") String configBackupHid,
			HttpServletRequest request) {
		String method = "restoreConfiguration";

		AccessKey accessKey = validateCanWriteGateway(hid);
		Gateway gateway = getKronosCache().findGatewayByHid(hid);
		Assert.notNull(gateway, "gateway is not found");
		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		ConfigBackup configBackup = configBackupService.getConfigBackupRepository().doFindByHid(configBackupHid);
		Assert.notNull(configBackup, "configBackup is not found");

		getGatewayService().restoreConfiguration(gateway, configBackup, accessKey.getId());

		return StatusModel.OK;
	}

	@ApiOperation(value = "list gateway configuration backups")
	@RequestMapping(path = "/{hid}/config-backups", method = RequestMethod.GET)
	public PagingResultModel<ConfigBackupModel> listConfigurationBackups(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "createdDateFrom") @RequestParam(name = "createdDateFrom", required = false) String createdDateFrom,
			@ApiParam(value = "createdDateTo") @RequestParam(name = "createdDateTo", required = false) String createdDateTo,
			@ApiParam(value = "sortField") @RequestParam(name = "sortField", required = false) String sortField,
			@ApiParam(value = "sortDirection") @RequestParam(name = "sortDirection", required = false) String sortDirection,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {

		size = apiUtil.validateSize(size);
		page = apiUtil.validatePage(page);

		Instant from = apiUtil.parseDateParam(createdDateFrom);
		Instant to = apiUtil.parseDateParam(createdDateTo);
		if (from != null && to != null && from.isAfter(to)) {
			throw new AcsLogicalException("createdDateFrom is after createdDateTo");
		}

		validateCanReadGateway(hid);

		Gateway gateway = getKronosCache().findGatewayByHid(hid);
		Assert.notNull(gateway, "gateway is not found");
		PageRequest pageRequest = apiUtil.buildPageRequest(page, size, sortField, sortDirection);
		ConfigBackupSearchParams params = new ConfigBackupSearchParams().addObjectIds(gateway.getId())
				.addCreatedAfter(from).addCreatedBefore(to);
		Page<ConfigBackup> backups = configBackupService.getConfigBackupRepository().findConfigBackups(pageRequest,
				params);
		List<ConfigBackupModel> data = backups.getContent().stream().map(this::buildConfigBackupModel)
				.collect(Collectors.toCollection(ArrayList::new));

		PagingResultModel<ConfigBackupModel> result = new PagingResultModel<ConfigBackupModel>();
		result.setPage(pageRequest.getPageNumber());
		result.setSize(pageRequest.getPageSize());
		result.setTotalPages(backups.getTotalPages());
		result.setTotalSize(backups.getTotalElements());
		result.setData(data);
		return result;
	}

	@ApiOperation(value = "request gateway configuration restore")
	@RequestMapping(path = "/{gatewayHid}/actions/configuration-restore", method = RequestMethod.POST)
	public HidModel requestGatewayConfigurationRestore(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			HttpServletRequest request) {
		String method = "requestGatewayConfigurationRestore";
		Assert.hasText(gatewayHid, "gatewayHid is empty");

		AccessKey accessKey = validateCanWriteGateway(gatewayHid);
		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);
		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		Event event = getGatewayService().requestConfigurationRestore(gateway.getId());
		return new HidModel().withHid(event.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "request device configuration restore")
	@RequestMapping(path = "/{gatewayHid}/devices/{deviceHid}/actions/configuration-restore", method = RequestMethod.POST)
	public HidModel requestDeviceConfigurationRestore(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			HttpServletRequest request) {
		String method = "requestDeviceConfigurationRestore";

		Assert.hasText(gatewayHid, "gatewayHid is empty");
		Assert.hasText(deviceHid, "deviceHid is empty");

		AccessKey accessKey = validateCanWriteGateway(gatewayHid);
		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);
		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		Device device = getKronosCache().findDeviceByHid(deviceHid);
		Assert.notNull(device, "device is not found");
		Assert.isTrue(gateway.getId().equals(device.getGatewayId()), "gateway does not match");

		Event event = deviceService.requestConfigurationRestore(device.getId());
		return new HidModel().withHid(event.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "delete gateway")
	@RequestMapping(path = "/{hid}", method = RequestMethod.DELETE)
	public StatusModel delete(@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "hid") String hid,
			HttpServletRequest request) {
		String method = "delete";

		AccessKey accessKey = validateCanDeleteGateway(hid);
		Gateway gateway = getKronosCache().findGatewayByHid(hid);
		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		getGatewayService().delete(gateway, accessKey.getId());

		return StatusModel.OK;
	}

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public GatewayModel find(@PathVariable(value = "hid") String hid) {
		validateCanReadGateway(hid);
		return populateModel(new GatewayModel(), getKronosCache().findGatewayByHid(hid));
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<GatewayModel> findAll(@RequestParam(name = "uids", required = false) String[] uids,
			@RequestParam(name = "userHids", required = false) String[] userHids,
			@RequestParam(name = "types", required = false) String[] gatewayTypeNames,
			@RequestParam(name = "deviceTypes", required = false) String[] deviceTypeNames,
			@RequestParam(name = "nodeHids", required = false) String[] nodeHids,
			@RequestParam(name = "osNames", required = false) String[] osNames,
			@RequestParam(name = "softwareNames", required = false) String[] softwareNames,
			@RequestParam(name = "softwareVersions", required = false) String[] softwareVersions,
			@RequestParam(name = "enabled", required = false) String enabled,
			@RequestParam(name = "createdBefore", required = false) String createdBefore,
			@RequestParam(name = "createdAfter", required = false) String createdAfter,
			@RequestParam(name = "updatedBefore", required = false) String updatedBefore,
			@RequestParam(name = "updatedAfter", required = false) String updatedAfter,
			@RequestParam(name = "_page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "_size", required = false, defaultValue = "100") int size) {

		Assert.isTrue(page >= 0, "page must be positive");
		Assert.isTrue(size >= 0 && size <= KronosConstants.PageResult.MAX_SIZE,
				"size must be between 0 and " + KronosConstants.PageResult.MAX_SIZE);

		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());

		PageRequest pageRequest = PageRequest.of(page, size);
		PagingResultModel<GatewayModel> result = new PagingResultModel<>();
		result.setPage(pageRequest.getPageNumber());

		GatewaySearchParams params = new GatewaySearchParams();
		params.addApplicationIds(accessKey.getApplicationId());
		if (uids != null) {
			Arrays.stream(uids).filter(StringUtils::isNotBlank).forEach(params::addUids);
		}
		if (userHids != null) {
			Arrays.asList(userHids).stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet()).stream()
					.map(userHid -> getCoreCacheService().findUserByHid(userHid)).filter(user -> user != null)
					.map(User::getId).forEach(params::addUserIds);
			if (params.getUserIds() == null || params.getUserIds().size() == 0) {
				return result;
			}
		}
		if (gatewayTypeNames != null) {
			params.addGatewayTypes(
					Arrays.stream(gatewayTypeNames).filter(StringUtils::isNotBlank).map(GatewayType::valueOf)
							.collect(Collectors.toCollection(() -> EnumSet.noneOf(GatewayType.class))));
		}
		if (deviceTypeNames != null) {
			Arrays.asList(deviceTypeNames).stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet()).stream()
					.map(deviceTypeName -> getKronosCache().findDeviceTypeByName(accessKey.getApplicationId(),
							deviceTypeName))
					.filter(deviceType -> deviceType != null).map(DeviceType::getId).forEach(params::addDeviceTypeIds);
			if (params.getDeviceTypeIds() == null || params.getDeviceTypeIds().size() == 0) {
				return result;
			}
		}
		if (nodeHids != null) {
			Arrays.asList(nodeHids).stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet()).stream()
					.map(nodeHid -> getKronosCache().findNodeByHid(nodeHid)).filter(node -> node != null)
					.map(Node::getId).forEach(params::addNodeIds);
			if (params.getNodeIds() == null || params.getNodeIds().size() == 0) {
				return result;
			}
		}
		if (osNames != null) {
			Arrays.stream(osNames).filter(StringUtils::isNotBlank).forEach(params::addOsNames);
		}
		if (softwareNames != null) {
			Arrays.stream(softwareNames).filter(StringUtils::isNoneBlank).forEach(params::addSoftwareNames);
		}
		if (softwareVersions != null) {
			Arrays.stream(softwareVersions).filter(StringUtils::isNotBlank).forEach(params::addSoftwareVersions);
		}
		if (StringUtils.isNotEmpty(enabled)) {
			params.setEnabled(Boolean.valueOf(enabled));
		}

		if (StringUtils.isNotEmpty(createdBefore)) {
			try {
				params.setCreatedBefore(Instant.parse(createdBefore));
			} catch (Exception e) {
				throw new AcsLogicalException("field createdBefore has invalid datetime format");
			}
		}

		if (StringUtils.isNotEmpty(createdAfter)) {
			try {
				params.setCreatedAfter(Instant.parse(createdAfter));
			} catch (Exception e) {
				throw new AcsLogicalException("field createdAfter has invalid datetime format");
			}
		}

		if (StringUtils.isNotEmpty(updatedBefore)) {
			try {
				params.setUpdatedBefore(Instant.parse(updatedBefore));
			} catch (Exception e) {
				throw new AcsLogicalException("field updatedBefore has invalid datetime format");
			}
		}

		if (StringUtils.isNotEmpty(updatedAfter)) {
			try {
				params.setUpdatedAfter(Instant.parse(updatedAfter));
			} catch (Exception e) {
				throw new AcsLogicalException("field updatedAfter has invalid datetime format");
			}
		}

		Page<Gateway> gateways = gatewayService.getGatewayRepository().findGateways(pageRequest, params);

		result.setData(gateways.map(gateway -> populateModel(new GatewayModel(), gateway)).getContent());
		result.setTotalPages(gateways.getTotalPages());
		result.setTotalSize(gateways.getTotalElements());
		result.setSize(gateways.getNumberOfElements());

		return result;
	}

	@RequestMapping(path = "/{hid}/checkin", method = RequestMethod.PUT)
	public StatusModel checkin(@PathVariable(value = "hid") String hid, HttpServletRequest request) {
		doCheckin(hid, "checkin", request);
		return StatusModel.OK;
	}

	@RequestMapping(path = "/{hid}/heartbeat", method = RequestMethod.PUT)
	public StatusModel heartbeat(@PathVariable(value = "hid") String hid, HttpServletRequest request) {
		doHeartbeat(hid, "heartbeat", request);
		return StatusModel.OK;
	}

	protected Gateway doCreate(CreateGatewayModel model) {
		String method = "doCreate";

		Assert.hasText(model.getUid(), "uid is required");
		Assert.hasLength(model.getName(), "name is required");
		Assert.hasText(model.getOsName(), "osName is required");
		Assert.hasText(model.getSoftwareName(), "softwareName is required");
		Assert.hasText(model.getSoftwareVersion(), "softwareVersion is required");
		Assert.notNull(model.getType(), "type is required");

		AccessKey accessKey = validateCanCreateGateway();

		Application application = accessKey.getRefApplication();
		if (!StringUtils.isEmpty(model.getApplicationHid())
				&& !StringUtils.equals(application.getHid(), model.getApplicationHid())) {
			Application overrideApplication = getCoreCacheService().findApplicationByHid(model.getApplicationHid());
			checkEnabled(overrideApplication, "overrideApplication");

			// TODO need to comment out for developer registration to work, may
			// need to revisit

			// Assert.isTrue(application.getCompanyId().equals(overrideApplication.getCompanyId()),
			// "companyId mismatched");
			if (!StringUtils.equals(application.getCompanyId(), overrideApplication.getCompanyId())) {
				logWarn(method, "companyId mismatched, ignoring ...");
			}

			logInfo(method, "use override application: %s", overrideApplication.getName());
			application = overrideApplication;
		}

		Gateway gateway = gatewayService.getGatewayRepository().findByApplicationIdAndUid(application.getId(),
				model.getUid());
		if (gateway != null) {
			checkEnabled(gateway, "gateway");
			gateway = populateGateway(gateway, model, accessKey.getId());
			gatewayService.update(gateway, accessKey.getId());
			logInfo(method, "gateway already exists, information has been updated");
		} else {
			gateway = new Gateway();
			gateway.setApplicationId(application.getId());
			gateway.setEnabled(true);
			gateway = populateGateway(gateway, model, accessKey.getId());
			gatewayService.create(gateway, accessKey.getId());

			logInfo(method, "registered new gateway: %s --> %s", gateway.getName(), gateway.getId());
		}
		return processCloudPlatformIntegration(gateway);
	}

	protected Gateway doCheckin(String hid, String callingMethod, HttpServletRequest request) {
		String method = "doCheckin";

		AccessKey accessKey = validateCanWriteGateway(hid);

		// find gateway
		Gateway gateway = gatewayService.getGatewayRepository().doFindByHid(hid);
		Assert.notNull(gateway, "Gateway not found for hid: " + hid);
		if (StringUtils.isNotEmpty(gateway.getUserId())) {
			User user = getCoreCacheService().findUserById(gateway.getUserId());
			Assert.notNull(user, "invalid userId: " + gateway.getUserId());
			logInfo(method, "gateway belongs to user: %s", user.getContact().getEmail());
		}

		Application application = accessKey.getRefApplication();

		auditLog(callingMethod, application.getId(), gateway.getId(), accessKey.getId(), request);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().productName(application.getRefProduct().getName())
				.type(KronosAuditLog.Gateway.GatewayCheckin).objectId(gateway.getId())
				.applicationId(application.getId()).parameter("hid", hid));

		logInfo(method, "applicationId: %s, hid: %s, uid: %s", application.getId(), hid, gateway.getUid());

		return processCloudPlatformIntegration(gateway);
	}

	protected Gateway doUpdate(UpdateGatewayModel model) {
		return doUpdate(model, null, null);
	}

	protected Gateway doUpdate(UpdateGatewayModel model, String callingMethod, HttpServletRequest request) {
		String method = "doUpdate";

		AccessKey accessKey = validateCanWriteGateway(model.getHid());
		Gateway gateway = gatewayService.getGatewayRepository().doFindByHid(model.getHid());

		if (callingMethod != null && request != null) {
			auditLog(callingMethod, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);
		}

		checkEnabled(gateway, "gateway");
		logInfo(method, "found gateway, id: %s, name: %s, userId: %s", gateway.getId(), gateway.getName(),
				gateway.getUserId());
		gateway = populateGateway(gateway, model, accessKey.getId());
		gatewayService.update(gateway, accessKey.getId());
		logInfo(method, "updated existing gateway: %s --> %s", gateway.getName(), gateway.getId());
		return gateway;
	}

	protected void doHeartbeat(String hid, String callingMethod, HttpServletRequest request) {
		String method = "doHeartbeat";

		AccessKey accessKey = validateCanReadGateway(hid);

		// find gateway
		Gateway gateway = gatewayService.getGatewayRepository().doFindByHid(hid);
		Assert.notNull(gateway, "Gateway not found for hid: " + hid);

		auditLog(callingMethod, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		getHeartbeatService().create(HeartbeatObjectType.GATEWAY, gateway.getId());
		getLastHeartbeatService().update(HeartbeatObjectType.GATEWAY, gateway.getId());
		logDebug(method, "heartbeat received for gateway %s", gateway.getId());
	}

	private Gateway processCloudPlatformIntegration(Gateway gateway) {
		String method = "cloudPlatformIntegration";

		switch (findGatewayIoTProvider(gateway)) {
		case AWS:
			AwsThing thing = awsIntService.checkAndCreateAwsThing(gateway);
			if (thing != null) {
				logInfo(method, "AWS integration OK: %s", thing.getThingArn());
			} else {
				logError(method, "AWS integration ERROR!");
			}
			break;
		case IBM:
			IbmGateway ibmGateway = ibmIntService.checkAndCreateGateway(gateway);
			if (ibmGateway != null) {
				logInfo(method, "IBM integration OK!");
			} else {
				logError(method, "IBM integration ERROR!");
			}
			break;
		case AZURE:
			AzureDevice azureDevice = azureIntService.checkAndCreateAzureDevice(gateway);
			if (azureDevice != null) {
				logInfo(method, "Azure integration OK: %s", azureDevice.getPri());
			} else {
				logError(method, "Azure integration ERROR!");
			}
			break;
		case ArrowConnect:
			break;
		default:
			break;
		}
		return gateway;
	}

	@Override
	protected String getProductSystemName() {
		return ProductSystemNames.KRONOS;
	}

	private String validateJson(String content) {
		AcsUtils.isNotEmpty(content);
		try {
			localObjectMapper.readTree(content);
			return content;
		} catch (Exception e) {
			throw new AcsLogicalException("Invalid JSON", e);
		}
	}

	private void validateApplicationIdMismatched(Application sourceApplication, Application destinationApplication) {
		String method = "validateApplicationIdMismatched";
		checkEnabled(sourceApplication, "sourceApplication");
		checkEnabled(destinationApplication, "destinationApplication");

		KronosApplication kronosApp = kronosApplicationService.getKronosApplicationRepository()
				.findByApplicationId(destinationApplication.getId());
		Assert.notNull(kronosApp, "kronosApplication not found for " + destinationApplication.getId());
		if (!kronosApp.isAllowCreateGatewayFromDifferentApp()) {
			logError(method, "mismatched applicationId not allowed");
			throw new NotAuthorizedException();
		}

		// TODO need to comment out for developer registration to work, may need
		// to revisit

		// Assert.isTrue(StringUtils.equals(sourceApplication.getCompanyId(),
		// destinationApplication.getCompanyId()),
		// "companyId mismatched");
		if (!StringUtils.equals(sourceApplication.getCompanyId(), destinationApplication.getCompanyId())) {
			logWarn(method, "companyId mismatched, ignoring ...");
		}
	}

	private Event doStartStopDevice(String eventName, String gatewayHid, String deviceHid, String callingMethod,
			HttpServletRequest request) {
		Assert.hasText(gatewayHid, "gateway hid is empty");
		Assert.hasText(deviceHid, "device hid is empty");
		AccessKey accessKey = validateCanWriteGateway(gatewayHid);

		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);
		auditLog(callingMethod, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		Device device = kronosCache.findDeviceByHid(deviceHid);
		Assert.notNull(device, "device not found");
		Assert.isTrue(gateway.getId().equals(device.getGatewayId()), "gateway does not match");

		EventBuilder builder = EventBuilder.create().applicationId(accessKey.getApplicationId()).name(eventName)
				.parameter(EventParameter.InString("deviceHid", device.getHid()));
		Event event = gatewayCommandService.sendEvent(builder.build(), device.getGatewayId(), accessKey);
		return event;
	}

	private Event doSendDeviceCommand(String gatewayHid, String deviceHid, String callingMethod,
			HttpServletRequest request) {
		String method = "doSendDeviceCommand";
		Assert.hasText(gatewayHid, "gateway hid is empty");

		DeviceCommandModel deviceCommandModel = JsonUtils.fromJson(getApiPayload(), DeviceCommandModel.class);
		Assert.notNull(deviceCommandModel, "deviceCommandModel is null");
		if (deviceHid != null) {
			deviceCommandModel.setDeviceHid(deviceHid);
		}
		Assert.hasText(deviceCommandModel.getDeviceHid(), "device hid is empty");
		Assert.hasText(deviceCommandModel.getCommand(), "command is empty");
		Assert.isTrue(
				deviceCommandModel.getMessageExpiration() == null || deviceCommandModel.getMessageExpiration() > 0,
				"invalid message expiration");
		String payload = validateJson(deviceCommandModel.getPayload());

		AccessKey accessKey = validateCanWriteGateway(gatewayHid);
		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);

		auditLog(callingMethod, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		Device device = kronosCache.findDeviceByHid(deviceCommandModel.getDeviceHid());
		Assert.notNull(device, "device not found");
		Assert.isTrue(gateway.getId().equals(device.getGatewayId()), "gateway does not match");

		EventBuilder eventBuilder = EventBuilder.create().applicationId(accessKey.getApplicationId())
				.name(AcnEventNames.ServerToGateway.DEVICE_COMMAND)
				.parameter(EventParameter.InString("deviceHid", deviceCommandModel.getDeviceHid()))
				.parameter(EventParameter.InString("command", deviceCommandModel.getCommand()))
				.parameter(EventParameter.InString("payload", payload));

		AccessKey gatewayOwnerKey = getCoreCacheService().findOwnerAccessKeyByPri(gateway.getPri());
		Assert.notNull(gatewayOwnerKey, "gateway owner is not found");

		Event event = gatewayCommandService.sendEvent(eventBuilder.build(), gateway.getId(), gatewayOwnerKey, null,
				deviceCommandModel.getMessageExpiration());
		logInfo(method, "event has been sent to device=%s: eventHid=%s", deviceHid, event.getHid());
		return event;
	}

	private IoTProvider findGatewayIoTProvider(Gateway gateway) {
		String method = "findGatewayIoTProvider";
		IoTProvider iotProvider = null;
		if (StringUtils.isNotEmpty(gateway.getUserId())) {
			// first search for KronosUser
			KronosUser kronosUser = kronosUserService.getKronosUserRepository()
					.findByUserIdAndApplicationId(gateway.getUserId(), gateway.getApplicationId());
			if (kronosUser != null) {
				// set IoTProvider to the one configured at user level
				iotProvider = kronosUser.getIotProvider();
				logInfo(method, "iotProvider at user level: %s", iotProvider);
			}
		}
		if (iotProvider == null) {
			// if IoTProvider is not configured at user level or set to null
			KronosApplication kronosApp = kronosApplicationService.getKronosApplicationRepository()
					.findByApplicationId(gateway.getApplicationId());
			Assert.notNull(kronosApp, "kronosApplication not found: " + gateway.getApplicationId());
			iotProvider = kronosApp.getIotProvider();
			logInfo(method, "iotProvider at application level: %s", iotProvider);
			Assert.notNull(iotProvider, "iotProvider is not found");
		}
		return iotProvider;
	}

	protected GatewayModel populateModel(GatewayModel model, Gateway gateway) {
		model.setHid(gateway.getHid());
		model.setName(gateway.getName());
		model.setUid(gateway.getUid());
		model.setInfo(gateway.getInfo());
		model.setProperties(gateway.getProperties());

		model.setType(GatewayModel.GatewayType.valueOf(gateway.getType().name()));
		if (StringUtils.isNotEmpty(gateway.getDeviceTypeId())) {
			DeviceType deviceType = getKronosCache().findDeviceTypeById(gateway.getDeviceTypeId());
			if (deviceType != null) {
				model.setDeviceType(deviceType.getName());
			}
		}
		model.setOsName(gateway.getOsName());
		if (StringUtils.isNotEmpty(gateway.getSoftwareReleaseId())) {
			SoftwareRelease softwareRelease = getRequestCache().findSoftwareReleaseById(gateway.getSoftwareReleaseId());
			if (softwareRelease != null) {
				model.setSoftwareReleaseHid(softwareRelease.getHid());
				model.setSoftwareReleaseVersion(getSoftwareReleaseService().buildSoftwareVersion(softwareRelease));
				SoftwareProduct softwareProduct = getRequestCache()
						.findSoftwareProductById(softwareRelease.getSoftwareProductId());
				if (softwareProduct != null) {
					model.setSoftwareReleaseName(softwareProduct.getName());
				}
			}
		}
		model.setSoftwareName(gateway.getSoftwareName());
		model.setSoftwareVersion(gateway.getSoftwareVersion());
		model.setSdkVersion(gateway.getSdkVersion());
		if (StringUtils.isNotEmpty(gateway.getUserId())) {
			User user = getCoreCacheService().findUserById(gateway.getUserId());
			if (user != null) {
				model.setUserHid(user.getHid());
			}
		}
		if (StringUtils.isNotEmpty(gateway.getNodeId())) {
			Node node = getRequestCache().findNodeById(gateway.getNodeId());
			if (node != null) {
				model.setNodeHid(node.getHid());
			}
		}
		if (StringUtils.isNotEmpty(gateway.getApplicationId())) {
			Application application = getRequestCache().findApplicationById(gateway.getApplicationId(), false);
			if (application != null) {
				model.setApplicationHid(application.getHid());
			}
		}
		model.setCreatedDate(gateway.getCreatedDate());
		model.setCreatedBy(gateway.getCreatedBy());
		model.setLastModifiedDate(gateway.getLastModifiedDate());
		model.setLastModifiedBy(gateway.getLastModifiedBy());
		return model;
	}

	protected Gateway populateGateway(Gateway gateway, CreateGatewayModel model, String who) {
		String method = "populateGateway";

		if (StringUtils.isNotEmpty(model.getName()))
			gateway.setName(model.getName());
		if (model.getType() != null)
			gateway.setType(GatewayType.valueOf(model.getType().name()));
		if (StringUtils.isNotEmpty(model.getOsName()))
			gateway.setOsName(model.getOsName());
		if (StringUtils.isNotEmpty(model.getSoftwareName()))
			gateway.setSoftwareName(model.getSoftwareName());
		if (StringUtils.isNotEmpty(model.getSoftwareVersion()))
			gateway.setSoftwareVersion(model.getSoftwareVersion());
		if (StringUtils.isNotEmpty(model.getUid()))
			gateway.setUid(model.getUid());
		if (StringUtils.isNotEmpty(model.getSdkVersion()))
			gateway.setSdkVersion(model.getSdkVersion());

		if (StringUtils.isNotEmpty(model.getUserHid())) {
			User user = getCoreCacheService().findUserByHid(model.getUserHid());
			Assert.notNull(user, "invalid userHid: " + model.getUserHid());
			logInfo(method, "found user: %s", user.getContact().getEmail());
			if (user.getStatus() != UserStatus.Active) {
				throw new AcsLogicalException("user is not active");
			}
			gateway.setUserId(user.getId());
		}

		if (StringUtils.isBlank(model.getDeviceType())) {
			// gateway device type omitted
			if (StringUtils.isBlank(gateway.getDeviceTypeId())) {
				DeviceType deviceType = getDeviceTypeService().checkCreateDefaultGatewayType(gateway.getApplicationId(),
						who);
				gateway.setDeviceTypeId(deviceType.getId());
			}
			// else - do not change gateway device type
		} else {
			DeviceType deviceType = checkCreateNewDeviceType(model.getDeviceType().trim(), gateway.getApplicationId(),
					who);
			gateway.setDeviceTypeId(deviceType.getId());
		}

		if (StringUtils.isNotEmpty(model.getSoftwareName()) || StringUtils.isNotEmpty(model.getSoftwareVersion())) {
			gateway.setSoftwareReleaseId(getSoftwareReleaseService().findRheaSoftwareReleaseId(
					gateway.getDeviceTypeId(), gateway.getSoftwareName(), gateway.getSoftwareVersion()));
		}

		// check and update info
		Map<String, String> info = model.getInfo();
		if (info != null && info.size() > 0) {
			for (String name : info.keySet()) {
				String value = StringUtils.trimToEmpty(info.get(name));
				Map<String, String> existingInfo = gateway.getInfo();
				String oldValue = StringUtils.trimToEmpty(existingInfo.get(name));
				if (!value.equals(oldValue)) {
					existingInfo.put(name, value);
				}
			}
		}

		// check and update properties
		Map<String, String> newProperties = model.getProperties();
		if (newProperties != null && newProperties.size() > 0) {
			for (String name : newProperties.keySet()) {
				String value = StringUtils.trimToEmpty(newProperties.get(name));
				Map<String, String> existingProperties = gateway.getProperties();
				String oldValue = StringUtils.trimToEmpty(existingProperties.get(name));
				if (!value.equals(oldValue)) {
					existingProperties.put(name, value);
				}
			}
		}

		return gateway;
	}

	private DeviceType checkCreateNewDeviceType(String deviceTypeName, String applicationId, String who) {
		String method = "checkCreateNewDeviceType";
		DeviceType type = getKronosCache().findDeviceTypeByName(applicationId, deviceTypeName);
		if (type == null) {
			try {
				type = new DeviceType();
				type.setName(deviceTypeName);
				type.setDescription(deviceTypeName);
				type.setApplicationId(applicationId);
				type.setDeviceCategory(AcnDeviceCategory.GATEWAY);
				type.setEnabled(true);

				type = getDeviceTypeService().create(type, who);
				logInfo(method, "created new device type: %s", deviceTypeName);
			} catch (Throwable t) {
				logError(method, "error creating new device type, could be duplicate!");
				type = getKronosCache().findDeviceTypeByName(applicationId, deviceTypeName);
			}
			Assert.notNull(type, "unable to create new device type");
		}
		return type;
	}

	@ApiOperation(value = "get available firmware for gateway by hid")
	@RequestMapping(path = "/{hid}/firmware/available", method = RequestMethod.GET)
	public List<AvailableFirmwareModel> availableFirmware(
			@ApiParam(value = "gateway hid", required = true) @PathVariable(value = "hid") String hid,
			HttpServletRequest request) {
		validateCanReadGateway(hid);
		return findAvailableSoftwarereleases(hid, AcnDeviceCategory.GATEWAY);
	}
}
