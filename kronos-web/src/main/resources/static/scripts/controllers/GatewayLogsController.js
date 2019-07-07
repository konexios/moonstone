controllers.controller('GatewayLogsController', ['$routeParams', '$uibModal', '$filter', 'GatewayService', 'SecurityService', 'ErrorService', 'SpinnerService', GatewayLogsController]);
function GatewayLogsController($routeParams, $uibModal, $filter, GatewayService, SecurityService, ErrorService, SpinnerService) {
    var vm = this;

    LogsController.call(vm, $uibModal, $filter);

    var canViewLogs = SecurityService.canViewGatewayAuditLogs();
    vm.find = function() {
        if(canViewLogs) {
            SpinnerService.wrap(GatewayService.findLogs, $routeParams.gatewayId, vm.pagination, vm.filter)
            .then(function(response) {
                vm.update(response.data.result);
            })
            .catch(ErrorService.handleHttpError);
        }
    };

    if (canViewLogs) {
        vm.getAuditLog = function(log) {
            return SpinnerService.wrap(GatewayService.getAuditLog, log.id)
            .catch(ErrorService.handleHttpError);
        };

        SpinnerService.wrap(GatewayService.getLogOptions)
        .then(function(response) {
            vm.options = response.data;
        })
        .catch(ErrorService.handleHttpError);
    }
    vm.find();
}
GatewayLogsController.prototype = Object.create(LogsController.prototype);
