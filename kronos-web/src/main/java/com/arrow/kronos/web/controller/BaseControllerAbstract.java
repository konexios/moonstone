package com.arrow.kronos.web.controller;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.kronos.repo.DeviceTypeSearchParams;
import com.arrow.kronos.repo.SoftwareReleaseTransSearchParams;
import com.arrow.kronos.service.DeviceTypeService;
import com.arrow.kronos.service.KronosCache;
import com.arrow.kronos.service.NodeService;
import com.arrow.kronos.service.SoftwareReleaseScheduleService;
import com.arrow.kronos.service.SoftwareReleaseService;
import com.arrow.kronos.service.SoftwareReleaseTransService;
import com.arrow.kronos.web.model.AuditLogModels.AuditLogDetails;
import com.arrow.kronos.web.model.AuditLogModels.AuditLogList;
import com.arrow.kronos.web.model.KronosModelUtil;
import com.arrow.kronos.web.model.RheaModels.SoftwareReleaseOption;
import com.arrow.kronos.web.model.SearchFilterModels.AuditLogSearchFilterModel;
import com.arrow.kronos.web.model.SearchResultModels.AuditLogSearchResultModel;
import com.arrow.kronos.web.model.SearchResultModels.SoftwareReleaseTransSearchResultModel;
import com.arrow.kronos.web.model.SoftwareReleaseTransModels.SoftwareReleaseTransModel;
import com.arrow.pegasus.client.api.ClientRoleApi;
import com.arrow.pegasus.client.api.ClientUserApi;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.service.AuditLogService;
import com.arrow.pegasus.util.CoreConfigurationPropertyUtil;
import com.arrow.pegasus.webapi.WebApiAbstract;
import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;
import com.arrow.rhea.client.api.ClientSoftwareReleaseApi;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.service.RheaCacheService;

import moonstone.acn.client.model.AcnDeviceCategory;
import moonstone.acn.client.model.RightToUseType;

public abstract class BaseControllerAbstract extends WebApiAbstract {

	protected final static SimpleDateFormat CUSTOM_DATE_TIME = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	protected final static SimpleDateFormat TELEMETRY_TIMESTAMP = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.S a z");
	protected final static String SOFTWARE_RELEASE_NAME_FORMAT = "%s %d.%d";

	@Autowired
	private ClientUserApi clientUserApi;
	@Autowired
	private ClientRoleApi clientRoleApi;
	@Autowired
	private KronosCache kronosCache;
	@Autowired
	private DeviceTypeService deviceTypeService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private CoreConfigurationPropertyUtil coreConfigurationPropertyUtil;
	@Autowired
	private KronosModelUtil kronosModelUtil;
	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private ClientSoftwareReleaseApi clientSoftwareReleaseApi;
	@Autowired
	private SoftwareReleaseTransService softwareReleaseTransService;
	@Autowired
	SoftwareReleaseService softwareReleaseService;
	@Autowired
	private RheaCacheService RheaCacheService;
	@Autowired
	private ClientSoftwareReleaseApi rheaClientSoftwareReleaseApi;
	@Autowired
	private SoftwareReleaseScheduleService softwareReleaseScheduleService;

	protected ClientUserApi getClientUserApi() {
		return clientUserApi;
	}

	protected ClientRoleApi getClientRoleApi() {
		return clientRoleApi;
	}

	protected KronosCache getKronosCache() {
		return kronosCache;
	}

	protected DeviceTypeService getDeviceTypeService() {
		return deviceTypeService;
	}

	protected CoreConfigurationPropertyUtil getCoreConfigurationPropertyUtil() {
		return coreConfigurationPropertyUtil;
	}

	protected KronosModelUtil getKronosModelUtil() {
		return kronosModelUtil;
	}

	protected AuditLogService getAuditLogService() {
		return auditLogService;
	}

	protected RheaCacheService getRheaCacheService() {
		return RheaCacheService;
	}

	protected boolean isSystemAdmin(User user) {
		Assert.notNull(user, "user is null");
		return user.isAdmin();
	}

	protected boolean isEditableOrIsSystemAdmin(boolean editable, User user) {
		return editable || !editable && isSystemAdmin(user);
	}

	protected SoftwareReleaseTransService getSoftwareReleaseTransService() {
		return softwareReleaseTransService;
	}

	protected SoftwareReleaseService getSoftwareReleaseService() {
		return softwareReleaseService;
	}

	protected ClientSoftwareReleaseApi getRheaClientSoftwareReleaseApi() {
		return rheaClientSoftwareReleaseApi;
	}

