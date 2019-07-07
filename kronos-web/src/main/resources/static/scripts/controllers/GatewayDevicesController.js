controllers.controller('GatewayDevicesController', ['$scope', '$routeParams', '$timeout', 'DeviceService', 'SecurityService', 'ErrorService', 'ToastrService', 'SpinnerService', 'EventService', GatewayDevicesController]);
function GatewayDevicesController($scope, $routeParams, $timeout, DeviceService, SecurityService, ErrorService, ToastrService, SpinnerService, EventService) {
    var vm = this;

    vm.canSendCommandToDevice = SecurityService.canSendCommandToDevice();

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        columnHeaders: [
            {label: 'Name', value: 'name', sortable: true},
            {label: 'UID', value: 'uid', sortable: true},
            {label: 'Device Type', value: 'deviceTypeName', sortable: false},
            {label: 'Enabled', value: 'enabled', sortable: false},
            {label: 'Commands', value: '', sortable: false}
        ]
    });

    var filter = {
        deviceTypeIds: [],
        gatewayIds: [$routeParams.gatewayId],
        userIds: [],
        nodeIds: [],
        enabled: null
    };

    EventTrackerMixin.call(vm, $scope, $timeout, EventService, ToastrService);

    vm.startDevice = function(device) {
        SpinnerService.wrap(DeviceService.startDevice, device.id)
        .then(function(response) {
            ToastrService.popupSuccess('Start event has been queued for the device ' + device.name);
            vm.trackEvent(response.data, 'start', device);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.stopDevice = function(device) {
        SpinnerService.wrap(DeviceService.stopDevice, device.id)
        .then(function(response) {
            ToastrService.popupSuccess('Stop event has been queued for the device ' + device.name);
            vm.trackEvent(response.data, 'stop', device);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.find = function() {
        SpinnerService.wrap(DeviceService.find, vm.pagination, filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.find();
}
GatewayDevicesController.prototype = Object.create(ListController.prototype);
