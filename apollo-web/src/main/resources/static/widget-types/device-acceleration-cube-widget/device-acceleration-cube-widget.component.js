(function() {
    'use strict';

    angular
        .module('widgets')
        .component('deviceAccelerationCubeWidget', {
            templateUrl: '/widget-types/device-acceleration-cube-widget/device-acceleration-cube-widget.tmpl.html',
            controller: deviceAccelerationCubeWidgetController,
            controllerAs: 'vm',
            bindings: {
                widget: '='
            }
        });


    function deviceAccelerationCubeWidgetController($scope) {
        var vm = this;

        vm.data = {
        	waiting: true,
        	error: false,
        	state: null,
        	result: null,
    		PI: 3.14159265358979323846264338327950288,
        	cubeModel: {
    			accelTransform: 'rotateX(0) rotateY(0) rotateZ(0)',
    			accelX: 0,
    			accelY: 0,
    			accelZ: 0,
    			accelRoll: 0,
    			accelPitch: 0,
    			transform: 'rotateX(0) rotateY(45)',
    			cachedX: 0,
    			cachedY: 45,
    			cubeTransform: 'rotate3d(0, 0, 0, 0rad)'
    		}
        };

		function getRollAndPitchTransformation(f3AccelerationXYZ) {
			if (f3AccelerationXYZ) {
				var v = f3AccelerationXYZ.split('|');
				var x = +v[0], y = +v[1], z = +v[2];
				if (isNaN(x) || isNaN(y) || isNaN(z)) {
					return vm.data.cubeModel.accelTransform;
				}
				
				var alpha = 1;
				
				var nX = vm.data.cubeModel.accelX;
				var nY = vm.data.cubeModel.accelY;
				var nZ = vm.data.cubeModel.accelZ;

				nX = x * alpha + (nX * (1.0 - alpha));
			    nY = y * alpha + (nY * (1.0 - alpha));
			    nZ = z * alpha + (nZ * (1.0 - alpha));

				vm.data.cubeModel.accelX = nX;
				vm.data.cubeModel.accelY = nY;
				vm.data.cubeModel.accelZ = nZ;
			    
				var roll = (Math.atan2(nY, -nZ) * 180) / vm.data.PI;
				var pitch = (Math.atan2(nX, Math.sqrt(nY * nY + nZ * nZ)) * 180) / vm.data.PI;
				
				vm.data.cubeModel.accelRoll = roll;
				vm.data.cubeModel.accelPitch = pitch;
				
				return 'rotateX(' + (roll) + 'deg) rotateY(' + (pitch) + 'deg)';					
			}
		}		
		
        vm.$onInit = function() {
        	vm.data.cubeModel.accelTransform = getRollAndPitchTransformation("0.0|0.0|0.0");

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
                
                
                if (vm.data.result.cubeValues != null)
					vm.data.cubeModel.accelTransform = getRollAndPitchTransformation(vm.data.result.cubeValues);
                
                $scope.$apply();
                vm.data.waiting = false;
            });
            
            vm.widget.subscribeForMetaDataUpdate(function() {
            	$scope.$apply();
            })
        };
    }

})();