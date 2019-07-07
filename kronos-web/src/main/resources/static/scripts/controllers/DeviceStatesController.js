controllers.controller('DeviceStatesController', ['$scope', '$uibModal', '$filter', '$routeParams', 'DeviceService', 'CommonService', 'ErrorService', 'SpinnerService', 'SecurityService', 'ToastrService', DeviceStatesController]);

function DeviceStatesController($scope, $uibModal, $filter, $routeParams, DeviceService, CommonService, ErrorService, SpinnerService, SecurityService, ToastrService) {
    var vm = this;

    vm.showError = function(item) {
        $uibModal.open({
            animation: false,
            templateUrl: 'partials/list/list-text-modal.html',
            controller: 'TextModalController',
            controllerAs: 'vm',
            resolve: {
                options: {
                    title: 'Error',
                    text: item.error
                }
            }
        });
    };

    var listConfig = {
        pageTitle: 'Device States',
        resultTitle: 'Device States',
        columnHeaders: [
            { label: 'Type', value: 'type', sortable: false },
            {
                label: 'Status',
                value: 'status',
                sortable: false,
                cellAction: function(item) { return item.status == 'ERROR' ? vm.showError(item) : false; },
                renderFunc: function(val) { return val == 'ERROR' ? '! ' + val : val; }
            },
            { label: 'Created', value: 'createdDate', sortable: true, renderFunc: CommonService.getFormatredDate },
            { label: 'Updated', value: 'lastModifiedDate', sortable: true, renderFunc: CommonService.getFormatredDate }
        ],
        tfootTopic: 'device states',
        openFilter: openFilter,
        openDetails: openDetails,
        canAdd: false,
        canEdit: true
    };

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'createdDate',
            direction: 'DESC'
        }
    }, listConfig);

    vm.filter = {
        types: [],
        statuses: [],
        createdDateFrom: null,
        createdDateTo: null,
        updatedDateFrom: null,
        updatedDateTo: null
    };
    vm.options = {
        types: [],
        statuses: []
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/device/device-state-filter.html',
            controller: 'DeviceStatesFilterController',
            controllerAs: 'vm',
            size: 'lg',
            resolve: {
                filter: vm.filter,
                options: vm.options
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

    function openDetails(stateTrans) {
        SpinnerService.wrap(DeviceService.getDeviceStateTrans, $routeParams.deviceId, stateTrans.id)
        .then(function(response) {
            $uibModal.open({
                animation: false,
                templateUrl: 'partials/device/device-state-trans-popup.html',
                controller: 'DeviceStateTransDetailsController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    stateMetadata: $scope.device.stateMetadata,
                    deviceStateTrans: response.data
                }
            });
        })
        .catch(ErrorService.handleHttpError);
    }

    vm.find = function() {
        SpinnerService.wrap(DeviceService.findDeviceStateTrans, $routeParams.deviceId, vm.pagination, vm.filter)
            .then(function(response) {
                vm.update(response.data.result);
            })
            .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(DeviceService.getDeviceStateTransFilterOptions)
    .then(function(response) {
        vm.options = response.data;
    });
    vm.find();
}

DeviceStatesController.prototype = Object.create(ListController.prototype);

DeviceStatesController.prototype.displayState = function(index, column, item) {
    if (index == 1 && item.status !== 'ERROR') {
        return 'text';
    } else {
        return ListController.prototype.displayState.call(this, index, column, item);
    }
};

controllers.controller('DeviceStatesFilterController', ['$uibModalInstance', 'filter', 'options', DeviceStatesFilterController]);
function DeviceStatesFilterController($uibModalInstance, filter, options) {
    var vm = this;
    vm.filter = angular.merge({}, filter);
    vm.options = options;

    vm.ok = function () {
        $uibModalInstance.close(vm.filter);
    };

    vm.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

controllers.controller('DeviceStateTransDetailsController', ['$uibModalInstance', 'deviceStateTrans', 'stateMetadata', DeviceStateTransDetailsController]);
function DeviceStateTransDetailsController($uibModalInstance, deviceStateTrans, stateMetadata) {
    var vm = this;

    vm.deviceStateTrans = deviceStateTrans;
    vm.stateMetadata = stateMetadata;

    vm.close = function () {
        $uibModalInstance.dismiss('cancel');
    };
}
