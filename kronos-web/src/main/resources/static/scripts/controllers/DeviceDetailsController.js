controllers.controller('DeviceDetailsController', [
    '$scope', '$routeParams', '$timeout', '$sce', '$uibModal', '$location', 'googleChartApiPromise', 'DeviceService', 'DeviceTypeService', 'EventService', 'SecurityService', 'ErrorService', 'SpinnerService', 'ToastrService', 'ConfigurationService',
    function ($scope, $routeParams, $timeout, $sce, $uibModal, $location, googleChartApiPromise, DeviceService, DeviceTypeService, EventService, SecurityService, ErrorService, SpinnerService, ToastrService, ConfigurationService) {

        $scope.pageTitle = 'Device Details';
        $scope.pageSubTitle = '';

        $scope.loading = true;

        $scope.activeTab = 0;
        $scope.activeSubTab = 0;

        $scope.isAdmin = SecurityService.isAdmin();
        $scope.canEditDevice = SecurityService.canEditDevice();
        $scope.canDeleteDevice = SecurityService.canDeleteDevice();
        $scope.canEditDeviceType = SecurityService.canEditDeviceType();
        $scope.canSendCommandToDevice = SecurityService.canSendCommandToDevice();
        $scope.canCreateDeviceAction = SecurityService.canCreateDeviceAction();
        $scope.canEditDeviceAction = SecurityService.canEditDeviceAction();
        $scope.canDeleteDeviceAction = SecurityService.canDeleteDeviceAction();
        $scope.canAddTag = SecurityService.canAddDeviceTag();
        $scope.canRemoveTag = SecurityService.canRemoveDeviceTag();
        $scope.canCreateTag = SecurityService.canCreateDeviceTag();
        $scope.canViewLogs = SecurityService.canViewDeviceAuditLogs();
        $scope.canViewAccessKeys = SecurityService.canReadAccessKeys();
        $scope.canViewDevicePayload = SecurityService.canViewDevicePayload();
        $scope.canReadSoftwareReleaseTrans = SecurityService.canReadSoftwareReleaseTrans();
        $scope.canReadDeviceState = SecurityService.canReadDeviceState();
        $scope.canUpdateDeviceState = SecurityService.canUpdateDeviceState();
        $scope.canDeleteDeviceState = SecurityService.canDeleteDeviceState();
        $scope.canUpdateDeviceTelemetry = SecurityService.canUpdateDeviceTelemetry();
        $scope.canDeleteDeviceTelemetry = SecurityService.canDeleteDeviceTelemetry();
        $scope.canReadDeviceStateTrans = SecurityService.canReadDeviceStateTrans();
        $scope.canReadDeviceTestResults = SecurityService.canReadDeviceTestResults();

        $scope.getOptions = getOptions;

        EventTrackerMixin.call($scope, $scope, $timeout, EventService, ToastrService);

        $scope.manageState = false;
        var selectedStates = [];
        var removeStateDefinition = false;

        $scope.updateManageState = function(bool) {
            $scope.manageState = bool;
            selectedStates = [];
            removeStateDefinition = false;
        };

        $scope.updateRemoveStateDefinition = function() {
            removeStateDefinition = !removeStateDefinition;
        };

        $scope.changeSelectedStates = function (stateId) {
            if (selectedStates.indexOf(stateId) === -1) {
                selectedStates.push(stateId);
            }
            else {
                selectedStates.splice(selectedStates.indexOf(stateId), 1);
            }
        };

        $scope.removeStates = function () {
            if (selectedStates.length === 0) {
                ToastrService.popupError('No values selected');
                return;
            }
            $scope.loading = true;

            SpinnerService.wrap(DeviceService.removeStates, $scope.device.id, selectedStates, removeStateDefinition)
                .then(function() {
                    ToastrService.popupSuccess(selectedStates.length === 1 ? 'State was removed successfully' : 'States was removed successfully');
                    $scope.refreshDeviceState();
                })
                .catch(ErrorService.handleHttpError)
                .finally(
                    function() {
                        $scope.updateManageState(false);
                        $scope.loading = false;
                    }
                );
        };

        $scope.manageTelemetry = false;

        $scope.updateManageTelemetry = function(bool) {
            $scope.manageTelemetry = bool;
            selectedTelemetry = [];
            removeTelemetryDefinition = false;
        };

        $scope.updateRemoveTelemetryDefinition = function () {
            removeTelemetryDefinition = !removeTelemetryDefinition;
        };

        var selectedTelemetry = [];
        var removeTelemetryDefinition = false;

        $scope.removeTelemetry = function () {
            if (selectedTelemetry.length === 0) {
                ToastrService.popupError('No values selected');
                return;
            }
            $scope.loading = true;

            SpinnerService.wrap(DeviceService.removeTelemetry, $scope.device.id, selectedTelemetry, removeTelemetryDefinition)
                .then(function () {
                    ToastrService.popupSuccess('Telemetry was removed successfully');
                    DeviceService.getDeviceLastTelemetry($scope.device.id)
                        .then(function (response) {
                            $scope.device.lastTelemetry = response.data;
                        });
                })
                .catch(ErrorService.handleHttpError)
                .finally(
                    function() {
                        $scope.updateManageTelemetry(false);
                        $scope.loading = false;
                    }
                );
        };

        $scope.changeSelectedTelemetry = function (telemetryId) {
            if (selectedTelemetry.indexOf(telemetryId) === -1) {
                selectedTelemetry.push(telemetryId);
            }
            else {
                selectedTelemetry.splice(selectedTelemetry.indexOf(telemetryId), 1);
            }
        };

        $scope.formatLastTelemetryItemValue = function (value) {
            var nValue = value.replace(/\|/g, '<br/>');
            var trustedObject = $sce.trustAsHtml(nValue)
            var trustedHtml = $sce.getTrustedHtml(trustedObject)

            return trustedHtml;
        };

        $scope.openAbstractActionModal = function (index) {
            var actions = $scope.abstractActions.actions,
                action;
            if (index < 0 || index >= actions.length) {
                index = -1;
                action = {
                    description: '',
                    criteria: '',
                    expiration: 60,
                    noTelemetry: false,
                    noTelemetrySeconds: 60,
                    enabled: true
                };
            } else {
                action = angular.merge({}, actions[index]);
            }
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/device/device-action.html',
                controller: 'DeviceActionController',
                size: 'lg',
                resolve: {
                    deviceActionIndex: index,
                    deviceAction: action
                },
                scope: $scope
            });

            modalInstance.result.then(function (result) {
                SpinnerService.wrap(DeviceService.saveDeviceAction, $scope.device, result.index, result.action)
                    .then(function (response) {
                        ToastrService.popupSuccess('Device Action has been saved successfully');
                        $scope.device.actions = $scope.abstractActions.actions = response.data;
                    })
                    .catch(ErrorService.handleHttpError);
            });
        };

        $scope.selectedActionIndices = [];
        $scope.toggleAction = function (actionIndex) {
            var index = $scope.selectedActionIndices.indexOf(actionIndex);
            if (index >= 0) {
                $scope.selectedActionIndices.splice(index, 1);
            } else {
                $scope.selectedActionIndices.push(actionIndex);
            }
        };

        $scope.toggleAllActions = function () {
            if ($scope.selectedActionIndices.length == $scope.abstractActions.actions.length) {
                $scope.selectedActionIndices = [];
            } else {
                // select all
                $scope.selectedActionIndices = [];
                for (var i = 0; i < $scope.abstractActions.actions.length; i++) {
                    $scope.selectedActionIndices[i] = i;
                }
            }
        };

        $scope.confirmDeleteActions = function () {
            if ($scope.selectedActionIndices.length > 0) {
                $uibModal.open({
                    animation: false,
                    templateUrl: 'partials/confirm-popup.html',
                    controller: 'ConfirmPopupController',
                    size: 'lg',
                    resolve: {
                        options: {
                            title: 'Confirm deleting ' + $scope.selectedActionIndices.length + ' action(s)',
                            message: 'This action is not recoverable. Are you sure you want to delete the selected actions?'
                        }
                    }
                }).result.then(function () {
                    SpinnerService.wrap(DeviceService.deleteDeviceActions, $scope.device, $scope.selectedActionIndices)
                        .then(function (response) {
                            ToastrService.popupSuccess('Device Actions have been deleted successfully');
                            $scope.device.actions = $scope.abstractActions.actions = response.data;
                            $scope.selectedActionIndices = [];
                        })
                        .catch(ErrorService.handleHttpError);
                });
            }
        };

        $scope.toggleTag = function (tag) {
            if (!$scope.device || !$scope.device.tags) return;
            var index = $scope.device.tags.indexOf(tag.id);
            if (index >= 0) {
                $scope.device.tags.splice(index, 1);
            } else {
                $scope.device.tags.push(tag.id);
            }
        };

        $scope.openNewDeviceTagModal = function () {
            $uibModal.open({
                animation: false,
                templateUrl: 'partials/prompt-popup.html',
                controller: 'PromptPopupController',
                size: 'lg',
                resolve: {
                    options: {
                        title: 'Create New Tag',
                        label: 'Tag Name',
                        value: '',
                        required: true,
                        ok: 'Save'
                    }
                }
            }).result.then(function (deviceTag) {
                SpinnerService.wrap(DeviceService.createDeviceTag, deviceTag)
                    .then(function (response) {
                        ToastrService.popupSuccess('Device tag ' + deviceTag + ' has been created successfully');
                        $scope.options.tags = response.data;
                    })
                    .catch(ErrorService.handleHttpError);
            });
        };

        $scope.save = function (deviceForm) {
            if (deviceForm.$invalid) {
                ToastrService.popupError('Device ' + $scope.device.name + ' cannot be saved because of invalid fields, please check errors.');
            } else {
                SpinnerService.wrap(DeviceService.saveDeviceSettings, $scope.device)
                    .then(function (response) {
                        angular.extend($scope.device, response.data);
                        var message = 'Device ' + $scope.device.name + ' has been saved successfully';
                        if ($scope.device.propertyChangeEventId) {
                            $scope.trackEvent($scope.device.propertyChangeEventId, 'propertyChange', $scope.device);
                            message += '. Settings are being updated on the device, please wait.';
                        }
                        deviceForm.$setPristine();
                        ToastrService.popupSuccess(message);
                    })
                    .catch(ErrorService.handleHttpError);
            }
        };

        $scope.saveTags = function () {
            SpinnerService.wrap(DeviceService.saveDeviceTags, $scope.device)
                .then(function (response) {
                    $scope.device.tags = response.data;
                    ToastrService.popupSuccess('Device tags has been saved successfully');
                })
                .catch(ErrorService.handleHttpError);
        };

        $scope.syncDeviceType = function () {
            SpinnerService.wrap(DeviceTypeService.syncDeviceType, $scope.device.id)
                .then(function (response) {
                    ToastrService.popupSuccess('Device type has been updated successfully');
                })
                .catch(ErrorService.handleHttpError);
        };

        $scope.syncConfigToDevice = function () {
            SpinnerService.wrap(ConfigurationService.syncConfigToDevice, $routeParams.deviceId)
                .then(function (response) {
                    if (response.data.status === 'ERROR') {
                        ToastrService.popupError(response.data.message);
                    } else {
                        ToastrService.popupSuccess('Cloud device configuration event has been queued for the device ' + $scope.device.name);
                        $scope.trackEvent(response.data.message, 'syncConfigToDevice', $scope.device);
                    }
                })
                .catch(ErrorService.handleHttpError);
        };

        $scope.syncDeviceConfigToCloud = function () {
            SpinnerService.wrap(ConfigurationService.syncDeviceConfigToCloud, $routeParams.deviceId)
                .then(function (response) {
                    if (response.data.status === 'ERROR') {
                        ToastrService.popupError(response.data.message);
                    } else {
                        ToastrService.popupSuccess('Current device configuration event has been queued for the device ' + $scope.device.name);
                        $scope.trackEvent(response.data.message, 'syncDeviceConfigToCloud', $scope.device);
                    }
                })
                .catch(ErrorService.handleHttpError);
        };

        function getDeviceDetails(deviceId) {
            $scope.loading = true;

            SpinnerService.wrap(DeviceService.getDeviceDetails, deviceId)
                .then(function (response) {
                    $scope.device = response.data;
                    $scope.abstractActions = {
                        actions: response.data.actions
                    };
                    $scope.deviceCustomProp = [];
                    angular.forEach($scope.device.properties, function (value, key) {
                        $scope.deviceCustomProp.push({
                            key: key,
                            value: value
                        });
                    });
                    googleChartApiPromise.then(initChart);
                    getOptions($scope.device.deviceTypeId);
                })
                .catch(ErrorService.handleHttpError)
                .finally(function () {
                    $scope.loading = false;
                });
        }

        function getOptions(deviceTypeId) {
            SpinnerService.wrap(DeviceService.getEditOptions, deviceTypeId)
                .then(function (response) {
                    $scope.options = response.data;
                    // make a map for device types
                    var deviceTypes = $scope.options.deviceTypes;
                    var i;
                    if (deviceTypes && deviceTypes.length > 0) {
                        for (i = 0; i < deviceTypes.length; i++) {
                            deviceTypes[deviceTypes[i].id] = deviceTypes[i];
                        }
                    }
                    // make a map for device actions
                    var actionTypes = $scope.options.actionTypes;
                    if (actionTypes && actionTypes.length > 0) {
                        for (i = 0; i < actionTypes.length; i++) {
                            actionTypes[actionTypes[i].id] = actionTypes[i];
                        }
                    }
                })
                .catch(ErrorService.handleHttpError);
        }

        $scope.sevenDayChart = null;

        function initChart() {
            var today = new Date();
            var sevenDaysAgo = new Date();
            sevenDaysAgo.setDate(today.getDate() - 6);
            sevenDaysAgo.setHours(0, 0, 0, 0);
           
            $scope.sevenDayChart = {
                type: 'LineChart',
                options: {
                    title: 'Last 7 Days',
                    colors: ['#00a19b', '#92278F'],
                    displayExactValues: true,
                    legend: {position: 'bottom'},
                    backgroundColor: '#F5F5F5',
                    pointSize: 7,
                    hAxis: {
                        format: 'E',
                        viewWindow: {
                            min: sevenDaysAgo,
                            max: today
                        }
                    },
                    vAxis: {
                        viewWindowMode: 'explicit',
                        format: '#,###',
                        viewWindow: {
                            min: 0
                        },
                        gridlines: {count: 5}
                    }
                },
                data: {
                    cols: [
                        {id: 'time', label: 'Time', type: 'date'},
                        {id: 'telemetry', label: 'Telemetry', type: 'number'},
                        {id: 'events', label: 'Notifications', type: 'number'}
                    ],
                    rows: []
                }
            };

            $scope.loadDeviceStats();
        }

        $scope.lastStatLoad = null;
        $scope.statsTimer = null;
        $scope.statsRefreshRate = (3 * 60 * 1000);		// 3 minutes
        $scope.loadingStats = false;
        $scope.loadDeviceStats = function () {
            $scope.loadingStats = true;

            DeviceService.getDeviceStats($scope.device.id)
                .then(function (response) {
                    // seven day chart data
                    $scope.device.deviceStats = response.data;
                    var data = response.data.telemetryEventCounts, datum, i = 0, length = data.length,
                        rows = new Array(length);
                    for (; i < length; i++) {
                        datum = data[i];
                        rows[i] = {
                            c: [
                                {v: new Date(datum.timestamp)},
                                {v: datum.telemetryCount},
                                {v: datum.deviceEventCount}
                            ]
                        };
                    }
                    if ($scope.sevenDayChart) {
                        $scope.sevenDayChart.data.rows = rows;
                    }
                })
                .catch(function (response) {
                    // do nothing...
                })
                .finally(function () {
                    $scope.lastStatLoad = new Date();
                    $scope.loadingStats = false;
                    // schedule next refresh
                    $scope.statsTimer = $timeout($scope.loadDeviceStats, $scope.statsRefreshRate);
                });

            // last telemetry
            DeviceService.getDeviceLastTelemetry($scope.device.id)
                .then(function (response) {
                    $scope.device.lastTelemetry = response.data;
                })
                .catch(function (response) {
                    // do nothing...
                })
                .finally(function () {
                });
        };

        $scope.$on('$destroy', function () {
            if ($scope.statsTimer) {
                $timeout.cancel($scope.statsTimer);
                $scope.statsTimer = null;
            }
        });

        $scope.enableDevice = function () {
            SpinnerService.wrap(DeviceService.enableDevice, $scope.device.id)
                .then(function (response) {
                    $scope.device.enabled = response.data;
                    ToastrService.popupSuccess('Device ' + $scope.device.name + ' has been enabled.');
                })
                .catch(ErrorService.handleHttpError);
        };

        $scope.disableDevice = function () {
            SpinnerService.wrap(DeviceService.disableDevice, $scope.device.id)
                .then(function (response) {
                    $scope.device.enabled = response.data;
                    ToastrService.popupSuccess('Device ' + $scope.device.name + ' has been disabled.');
                })
                .catch(ErrorService.handleHttpError);
        };

        $scope.deleteDevice = function () {
            $uibModal.open({
                animation: false,
                templateUrl: 'partials/confirm-popup.html',
                controller: 'ConfirmPopupController',
                size: 'lg',
                resolve: {
                    options: {
                        title: 'Confirm deleting device ' + $scope.device.name,
                        message: 'WARNING: THIS ACTION IS NOT RECOVERABLE! This device and all related information will be deleted from the system. Are you sure you want to proceed?'
                    }
                }
            }).result.then(function () {
                SpinnerService.wrap(DeviceService.deleteDevice, $scope.device.id)
                    .then(function (response) {
                        ToastrService.popupSuccess('Device ' + $scope.device.name + ' has been deleted successfully');
                        $location.path('/devices');
                    })
                    .catch(ErrorService.handleHttpError);
            });
        };

        $scope.startDevice = function () {
            SpinnerService.wrap(DeviceService.startDevice, $scope.device.id)
                .then(function (response) {
                    ToastrService.popupSuccess('Start event has been queued for the device ' + $scope.device.name);
                    $scope.trackEvent(response.data, 'start', $scope.device);
                })
                .catch(ErrorService.handleHttpError);
        };

        $scope.stopDevice = function () {
            SpinnerService.wrap(DeviceService.stopDevice, $scope.device.id)
                .then(function (response) {
                    ToastrService.popupSuccess('Stop event has been queued for the device ' + $scope.device.name);
                    $scope.trackEvent(response.data, 'stop', $scope.device);
                })
                .catch(ErrorService.handleHttpError);
        };

        $scope.deviceState = {};
        $scope.refreshDeviceState = function () {
            SpinnerService.wrap(DeviceService.getDeviceState, $scope.device.id)
                .then(function (response) {
                    $scope.deviceState = response.data;
                })
                .catch(ErrorService.handleHttpError);
        };

        $scope.updateDeviceState = function () {
            $uibModal.open({
                animation: false,
                templateUrl: 'partials/device/device-state-update.html',
                controller: 'DeviceStateUpdateController',
                size: 'lg',
                resolve: {
                    stateMetadata: $scope.device.stateMetadata
                }
            }).result.then(function (deviceState) {
                SpinnerService.wrap(DeviceService.updateDeviceState, $scope.device.id, deviceState)
                    .then(function (response) {
                        ToastrService.popupSuccess('Device State update request has been queued for the device ' + $scope.device.name);
                        $scope.trackEvent(response.data, 'device-state-update', $scope.device);
                    })
                    .catch(ErrorService.handleHttpError);
            });
        };

        $scope.deviceCommand = {
            command: '',
            payload: '{\n\t\n}'
        };
        $scope.sendCommand = function (form) {
            if (form.$invalid) {
                ToastrService.popupError('Device command cannot be sent because of invalid fields, please check errors.');
            } else {
                SpinnerService.wrap(DeviceService.sendCommand, $scope.device.id, $scope.deviceCommand)
                    .then(function (response) {
                        ToastrService.popupSuccess('Command event has been queued for the device ' + $scope.device.name);
                        $scope.trackEvent(response.data, 'command', $scope.device, $scope.deviceCommand.command);
                        $scope.deviceCommand = {
                            command: '',
                            payload: '{\n\t\n}'
                        };
                    })
                    .catch(ErrorService.handleHttpError);
            }
        };

        $scope.switchDevice = function () {
            $uibModal.open({
                animation: false,
                templateUrl: 'partials/device/switch-device.html',
                controller: 'SwitchDeviceController',
                size: 'lg',
                resolve: {
                    device: $scope.device
                }
            });
        };

        // initialization
        getDeviceDetails($routeParams.deviceId);

        $scope.collapseMenu = function () {
            if ($scope.mobileLayot) {
                $scope.mobileMenuShowed = !$scope.mobileMenuShowed;
            } else {
                $scope.collapsedView = !$scope.collapsedView;
            }
            $timeout(function () {
                $(window).trigger('resize');
            }, 550, false); // animation is done in 500ms, trigger resize event a bit later
        };

        $scope.$on('resize', function (event, data) {
            var triggerResize = false;
            if (data.width < 992) {
                if (!$scope.mobileLayot) triggerResize = true;
                $scope.mobileLayot = true;
            } else {
                if ($scope.mobileLayot) triggerResize = true;
                $scope.mobileLayot = false;
                $scope.mobileMenuShowed = false;
            }
            if (triggerResize) {
                $timeout(function () {
                    $(window).trigger('resize');
                }, 550, false); // animation is done in 500ms, trigger resize event a bit later
            }
            $timeout(function () {
                $scope.$apply();
            }, 0, false);
        });
        $scope.mobileMenuShowed = false;

        $scope.openObjectLocation = function (size) {
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/object-location-popup.html',
                controller: 'ObjectLocationController',
                size: size,
                resolve: {
                    lastLocation: function () {
                        return $scope.device.lastLocation;
                    }
                }
            });

            modalInstance.result.then(
                function (lastLocation) {
                    SpinnerService.wrap(DeviceService.updateDeviceLocation, $scope.device.id, lastLocation)
                        .then(function (response) {
                            $scope.device.lastLocation = response.data;
                            ToastrService.popupSuccess('Device last location has been updated');
                        })
                        .catch(function () {
                            ToastrService.popupError('Failed to update device last location');
                        });
                }
            );
        };

        $scope._isJson = function _isJson(item) {
            item = typeof item !== "string" ? JSON.stringify(item) : item;

            try {
                item = JSON.parse(item);
            } catch (e) {
                return false;
            }

            if (typeof item === "object" && item !== null) {
                return true;
            }

            return false;
        };

        $scope.isObjectEmpty = function (device) {
            return Object.keys(device).length === 0;
        };
    }
]);

