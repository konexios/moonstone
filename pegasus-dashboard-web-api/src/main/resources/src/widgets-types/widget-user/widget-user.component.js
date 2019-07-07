(function() {
    'use strict';

    angular.module('widgets').component('widgetUser', {
        // TODO: change path
        templateUrl: '/views/widgets-types/widget-user/widget-user.tmpl.html',
        controller: widgetUserController,
        controllerAs: 'vm',
        bindings: {
            widget: '='
        }
    });

    function widgetUserController($scope) {
        var vm = this;

        vm.user = { name: '-' };

        vm.$onInit = function() {
            vm.widget.subscribe('/user', function(user) {
                vm.user = JSON.parse(user);
                $scope.$apply();
            });
        };
    }
})();