	protected AuditLogSearchResultModel findAuditLogs(String objectId, String applicationId,
			AuditLogSearchFilterModel searchFilter) {
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		Instant createdDateFrom = null, createdDateTo = null;
		if (searchFilter.getCreatedDateFrom() != null) {
			createdDateFrom = Instant.ofEpochSecond(searchFilter.getCreatedDateFrom());
		}
		if (searchFilter.getCreatedDateTo() != null) {
			createdDateTo = Instant.ofEpochSecond(searchFilter.getCreatedDateTo());
		}

		Page<AuditLog> auditLogPage = auditLogService.getAuditLogRepository().findAuditLogs(pageRequest, null,
				new String[] { applicationId }, searchFilter.getTypes(), new String[] { objectId }, null,
				createdDateFrom, createdDateTo, searchFilter.getUserIds());

		List<AuditLogList> auditLogModels = new ArrayList<>();
		for (AuditLog auditLog : auditLogPage) {
			User createdBy = getCoreCacheService().findUserById(auditLog.getCreatedBy());
			auditLogModels.add(new AuditLogList(auditLog, createdBy));
		}
		Page<AuditLogList> result = new PageImpl<>(auditLogModels, pageRequest, auditLogPage.getTotalElements());

		return new AuditLogSearchResultModel(result, searchFilter);
	}

	protected AuditLogDetails findAuditLog(String id, String applicationId) {
		AuditLog auditLog = auditLogService.getAuditLogRepository().findById(id).orElse(null);
		Assert.isTrue(applicationId.equals(auditLog.getApplicationId()),
				"user and audit log must have the same application id");

		// move to the AuditLogService.populate
		Application refApplication = getCoreCacheService().findApplicationById(auditLog.getApplicationId());
		auditLog.setRefApplication(refApplication);

		User createdBy = getCoreCacheService().findUserById(auditLog.getCreatedBy());

		return new AuditLogDetails(auditLog, createdBy);
	}

	// all users from the same company with an least one enabled role for the
	// application
	protected List<User> getAvailableUsers(Application application, User authenticatedUser) {
		List<Role> roleList = clientRoleApi.findByApplicationIdAndEnabled(application.getId(), true);
		String[] roles = new String[roleList.size()];
		int i = 0;
		for (Role role : roleList) {
			roles[i++] = role.getId();
		}
		return clientUserApi.findByCompanyIdAndRoleIds(authenticatedUser.getCompanyId(), roles);
	}

	// all enabled nodes for the application
	protected List<Node> getAvailableNodes(Application application) {
		return nodeService.getNodeRepository().findByApplicationIdAndEnabled(application.getId(), true);
	}

	// all enabled device types for the application
	protected List<DeviceType> getAvailableDeviceTypes(String applicationId, AcnDeviceCategory deviceCategory,
			boolean enabled) {
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.notNull(deviceCategory, "deviceCategory is null");

		DeviceTypeSearchParams params = new DeviceTypeSearchParams();
		params.addApplicationIds(applicationId);
		params.setDeviceCategories(EnumSet.of(deviceCategory));
		params.setEnabled(enabled);

		return getDeviceTypeService().getDeviceTypeRepository().findDeviceTypes(params);
	}

	// all software releases for rheaDeviceTypeId and enabled
	protected List<SoftwareReleaseOption> getAvailableSoftwareReleases(String rheaDeviceTypeId, HttpSession session) {
		List<SoftwareRelease> softwareReleases = new ArrayList<>();
		List<SoftwareReleaseOption> softwareReleaseOptions = new ArrayList<>();
		if (!StringUtils.isEmpty(rheaDeviceTypeId)) {

			Application application = getApplication(session);
			boolean hasFirmwareManagementFeature = false;
			EnumSet<RightToUseType> rtuTypes = EnumSet.of(RightToUseType.Unrestricted);
			if (application.getProductFeatures() != null && !application.getProductFeatures().isEmpty()
					&& application.getProductFeatures().contains(KronosConstants.ProductFeatures.FIRMWARE_MANAGEMENT)) {
				hasFirmwareManagementFeature = true;
				rtuTypes.add(RightToUseType.Public);
			}

			String[] rtuTypeNames = new String[rtuTypes.size()];
			int rightToUseNameCount = 0;
			for (RightToUseType rtut : rtuTypes) {
				rtuTypeNames[rightToUseNameCount++] = rtut.name();
			}

			softwareReleases = clientSoftwareReleaseApi.findAll(null, null, new String[] { rheaDeviceTypeId },
					rtuTypeNames, true, null);
			if (hasFirmwareManagementFeature) {
				softwareReleases.addAll(softwareReleaseScheduleService
						.getPrivateApprovedSwReleases(application.getCompanyId(), new String[] { rheaDeviceTypeId }));
			}

			for (SoftwareRelease softwareRelease : softwareReleases) {
				softwareReleaseOptions.add(getSoftwareReleaseOption(softwareRelease));
			}
		}
		return softwareReleaseOptions;
	}

