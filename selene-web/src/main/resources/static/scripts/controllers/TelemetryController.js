controllers.controller('TelemetryController',
    [
        '$scope', '$location', '$uibModal','$routeParams', 'ErrorService', 'SpinnerService', 'DeviceService', 'ToastrService','TransposeService',
	    function ($scope, $location ,$uibModal, $routeParams, ErrorService, SpinnerService, DeviceService, ToastrService,TransposeService) {
        	
        	//Telemetry transpose format example
        	$scope.telemetryTransposeExample = {
             		    "f|temperature"    : "26.2",
             		    "b|switch"            : "true",
             		    "s|alarm"        : "Intruder Detected"
             }
          
        	$scope.telTransposeObj = TransposeService.getTelTransposeObj();
        	
        	//Telemetry transpose format
            $scope.telemetryTran = {
            		    "key1"    : "value1",
            		    "key2"    : "value2",
            		    "key3"    : "value3"
            }
        	
            //Edit transpose file
        	DeviceService.edittransposefile($routeParams.deviceId,"telemetry")
        	.then(function (response) {
        		if(response.data.status !='ERROR'){
        			$("#transposeText").val(response.data.message);	
        		}
        	});
            
            //Save transpose file
        	$scope.saveTranspose = function(){
            	$scope.telTransposeObj["transposeFunction"]= transposeText.value;
            	
            	TransposeService.setTelTransposeObj($scope.telTransposeObj);
            	            	
            	DeviceService.saveTranspose($scope.telTransposeObj)
                 .then(function() {
                	 $location.path('#/device'+$scope.telTransposeObj.deviceId);
                     ToastrService.popupSuccess('Transpose function has been successfully saved.');
                     $scope.busy = false;
                 })
                 .catch(function (response) {
                     $scope.busy = false;
                     ErrorService.handleHttpError(response);
                 });
            	 
             }
        	
        	//Test javascript function
        	$scope.Test = function(){
                $uibModal.open({
                    animation: false,
                    templateUrl: 'partials/testRegTelTranspose.html',
                    controller: 'TestDeviceController',
                    size:'lg',
                    resolve: {
                   	 textToSave: function() {
                            return transposeText.value;
                        }
                    }
                })
       	
        	}
        	
        	$scope.back = function(){
        		$location.path('#/device'+$routeParams.deviceId);
            }
        	
        	//JQUERY to disable some content
    		$("#transposeText").keydown(function(e) {
        	    var oldvalue=$(this).val();
        	    var field=this;
        	    setTimeout(function () {
        	        if(field.value.indexOf('function transpose(incoming_payload)') !== 0) {
        	            $(field).val(oldvalue);
        	        } 
        	    }, 1);
        	});
        	
        	$scope.reset = function(){
              	 transposeText.value= "function transpose(incoming_payload)";
            }
        	
        	
        }
    ]
);