controllers.controller('DeviceActionController',
    [
        '$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'deviceActionIndex', 'deviceAction',
        function ($scope, $uibModalInstance, SecurityService, ToastrService, deviceActionIndex, deviceAction) {
            $scope.deviceActionIndex = deviceActionIndex;
            $scope.deviceAction = deviceAction;
            $scope.canSaveDeviceAction = ($scope.canCreateDeviceAction && deviceActionIndex < 0) || ($scope.canEditDeviceAction && deviceActionIndex >= 0);

            $scope.optionValues = {};

            function updateOptionValues() {
                $scope.optionValues = {};
                if ($scope.deviceAction.deviceActionTypeId && $scope.options.actionTypes[$scope.deviceAction.deviceActionTypeId].systemName == 'PostBackURL') {
                    if (!$scope.optionValues['ContentType']) {
                        $scope.optionValues['ContentType'] = $scope.options.contentTypes.map(function (contentType) {
                            return contentType.value;
                        });
                    }
                }
            }

            updateOptionValues();

            $scope.onActionTypeChanged = function () {
                if ($scope.deviceAction.deviceActionTypeId) {
                    $scope.deviceAction.parameters = angular.extend({}, $scope.options.actionTypes[$scope.deviceAction.deviceActionTypeId].parameters);
                } else {
                    $scope.deviceAction.parameters = null;
                }
                updateOptionValues();
            };

            $scope.isInputField = function (key) {
                return !($scope.isTextareaField(key) || $scope.isSelectField(key));
            };

            $scope.isTextareaField = function (key) {
                var systemName = $scope.options.actionTypes[$scope.deviceAction.deviceActionTypeId].systemName;
                return (systemName == 'PostBackURL' && (key == 'Headers' || key == 'RequestBody')) || (systemName == 'SendEmail' && key == 'Body');
            };

            $scope.isSelectField = function (key) {
                return $scope.options.actionTypes[$scope.deviceAction.deviceActionTypeId].systemName == 'PostBackURL' && key == 'ContentType';
            };

            $scope.isRequired = function (key) {
                return $scope.options.actionTypes[$scope.deviceAction.deviceActionTypeId].systemName != 'PostBackURL' || key != 'Headers';
            };

            $scope.save = function () {
                if ($scope.deviceActionForm.$valid) {
                    $uibModalInstance.close({
                        index: $scope.deviceActionIndex,
                        action: $scope.deviceAction
                    });
                } else {
                    ToastrService.popupError('Device Action cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

controllers.controller('SwitchDeviceController',
    [
        '$scope', '$uibModalInstance', 'DeviceService', 'ErrorService', 'SpinnerService', 'device',
        function ($scope, $uibModalInstance, DeviceService, ErrorService, SpinnerService, device) {
            $scope.devices = [];

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

            // find devices having the same gatewayId
            SpinnerService.wrap(DeviceService.find, {
                pageIndex: 0,
                itemsPerPage: 100, // limit the list up to 100 devices since there is no pagination
                sort: {
                    property: 'name',
                    direction: 'ASC'
                }
            }, {
                gatewayIds: [device.gateway.id] // filter by gatewayId
            })
                .then(function (response) {
                    $scope.devices = response.data.result.content;
                })
                .catch(ErrorService.handleHttpError);
        }
    ]
);
