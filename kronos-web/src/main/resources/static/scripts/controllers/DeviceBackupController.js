controllers.controller('DeviceBackupController', ['$route', '$uibModal', '$routeParams', 'ErrorService', 'ToastrService', 'SpinnerService', 'BackupService', DeviceBackupController]);

function DeviceBackupController($route, $uibModal, $routeParams, ErrorService, ToastrService, SpinnerService, BackupService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'createdDate',
            direction: 'DESC'
        }
    }, {
        columnHeaders: [
            {
                label: 'Name',
                value: 'name',
                sortable: true,
                style: {'text-overflow': 'ellipsis', 'word-break': 'normal'}
            },
            {label: 'Created Date', value: 'createdDate', sortable: true},
            {label: 'Create By', value: 'createBy', sortable: false},
            {label: 'Action', value: '', sortable: false}
        ]
    });

    vm.filter = {
        "name": null
    };

    vm.loadingData = false;

    vm.open = function openFilter(size) {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/backup/backup-filter.html',
            controller: 'DeviceBackupFilterController',
            size: size,
            resolve: {
                filter: function () {
                    return vm.filter;
                },
                filterOptions: function () {
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
    };

    vm.find = function () {
        vm.loadingData = true;
        SpinnerService.wrap(BackupService.findDeviceBackups, $routeParams.deviceId, vm.pagination, vm.filter)
            .then(function (response) {
                vm.update(response.data.result);
                vm.loadingData = false;
            })
            .catch(ErrorService.handleHttpError);
    };

    vm.createBackupNow = function () {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/backup/backup-create-modal.html',
            controller: 'DeviceCreateBackupController',
            size: 'lg',
            resolve: {
                backupName: function () {
                    return vm.backupName;
                }
            }
        });

        modalInstance.result.then(
            function (backupName) {
                SpinnerService.wrap(BackupService.createDeviceBackup, $routeParams.deviceId, backupName)
                    .then(function (response) {
                        ToastrService.popupSuccess('Backup ' + response.data.name + ' has been created successfully');
                        vm.find();
                    })
                    .catch(ErrorService.handleHttpError);
            }
        );
    };

    vm.restoreBackupDeviceConfiguration = function (backupId) {

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/backup/backup-confirm-restore-modal.html',
            controller: 'DeviceConfirmRestoreBackupController',
            size: 'lg'
        });

        modalInstance.result.then(
            function () {
                SpinnerService.wrap(BackupService.restoreDeviceBackup, $routeParams.deviceId, backupId)
                    .then(function (response) {
                        $route.reload();
                        ToastrService.popupSuccess('Backup configuration has been restored successfully');
                    })
                    .catch(ErrorService.handleHttpError);
            }
        );
    };

    vm.find();

}

DeviceBackupController.prototype = Object.create(ListController.prototype);

controllers.controller('DeviceBackupFilterController',
    [
        '$scope', '$uibModalInstance', 'filter', 'filterOptions',
        function ($scope, $uibModalInstance, filter, filterOptions) {

            $scope.filter = angular.merge({}, filter);
            $scope.filterOptions = filterOptions;

            $scope.ok = function () {
                $scope.filter.name = $scope.filter.name || null;
                $uibModalInstance.close($scope.filter);
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

controllers.controller('DeviceCreateBackupController',
    [
        '$scope', '$uibModalInstance', 'backupName',
        function ($scope, $uibModalInstance, backupName) {
            $scope.backupName = backupName;
            $scope.ok = function () {
                $uibModalInstance.close($scope.backupName);
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

controllers.controller('DeviceConfirmRestoreBackupController',
    [
        '$scope', '$uibModalInstance',
        function ($scope, $uibModalInstance) {
            $scope.ok = function () {
                $uibModalInstance.close();
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);
