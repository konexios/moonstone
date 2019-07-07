(function() {
    'use strict';

    angular
        .module('widgets')
        .component('deviceTelemetryGaugeWidget', {
            templateUrl: '/widget-types/device-telemetry-gauge-widget/device-telemetry-gauge-widget.tmpl.html',
            controller: widgetDeviceTelemetryGaugeController,
            controllerAs: 'vm',
            bindings: {
                widget: '='
            }
        });


    function widgetDeviceTelemetryGaugeController($scope, $sce) {
        var vm = this;
        vm.data = {
        	waiting: true,
        	error: false,
        	state: null,
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
        		var widgetData = JSON.parse(message);
            	var json = { 
            		state: widgetData.state,
            		result: widgetData.data 
            	};
            	angular.extend(vm.data, json);
                $scope.$apply();
            	
            	vm.data.waiting = false;
            });
            
            vm.widget.subscribeForMetaDataUpdate(function() {
            	$scope.$apply();
            })
        };
        
        vm.formatLastTelemetryItemValue = function (value) {
        	var nValue = value.replace(/\|/g, '<br/>');
        	var trustedObject = $sce.trustAsHtml(nValue)
        	var trustedHtml = $sce.getTrustedHtml(trustedObject)

        	return trustedHtml;
        };
    }
})();