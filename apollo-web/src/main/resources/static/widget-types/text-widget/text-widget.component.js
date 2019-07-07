(function() {
    'use strict';

    angular
        .module('widgets')
        .component('textWidget', {
            templateUrl: '/widget-types/text-widget/text-widget.tmpl.html',
            controller: widgetTextController,
            controllerAs: 'vm',
            bindings: {
                widget: '='
            }
        });


    function widgetTextController($scope) {
        var vm = this;
        vm.data = {
        	waiting: true,
        	error: false,
        	state: null,
            mapData: {
            	map: null,
            	marker: null,
            	infoWindow: null,
            	infoWindowOpen: false
            },
        	result: null
        };
        
        vm.$onInit = function() {
        	vm.widget.subscribe("/widget-state", function(message) {
        		// TODO handle state change
        	});
        	
        	vm.widget.subscribe("/widget-error", function(message) {
        		// TODO handle error
        	});
            
        	vm.widget.subscribe("/widget-data", function(message) {
        		console.log(message);

        		var widgetData = JSON.parse(message);
            	var json = { 
            		state: widgetData.state,
            		result: widgetData.data 
            	};
            	angular.extend(vm.data, json);
                vm.data.waiting = false;
            	$scope.$apply();
            });
            
            vm.widget.subscribeForMetaDataUpdate(function() {
            	$scope.$apply();
            });
        };
    }
})();