controllers.controller('SoftwareReleasesController', ['$uibModal', 'SoftwareReleaseService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', SoftwareReleasesController]);

function SoftwareReleasesController($uibModal, SoftwareReleaseService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'major'
        }
    }, {
        pageTitle: 'Software Releases',
        resultTitle: 'Software Releases',
        columnHeaders: [
            {label: 'Software Product', value: 'softwareProduct.name', sortable: false},
            {label: 'Owner name', value: 'ownerName', sortable: false},
            {label: 'Owner email', value: 'ownerEmail', sortable: false},
            {label: 'Major', value: 'major', sortable: true},
            {label: 'Minor', value: 'minor', sortable: true},
            {label: 'Build', value: 'build', sortable: true},
            {label: 'Firmware', value: 'fileName', sortable: false, renderFunc: function(val) { return val != null ? 'Yes': 'No'; }},
            {label: 'Right To Use', value: 'rtuType', sortable: false},
            {label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val) { return val ? 'Yes': 'No'; }}
        ],
        tfootTopic: 'software releases',
        canAdd: SecurityService.canCreateSoftwareRelease(),
        canEdit: SecurityService.canEditSoftwareRelease(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = {
        enabled: true,
        softwareProductIds: [],
        deviceTypeIds: [],
        upgradeableFromIds: []
    };
    vm.filterOptions = {
        softwareProducts: [],
        deviceTypes: [],
        upgradeableFroms: []
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/softwarerelease/softwarerelease-filter.html',
            controller: 'SoftwareReleaseFilterController',
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

    function openDetails(softwareRelease) {
        softwareRelease = softwareRelease || {
            softwareProduct: null,
            noLongerSupported: false,
            enabled: true,
            editable: true,
            //major, minor, build
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/softwarerelease/softwarerelease-details.html',
            controller: 'SoftwareReleaseDetailsController',
            size: 'lg',
            backdrop: 'static',
            resolve: {
                softwareRelease: softwareRelease
            }
        });

        modalInstance.result.then(function(result) {
            SpinnerService.wrap(SoftwareReleaseService.saveWithFile, result)
                .then(function (response) {
                    ToastrService.popupSuccess('Software Release has been saved successfully');
                    // re-read the current page
                    vm.find();
                })
                .catch(ErrorService.handleHttpError);

        });
    }

    vm.find = function() {
        SpinnerService.wrap(SoftwareReleaseService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(SoftwareReleaseService.filterOptions)
    .then(function(response) {
        vm.filterOptions.softwareProducts = response.data.softwareProducts;
        vm.filterOptions.deviceTypes = response.data.deviceTypes;
        vm.filterOptions.upgradeableFroms = response.data.softwareReleases;
    });

    vm.find();
}

SoftwareReleasesController.prototype = Object.create(ListController.prototype);

controllers.controller('SoftwareReleaseFilterController',
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

controllers.controller('SoftwareReleaseDetailsController',
    [
        '$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'SoftwareReleaseService', 'ErrorService', 'SpinnerService', 'softwareRelease',
        function ($scope, $uibModalInstance, SecurityService, ToastrService, SoftwareReleaseService, ErrorService, SpinnerService, softwareRelease) {

            $scope.softwareRelease = angular.merge({
                deviceTypeIds: [],
                upgradeableFromIds: []
            }, softwareRelease);
            $scope.activeTab = 0;
            $scope.softwareVendors = [];
            $scope.options = {
                selection: {
                    softwareReleaseId: $scope.softwareRelease.id,
                    softwareVendorId: ($scope.softwareRelease.softwareVendor && $scope.softwareRelease.softwareVendor.id) ? $scope.softwareRelease.softwareVendor.id : null
                }
            };
            $scope.owner = {
                name: '',
                email: ''
            };
            $scope.firmware = { file: null };

            $scope.canSaveSoftwareRelease = ($scope.softwareRelease.id && SecurityService.canEditSoftwareRelease())
                    || (!$scope.softwareRelease.id && SecurityService.canCreateSoftwareRelease());

            $scope.save = function () {
                if ($scope.softwareReleaseForm.$valid) {
                    // save selected deviceTypeIds in model
                    if($scope.options.deviceTypes){
                        $scope.softwareRelease.deviceTypeIds = $scope.options.deviceTypes.filter($scope.selectedDeviceTypesFilter).map(function(object) { return object.id; });
                    }
                    if($scope.options.softwareReleases){
                        $scope.softwareRelease.upgradeableFromIds = $scope.options.softwareReleases.filter($scope.selectedSoftwareReleasesFilter).map(function(object) { return object.id; });
                    }
                    var data = $scope.softwareRelease;
                    data.ownerName = $scope.owner.name;
                    data.ownerEmail = $scope.owner.email;
                    $uibModalInstance.close({data: data, file: $scope.firmware.file});
                } else {
                    ToastrService.popupError('Software Release cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

            $scope.updateSelectionOptions = function(fieldsToReset, needToUpdateLists) {
            	if (fieldsToReset) {
            		for (var i=0; i<fieldsToReset.length; i++) {
                        if(fieldsToReset[i] == 'deviceTypes'){
                            $scope.softwareRelease.deviceTypeIds = [];
                            continue;
                        }
                        if(fieldsToReset[i] == 'softwareReleases'){
                            $scope.softwareRelease.upgradeableFromIds = [];
                            continue;
                        }
                        $scope.options.selection[fieldsToReset[i]] = null;
            		}
            	}

	            if(needToUpdateLists){
                    SpinnerService.wrap(SoftwareReleaseService.options, $scope.options.selection)
                    .then(function(response) {
                        $scope.options = response.data;
                        if (!$scope.owner.name || $scope.owner.name.length === 0) {
                          $scope.owner.name = response.data.ownerName;
                        }
                        if (!$scope.owner.email || $scope.owner.email.length === 0) {
                          $scope.owner.email = response.data.ownerEmail;
                        }
                    })
                    .catch(ErrorService.handleHttpError);
                }
            };
            $scope.updateSelectionOptions([], true);


            // Tab-2 Device Type filtering
            $scope.availableDeviceTypesFilter = function(obj) {
                var displayed = true;
                if($scope.softwareRelease.deviceTypeIds.indexOf(obj.id) >= 0){
                    displayed = false;
                }
                return displayed;
            };

            $scope.selectedDeviceTypesFilter = function(obj) {
                var displayed = false;
                if($scope.softwareRelease.deviceTypeIds.indexOf(obj.id) >= 0){
                    displayed = true;
                }
                return displayed;
            };

            $scope.addDeviceType = function(deviceType) {
                $scope.softwareRelease.deviceTypeIds.push(deviceType.id);
            };

            $scope.removeDeviceType = function(deviceType) {
                var index = $scope.softwareRelease.deviceTypeIds.indexOf(deviceType.id);
                if (index >= 0) {
                    $scope.softwareRelease.deviceTypeIds.splice(index, 1);
                }
            };


            // Tab-3 Software Release filtering
            $scope.availableSoftwareReleasesFilter = function(obj) {
                var displayed = true;
                if($scope.softwareRelease.upgradeableFromIds.indexOf(obj.id) >= 0 || $scope.softwareRelease.id == obj.id){
                    displayed = false;
                }
                return displayed;
            };

            $scope.selectedSoftwareReleasesFilter = function(obj) {
                var displayed = false;
                if($scope.softwareRelease.upgradeableFromIds.indexOf(obj.id) >= 0){
                    displayed = true;
                }
                return displayed;
            };

            $scope.addSoftwareRelease = function(softwareRelease) {
                $scope.softwareRelease.upgradeableFromIds.push(softwareRelease.id);
            };

            $scope.removeSoftwareRelease = function(softwareRelease) {
                var index = $scope.softwareRelease.upgradeableFromIds.indexOf(softwareRelease.id);
                if (index >= 0) {
                    $scope.softwareRelease.upgradeableFromIds.splice(index, 1);
                }
            };
            // end of controller

        }
    ]
);

