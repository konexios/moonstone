controllers.controller('ManufacturersController', ['$uibModal', 'ManufacturerService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', ManufacturersController]);

function ManufacturersController($uibModal, ManufacturerService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Hardware Manufacturers',
        resultTitle: 'Hardware Manufacturers',
        columnHeaders: [
            {label: 'Name', value: 'name', sortable: true},
            {label: 'Description', value: 'description', sortable: true},
            {label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val){ return val ? 'Yes': 'No'; }}
        ],
        tfootTopic: 'hardware manufacturers',
        canAdd: SecurityService.canCreateManufacturer(),
        canEdit: SecurityService.canEditManufacturer(),
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

    function openDetails(manufacturer) {
        manufacturer = manufacturer || {
            name: '',
            description: '',
            enabled: true,
            editable: true
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/manufacturer/manufacturer-details.html',
            controller: 'ManufacturerDetailsController',
            size: 'lg',
            resolve: {
                manufacturer: manufacturer
            }
        });

        modalInstance.result.then(function(manufacturer) {
            SpinnerService.wrap(ManufacturerService.save, manufacturer)
            .then(function(response) {
                ToastrService.popupSuccess('Hardware Manufacturer ' + manufacturer.name + ' has been saved successfully');
                // re-read the current page
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(ManufacturerService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.find();
}
ManufacturersController.prototype = Object.create(ListController.prototype);

controllers.controller('ManufacturerDetailsController',
    [
        '$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'manufacturer',
        function ($scope, $uibModalInstance, SecurityService, ToastrService, manufacturer) {
            $scope.manufacturer = angular.merge({}, manufacturer);

            $scope.canSaveManufacturer = $scope.manufacturer.editable
                && (($scope.manufacturer.id && SecurityService.canEditManufacturer())
                    || (!$scope.manufacturer.id && SecurityService.canCreateManufacturer()));

            $scope.save = function () {
                if ($scope.manufacturerForm.$valid) {
                    $uibModalInstance.close($scope.manufacturer);
                } else {
                    ToastrService.popupError('Hardware Manufacturer cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

