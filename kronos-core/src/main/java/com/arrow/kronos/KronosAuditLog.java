package com.arrow.kronos;

public interface KronosAuditLog {

	public interface Application {
		public final static String CreateApplication = "CreateApplication";
		public final static String UpdateApplication = "UpdateApplication";
	}

	public interface UserRegistration {
		public final static String CreateUserRegistration = "CreateUserRegistration";
		public final static String UpdateUserRegistration = "UpdateUserRegistration";
	}

	public interface User {
		public final static String CreateUser = "CreateUser";
		public final static String UpdateUser = "UpdateUser";
	}

	public interface Account {
		public final static String LoginOK = "LoginOK";
		public final static String LoginFailed = "LoginFailed";
		public final static String CreateAccount = "CreateAccount";
		public final static String UpdateAccount = "UpdateAccount";
	}

	public interface Device {
		public final static String CreateDevice = "CreateDevice";
		public final static String UpdateDevice = "UpdateDevice";
		public final static String DeleteDevice = "DeleteDevice";
		public final static String MoveDeviceOut = "MoveDeviceOut";
		public final static String MoveDeviceIn = "MoveDeviceIn";
		public final static String CreateDeviceAction = "CreateDeviceAction";
		public final static String UpdateDeviceAction = "UpdateDeviceAction";
		public final static String DeleteDeviceActions = "DeleteDeviceActions";
		public final static String EnableDevice = "EnableDevice";
		public final static String DisableDevice = "DisableDevice";
		public final static String DeviceError = "DeviceError";
		public final static String BackupDeviceConfiguration = "BackupDeviceConfiguration";
		public final static String RestoreDeviceConfiguration = "RestoreDeviceConfiguration";
		public final static String SendCommand = "SendCommand";
	}

	public interface DeviceType {
		public final static String CreateDeviceType = "CreateDeviceType";
		public final static String UpdateDeviceType = "UpdateDeviceType";
		public final static String EnableDeviceType = "EnableDeviceType";
		public final static String DisableDeviceType = "DisableDeviceType";
	}

	public interface DeviceActionType {
		public final static String CreateDeviceActionType = "CreateDeviceActionType";
		public final static String UpdateDeviceActionType = "UpdateDeviceActionType";
	}

	public interface DeviceEvent {
		public final static String UpdateDeviceEvent = "UpdateDeviceEvent";
		public final static String DeleteDeviceEvent = "DeleteDeviceEvent";
		public final static String MoveDeviceEventOut = "MoveDeviceEventOut";
		public final static String MoveDeviceEventIn = "MoveDeviceEventIn";
	}

	public interface Gateway {
		public final static String CreateGateway = "CreateGateway";
		public final static String UpdateGateway = "UpdateGateway";
		public final static String GatewayCheckin = "GatewayCheckin";
		public final static String DisableGateway = "DisableGateway";
		public final static String EnableGateway = "EnableGateway";
		public final static String GatewayError = "GatewayError";
		public final static String BackupGatewayConfiguration = "BackupGatewayConfiguration";
		public final static String RestoreGatewayConfiguration = "RestoreGatewayConfiguration";
		public final static String DeleteGateway = "DeleteGateway";
		public final static String MoveGatewayOut = "MoveGatewayOut";
		public final static String MoveGatewayIn = "MoveGatewayIn";
	}

	public interface NodeType {
		public final static String CreateNodeType = "CreateNodeType";
		public final static String UpdateNodeType = "UpdateNodeType";
	}

	public interface Node {
		public final static String CreateNode = "CreateNode";
		public final static String UpdateNode = "UpdateNode";
	}

	public interface Api {
		public final static String ApiStarted = "ApiStarted";
		public final static String ApiStopped = "ApiStopped";
		public final static String ApiMethod = "ApiMethod";

		public interface Parameter {
			public final static String urlParams = "urlParams";
			public final static String requestParams = "requestParams";
			public final static String requestUri = "requestUri";
			public final static String requestBody = "requestBody";
			public final static String requestMappingPath = "requestMappingPath";
			public final static String apiName = "apiName";
			public final static String apiMethodName = "apiMethodName";
			public final static String httpMethod = "httpMethod";
			public final static String accessKey = "accessKey";
		}
	}

	public interface Engine {
		public final static String EngineStarted = "EngineStarted";
		public final static String EngineStopped = "EngineStopped";
	}

	public interface AwsAccount {
		public final static String CreateAwsAccount = "CreateAwsAccount";
		public final static String UpdateAwsAccount = "UpdateAwsAccount";
	}

	public interface IbmAccount {
		public final static String CreateIbmAccount = "CreateIbmAccount";
		public final static String UpdateIbmAccount = "UpdateIbmAccount";
	}

	public interface AzureAccount {
		public final static String CreateAzureAccount = "CreateAzureAccount";
		public final static String UpdateAzureAccount = "UpdateAzureAccount";
	}

