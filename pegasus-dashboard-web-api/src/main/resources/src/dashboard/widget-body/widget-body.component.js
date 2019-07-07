(function() {
    'use strict';

    angular.module('widgets').component('widgetBody', {
        // TODO: change path
        templateUrl: '/views/dashboard/widget-body/widget-body.tmpl.html',
        controller: widgetBodyController,
        controllerAs: 'vm',
        bindings: {
            widget: '=',
            editMode: '=?'
        }
    });

    function widgetBodyController($scope, $compile, $element, $sce) {
        var vm = this;

        vm.$onInit = function() {
            // with watch we can change widget type\view dynamically
            $scope.$watch('vm.widget.directive', function() {
                var html = '<' + vm.widget.directive + ' widget="vm.widget"></' + vm.widget.directive + '>';
                $element.find('.widget-content').html($compile(html)($scope));
            });
        };
    }
})();
