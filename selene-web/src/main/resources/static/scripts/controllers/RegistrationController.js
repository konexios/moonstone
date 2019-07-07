controllers.controller('RegistrationController',
    [
        '$scope', '$location', '$uibModal','$routeParams',  'ErrorService', 'SpinnerService', 'DeviceService', 'ToastrService','TransposeService',
	    function ($scope, $location ,$uibModal,$routeParams, ErrorService, SpinnerService, DeviceService, ToastrService,TransposeService) {
        	//Registration transpose format
        	$scope.regTranspose = [
              	   {
              		     "deviceUid": "BPS_02:E3:A4:BC:33:D7",
              		     "deviceName": "BloodPressureSensor",
              		     "properties": [
              		       {
              		         "maxValue": 180,
              		         "minValue": 40,
              		         "name": "systolic",
              		         "units": "mm Hg",
              		         "valueType": "integer",
              		         "operation": "r"
              		       },
              		       {
              		         "maxValue": 120,
              		         "minValue": 40,
              		         "name": "diastolic",
              		         "units": "mm Hg",
              		         "valueType": "integer",
              		         "operation": "r"
              		       }
              		     ]
             	   }
             ];
       
        	$scope.regTransposeObj = TransposeService.getRegTransposeObj();
        	
        	//Edit transpose file
        	DeviceService.edittransposefile($routeParams.deviceId,"registration")
        	.then(function (response) {
        		if(response.data.status !='ERROR'){
        			$("#transposeText").val(response.data.message);	
        		}
        			
        	});
        	
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
            	$location.path('/device/'+$routeParams.deviceId);
            }
           
        	//Save transpose file
        	$scope.saveTranspose = function(){
        		$scope.regTransposeObj["transposeFunction"]= transposeText.value
            	TransposeService.setRegTransposeObj($scope.regTransposeObj);
            	
            	DeviceService.saveTranspose($scope.regTransposeObj)
            	  .then(function(response) {
                    	$location.path("/device/"+$scope.regTransposeObj.deviceId);
                        ToastrService.popupSuccess('Transpose function has been successfully saved.');
                        $scope.busy = false;
                    })
                    .catch(function (response) {
                        $scope.busy = false;
                        ErrorService.handleHttpError(response);
                    });
            	 
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
