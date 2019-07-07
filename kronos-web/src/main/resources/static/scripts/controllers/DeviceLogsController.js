controllers.controller('DeviceLogsController', ['$routeParams', '$uibModal', '$filter', 'DeviceService', 'SecurityService', 'ErrorService', 'SpinnerService', DeviceLogsController]);
function DeviceLogsController($routeParams, $uibModal, $filter, DeviceService, SecurityService, ErrorService, SpinnerService) {
    var vm = this;

    LogsController.call(vm, $uibModal, $filter);

    var canViewLogs = SecurityService.canViewDeviceAuditLogs();
    vm.find = function() {
        if(canViewLogs) {
            SpinnerService.wrap(DeviceService.findLogs, $routeParams.deviceId, vm.pagination, vm.filter)
            .then(function(response) {
                vm.update(response.data.result);
            })
            .catch(ErrorService.handleHttpError);
        }
    };

    if (canViewLogs) {
        vm.getAuditLog = function(log) {
            return SpinnerService.wrap(DeviceService.getAuditLog, log.id)
            .catch(ErrorService.handleHttpError);
        };

        SpinnerService.wrap(DeviceService.getLogOptions)
        .then(function(response) {
            vm.options = response.data;
        })
        .catch(ErrorService.handleHttpError);
    }
    vm.find();
}
DeviceLogsController.prototype = Object.create(LogsController.prototype);
