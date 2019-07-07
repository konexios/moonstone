controllers.controller('TelemetryUnitsController', ['$uibModal', 'TelemetryUnitService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', TelemetryUnitsController]);

function TelemetryUnitsController($uibModal, TelemetryUnitService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Telemetry Units',
        resultTitle: 'Telemetry Unit',
        columnHeaders: [
            {label: 'Name', value: 'name', sortable: true},
            {label: 'System Name', value: 'systemName', sortable: true},
            {label: 'Description', value: 'description', sortable: true},
            {label: 'Enabled', value: 'enabled', sortable: false},
            {label: 'Last Modified', value: 'lastModifiedDate', sortable: false},
            {label: 'Last Modified By', value: 'lastModifiedBy', sortable: false, renderFunc: function(val) { return val === "" ? 'Unknown': val;}}
        ],
        tfootTopic: 'telemetry units',
        canAdd: SecurityService.isAdmin(),
        canEdit: SecurityService.isAdmin(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = {
        enabled: true
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/enabled-filter.html',
            controller: 'FilterController',
            size: 'lg',
            resolve: {
                filter: vm.filter
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

    function openDetails(telemetryUnit) {
        telemetryUnit = telemetryUnit || {
            name: '',
            systemName: '',
            description: '',
            enabled: true
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/telemetryunit/telemetryunit-details.html',
            controller: 'TelemetryUnitDetailsController',
            size: 'lg',
            resolve: {
                telemetryUnit: telemetryUnit
            }
        });

        modalInstance.result.then(function(telemetryUnit) {
            // re-read the current page
            vm.find();
        });
    }

    vm.find = function() {
        SpinnerService.wrap(TelemetryUnitService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.find();
}
TelemetryUnitsController.prototype = Object.create(ListController.prototype);

TelemetryUnitsController.prototype.getCellText = function (item, column) {
    if (column.value == 'lastModifiedDate') {
        return moment(new Date(item.lastModifiedDate)).format('MM/DD/YYYY hh:mm:ss A');
    } else {
        return ListController.prototype.getCellText.call(this, item, column);
    }
};

controllers.controller('TelemetryUnitDetailsController',
    [
        '$scope', 'TelemetryUnitService', 'ErrorService', '$uibModalInstance', 'SecurityService', 'SpinnerService', 'ToastrService', 'KronosExceptions', 'telemetryUnit',
        function ($scope, TelemetryUnitService, ErrorService, $uibModalInstance, SecurityService, SpinnerService, ToastrService, KronosExceptions, telemetryUnit) {
            $scope.telemetryUnit = angular.merge({}, telemetryUnit);

            $scope.canSaveTelemetryUnit = function() {
                return SecurityService.isAdmin();
            };

            $scope.save = function () {
                if ($scope.telemetryUnitForm.$valid) {
                    SpinnerService.wrap(TelemetryUnitService.save, $scope.telemetryUnit)
                    .then(function(response) {
                        ToastrService.popupSuccess('Telemetry Unit ' + $scope.telemetryUnit.name + ' has been saved successfully');
                        $uibModalInstance.close($scope.telemetryUnit);
                    })
                    .catch(function(response) {
                        if (response.status == 400 && response.data.exceptionClassName === KronosExceptions.InvalidInputException) {
                            $scope.errorMessage = JSON.parse(response.data.message);
                        } else {
                            ErrorService.handleHttpError(response);
                            $uibModalInstance.close($scope.telemetryUnit);
                        }
                    });
                } else {
                    ToastrService.popupError('Telemetry Unit cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

