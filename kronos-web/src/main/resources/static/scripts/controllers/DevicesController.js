controllers.controller('DevicesController', ['$location', '$uibModal', 'DeviceService', 'BackupService', 'ErrorService', 'ToastrService', 'SpinnerService', 'SecurityService', DevicesController]);
function DevicesController($location, $uibModal, DeviceService, BackupService, ErrorService, ToastrService, SpinnerService, SecurityService) {
    var vm = this;

    vm.bulkActions = [];
    if (SecurityService.canEditDevice()) {
        vm.bulkActions.push({
            caption: function() {
                return 'Update All '+vm.selectedItemIds.length+' Devices';
            },
            onClick: bulkUpdate
        });
    }
    if (SecurityService.canDeleteDevice()) {
        vm.bulkActions.push({
            caption: function() {
                return 'Delete All '+vm.selectedItemIds.length+' Devices';
            },
            onClick: bulkDelete
        }, {
            caption: function() {
                return 'Back up Configurations for '+vm.selectedItemIds.length+' Devices';
            },
            onClick: bulkBackupConfigurations
        });
    }
    vm.toolbar = [{
        caption: 'Tree view',
        icon: 'fa fa-sitemap',
        onClick: function () {
            $location.path('/nodetree');
        }
    }];
    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Devices',
        resultTitle: 'Device',
        columnHeaders: [
            {label: 'Device', value: 'name', sortable: true},
            {label: 'UID', value: 'uid', sortable: true},
            {label: 'Type', value: 'deviceTypeName', sortable: false},
            {label: 'Gateway', value: 'gatewayName', sortable: false},
            {label: 'Group', value: 'nodeName', sortable: false},
            {label: 'Owner', value: 'ownerName', sortable: false},
            {label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val) { return val ? 'Yes': 'No'; }},
            {label: 'Last Modified', value: 'lastModifiedDate', sortable: false}
        ],
        tfootTopic: 'devices',
        canAdd: SecurityService.canCreateDevice(),
        canEdit: true,
        canBulkEdit: SecurityService.canDeleteDevice(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = {
		hid: "",
        uid:"",
        deviceTypeIds: [],
        gatewayIds: [],
        userIds: [],
        nodeIds: [],
        enabled: true
    };
    vm.filterOptions = {
		hid: "",
        uid:"",
        deviceTypes: [],
        gateways: [],
        users: [],
        nodes: []
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/device/device-filter.html',
            controller: 'DevicesFilterController',
            size: 'lg',
            resolve: {
                filter: function() {
                    return vm.filter;
                },
                filterOptions: function() {
                    return vm.filterOptions;
                }
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

    function openDetails(device) {
        if (device) {
            $location.path('/device/'+device.id);
        } else {
            $uibModal.open({
                animation: false,
                templateUrl: 'partials/device/device.html',
                controller: 'DeviceController',
                size: 'lg'
            }).result.then(function(model) {
                ToastrService.popupSuccess('Device has been added');
                vm.find();
            });
        }
    }

    function bulkUpdate() {
        if (vm.selectedItemIds.length <= 0) {
            ToastrService.popupError('Please select 1 or more devices');
            return;
        }
        $uibModal.open({
            animation: false,
            templateUrl: 'partials/bulk/bulk-update.html',
            controller: 'BulkUpdateController',
            size: 'md',
            resolve: {
                modalOptions: {
                    title: 'Bulk update',
                },
                fields: function() {
                    return SpinnerService.wrap(DeviceService.getBulkUpdateFields).then(function(response) { return response.data; });
                }
            }
        }).result.then(function(updateOptionsModel) {
            SpinnerService.wrap(DeviceService.bulkUpdate, vm.selectedItemIds, updateOptionsModel)
                .then(function(response) {
                    var result = response.data;
                    var msg = result.succeeded + ' devices have been updated from total ' + result.total;
                    if (result.failed > 0) {
                         ToastrService.popupError(msg + '. Updating of ' + result.failed + ' devices has failed.');
                    } else {
                         ToastrService.popupSuccess(msg);
                    }
                    vm.find();
            }).catch(ErrorService.handleHttpError);
        });
    }

    function bulkDelete() {
        if (vm.selectedItemIds.length <= 0) {
            ToastrService.popupError('Please select 1 or more devices');
            return;
        }
        $uibModal.open({
            animation: false,
            templateUrl: 'partials/confirm-popup.html',
            controller: 'ConfirmPopupController',
            size: 'lg',
            resolve: {
                options: {
                    title: 'Confirm deleting devices',
                    message: 'WARNING: The delete action is permanent. Are you sure you want to delete all '+vm.selectedItemIds.length+' devices?',
                    ok: 'Continue'
                }
            }
        }).result.then(function() {
            SpinnerService.wrap(DeviceService.deleteDevices, vm.selectedItemIds)
            .then(function(response) {
                var result = response.data;
                var msg = result.succeeded + ' devices have been deleted from total ' + result.total;
                if (result.failed > 0) {
                    ToastrService.popupError(msg + '. Deleting of ' + result.failed + ' devices has failed.');
                } else {
                    ToastrService.popupSuccess(msg);
                }
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });
    }

    function bulkBackupConfigurations() {
        if (vm.selectedItemIds.length <= 0) {
            ToastrService.popupError('Please select 1 or more devices');
            return;
        }
        $uibModal.open({
            animation: false,
            templateUrl: 'partials/confirm-popup.html',
            controller: 'ConfirmPopupController',
            size: 'lg',
            resolve: {
                options: {
                    title: 'Confirm action',
                    message: 'Are you sure you want to back up configurations for these  '+vm.selectedItemIds.length+' devices?',
                    ok: 'Continue'
                }
            }
        }).result.then(function() {
            $uibModal.open({
                animation: false,
                templateUrl: 'partials/backup/backup-create-modal.html',
                controller: 'DeviceCreateBackupController',
                size: 'lg',
                resolve: {
                    backupName: function () {
                        return '';
                    }
                }
            }).result.then(
                function (backupName) {
                    SpinnerService.wrap(BackupService.backupDeviceConfigs, backupName, vm.selectedItemIds)
                    .then(function(response) {
                        var result = response.data;
                        var msg = result.succeeded + ' devices have been backed up from total ' + result.total;
                        if (result.failed > 0) {
                            ToastrService.popupError(msg + '. Backing up of ' + result.failed + ' devices has failed.');
                        } else {
                            ToastrService.popupSuccess(msg);
                        }
                        vm.find();
                    })
                    .catch(ErrorService.handleHttpError);
                }
            );
        });
    }

    vm.find = function() {
        SpinnerService.wrap(DeviceService.find, vm.pagination, vm.filter, vm.bulkEdit)
        .then(function(response) {
            vm.update(response.data.result, response.data.documentIds);
        })
        .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(DeviceService.getSearchCriteria)
    .then(function(response) {
        vm.filterOptions = response.data;
    });

    vm.find();
}
DevicesController.prototype = Object.create(ListController.prototype);

controllers.controller('DevicesFilterController',
    [
        '$scope', '$uibModalInstance', 'filter', 'filterOptions',
        function ($scope, $uibModalInstance, filter, filterOptions) {
            $scope.filter = angular.merge({}, filter);
            $scope.filterOptions = filterOptions;

            $scope.ok = function () {
                $uibModalInstance.close($scope.filter);
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

DevicesController.prototype.toggleBulkEditMode = function() {
    ListController.prototype.toggleBulkEditMode.call(this);
    if (this.bulkEdit) {
        this.btnDropdownActions = this.bulkActions;
        this.toolbar[3].caption = 'Back to Devices';
    } else {
        this.btnDropdownActions = [];
        this.toolbar[3].caption = 'Bulk Edit';
    }
    this.find();
};

DevicesController.prototype.getCellText = function (item, column) {
    if (column.value === 'lastModifiedDate') {
        return moment(new Date(item.lastModifiedDate)).format('MM/DD/YYYY hh:mm:ss A');
    } else {
        return ListController.prototype.getCellText.call(this, item, column);
    }
};