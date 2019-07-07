/* global controllers */

controllers.controller('GatewaysController', ['$uibModal', '$location', 'GatewayService', 'BackupService', 'ErrorService', 'ToastrService', 'SpinnerService', 'SecurityService', GatewaysController]);

function GatewaysController($uibModal, $location, GatewayService, BackupService, ErrorService, ToastrService, SpinnerService, SecurityService) {
    var vm = this;

    vm.bulkActions = [];
    if (SecurityService.canEditGateway()) {
        vm.bulkActions.push({
            caption: function() {
                return 'Update All '+vm.selectedItemIds.length+' Gateways';
            },
            onClick: bulkUpdate
        });
    }
    if (SecurityService.canDeleteGateway()) {
        vm.bulkActions.push({
            caption: function() {
                return 'Delete All '+vm.selectedItemIds.length+' Gateways';
            },
            onClick: bulkDelete
        }, {
            caption: function() {
                return 'Back up Configurations for '+vm.selectedItemIds.length+' Gateways';
            },
            onClick: bulkBackupConfigurations
        });
    }
    vm.toolbar = [];
    if (SecurityService.canMoveGateway()) {
        vm.toolbar.push({
            caption: 'Associate Registered Gateway',
            icon: 'fa fa-cloud-download',
            onClick: function () {
                $uibModal.open({
                    animation: false,
                    templateUrl: 'partials/gateway/move-gateway.html',
                    controller: 'MoveGatewayController',
                    size: 'lg'
                })
                .result.then(function(result) {
                    // after the gateway has been moved - update the filter so the list will contain moved gateway
                    vm.filter = {
                        "uid": result.uid,
                        "osNames": [],
                        "softwareNames": [],
                        "softwareVersions": [],
                        "userIds": [],
                        "nodeIds": [],
                        "gatewayTypes": [],
                        "enabled": result.enabled
                    };
                    vm.find();
                });
            }
        });
    }
    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Gateways',
        resultTitle: 'Gateway',
        columnHeaders: [
            {label: 'Gateway', value: 'name', sortable: true},
            {label: 'UID', value: 'uid', sortable: true},
            {label: 'Type', value: 'deviceTypeName', sortable: false},
            //{label: 'Type', value: 'type', sortable: true},
            //{label: 'OS Name', value: 'osName', sortable: true},
            //{label: 'Software Name', value: 'softwareName', sortable: true},
            //{label: 'Software Version', value: 'softwareVersion', sortable: true},
            {label: 'Group', value: 'nodeName', sortable: false},
            {label: 'Owner', value: 'ownerName', sortable: false},
            {label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val) { return val ? 'Yes': 'No'; }},
            {label: 'Last Heartbeat', value: 'lastHeartbeat', sortable: false},
            {label: 'Last Modified', value: 'lastModifiedDate', sortable: false}
        ],
        tfootTopic: 'gateways',
        canAdd: SecurityService.canCreateGateway(),
        canEdit: true,
        canBulkEdit: SecurityService.canDeleteGateway(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = {
        "uid": "",
	"hid": "",
        "deviceTypeIds": [],
        "osNames": [],
        "softwareNames": [],
        "softwareVersions": [],
        "userIds": [],
        "nodeIds": [],
        "gatewayTypes": [],
        "enabled": true
    };

    vm.filterOptions = {
        "uid": "",
	"hid": "",
    	"deviceTypeIds": [],
        "uid": "",
        "osNames": [],
        "softwareNames": [],
        "softwareVersions": [],
        "userIds": [],
        "nodeIds": [],
        "gatewayTypes": [],
        "enabled": true
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/gateway/gateway-filter.html',
            controller: 'GatewaysFilterController',
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

    function openDetails(gateway) {
        if (gateway) {
            $location.path('/gateway/'+gateway.id);
        } else {
            $uibModal.open({
                animation: false,
                templateUrl: 'partials/gateway/gateway.html',
                controller: 'GatewayController',
                size: 'lg'
            })
            .result.then(function(gateway) {
                // after the gateway has been created - update the filter so the list will contain newly created gateway
                // (?!)
                // vm.filter = {
                //     "uid": gateway.uid,
                //     "osNames": [],
                //     "softwareNames": [],
                //     "softwareVersions": [],
                //     "userIds": [],
                //     "gatewayTypes": [],
                //     "enabled": gateway.enabled
                // };
                ToastrService.popupSuccess('Gateway has been added');
                vm.find();
            });
        }
    }

    function bulkUpdate() {
        if (vm.selectedItemIds.length <= 0) {
            ToastrService.popupError('Please select 1 or more gateways');
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
                    return SpinnerService.wrap(GatewayService.getBulkUpdateFields).then(function(response) { return response.data; });
                }
            }
        }).result.then(function(updateOptionsModel) {
            SpinnerService.wrap(GatewayService.bulkUpdate, vm.selectedItemIds, updateOptionsModel)
                .then(function(response) {
                    var result = response.data;
                    var msg = result.succeeded + ' gateways have been updated from total ' + result.total;
                    if (result.failed > 0) {
                         ToastrService.popupError(msg + '. Updating of ' + result.failed + ' gateways has failed.');
                    } else {
                         ToastrService.popupSuccess(msg);
                    }
                    vm.find();
            }).catch(ErrorService.handleHttpError);;
        });
    }

    function bulkDelete() {
        if (vm.selectedItemIds.length <= 0) {
            ToastrService.popupError('Please select 1 or more gateways');
            return;
        }
        $uibModal.open({
            animation: false,
            templateUrl: 'partials/confirm-popup.html',
            controller: 'ConfirmPopupController',
            size: 'lg',
            resolve: {
                options: {
                    title: 'Confirm deleting gateways',
                    message: 'WARNING: The delete action is permanent. Are you sure you want to delete all '+vm.selectedItemIds.length+' gateways?',
                    ok: 'Continue'
                }
            }
        }).result.then(function() {
            SpinnerService.wrap(GatewayService.deleteGateways, vm.selectedItemIds)
            .then(function(response) {
                var result = response.data;
                var msg = result.succeeded + ' gateways have been deleted from total ' + result.total;
                if (result.failed > 0) {
                    ToastrService.popupError(msg + '. Deleting of ' + result.failed + ' gateways has failed.');
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
            ToastrService.popupError('Please select 1 or more gateways');
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
                    message: 'Are you sure you want to back up configurations for these  '+vm.selectedItemIds.length+' gateways?',
                    ok: 'Continue'
                }
            }
        }).result.then(function() {
            $uibModal.open({
                animation: false,
                templateUrl: 'partials/backup/backup-create-modal.html',
                controller: 'GatewayCreateBackupController',
                size: 'lg',
                resolve: {
                    backupName: function () {
                        return '';
                    }
                }
            }).result.then(
                function (backupName) {
                    SpinnerService.wrap(BackupService.backupGatewayConfigs, backupName, vm.selectedItemIds)
                    .then(function(response) {
                        var result = response.data;
                        var msg = result.succeeded + ' gateways have been backed up from total ' + result.total;
                        if (result.failed > 0) {
                            ToastrService.popupError(msg + '. Backing up of ' + result.failed + ' gateways has failed.');
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
        SpinnerService.wrap(GatewayService.find, vm.pagination, vm.filter, vm.bulkEdit)
        .then(function(response) {
            vm.update(response.data.result, response.data.documentIds);
        })
        .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(GatewayService.getSearchCriteria)
    .then(function(response) {
        response.data.enabled = true;
        vm.filterOptions = response.data;
    });

    vm.find();
}
GatewaysController.prototype = Object.create(ListController.prototype);

controllers.controller('GatewaysFilterController',
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

GatewaysController.prototype.getCellText = function (item, column) {
    if (column.value === 'lastModifiedDate') {
        return moment(new Date(item.lastModifiedDate)).format('MM/DD/YYYY hh:mm:ss A');
    } else if (column.value === 'lastHeartbeat' && item.lastHeartbeat != null) {
        return moment(new Date(item.lastHeartbeat)).format('MM/DD/YYYY hh:mm:ss A');
    } else {
        return ListController.prototype.getCellText.call(this, item, column);
    }
};

GatewaysController.prototype.toggleBulkEditMode = function() {
    ListController.prototype.toggleBulkEditMode.call(this);
    if (this.bulkEdit) {
        this.btnDropdownActions = this.bulkActions;
        this.toolbar[this.toolbar.length-1].caption = 'Back to Gateways';
    } else {
        this.btnDropdownActions = [];
        this.toolbar[this.toolbar.length-1].caption = 'Bulk Edit';
    }
    this.find();
};
