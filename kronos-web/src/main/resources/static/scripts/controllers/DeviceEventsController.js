controllers.controller('DeviceEventsController', ['$routeParams', '$uibModal', 'DeviceService', 'ErrorService', 'SpinnerService', 'SecurityService', DeviceEventsController]);
function DeviceEventsController($routeParams, $uibModal, DeviceService, ErrorService, SpinnerService, SecurityService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            direction: "DESC",
            property: 'createdDate'
        }
    }, {
        columnHeaders: [
            {label: 'Type', value: 'deviceActionTypeName', sortable: false},
            {label: 'Criteria', value: 'criteria', sortable: true},
            {label: 'Count', value: 'counter', sortable: true},
            {label: 'Status', value: 'status', sortable: true},
            {label: 'Created Date', value: 'createdDate', sortable: true}
        ]
    });

    vm.filter = {
        deviceActionTypeIds: [],
        statuses: [],
        createdDateFrom: null,
        createdDateTo: null
    };

    vm.open = function(size, options) {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/device/device-event-filter.html',
            controller: 'DeviceEventsFilterController',
            controllerAs: 'vm',
            size: size,
            resolve: {
                filter: vm.filter,
                options: options
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

    vm.canEditDeviceEvent = SecurityService.canEditDeviceEvent();
    vm.selectedEventIds = [];
    vm.openEventsCount = 0;

    function initSelection() {
        vm.selectedEventIds = [];
        vm.openEventsCount = 0;
        for(var i=0; i<vm.pagination.content.length; i++) {
            var deviceEvent = vm.pagination.content[i];
            if (deviceEvent.status == 'Open') {
                vm.openEventsCount++;
            }
        }
    }

    vm.find = function() {
        SpinnerService.wrap(DeviceService.findEvents, $routeParams.deviceId, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
            initSelection();
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.toggleEvent = function(eventId) {
        var index = vm.selectedEventIds.indexOf(eventId);
        if (index >= 0) {
            vm.selectedEventIds.splice(index, 1);
        } else {
            vm.selectedEventIds.push(eventId);
        }
    };

    vm.toggleAllEvents = function() {
            if (vm.selectedEventIds.length == vm.openEventsCount) {
                vm.selectedEventIds = [];
            } else {
                // select all
                vm.selectedEventIds = [];
                for(var i=0; i<vm.pagination.content.length; i++) {
                    var deviceEvent = vm.pagination.content[i];
                    if (deviceEvent.status == 'Open') {
                        vm.selectedEventIds.push(deviceEvent.id);
                    }
                }
            }
    };

    vm.closeEvents = function() {
        SpinnerService.wrap(DeviceService.closeEvents, $routeParams.deviceId, vm.selectedEventIds)
        .then(function(response) {
            vm.find();
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.find();
}
DeviceEventsController.prototype = Object.create(ListController.prototype);

controllers.controller('DeviceEventsFilterController', ['$scope','$uibModalInstance', 'filter', 'options', DeviceEventsFilterController]);
function DeviceEventsFilterController($scope,$uibModalInstance, filter, options) {
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

