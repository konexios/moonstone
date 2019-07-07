(function () {
    'use strict';

    controllers.controller("DeviceTelemetryExportController", DeviceTelemetryExportController);

    DeviceTelemetryExportController.$inject = ["$scope","$routeParams", "DeviceService"];

    function DeviceTelemetryExportController($scope,$routeParams, DeviceService) {
        var vm = this;

        var initialTo = new Date();
        initialTo.setMilliseconds(0); // trim current date/time to seconds
        var initialFrom = new Date(initialTo.getFullYear(), initialTo.getMonth(), initialTo.getDate());

        vm.model = {
            type: 'json',
            sort: 'DESC',
            names: null,
            from: initialFrom,
            to: initialTo
        };
        
        vm.exportParams = {};

        vm.exportUrl = DeviceService.exportTelemetryUrl($routeParams.deviceId);

        vm.onChange = function() {
            // sync vm.exportParams with vm.model
            vm.exportParams.type = vm.model.type;
            vm.exportParams.sort = vm.model.sort;
            vm.exportParams.names = vm.model.names && vm.model.names.join ? vm.model.names.join(',') : null;
            vm.exportParams.from = vm.model.from && !isNaN(vm.model.from) ? vm.model.from.getTime() : null;
            vm.exportParams.to = vm.model.to && !isNaN(vm.model.to) ? vm.model.to.getTime() : null;
        };
        vm.onChange();
    }
})();