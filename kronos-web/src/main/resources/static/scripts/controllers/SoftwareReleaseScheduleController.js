controllers.controller('SoftwareReleaseScheduleController', ['$uibModal', 'SoftwareReleaseScheduleService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', SoftwareReleaseScheduleController]);

function SoftwareReleaseScheduleController($uibModal, SoftwareReleaseScheduleService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'scheduledDate',
            direction: 'DESC'
        }
    }, {
        pageTitle: 'Software Release Schedule',
        resultTitle: 'Software Release Schedule',
        columnHeaders: [
            {label: 'Scheduled Date', value: 'scheduledDate', sortable: true},
            {label: 'Software Release Name', value: 'softwareReleaseName', sortable: false},
            {label: 'Status', value: 'status', sortable: true},
            {label: 'Notify On Start', value: 'notifyOnStart', sortable: false},
            {label: 'Notify On End', value: 'notifyOnEnd', sortable: false}
        ],
        tfootTopic: 'software release schedules',
        canAdd: SecurityService.canCreateSoftwareReleaseSchedule(),
        canEdit: SecurityService.canEditSoftwareReleaseSchedule(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filterOptions = {
        categories: [],
        statuses: []
    };

    vm.filter = {
        scheduledDateFrom: null,
        scheduledDateTo: null,
        deviceCategories: [],
        statuses: []
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/swreleaseschedule/filter.html',
            controller: 'SoftwareReleaseScheduleFilterController',
            size: 'lg',
            resolve: {
                filter: vm.filter,
                options: vm.filterOptions
            }
        });

        modalInstance.result.then(
            function (filter) {
                vm.filter = filter;
                vm.pagination.pageIndex = 0;
                vm.find();
            }
        );
    }

    function openDetails(swReleaseSchedule) {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/swreleaseschedule/details-popup.html',
            controller: 'SoftwareReleaseScheduleDetailsController',
            size: 'lg',
            resolve: {
                swReleaseScheduleId: function() {
                    return swReleaseSchedule ? swReleaseSchedule.id : null;
                }
            }
        });

        modalInstance.result.then(function(swReleaseSchedule) {
            SpinnerService.wrap(SoftwareReleaseScheduleService.save, swReleaseSchedule)
            .then(function(response) {
                ToastrService.popupSuccess('Software Release Schedule has been saved successfully');
                // re-read the current page
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(SoftwareReleaseScheduleService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(SoftwareReleaseScheduleService.filterOptions)
    .then(function(response) {
        vm.filterOptions = response.data;
    });
    vm.find();
}
SoftwareReleaseScheduleController.prototype = Object.create(ListController.prototype);

SoftwareReleaseScheduleController.prototype.getCellText = function(item, column) {
    item = ListController.prototype.getCellText.call(this, item, column);
    if (column.value == 'scheduledDate') {
        item = moment(new Date(item)).format('MM/DD/YYYY hh:mm:ss A');
    }
    return item;
}

controllers.controller('SoftwareReleaseScheduleFilterController',
    [
        '$scope', '$uibModalInstance', 'filter', 'options',
        function ($scope, $uibModalInstance, filter, options) {
            $scope.filter = angular.merge({}, filter);
            $scope.options = options;

            $scope.ok = function () {
                $uibModalInstance.close($scope.filter);
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

controllers.controller('SoftwareReleaseScheduleDetailsController',
    [
        '$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'SoftwareReleaseScheduleService', 'ErrorService', 'SpinnerService', 'swReleaseScheduleId',
        function ($scope, $uibModalInstance, SecurityService, ToastrService, SoftwareReleaseScheduleService, ErrorService, SpinnerService, swReleaseScheduleId) {
            var initNewSchedule = false;
            // tabs
            $scope.activeTab = 0;
            $scope.tabs = [{disabled: false}, {disabled: false}, {disabled: false}];
            $scope.tabClicked = function(index) {
                if (!$scope.tabs[index].disabled) {
                    $scope.activeTab = index;
                }
            };
            $scope.updateTabs = function() {
                // 1st tab always enabled
                $scope.tabs[0].disabled = false;
                // 2nd tab enabled if softwareReleaseId, scheduledDate, timezone are selected
                $scope.tabs[1].disabled = !($scope.model.softwareReleaseId != null && $scope.model.scheduledDate != null && $scope.model.targetTimezone != null);
                // 3nd tab enabled if any device selected
                var selectedDevice = $scope.options.availableObjects && $scope.options.availableObjects.find($scope.selectedObjectsFilter);
                $scope.tabs[2].disabled = $scope.tabs[1].disabled || !selectedDevice;
            };
            $scope.startDateBeforeRender = function($view, $dates) {
                var now = new Date();
                // $view = year, month, day, hour
                switch($view) {
                    case 'year':
                        now.setMonth(0, 1);
                        now.setHours(0, 0, 0, 0);
                        break;
                    case 'month':
                        now.setDate(1);
                        now.setHours(0, 0, 0, 0);
                        break;
                    case 'day':
                        now.setHours(0, 0, 0, 0);
                        break;
                    case 'hour':
                        now.setMinutes(59, 59, 999);
                        break;
                }
                for(var i=0; i<$dates.length; i++) {
                    if ($dates[i].localDateValue() < now.getTime()) {
                        $dates[i].selectable = false;
                    }
                }
            }

            // model
            $scope.model = {
                objectIds: [],
                localTimezone: Util.getTimezone(),
                targetTimezone: Util.getTimezone()
            };
            $scope.options = {
                selection: {}
            };
            $scope.filter = {
                deviceName: null,
                ownerId: null,
                nodeId: null,
                softwareReleaseId: null
            };
            $scope.filterOptions = {
                owners: [],
                nodes: [],
                softwareReleases: []
            };

            $scope.canSave =(swReleaseScheduleId && SecurityService.canEditSoftwareReleaseSchedule())
                    || (!swReleaseScheduleId && SecurityService.canCreateSoftwareReleaseSchedule());

            $scope.save = function(form) {
                if (form.$valid) {
                    // remove objectIds which are not in $scope.options.availableObjects
                    $scope.model.objectIds = $scope.options.availableObjects
                    .filter($scope.selectedObjectsFilter)
                    .map(function(object) {
                        return object.id;
                    });
                    $uibModalInstance.close($scope.model);
                } else {
                    ToastrService.popupError('Software Release Schedule cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

            $scope.updateSelectionOptions = function(fieldsToReset) {
                if (fieldsToReset) {
                    for (var i=0; i<fieldsToReset.length; i++) {
                        $scope.options.selection[fieldsToReset[i]] = null;
                    }
                }
                SpinnerService.wrap(SoftwareReleaseScheduleService.getSelectionOptions, $scope.options.selection)
                .then(function(response) {
                    $scope.options = response.data;
                    $scope.model.softwareReleaseId = $scope.options.selection.softwareReleaseId;
                    $scope.model.deviceCategory = $scope.options.selection.deviceCategory;
                    if (initNewSchedule) {
                        initNewSchedule = false;
                        $scope.model.notifyEmails = $scope.options.defaultSoftwareReleaseEmails;
                    }
                    if ($scope.options.availableObjects && $scope.options.availableObjects.length > 0) {
                        updateFilterOptions($scope.options.availableObjects);
                    }
                    $scope.updateTabs();
                })
                .catch(ErrorService.handleHttpError);
            };

            function updateFilterOptions(availableObjects) {
                $scope.filterOptions = {
                    owners: [],
                    nodes: [],
                    softwareReleases: []
                };
                availableObjects.forEach(function(object) {
                    if (object.ownerId) {
                        var owner = $scope.filterOptions.owners.find(function(owner) {
                            return owner.id === object.ownerId;
                        });
                        if (!owner) {
                            $scope.filterOptions.owners.push({
                                id: object.ownerId,
                                name: object.ownerName
                            });
                        }
                    }
                    if (object.nodeId) {
                        var node = $scope.filterOptions.nodes.find(function(node) {
                            return node.id === object.nodeId;
                        });
                        if (!node) {
                            $scope.filterOptions.nodes.push({
                                id: object.nodeId,
                                name: object.nodeName
                            });
                        }
                    }
                    if (object.softwareReleaseId) {
                        var softwareRelease = $scope.filterOptions.softwareReleases.find(function(softwareRelease) {
                            return softwareRelease.id === object.softwareReleaseId;
                        });
                        if (!softwareRelease) {
                            $scope.filterOptions.softwareReleases.push({
                                id: object.softwareReleaseId,
                                name: object.softwareReleaseName
                            });
                        }
                    }
                });

                // make sure currect filter is in filterOptions
                if ($scope.filter.ownerId) {
                    var owner = $scope.filterOptions.owners.find(function(owner) {
                        return owner.id === $scope.filter.ownerId;
                    });
                    if (!owner) {
                        $scope.filter.ownerId = null;
                    }
                }
                if ($scope.filter.nodeId) {
                    var node = $scope.filterOptions.nodes.find(function(node) {
                        return node.id === $scope.filter.nodeId;
                    });
                    if (!node) {
                        $scope.filter.nodeId = null;
                    }
                }
                if ($scope.filter.softwareReleaseId) {
                    var softwareRelease = $scope.filterOptions.softwareReleases.find(function(softwareRelease) {
                        return softwareRelease.id === $scope.filter.softwareReleaseId;
                    });
                    if (!softwareRelease) {
                        $scope.filter.softwareReleaseId = null;
                    }
                }
            }

            $scope.addDevice = function(device) {
                $scope.model.objectIds.push(device.id);
                $scope.updateTabs();
            };

            $scope.removeDevice = function(device) {
                var index = $scope.model.objectIds.indexOf(device.id);
                if (index >= 0) {
                    $scope.model.objectIds.splice(index, 1);
                    $scope.updateTabs();
                }
            };

            $scope.availableObjectsFilter = function(object) {
                var displayed = true;
                // removed added objects
                if ($scope.model.objectIds.indexOf(object.id) >= 0) {
                    displayed = false;
                }
                // apply filter
                if (displayed && $scope.filter.deviceName) {
                    displayed = (''+object.name).toLowerCase().indexOf($scope.filter.deviceName.toLowerCase()) >= 0;
                }
                if (displayed && $scope.filter.ownerId) {
                    displayed = object.ownerId === $scope.filter.ownerId;
                }
                if (displayed && $scope.filter.nodeId) {
                    displayed = object.nodeId === $scope.filter.nodeId;
                }
                if (displayed && $scope.filter.softwareReleaseId) {
                    displayed = object.softwareReleaseId === $scope.filter.softwareReleaseId;
                }
                return displayed;
            };

            $scope.selectedObjectsFilter = function(object) {
                var displayed = false;
                if ($scope.model.objectIds.indexOf(object.id) >= 0) {
                    displayed = true;
                }
                return displayed;
            };

            if (swReleaseScheduleId != null) {
                SpinnerService.wrap(SoftwareReleaseScheduleService.findOne, swReleaseScheduleId)
                .then(function(response) {
                    angular.merge($scope.model, response.data);
                    $scope.model.scheduledDate = new Date($scope.model.scheduledDate);
                    $scope.options.selection.softwareReleaseScheduleId = $scope.model.id;
                    $scope.options.selection.softwareReleaseId = $scope.model.softwareReleaseId;
                    $scope.options.selection.deviceCategory = $scope.model.deviceCategory;
                    $scope.updateSelectionOptions();
                })
                .catch(ErrorService.handleHttpError);
            } else {
                // init options
                initNewSchedule = true;
                $scope.updateSelectionOptions();
            }

        }
    ]
);

