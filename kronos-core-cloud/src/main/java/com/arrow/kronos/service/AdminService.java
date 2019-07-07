package com.arrow.kronos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.pegasus.client.api.ClientApplicationApi;

@Service
public class AdminService extends KronosServiceAbstract {
	@Autowired
	AwsAccountService awsAccountService;
	@Autowired
	AwsThingService awsThingService;
	@Autowired
	AzureAccountService azureAccountService;
	@Autowired
	AzureDeviceService azureDeviceService;
	@Autowired
	ConfigBackupService configBackupService;
	@Autowired
	DeviceActionTypeService deviceActionTypeService;
	@Autowired
	DeviceEventService deviceEventService;
	@Autowired
	DeviceService deviceService;
	@Autowired
	DeviceStateService deviceStateService;
	@Autowired
	DeviceStateTransService deviceStateTransService;
	@Autowired
	DeviceTypeService deviceTypeService;
	@Autowired
	SpringDataEsTelemetryItemService esTelemetryItemService;
	@Autowired
	GatewayService gatewayService;
	@Autowired
	GlobalActionService globalActionService;
	@Autowired
	GlobalActionTypeService globalActionTypeService;
	@Autowired
	IbmAccountService ibmAccountService;
	@Autowired
	IbmDeviceService ibmDeviceService;
	@Autowired
	IbmGatewayService ibmGatewayService;
	@Autowired
	KronosApplicationService kronosApplicationService;
	@Autowired
	KronosUserService kronosUserService;
	@Autowired
	LastTelemetryItemService lastTelemetryItemService;
	@Autowired
	NodeService nodeService;
	@Autowired
	NodeTypeService nodeTypeService;
	@Autowired
	SoftwareReleaseScheduleService softwareReleaseScheduleService;
	@Autowired
	SoftwareReleaseTransService softwareReleaseTransService;
	@Autowired
	TelemetryItemService telemetryItemService;
	@Autowired
	TelemetryReplayService telemetryReplayService;
	@Autowired
	TelemetryReplayTypeService telemetryReplayTypeService;
	@Autowired
	TelemetryService telemetryService;
	@Autowired
	TestProcedureService testProcedureService;
	@Autowired
	TestResultService testResultService;
	@Autowired
	ClientApplicationApi clientApplicationApi;

	public Long deleteApplication(String applicationId, String who) {
		String method = "deleteApplication";
		long result = 0;

		try {
			logInfo(method, "deleting awsAccounts for applicationId: %s", applicationId);
			long count = awsAccountService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d awsAccounts for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting awsAccounts for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting awsThings for applicationId: %s", applicationId);
			long count = awsThingService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d awsThings for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting awsThings for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting azureAccounts for applicationId: %s", applicationId);
			long count = azureAccountService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d azureAccounts for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting azureAccounts for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting azureDevices for applicationId: %s", applicationId);
			long count = azureDeviceService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d azureDevices for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting awsDevices for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting configBackups for applicationId: %s", applicationId);
			long count = configBackupService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d configBackups for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting configBackups for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting deviceActionTypes for applicationId: %s", applicationId);
			long count = deviceActionTypeService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d deviceActionTypes for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting deviceActionTypes for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting deviceEvents for applicationId: %s", applicationId);
			long count = deviceEventService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d deviceEvents for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting deviceEvents for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting devices for applicationId: %s", applicationId);
			long count = deviceService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d devices for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting devices for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting deviceStates for applicationId: %s", applicationId);
			long count = deviceStateService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d deviceStates for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting deviceStates for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting deviceStateTrans for applicationId: %s", applicationId);
			long count = deviceStateTransService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d deviceStateTrans for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting deviceStateTrans for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting deviceTypes for applicationId: %s", applicationId);
			long count = deviceTypeService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d deviceTypes for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting deviceTypes for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting esTelemetryItems for applicationId: %s", applicationId);
			long count = esTelemetryItemService.deleteByApplicationId(applicationId);
			logInfo(method, "deleted %d esTelemetryItems for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting esTelemetryItems for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting gateways for applicationId: %s", applicationId);
			long count = gatewayService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d gateways for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting gateways for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting globalActions for applicationId: %s", applicationId);
			long count = globalActionService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d globalActions for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting globalActions for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting globalActionTypes for applicationId: %s", applicationId);
			long count = globalActionTypeService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d globalActionTypes for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting globalActionTypes for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting ibmAccounts for applicationId: %s", applicationId);
			long count = ibmAccountService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d ibmAccounts for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting ibmAccounts for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting ibmDevices for applicationId: %s", applicationId);
			long count = ibmDeviceService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d ibmDevices for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting ibmDevices for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting ibmGateways for applicationId: %s", applicationId);
			long count = ibmGatewayService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d ibmGateways for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting ibmGateways for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting kronosApplication for applicationId: %s", applicationId);
			long count = kronosApplicationService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d kronosApplication for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting kronosApplication for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting kronosUsers for applicationId: %s", applicationId);
			long count = kronosUserService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d kronosUsers for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting kronosUsers for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting lastTelemetryItems for applicationId: %s", applicationId);
			long count = lastTelemetryItemService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d lastTelemetryItems for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting lastTelemetryItems for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting nodes for applicationId: %s", applicationId);
			long count = nodeService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d nodes for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting nodes for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting nodeTypes for applicationId: %s", applicationId);
			long count = nodeTypeService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d nodeTypes for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting nodeTypes for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting softwareReleaseSchedules for applicationId: %s", applicationId);
			long count = softwareReleaseScheduleService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d softwareReleaseSchedules for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting softwareReleaseSchedules for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting softwareReleaseTrans for applicationId: %s", applicationId);
			long count = softwareReleaseTransService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d softwareReleaseTrans for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting softwareReleaseTrans for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting telemetryItems for applicationId: %s", applicationId);
			long count = telemetryItemService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d telemetryItems for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting telemetryItems for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting telemetryReplays for applicationId: %s", applicationId);
			long count = telemetryReplayService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d telemetryReplays for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting telemetryReplays for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting telemetries for applicationId: %s", applicationId);
			long count = telemetryService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d telemetries for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting telemetries for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting testProcedures for applicationId: %s", applicationId);
			long count = testProcedureService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d testProcedure for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting testProcedures for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting testResults for applicationId: %s", applicationId);
			long count = testResultService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d testResults for applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting testResults for applicationId: " + applicationId, t);
		}

		try {
			logInfo(method, "deleting pegasus applicationId: %s", applicationId);
			long count = clientApplicationApi.delete(applicationId);
			logInfo(method, "deleted %d records for pegasus applicationId: %s", count, applicationId);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting pegasus application for applicationId: " + applicationId, t);
		}

		return result;
	}
}
