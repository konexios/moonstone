controllers.controller('DeviceProductsController', ['$uibModal', 'DeviceProductService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', DeviceProductsController]);

function DeviceProductsController($uibModal, DeviceProductService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Hardware Products',
        resultTitle: 'Hardware Products',
        columnHeaders: [
            {label: 'Name', value: 'name', sortable: true},
            {label: 'Description', value: 'description', sortable: true},
            {label: 'Hardware Category', value: 'deviceCategory.name', sortable: false},
            {label: 'Hardware Manufacturer', value: 'deviceManufacturer.name', sortable: false},
            {label: 'Enabled', value: 'enabled', sortable: false}
        ],
        tfootTopic: 'hardware products',
        canAdd: SecurityService.canCreateDeviceProduct(),
        canEdit: SecurityService.canEditDeviceProduct(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = {
        enabled: true,
        deviceCategories: [],
        deviceManufacturerIds: []
    };
    vm.filterOptions = {
        manufacturers: [],
        deviceCategories: []
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/deviceproduct/deviceproduct-filter.html',
            controller: 'DeviceProductsFilterController',
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

    function openDetails(deviceProduct) {
        deviceProduct = deviceProduct || {
            name: '',
            description: '',
            enabled: true
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/deviceproduct/deviceproduct-details.html',
            controller: 'DeviceProductDetailsController',
            size: 'lg',
            resolve: {
                deviceProduct: deviceProduct
            }
        });

        modalInstance.result.then(function(deviceProduct) {
            SpinnerService.wrap(DeviceProductService.save, deviceProduct)
            .then(function(response) {
                ToastrService.popupSuccess('Hardware Product ' + deviceProduct.name + ' has been saved successfully');
                // re-read the current page
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(DeviceProductService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(DeviceProductService.options)
    .then(function(response) {
        vm.filterOptions.deviceCategories = response.data.deviceCategoryOptions;
        vm.filterOptions.manufacturers = response.data.manufacturerOptions;
    });

    vm.find();
}
DeviceProductsController.prototype = Object.create(ListController.prototype);

controllers.controller('DeviceProductsFilterController',
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

controllers.controller('DeviceProductDetailsController',
    [
        '$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'DeviceProductService', 'ErrorService', 'SpinnerService', 'deviceProduct',
        function ($scope, $uibModalInstance, SecurityService, ToastrService, DeviceProductService, ErrorService, SpinnerService, deviceProduct) {
            $scope.deviceProduct = angular.merge({}, deviceProduct);

            $scope.manufacturers = [];
            $scope.deviceCategories = [];

            $scope.canSaveDeviceProduct = ($scope.deviceProduct.id && SecurityService.canEditDeviceProduct())
                    || (!$scope.deviceProduct.id && SecurityService.canCreateDeviceProduct());

            $scope.save = function () {
                if ($scope.deviceProductForm.$valid) {
                    $uibModalInstance.close($scope.deviceProduct);
                } else {
                    ToastrService.popupError('Hardware Product cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

            SpinnerService.wrap(DeviceProductService.options)
            .then(function(response) {
                $scope.deviceCategories = response.data.deviceCategoryOptions;
                $scope.manufacturers = response.data.manufacturerOptions;
            })
            .catch(ErrorService.handleHttpError);
        }
    ]
);

