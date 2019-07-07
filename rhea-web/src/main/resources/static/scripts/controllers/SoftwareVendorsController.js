controllers.controller('SoftwareVendorsController', ['$uibModal', 'SoftwareVendorService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', SoftwareVendorsController]);

function SoftwareVendorsController($uibModal, SoftwareVendorService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Software Vendors',
        resultTitle: 'Software Vendors',
        columnHeaders: [
            {label: 'Name', value: 'name', sortable: true},
            {label: 'Description', value: 'description', sortable: true},
            {label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val) { return val ? 'Yes': 'No'; }}
        ],
        tfootTopic: 'software vendors',
        canAdd: SecurityService.canCreateSoftwareVendor(),
        canEdit: SecurityService.canEditSoftwareVendor(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = {
        enabled: true,
        editable: null
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

    function openDetails(softwareVendor) {
        softwareVendor = softwareVendor || {
            name: '',
            description: '',
            enabled: true,
            editable: true
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/softwarevendor/softwarevendor-details.html',
            controller: 'SoftwareVendorDetailsController',
            size: 'lg',
            resolve: {
                softwareVendor: softwareVendor
            }
        });

        modalInstance.result.then(function(softwareVendor) {
            SpinnerService.wrap(SoftwareVendorService.save, softwareVendor)
            .then(function(response) {
                ToastrService.popupSuccess('Software Vendor ' + softwareVendor.name + ' has been saved successfully');
                // re-read the current page
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(SoftwareVendorService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.find();
}
SoftwareVendorsController.prototype = Object.create(ListController.prototype);

controllers.controller('SoftwareVendorDetailsController',
    [
        '$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'softwareVendor',
        function ($scope, $uibModalInstance, SecurityService, ToastrService, softwareVendor) {
            $scope.softwareVendor = angular.merge({}, softwareVendor);

            $scope.canSaveSoftwareVendor = $scope.softwareVendor.editable
                && (($scope.softwareVendor.id && SecurityService.canEditSoftwareVendor())
                    || (!$scope.softwareVendor.id && SecurityService.canCreateSoftwareVendor()))
                    || !$scope.softwareVendor.editable && SecurityService.isAdmin();

            $scope.save = function () {
                if ($scope.softwareVendorForm.$valid) {
                    $uibModalInstance.close($scope.softwareVendor);
                } else {
                    ToastrService.popupError('Software Vendor cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