	public interface TelemetryReplayType {
		public final static String CreateTelemetryReplayType = "CreateTelemetryReplayType";
		public final static String UpdateTelemetryReplayType = "UpdateTelemetryReplayType";
	}

	public interface TelemetryReplay {
		public final static String CreateTelemetryReplay = "CreateTelemetryReplay";
		public final static String UpdateTelemetryReplay = "UpdateTelemetryReplay";
	}

	public interface TelemetryUnit {
		public final static String CreateTelemetryUnit = "CreateTelemetryUnit";
		public final static String UpdateTelemetryUnit = "UpdateTelemetryUnit";
	}

	public interface TelemetryCleanup {
		public final static String CleanupTelemetryByApplicationId = "CleanupTelemetryByApplicationId";
		public final static String CleanupTelemetryItemByApplicationId = "CleanupTelemetryItemByApplicationId";
		public final static String CleanupElasticSearchByApplicationId = "CleanupElasticSearchByApplicationId";

		public final static String CleanupTelemetryByDeviceId = "CleanupTelemetryByDeviceId";
		public final static String CleanupTelemetryItemByDeviceId = "CleanupTelemetryItemByDeviceId";
		public final static String CleanupElasticSearchByDeviceId = "CleanupElasticSearchByDeviceId";
	}

	public interface Developer {
		public final static String RegisterDeveloper = "RegisterDeveloper";
		public final static String VerifyDeveloperAccount = "VerifyDeveloperAccount";
		public final static String ActivateDeveloperAccount = "ActivateDeveloperAccount";
		public final static String ExpireUnverifiedDeveloperAccount = "ExpireUnverifiedDeveloperAccount";
		public final static String ExpireDeveloperAccount = "ExpireDeveloperAccount";
		public final static String DeactivateDeveloperAccount = "DeactivateDeveloperAccount";
		public final static String ReactivateDeveloperAccount = "ReactivateDeveloperAccount";
		public final static String SetDeveloperPassword = "SetDeveloperPassword";
		public final static String ResetDeveloperPassword = "ResetDeveloperPassword";
	}

	public interface DeviceState {
		public final static String CreateDeviceState = "CreateDeviceState";
		public final static String UpdateDeviceState = "UpdateDeviceState";
		public final static String DeleteDeviceState = "DeleteDeviceState";
		public final static String MoveDeviceStateOut = "MoveDeviceStateOut";
		public final static String MoveDeviceStateIn = "MoveDeviceStateIn";
	}

	public interface DeviceStateTrans {
		public final static String CreateDeviceStateTrans = "CreateDeviceStateTrans";
		public final static String UpdateDeviceStateTrans = "UpdateDeviceStateTrans";
		public final static String DeleteDeviceStateTrans = "DeleteDeviceStateTrans";
		public final static String MoveDeviceStateTransOut = "MoveDeviceStateTransOut";
		public final static String MoveDeviceStateTransIn = "MoveDeviceStateTransIn";
	}

	public interface DeviceProduct {
		public final static String CreateDeviceProduct = "CreateDeviceProduct";
		public final static String UpdateDeviceProduct = "UpdateDeviceProduct";
	}

	public interface Manufacturer {
		public final static String CreateManufacturer = "CreateManufacturer";
		public final static String UpdateManufacturer = "UpdateManufacturer";
	}

	public interface SoftwareVendor {
		public final static String CreateSoftwareVendor = "CreateSoftwareVendor";
		public final static String UpdateSoftwareVendor = "UpdateSoftwareVendor";
	}

	public interface SoftwareRelease {
		public final static String CreateSoftwareRelease = "CreateSoftwareRelease";
		public final static String UpdateSoftwareRelease = "UpdateSoftwareRelease";
	}

	public interface SoftwareProduct {
		public final static String CreateSoftwareProduct = "CreateSoftwareProduct";
		public final static String UpdateSoftwareProduct = "UpdateSoftwareProduct";
	}

	public interface SoftwareReleaseSchedule {
		public final static String CreateSoftwareReleaseSchedule = "CreateSoftwareReleaseSchedule";
		public final static String UpdateSoftwareReleaseSchedule = "UpdateSoftwareReleaseSchedule";
		public final static String SoftwareReleaseScheduleAlert = "SoftwareReleaseScheduleAlert";
		public final static String SoftwareReleaseScheduleAssetStarted = "SoftwareReleaseScheduleAssetStarted";
		public final static String SoftwareReleaseScheduleAssetReceived = "SoftwareReleaseScheduleAssetReceived";
		public final static String SoftwareReleaseScheduleAssetManuallyReceived = "SoftwareReleaseScheduleAssetManuallyReceived";
		public final static String SoftwareReleaseScheduleAssetSucceeded = "SoftwareReleaseScheduleAssetSucceeded";
		public final static String SoftwareReleaseScheduleAssetFailed = "SoftwareReleaseScheduleAssetFailed";
		public final static String SoftwareReleaseScheduleAssetManuallyFailed = "SoftwareReleaseScheduleAssetManuallyFailed";
		public final static String SoftwareReleaseScheduleAssetExpired = "SoftwareReleaseScheduleAssetExpired";
		public final static String SoftwareReleaseScheduleAssetRetried = "SoftwareReleaseScheduleAssetRetried";
		public final static String SoftwareReleaseScheduleAssetManuallyRetried = "SoftwareReleaseScheduleAssetManuallyRetried";
		public final static String SoftwareReleaseScheduleAssetCancelled = "SoftwareReleaseScheduleAssetCancelled";
		public final static String SoftwareReleaseScheduleAssetFirmwareDownload = "SoftwareReleaseScheduleAssetFirmwareDownload";
		public final static String SoftwareReleaseScheduleAssetTempTokenCreated = "SoftwareReleaseScheduleAssetTempTokenCreated";
		public final static String SoftwareReleaseScheduleAssetTempTokenExpired = "SoftwareReleaseScheduleAssetTempTokenExpired";
		public final static String SoftwareReleaseScheduleAssetCommandSentToGateway = "SoftwareReleaseScheduleAssetCommandSentToGateway";

