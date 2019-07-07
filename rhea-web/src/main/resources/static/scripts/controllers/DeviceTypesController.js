controllers.controller('DeviceTypesController', ['$uibModal', 'DeviceTypeService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', DeviceTypesController]);

function DeviceTypesController($uibModal, DeviceTypeService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Hardware Versions',
        resultTitle: 'Hardware Versions',
        columnHeaders: [
            {label: 'Name', value: 'name', sortable: true},
            {label: 'Description', value: 'description', sortable: true},
            {label: 'Hardware Product', value: 'deviceProduct.name', sortable: false},
            {label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val){ return val ? 'Yes': 'No'; }}
        ],
        tfootTopic: 'hardware versions',
        canAdd: SecurityService.canCreateDeviceType(),
        canEdit: SecurityService.canEditDeviceType(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = {
        enabled: true,
        deviceProduct: null
    };
    vm.filterOptions = {
        deviceProducts: []
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/devicetype/devicetype-filter.html',
            controller: 'DeviceTypesFilterController',
            size: 'lg',
            resolve: {
                filter: vm.filter,
                filterOptions: vm.filterOptions
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

    function openDetails(deviceType) {
        deviceType = deviceType || {
            name: '',
            description: '',
            enabled: true
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/devicetype/devicetype-details.html',
            controller: 'DeviceTypeDetailsController',
            size: 'lg',
            resolve: {
                deviceType: deviceType
            }
        });

        modalInstance.result.then(function(deviceType) {
            SpinnerService.wrap(DeviceTypeService.save, deviceType)
            .then(function(response) {
                ToastrService.popupSuccess('Hardware Version ' + deviceType.name + ' has been saved successfully');
                // re-read the current page
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(DeviceTypeService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(DeviceTypeService.options)
    .then(function(response) {
        vm.filterOptions.deviceProducts = response.data;
    });

    vm.find();
}
DeviceTypesController.prototype = Object.create(ListController.prototype);

controllers.controller('DeviceTypesFilterController',
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

controllers.controller('DeviceTypeDetailsController',
    [
        '$scope', '$uibModalInstance', 'DeviceTypeService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', 'deviceType',
        function ($scope, $uibModalInstance, DeviceTypeService, ErrorService, ToastrService, SecurityService, SpinnerService, deviceType) {
            $scope.deviceType = angular.merge({}, deviceType);

            $scope.deviceProducts = [];

            $scope.canSaveDeviceType = ($scope.deviceType.id && SecurityService.canEditDeviceType())
                    || (!$scope.deviceType.id && SecurityService.canCreateDeviceType());

            $scope.save = function () {
                if ($scope.deviceTypeForm.$valid) {
                    $uibModalInstance.close($scope.deviceType);
                } else {
                    ToastrService.popupError('Hardware Version cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

            SpinnerService.wrap(DeviceTypeService.options)
            .then(function(response) {
                $scope.deviceProducts = response.data;
            })
            .catch(ErrorService.handleHttpError);
        }
    ]
);

