controllers.controller('SoftwareProductsController', ['$uibModal', 'SoftwareProductService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', SoftwareProductsController]);

function SoftwareProductsController($uibModal, SoftwareProductService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Software Products',
        resultTitle: 'Software Products',
        columnHeaders: [
            {label: 'Name', value: 'name', sortable: true},
            {label: 'Description', value: 'description', sortable: true},
            {label: 'Software Vendor', value: 'softwareVendor.name', sortable: false},
            {label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val) { return val ? 'Yes': 'No'; }}
        ],
        tfootTopic: 'software products',
        canAdd: SecurityService.canCreateSoftwareProduct(),
        canEdit: SecurityService.canEditSoftwareProduct(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = {
        softwareVendorIds: [],
        enabled: true,
        editable: null
    };
    vm.filterOptions = {
        softwareVendors: []
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/softwareproduct/softwareproduct-filter.html',
            controller: 'SoftwareProductsFilterController',
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

    function openDetails(softwareProduct) {
        softwareProduct = softwareProduct || {
            name: '',
            description: '',
            enabled: true,
            editable: true
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/softwareproduct/softwareproduct-details.html',
            controller: 'SoftwareProductDetailsController',
            size: 'lg',
            resolve: {
                softwareProduct: softwareProduct
            }
        });

        modalInstance.result.then(function(softwareProduct) {
            SpinnerService.wrap(SoftwareProductService.save, softwareProduct)
            .then(function(response) {
                ToastrService.popupSuccess('Software Product ' + softwareProduct.name + ' has been saved successfully');
                // re-read the current page
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(SoftwareProductService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(SoftwareProductService.options)
    .then(function(response) {
        vm.filterOptions.softwareVendors = response.data;
    });

    vm.find();
}
SoftwareProductsController.prototype = Object.create(ListController.prototype);

controllers.controller('SoftwareProductsFilterController',
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

controllers.controller('SoftwareProductDetailsController',
    [
        '$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'softwareProduct', 'SoftwareProductService', 'SpinnerService', 'ErrorService',
        function ($scope, $uibModalInstance, SecurityService, ToastrService, softwareProduct, SoftwareProductService, SpinnerService, ErrorService) {
            $scope.softwareProduct = angular.merge({}, softwareProduct);

            $scope.canSaveSoftwareProduct = $scope.softwareProduct.editable
                && (($scope.softwareProduct.id && SecurityService.canEditSoftwareProduct())
                    || (!$scope.softwareProduct.id && SecurityService.canCreateSoftwareProduct()))
                    || !$scope.softwareProduct.editable && SecurityService.isAdmin();

            $scope.save = function () {
                if ($scope.softwareProductForm.$valid) {
                    $uibModalInstance.close($scope.softwareProduct);
                } else {
                    ToastrService.popupError('Software Product cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

            SpinnerService.wrap(SoftwareProductService.options)
            .then(function(response) {
                $scope.softwareVendors = response.data;
            })
            .catch(ErrorService.handleHttpError);
        }
    ]
);

