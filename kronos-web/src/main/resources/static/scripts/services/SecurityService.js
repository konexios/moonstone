services.factory('SecurityService',
    [ '$cookies', '$rootScope',
    function($cookies, $rootScope) {
        var KRONOS_ME = 'kronosme';
        var KRONOS_APP = 'kronosapp';

        function isLoggedIn() {
            return getKronosMe() != null && getKronosApp() != null;
        }

        function getKronosMe() {
            return $cookies.get(KRONOS_ME) || null;
        }

        function getKronosApp() {
            return $cookies.get(KRONOS_APP) || null;
        }

        function saveSession(login, hid) {
            deleteSession();

            if (login != null && login != undefined && login != "")
                $cookies.put(KRONOS_ME, login);

            if (hid != null && hid != undefined && hid != "")
                $cookies.put(KRONOS_APP, hid);
        }

        function deleteSession() {
            $cookies.remove(KRONOS_ME);
            $cookies.remove(KRONOS_APP);
        }

        function getUser() {
            return $rootScope.user;
        }

        function hasPrivilege(privilegeName) {
            var user = getUser();
            return (user != null && userHasPrivilege(user, privilegeName));
        }

        function isAdmin() {
            var user = getUser();

            return (user != null && user.admin);
        }

        function userHasPrivilege(user, privilegeName) {

            if (user == null || user == undefined || privilegeName == null || privilegeName == undefined || privilegeName == "")
                return false;

            var applicationInstance = $rootScope.app;
            if (applicationInstance == null || applicationInstance == undefined)
                return false;

            var result = false;

            outerLoop: for (var i = 0; i < user.applications.length; i++) {
                var application = user.applications[i];
                if (application.id === applicationInstance.id) {
                    for (var p = 0; p < application.privileges.length; p++) {
                        var privilege = application.privileges[p];
                        if (privilege.name == privilegeName) {
                            result = true;
                            break outerLoop;
                        }
                    }
                }
            }

            return result;
        }

        // privileges
        function canViewAllDevices() {
            return hasPrivilege('KRONOS_VIEW_ALL_DEVICES');
        }

        function canReadDeviceTypes() {
            return hasPrivilege('KRONOS_VIEW_DEVICE_TYPES');
        }

        function canCreateDeviceType() {
            return hasPrivilege('KRONOS_CREATE_DEVICE_TYPE');
        }

        function canEditDeviceType() {
            return hasPrivilege('KRONOS_EDIT_DEVICE_TYPE');
        }

        function canEditDevice() {
            return hasPrivilege('KRONOS_EDIT_DEVICE');
        }

        function canCreateDevice() {
            return hasPrivilege('KRONOS_CREATE_DEVICE');
        }

        function canDeleteDevice() {
            return hasPrivilege('KRONOS_DELETE_DEVICE');
        }

        function canSendCommandToDevice() {
            return hasPrivilege('KRONOS_SEND_COMMAND_TO_DEVICE');
        }

        function canAddDeviceTag() {
            return hasPrivilege('KRONOS_ADD_DEVICE_TAG');
        }

        function canRemoveDeviceTag() {
            return hasPrivilege('KRONOS_REMOVE_DEVICE_TAG');
        }

        function canCreateDeviceTag() {
            return hasPrivilege('KRONOS_CREATE_DEVICE_TAG');
        }

        function canReadGlobalActions() {
            return hasPrivilege('KRONOS_VIEW_GLOBAL_ACTIONS');
        }

        function canCreateGlobalAction() {
            return hasPrivilege('KRONOS_CREATE_GLOBAL_ACTION');
        }

        function canEditGlobalAction() {
            return hasPrivilege('KRONOS_EDIT_GLOBAL_ACTION');
        }

        function canDeleteGlobalAction() {
            return hasPrivilege('KRONOS_DELETE_GLOBAL_ACTION');
        }

        function canCreateDeviceAction() {
            return hasPrivilege('KRONOS_CREATE_DEVICE_ACTION');
        }

        function canEditDeviceAction() {
            return hasPrivilege('KRONOS_EDIT_DEVICE_ACTION');
        }

        function canDeleteDeviceAction() {
            return hasPrivilege('KRONOS_DELETE_DEVICE_ACTION');
        }

        function canEditDeviceEvent() {
            return hasPrivilege('KRONOS_EDIT_DEVICE_EVENT');
        }

        function canReadNodeTypes() {
            return hasPrivilege('KRONOS_VIEW_NODE_TYPES');
        }

        function canCreateNodeType() {
            return hasPrivilege('KRONOS_CREATE_NODE_TYPE');
        }

        function canEditNodeType() {
            return hasPrivilege('KRONOS_EDIT_NODE_TYPE');
        }

        function canReadNodes() {
            return hasPrivilege('KRONOS_VIEW_NODES');
        }

        function canCreateNode() {
            return hasPrivilege('KRONOS_CREATE_NODE');
        }

        function canEditNode() {
            return hasPrivilege('KRONOS_EDIT_NODE');
        }

        function canReadGateways() {
            return hasPrivilege('KRONOS_VIEW_GATEWAYS');
        }

        function canEditGateway() {
            return hasPrivilege('KRONOS_EDIT_GATEWAY');
        }

        function canReadRightToUse() {
            return hasPrivilege('KRONOS_RIGHT_TO_USE_FIRMWARE');
        }

        function canSendRequestToFirmware() {
          return hasPrivilege('KRONOS_RIGHT_TO_REQUEST_FIRMWARE');
        }

        function canCreateGateway() {
            return hasPrivilege('KRONOS_CREATE_GATEWAY');
        }

        function canDeleteGateway() {
            return hasPrivilege('KRONOS_DELETE_GATEWAY');
        }

        function canMoveGateway() {
            return hasPrivilege('KRONOS_MOVE_GATEWAY');
        }

        function canViewGatewayAuditLogs() {
            return hasPrivilege('KRONOS_VIEW_GATEWAY_AUDIT_LOGS');
        }

        function canViewDeviceAuditLogs() {
            return hasPrivilege('KRONOS_VIEW_DEVICE_AUDIT_LOGS');
        }

        function canEditIoTConfig() {
            return hasPrivilege('KRONOS_EDIT_IOT_CONFIGURATION');
        }

        function canEditUserIoTConfig() {
            return hasPrivilege('KRONOS_EDIT_USER_IOT_CONFIGURATION');
        }

        function canReadAccessKeys() {
            return hasPrivilege('KRONOS_VIEW_ACCESS_KEYS');
        }

        function canCreateAccessKey() {
            return hasPrivilege('KRONOS_CREATE_ACCESS_KEY');
        }

        function canEditAccessKey() {
            return hasPrivilege('KRONOS_EDIT_ACCESS_KEY');
        }

        function canReadSettings() {
            return hasPrivilege('KRONOS_VIEW_APPLICATION_SETTINGS');
        }

        function canSaveSettings() {
            return hasPrivilege('KRONOS_EDIT_APPLICATION_SETTINGS');
        }

        function canReadGlobalActionTypes() {
            return hasPrivilege('KRONOS_VIEW_GLOBAL_ACTION_TYPES');
        }

        function canCreateGlobalActionType() {
            return hasPrivilege('KRONOS_CREATE_GLOBAL_ACTION_TYPE');
        }

        function canEditGlobalActionType() {
            return hasPrivilege('KRONOS_EDIT_GLOBAL_ACTION_TYPE');
        }

        function canReadDeviceActionTypes() {
            return hasPrivilege('KRONOS_VIEW_DEVICE_ACTION_TYPES');
        }

        function canCreateDeviceActionType() {
            return hasPrivilege('KRONOS_CREATE_DEVICE_ACTION_TYPE');
        }

        function canEditDeviceActionType() {
            return hasPrivilege('KRONOS_EDIT_DEVICE_ACTION_TYPE');
        }

        function canProvisionApplication() {
            return hasPrivilege('KRONOS_PROVISION_APPLICATION');
        }

        function canViewDevicePayload() {
            return hasPrivilege('KRONOS_DEVELOPER_DEVICE_PAYLOAD');
        }

        function canReadSoftwareReleaseSchedule() {
            return hasPrivilege('KRONOS_READ_SOFTWARE_RELEASE_SCHEDULE');
        }

        function canCreateSoftwareReleaseSchedule() {
            return hasPrivilege('KRONOS_CREATE_SOFTWARE_RELEASE_SCHEDULE');
        }

        function canEditSoftwareReleaseSchedule() {
            return hasPrivilege('KRONOS_UPDATE_SOFTWARE_RELEASE_SCHEDULE');
        }

        function canReadSoftwareReleaseTrans() {
            return hasPrivilege('KRONOS_READ_SOFTWARE_RELEASE_TRANS');
        }

        function canReadDeviceState() {
            return hasPrivilege('KRONOS_READ_DEVICE_STATE');
        }

        function canUpdateDeviceState() {
            return hasPrivilege('KRONOS_UPDATE_DEVICE_STATE');
        }

        function canDeleteDeviceState() {
            return hasPrivilege('KRONOS_DELETE_DEVICE_STATE');
        }

        function canUpdateDeviceTelemetry() {
            return hasPrivilege('KRONOS_UPDATE_DEVICE_TELEMETRY');
        }

        function canDeleteDeviceTelemetry() {
            return hasPrivilege('KRONOS_DELETE_DEVICE_TELEMETRY');
        }

        function canReadDeviceStateTrans() {
            return hasPrivilege('KRONOS_READ_DEVICE_STATE_TRANS');
        }

        function canCreateTestProcedure() {
            return hasPrivilege('KRONOS_CREATE_TEST_PROCEDURE');
        }

        function canEditTestProcedure() {
            return hasPrivilege('KRONOS_UPDATE_TEST_PROCEDURE');
        }

        function canReadTestProcedures() {
            return hasPrivilege('KRONOS_READ_TEST_PROCEDURES');
        }

        function canEditTestResult() {
            return hasPrivilege('KRONOS_UPDATE_TEST_RESULT');
        }

        function canReadTestResults() {
            return hasPrivilege('KRONOS_READ_TEST_RESULTS');
        }

        function canReadDeviceTestResults() {
            return hasPrivilege('KRONOS_READ_DEVICE_TEST_RESULTS');
        }

        function canReadGatewayTestResults() {
            return hasPrivilege('KRONOS_READ_GATEWAY_TEST_RESULTS');
        }

        function canSchedulingOnDemand() {
            return hasPrivilege('KRONOS_FWM_ON_DEMAND_SCHEDULING');
        }

        function canSchedulingByDateTime() {
            return hasPrivilege('KRONOS_FWM_DATETIME_SCHEDULING');
        }

        function canCancelSoftwareReleaseSchedule() {
            return hasPrivilege('KRONOS_CANCEL_SOFTWARE_RELEASE_SCHEDULE');
        }

        function canStartSoftwareReleaseSchedule() {
            return hasPrivilege('KRONOS_START_SOFTWARE_RELEASE_SCHEDULE');
        }
        
        function canMoveSoftwareReleaseTransactionToError() {
            return hasPrivilege('KRONOS_SOFTWARE_RELEASE_TRANSACTION_MOVE_TO_ERROR');
        }
        
        function canRetrySoftwareReleaseTransactionOnError() {
            return hasPrivilege('KRONOS_SOFTWARE_RELEASE_TRANSACTION_RETRY_ON_ERROR');
        }
        
        function canRetrySoftwareReleaseTransaction() {
            return hasPrivilege('KRONOS_RETRY_SOFTWARE_RELEASE_TRANSACTION');
        }
        
        function canCancelSoftwareReleaseTransaction() {
            return hasPrivilege('KRONOS_CANCEL_SOFTWARE_RELEASE_TRANSACTION');
        }

        return {
            isLoggedIn: isLoggedIn,
            saveSession: saveSession,
            deleteSession: deleteSession,
            getUserLogin: getKronosMe,
            getUserApp: getKronosApp,
            getUser: getUser,
            // privileges
            isAdmin: isAdmin,
            canViewAllDevices: canViewAllDevices,
            canReadDeviceTypes: canReadDeviceTypes,
            canCreateDeviceType: canCreateDeviceType,
            canEditDeviceType: canEditDeviceType,
            canEditDevice: canEditDevice,
            canCreateDevice: canCreateDevice,
            canDeleteDevice: canDeleteDevice,
            canSendCommandToDevice: canSendCommandToDevice,
            canReadGlobalActions: canReadGlobalActions,
            canCreateGlobalAction: canCreateGlobalAction,
            canEditGlobalAction: canEditGlobalAction,
            canDeleteGlobalAction: canDeleteGlobalAction,
            canCreateDeviceAction: canCreateDeviceAction,
            canEditDeviceAction: canEditDeviceAction,
            canDeleteDeviceAction: canDeleteDeviceAction,
            canEditDeviceEvent: canEditDeviceEvent,
            canAddDeviceTag: canAddDeviceTag,
            canRemoveDeviceTag: canRemoveDeviceTag,
            canCreateDeviceTag: canCreateDeviceTag,
            canReadNodeTypes: canReadNodeTypes,
            canCreateNodeType: canCreateNodeType,
            canEditNodeType: canEditNodeType,
            canReadNodes: canReadNodes,
            canCreateNode: canCreateNode,
            canEditNode: canEditNode,
            canReadGateways: canReadGateways,
            canEditGateway: canEditGateway,
            canCreateGateway: canCreateGateway,
            canDeleteGateway: canDeleteGateway,
            canMoveGateway: canMoveGateway,
            canViewGatewayAuditLogs: canViewGatewayAuditLogs,
            canViewDeviceAuditLogs: canViewDeviceAuditLogs,
            canEditIoTConfig: canEditIoTConfig,
            canEditUserIoTConfig: canEditUserIoTConfig,
            canReadAccessKeys: canReadAccessKeys,
            canReadRightToUse: canReadRightToUse,
            canSendRequestToFirmware: canSendRequestToFirmware,
            canCreateAccessKey: canCreateAccessKey,
            canEditAccessKey: canEditAccessKey,
            canReadSettings: canReadSettings,
            canSaveSettings: canSaveSettings,
            canReadGlobalActionTypes: canReadGlobalActionTypes,
            canCreateGlobalActionType: canCreateGlobalActionType,
            canEditGlobalActionType: canEditGlobalActionType,
            canReadDeviceActionTypes: canReadDeviceActionTypes,
            canCreateDeviceActionType: canCreateDeviceActionType,
            canEditDeviceActionType: canEditDeviceActionType,
            canProvisionApplication: canProvisionApplication,
            canViewDevicePayload: canViewDevicePayload,
            canReadSoftwareReleaseSchedule: canReadSoftwareReleaseSchedule,
            canCreateSoftwareReleaseSchedule: canCreateSoftwareReleaseSchedule,
            canEditSoftwareReleaseSchedule: canEditSoftwareReleaseSchedule,
            canReadSoftwareReleaseTrans: canReadSoftwareReleaseTrans,
            canReadDeviceState: canReadDeviceState,
            canUpdateDeviceState: canUpdateDeviceState,
            canDeleteDeviceState: canDeleteDeviceState,
            canUpdateDeviceTelemetry: canUpdateDeviceTelemetry,
            canDeleteDeviceTelemetry: canDeleteDeviceTelemetry,
            canReadDeviceStateTrans: canReadDeviceStateTrans,
            canCreateTestProcedure: canCreateTestProcedure,
            canEditTestProcedure: canEditTestProcedure,
            canReadTestProcedures: canReadTestProcedures,
            canEditTestResult: canEditTestResult,
            canReadTestResults: canReadTestResults,
            canReadDeviceTestResults: canReadDeviceTestResults,
            canReadGatewayTestResults: canReadGatewayTestResults,
            canSchedulingOnDemand: canSchedulingOnDemand,
            canSchedulingByDateTime: canSchedulingByDateTime,
            canCancelSoftwareReleaseSchedule: canCancelSoftwareReleaseSchedule,
            canStartSoftwareReleaseSchedule: canStartSoftwareReleaseSchedule,
            canMoveSoftwareReleaseTransactionToError: canMoveSoftwareReleaseTransactionToError,
            canRetrySoftwareReleaseTransaction: canRetrySoftwareReleaseTransaction,
            canCancelSoftwareReleaseTransaction: canCancelSoftwareReleaseTransaction, 
            canRetrySoftwareReleaseTransactionOnError: canRetrySoftwareReleaseTransactionOnError
        };
    }
]);