		public interface Params {
			public final static String ASSET_CATEGORY = "deviceCategory";
			public final static String OBJECT_ID = "objectId";
			public final static String ASSET_NAME = "assetName";
			public final static String ASSET_UID = "assetUid";
			public final static String ERROR = "error";
		}
	}

	public interface SoftwareReleaseTrans {
		public final static String CreateSoftwareReleaseTrans = "CreateSoftwareReleaseTrans";
		public final static String UpdateSoftwareReleaseTrans = "UpdateSoftwareReleaseTrans";
		public final static String SoftwareReleaseTransStarted = "SoftwareReleaseTransStarted";
		public final static String SoftwareReleaseTransReceived = "SoftwareReleaseTransReceived";
		public final static String SoftwareReleaseTransManuallyReceived = "SoftwareReleaseTransManuallyReceived";
		public final static String SoftwareReleaseTransSucceeded = "SoftwareReleaseTransSucceeded";
		public final static String SoftwareReleaseTransFailed = "SoftwareReleaseTransFailed";
		public final static String SoftwareReleaseTransManuallyFailed = "SoftwareReleaseTransManuallyFailed";
		public final static String SoftwareReleaseTransExpired = "SoftwareReleaseTransExpired";
		public final static String SoftwareReleaseTransRetried = "SoftwareReleaseTransRetried";
		public final static String SoftwareReleaseTransManuallyRetried = "SoftwareReleaseTransManuallyRetried";
		public final static String SoftwareReleaseTransCancelled = "SoftwareReleaseTransCancelled";
		public final static String SoftwareReleaseTransFirmwareDownload = "SoftwareReleaseTransFirmwareDownload";
		public final static String SoftwareReleaseTransTempTokenCreated = "SoftwareReleaseTransTempTokenCreated";
		public final static String SoftwareReleaseTransTempTokenExpired = "SoftwareReleaseTransTempTokenExpired";
		public final static String SoftwareReleaseTransCommandSentToGateway = "SoftwareReleaseTransCommandSentToGateway";
	}

	public interface TestProcedure {
		public final static String CreateTestProcedure = "CreateTestProcedure";
		public final static String UpdateTestProcedure = "UpdateTestProcedure";
	}

	public interface TestResult {
		public final static String CreateTestResult = "CreateTestResult";
		public final static String UpdateTestResult = "UpdateTestResult";
		public final static String DeleteTestResult = "DeleteTestResult";
		public final static String MoveTestResultOut = "MoveTestResultOut";
		public final static String MoveTestResultIn = "MoveTestResultIn";
	}

	public interface ConfigBackup {
		public final static String CreateConfigBackup = "CreateConfigBackup";
		public final static String UpdateConfigBackup = "UpdateConfigBackup";
		public final static String DeleteConfigBackup = "DeleteConfigBackup";
	}

	public interface GlobalActionType {
		public final static String CreateGlobalActionType = "CreateGlobalActionType";
		public final static String UpdateGlobalActionType = "UpdateGlobalActionType";
	}

	public interface GlobalAction {
		public final static String CreateGlobalAction = "CreateGlobalAction";
		public final static String UpdateGlobalAction = "UpdateGlobalAction";
	}

	public interface SocialEventRegistration {
		public final static String CreateSocialEventRegistration = "CreateSocialEventRegistration";
		public final static String UpdateSocialEventRegistration = "UpdateSocialEventRegistration";
		public final static String DeleteSocialEventRegistration = "DeleteSocialEventRegistration";
	}

	public interface GlobalTag {
		public final static String CreateGlobalTag = "CreateGlobalTag";
		public final static String UpdateGlobalTag = "UpdateGlobalTag";
	}

	public interface SocialEventDevice {
		public final static String CreateSocialEventDevice = "CreateSocialEventDevice";
		public final static String UpdateSocialEventDevice = "UpdateSocialEventDevice";
	}
}