	protected SoftwareReleaseOption getSoftwareReleaseOption(SoftwareRelease softwareRelease) {
		SoftwareReleaseOption softwareReleaseOption = null;
		if (softwareRelease != null) {
			SoftwareProduct softwareProduct = getRheaCacheService()
					.findSoftwareProductById(softwareRelease.getSoftwareProductId());
			softwareReleaseOption = new SoftwareReleaseOption(softwareRelease, softwareProduct.getName());
		}
		return softwareReleaseOption;
	}

	// all softwareReleaseTrans records
	protected SoftwareReleaseTransSearchResultModel getSoftwareReleaseTransRecords(CoreSearchFilterModel searchFilter,
			String applicationId, AcnDeviceCategory deviceCategory, String objectId) {
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.notNull(deviceCategory, "deviceCategory is null");
		Assert.notNull(objectId, "objectId is null");

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		SoftwareReleaseTransSearchParams params = new SoftwareReleaseTransSearchParams();
		params.addApplicationIds(applicationId);
		params.setDeviceCategories(EnumSet.of(deviceCategory));
		params.addObjectIds(objectId);

		Page<SoftwareReleaseTrans> softwareReleaseTransRecords = getSoftwareReleaseTransService()
				.getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(pageRequest, params);

		// convert to visual model
		List<SoftwareReleaseTransModel> softwareReleaseTransModels = new ArrayList<>();
		for (SoftwareReleaseTrans softwareReleaseTransRecord : softwareReleaseTransRecords) {
			SoftwareReleaseTransModel model = new SoftwareReleaseTransModel(softwareReleaseTransRecord);
			model.setFromVersion(getSoftwareReleaseName(softwareReleaseTransRecord.getFromSoftwareReleaseId()));
			model.setToVersion(getSoftwareReleaseName(softwareReleaseTransRecord.getToSoftwareReleaseId()));
			model.setJobName(getJobName(softwareReleaseTransRecord.getSoftwareReleaseScheduleId()));
			softwareReleaseTransModels.add(model);
		}

		Page<SoftwareReleaseTransModel> result = new PageImpl<>(softwareReleaseTransModels, pageRequest,
				softwareReleaseTransRecords.getTotalElements());

		return new SoftwareReleaseTransSearchResultModel(result, searchFilter);
	}

	private String getJobName(String softwareReleaseScheduleId) {
		String softwareReleaseScheduleName = null;
		if (softwareReleaseScheduleId != null) {
			SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
					.findSoftwareReleaseScheduleById(softwareReleaseScheduleId);
			if (softwareReleaseSchedule != null) {
				softwareReleaseScheduleName = softwareReleaseSchedule.getName();
			}
		}
		return softwareReleaseScheduleName;
	}

	// private String getVersionOfSoftwareRelease(String softwareReleaseId) {
	// Assert.notNull(softwareReleaseId, "softwareReleaseId is null");
	// SoftwareRelease sr =
	// getRheaClientCache().findSoftwareReleaseById(softwareReleaseId);
	// Assert.notNull(sr, "software release was not found");
	// String version = sr.getMajor() + "." + sr.getMinor();
	// if (sr.getBuild() != null) {
	// version += "." + sr.getBuild();
	// }
	// return version;
	// }

	protected String getSoftwareReleaseName(String softwareReleaseId) {
		String softwareReleaseName = "Unknown";
		if (StringUtils.isNotEmpty(softwareReleaseId)) {
			SoftwareRelease softwareRelease = getRheaCacheService().findSoftwareReleaseById(softwareReleaseId);
			if (softwareRelease != null && StringUtils.isNotEmpty(softwareRelease.getSoftwareProductId())) {
				SoftwareProduct softwareProduct = getRheaCacheService()
						.findSoftwareProductById(softwareRelease.getSoftwareProductId());
				if (softwareProduct != null) {
					softwareReleaseName = String.format(SOFTWARE_RELEASE_NAME_FORMAT, softwareProduct.getName(),
							softwareRelease.getMajor(), softwareRelease.getMinor());
					if (softwareRelease.getBuild() != null) {
						softwareReleaseName += "." + softwareRelease.getBuild();
					}
				}
			}
		}
		return softwareReleaseName;
	}

	protected String getHardwareVersionName(String assetTypeId) {
		return getHardwareVersion(assetTypeId).getName();
	}

	private com.arrow.rhea.data.DeviceType getHardwareVersion(String hardwareVersionId) {
		Assert.hasText(hardwareVersionId, "hardwareVersionId is empty");

		com.arrow.rhea.data.DeviceType hardwareVersion = getRheaCacheService().findDeviceTypeById(hardwareVersionId);
		Assert.notNull(hardwareVersion, "hardwareVersion not found! rheaDeviceTypeId: " + hardwareVersionId);

		return hardwareVersion;
	}
}
