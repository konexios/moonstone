(function() {
    'use strict';

    angular
        .module('widgets')
        .component('eventRegisteredWidget', {
            templateUrl: '/widget-types/event-registered-widget/event-registered-widget.tmp.html',
            controller: eventRegisteredController,
            controllerAs: 'vm',
            bindings: {
                widget: '='
            }
        });

    function eventRegisteredController($scope) {
        var vm = this;
        vm.data = {
        	waiting: true
        };

        vm.$onInit = function() {

            vm.widget.subscribe("/widget-state", function(message) {});


            vm.widget.subscribe("/widget-error", function(message) {
                // TODO handle error
            });

            vm.widget.subscribe("/statistics", function(message) {
                var stat = JSON.parse(message);
                vm.total = stat.data.allRegisteredUsersCount;
                vm.today = stat.data.todayRegisteredUsersCount;
                vm.data.waiting = false;
                $scope.$apply();
            });
        };

    }

})();