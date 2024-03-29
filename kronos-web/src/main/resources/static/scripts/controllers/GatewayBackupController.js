controllers.controller('GatewayBackupController', ['$route', '$uibModal', '$routeParams', 'ErrorService', 'ToastrService', 'SpinnerService', 'BackupService', GatewayBackupController]);

function GatewayBackupController($route, $uibModal, $routeParams, ErrorService, ToastrService, SpinnerService, BackupService) {
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
            controller: 'GatewayBackupFilterController',
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
        SpinnerService.wrap(BackupService.findGatewayBackups, $routeParams.gatewayId, vm.pagination, vm.filter)
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
            controller: 'GatewayCreateBackupController',
            size: 'lg',
            resolve: {
                backupName: function () {
                    return vm.backupName;
                }
            }
        });

        modalInstance.result.then(
            function (backupName) {
                SpinnerService.wrap(BackupService.createGatewayBackup, $routeParams.gatewayId, backupName)
                    .then(function (response) {
                        ToastrService.popupSuccess('Backup ' + response.data.name + ' has been created successfully');
                        vm.find();
                    })
                    .catch(ErrorService.handleHttpError);
            }
        );
    };

    vm.restoreBackupGatewayConfiguration = function (backupId) {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/backup/backup-confirm-restore-modal.html',
            controller: 'GatewayConfirmRestoreBackupController',
            size: 'lg'
        });

        modalInstance.result.then(
            function () {
                SpinnerService.wrap(BackupService.restoreGatewayBackup, $routeParams.gatewayId, backupId)
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
GatewayBackupController.prototype = Object.create(ListController.prototype);

controllers.controller('GatewayBackupFilterController',
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

controllers.controller('GatewayCreateBackupController',
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

controllers.controller('GatewayConfirmRestoreBackupController',
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
