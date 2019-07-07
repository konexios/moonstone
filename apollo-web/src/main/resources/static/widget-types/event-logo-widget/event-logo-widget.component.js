(function() {
    'use strict';

    angular
        .module('widgets')
        .component('logoWidget', {
            templateUrl: '/widget-types/event-logo-widget/event-logo-widget.tmp.html',
            controller: eventLogoWidgetController,
            controllerAs: 'vm',
            bindings: {
                widget: '='
            }
        });

    function eventLogoWidgetController($scope) {
        var vm = this;
        vm.data = {
        	waiting: true
        };

        vm.$onInit = function() {

            vm.widget.subscribe("/widget-state", function(message) {
                // on each state change - for instance on configuration change - request url again
                vm.widget.send("/url", null);
            });

            vm.widget.subscribe("/widget-error", function(message) {
                // TODO handle error
            });

            vm.widget.subscribe("/url", function(message) {
                vm.url = message;
                vm.data.waiting = false;
                $scope.$apply();
            });

            vm.widget.send("/url", "dummy_data-to_fix_router");
        };

    }

})();