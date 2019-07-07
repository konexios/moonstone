controllers.controller('DeviceTypeDetailsController',
    [
        '$scope', '$uibModal', '$timeout', '$location', '$routeParams', 'DeviceTypeService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService',
        function ($scope, $uibModal, $timeout, $location, $routeParams, DeviceTypeService, ErrorService, ToastrService, SecurityService, SpinnerService) {
            $scope.activeTab = 0;

            $scope.options = {
                telemetryUnits: [],
                selection: {}
            };

            $scope.add = function (array) {
                array.push({
                    name: '',
                    description: '',
                    type: 'Integer',
                    controllable: false,
                    editing: true
                });
            };

            $scope.edit = function (array, index) {
                array[index].editing = !array[index].editing;
            };

            $scope.delete = function (array, index) {
                array.splice(index, 1);
               /* if (array == $scope.deviceType.stateMetadata) {
                    $scope.verifyNameUnique();
                }*/
            };
            
            $scope.saveGeneralDeviceType = function (form) {
                form.$setSubmitted();
                $scope.errorgMessage = null;

                if (form.$valid) {
                    SpinnerService.wrap(DeviceTypeService.save, getModelForSavingGeneral())
                        .then(function (response) {
                            ToastrService.popupSuccess('Asset Type ' + response.data.name + ' has been saved successfully');
                            if ($scope.deviceType.id) {
                                initModels(response.data);
                                // clear firmware management options if it is not actual
                                // if productTypeId is null - clear all selections
                                if ($scope.deviceType.productTypeId === undefined) {
                                	$scope.updateSelectionOptions(['manufacturerId', 'productId', 'productTypeId'], true);
                                }
                                
                            } else {
                                $location.path('/devicetype/' + response.data.id);
                            }
                        })
                        .catch(function(response) {
                            if (typeof response.data === 'string') {
                                $scope.errorgMessage = response.data;
                            } else if (response.status === 400 && response.data.message) {
                                $scope.errorgMessage = response.data.message;
                            } else {
                                ErrorService.handleHttpError(response);
                            }
                        });

                } else {
                    ToastrService.popupError('Asset Type cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.saveDeviceTypeTelemetry = function (form) {
                if (form.$valid) {
                    SpinnerService.wrap(DeviceTypeService.saveDeviceTypeTelemetry, getModelForSaving())
                        .then(function (response) {
                            initModels(response.data);
                            ToastrService.popupSuccess('Asset Type ' + response.data.name + ' has been saved successfully');
                        })
                        .catch(ErrorService.handleHttpError);

                } else {
                    ToastrService.popupError('Asset Type cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.saveDeviceTypeStateMetadata = function (form) {
                if (form.$valid) {
                    SpinnerService.wrap(DeviceTypeService.saveDeviceTypeStateMetadata, getModelForSaving())
                        .then(function (response) {
                            initModels(response.data);
                            ToastrService.popupSuccess('Asset Type ' + response.data.name + ' has been saved successfully');
                        })
                        .catch(ErrorService.handleHttpError);

                } else {
                    ToastrService.popupError('Asset Type cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.saveDeviceTypeFirmwareManagment = function (form) {
                if (form.$valid) {
                    SpinnerService.wrap(DeviceTypeService.saveDeviceTypeFirmware, getModelForSaving())
                        .then(function (response) {
                            initModels(response.data);
                            ToastrService.popupSuccess('Asset Type ' + response.data.name + ' has been saved successfully');
                        })
                        .catch(ErrorService.handleHttpError);
                } else {
                    ToastrService.popupError('Asset Type cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $location.path('/devicetypes');
            };

            $scope.getTelemetryUnitName = function (telemetry) {
                if (telemetry.telemetryUnitId) {
                	
                	var unit;
                    for(var i=0;i<$scope.options.telemetryUnits.length;i++) {
                        if( $scope.options.telemetryUnits[i].id == telemetry.telemetryUnitId){
                        	unit = $scope.options.telemetryUnits[i];
                        	
                        }
                    }
                   
                   
                    if (unit) {
                        return unit.name;
                    }
                }
                return "Unassigned";
            };

            $scope.updateSelectionOptions = function (fieldsToReset, needToUpdateLists) {
                if (fieldsToReset) {
                    for (var i = 0; i < fieldsToReset.length; i++) {
                        $scope.options.selection[fieldsToReset[i]] = null;
                    }
                }
                $scope.deviceType.productTypeId = $scope.options.selection.productTypeId;
                $scope.options.selection.deviceCategory = $scope.deviceType.deviceCategory
                // update lists if fields changed
                if (needToUpdateLists) {
                    SpinnerService.wrap(DeviceTypeService.options, $scope.options.selection)
                        .then(function (response) {
                            $scope.options = response.data;
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
                    controller: 'DeviceActionTypeController',
                    size: 'lg',
                    resolve: {
                        deviceActionIndex: index,
                        deviceAction: action
                    },
                    scope: $scope
                });

                modalInstance.result.then(function (result) {
                    if (result.index >= 0 && result.index < $scope.abstractActions.actions.length) {
                        $scope.abstractActions.actions[result.index] = result.action;
                    } else {
                        $scope.abstractActions.actions.push(result.action);
                    }
                    SpinnerService.wrap(DeviceTypeService.saveDeviceTypeActions, getModelForSaving())
                        .then(function (response) {
                            initModels(response.data);
                            ToastrService.popupSuccess('Asset Type ' + response.data.name + ' has been saved successfully');
                        })
                        .catch(ErrorService.handleHttpError);
                });
                $scope.selectedActionIndices = [];
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
                        if ($scope.selectedActionIndices.length !== $scope.abstractActions.actions.length) {
                            for (var action = 0; action < $scope.selectedActionIndices.length; action++) {
                                $scope.abstractActions.actions.splice($scope.selectedActionIndices[action], 1);
                            }
                            $scope.selectedActionIndices = [];
                        } else {
                            $scope.abstractActions.actions = [];
                            $scope.selectedActionIndices = [];
                        }

                        SpinnerService.wrap(DeviceTypeService.saveDeviceTypeActions, getModelForSaving())
                            .then(function (response) {
                                initModels(response.data);
                                ToastrService.popupSuccess('Asset Type ' + response.data.name + ' has been saved successfully');
                            })
                            .catch(ErrorService.handleHttpError);
                    });
                }
            };

            $scope.enableDeviceType = function () {
                SpinnerService.wrap(DeviceTypeService.enableDeviceType, $scope.deviceType.id)
                    .then(function (response) {
                        $scope.deviceType.enabled = response.data;
                        ToastrService.popupSuccess('Asset Type ' + $scope.deviceType.name + ' has been enabled.');
                    })
                    .catch(ErrorService.handleHttpError);
            };

            $scope.disableDeviceType = function () {
                SpinnerService.wrap(DeviceTypeService.disableDeviceType, $scope.deviceType.id)
                    .then(function (response) {
                        $scope.deviceType.enabled = response.data;
                        ToastrService.popupSuccess('Asset Type ' + $scope.deviceType.name + ' has been disabled.');
                    })
                    .catch(ErrorService.handleHttpError);
            };

            function initModels(deviceType) {
                if (!deviceType) {
                    deviceType = {
                        enabled: true,
                        editable: true
                    };
                }

                $scope.deviceType = deviceType;
                $scope.canSaveDeviceType = function() {
                    return (deviceType.editable && ((deviceType.id && SecurityService.canEditDeviceType()) || (!deviceType.id && SecurityService.canCreateDeviceType())) || SecurityService.isAdmin());
                };

                if (!deviceType.telemetries) {
                    deviceType.telemetries = [];
                }
                for (var i = 0; i < deviceType.telemetries.length; i++) {
                    deviceType.telemetries[i].editing = false;
                }

                if (!deviceType.stateMetadata) {
                    deviceType.stateMetadata = {};
                }
                var stateArray = [];
                for (var id in deviceType.stateMetadata) {
                    if (deviceType.stateMetadata.hasOwnProperty(id)) {
                        var state = deviceType.stateMetadata[id];
                        state.editing = false;
                        stateArray.push(state);
                    }
                }
                deviceType.stateMetadata = stateArray;
                // keep deviceType in separate variable for selection input. persisted value is not changed utill we save changes
                $scope.general = {
                		assetCategoryDirty: false,
                		modelDeviceCategory: deviceType.deviceCategory
                };
            }
            
            function getModelForSaving() {
                // remove UI-only properties of telemetries array
                var deviceType = $scope.deviceType;
                if (deviceType.id) {
                    for (var i = 0; i < deviceType.telemetries.length; i++) {
                        delete deviceType.telemetries[i].editing;
                    }

                    // convert stateMetadata to object, remove UI-only properties
                    var deviceState = {};
                    for (var i = 0; i < deviceType.stateMetadata.length; i++) {
                        var state = deviceType.stateMetadata[i];
                        delete state.editing;
                        deviceState[state.name] = state;
                    }
                    deviceType.stateMetadata = deviceState;
                    deviceType.actions = $scope.abstractActions.actions;
                }

                return deviceType;
            }

            function getModelForSavingGeneral() {
            	var deviceType = getModelForSaving();
            	deviceType.deviceCategory = $scope.general.modelDeviceCategory;
            	return deviceType;
            }
            
            var verifyNameUniquePromise;

            function verifyNameUnique() {
                verifyNameUniquePromise = null;
                var form = $scope.deviceTypeStateMetadataForm, stateMetadata = $scope.deviceType.stateMetadata;
                for (var i = 0; i < stateMetadata.length; i++) {
                    var dup = false;
                    if (!form['state' + i + 'Name'] && !form['state' + i + 'Name_m']) {
                        continue;
                    }
                    for (var j = 0; j < stateMetadata.length; j++) {
                        if (i != j && stateMetadata[i].name == stateMetadata[j].name) {
                            dup = true;
                            break;
                        }
                    }
                    if (form['state' + i + 'Name']) {
                        form['state' + i + 'Name'].$setValidity('duplicate', !dup);
                    }
                    if (form['state' + i + 'Name_m']) {
                        form['state' + i + 'Name_m'].$setValidity('duplicate', !dup);
                    }
                }
            }

            $scope.verifyNameUnique = function () {
                if (!verifyNameUniquePromise) {
                    verifyNameUniquePromise = $timeout(verifyNameUnique, 0);
                }
            };

            if ($routeParams.deviceTypeId != "add") {
                SpinnerService.wrap(DeviceTypeService.findOne, $routeParams.deviceTypeId)
                    .then(function (response) {
                        $scope.title = 'Edit';
                        $scope.abstractActions = response.data;
                        initModels(response.data);
                        $scope.options.selection.productTypeId = $scope.deviceType.productTypeId;
                        $scope.updateSelectionOptions([], true);
                    })
                    .catch(ErrorService.handleHttpError);
            } else {
                initModels();
                $scope.options.selection.productTypeId = $scope.deviceType.productTypeId;
                $scope.updateSelectionOptions([], true);
            }

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


        }
    ]
);

controllers.controller('DeviceActionTypeController',
    [
        '$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'deviceActionIndex', 'deviceAction',
        function ($scope, $uibModalInstance, SecurityService, ToastrService, deviceActionIndex, deviceAction) {
            $scope.deviceActionIndex = deviceActionIndex;
            $scope.deviceAction = deviceAction;
            $scope.canSaveDeviceAction = (deviceActionIndex < 0) || (deviceActionIndex >= 0);
            $scope.optionValues = {};
            var actionTypes = $scope.options.actionTypes;
            if (actionTypes && actionTypes.length > 0) {
                for (i = 0; i < actionTypes.length; i++) {
                    actionTypes[actionTypes[i].id] = actionTypes[i];
                }
            }

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
                return $scope.options.actionTypes[$scope.deviceAction.deviceActionTypeId].systemName == 'PostBackURL' && key == 'Headers';
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
