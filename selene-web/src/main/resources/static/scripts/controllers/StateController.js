controllers.controller('StateController',
    [
        '$scope', '$location', '$uibModal', '$routeParams', 'ErrorService', 'SpinnerService', 'DeviceService', 'ToastrService','TransposeService',
	    function ($scope, $location ,$uibModal, $routeParams, ErrorService, SpinnerService, DeviceService, ToastrService,TransposeService) {
        	
        	//State transpose format
        	$scope.stateTranspose = [
        	 	{
        	         "propertyName": "led0",
        	         "propertyValue": "true",
        	         "timestamp": "2018­04­19T08:51:14.330Z"
        	     },
        	     {
        	         "propertyName": "led1",
        	         "propertyValue": "true",
        	         "timestamp": "2018­04­19T08:51:14.330Z"
        	     },
        	     {
        	         "propertyName": "color",
        	         "propertyValue": "345457",
        	         "timestamp": "2018­04­19T08:51:14.330Z"
        	     }
        	 ];
         
       	$scope.stateTransposeObj = TransposeService.getStateTransposeObj();
   
        //Edit transpose file   
        DeviceService.edittransposefile($routeParams.deviceId,"state")
       	.then(function (response) {
       		if(response.data.status !='ERROR'){
       			$("#transposeText").val(response.data.message);
       		}
       	});
   
        //Save transpose file
        $scope.saveTranspose = function(){
            	$scope.stateTransposeObj["transposeFunction"]= transposeText.value;
            	
            	TransposeService.setStateTransposeObj($scope.stateTransposeObj);
            	
            		DeviceService.saveTranspose($scope.stateTransposeObj)
                    .then(function() {
                    	$location.path('#/device'+$scope.stateTransposeObj.deviceId);
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
                    templateUrl: 'partials/testStateTranspose.html',
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
        	        if(field.value.indexOf('function transpose(deviceUid,deviceName,deviceStates)') !== 0) {
        	            $(field).val(oldvalue);
        	        } 
        	    }, 1);
        	});
        	
        	$scope.reset = function(){
              	 transposeText. value= "function transpose(deviceUid,deviceName,deviceStates)";
            }

        }
    ]
);
