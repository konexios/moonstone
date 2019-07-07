controllers.controller('TestDeviceController',
    [
        '$scope', '$uibModalInstance', 'ErrorService', 'SpinnerService', 'DeviceService', 'ToastrService','textToSave',
	    function ($scope, $uibModalInstance, ErrorService, SpinnerService, DeviceService, ToastrService,textToSave) {
        	
        	$scope.textToSave = textToSave;
  
           //Test function for registration and telemetry
           $scope.testRegTelTransposeFunc = function()
            {
           	 $scope.input = inputJSON.value;
           	 DeviceService.testregteltranspose($scope.input,$scope.textToSave)
           	 .then(function (response) {
        		$scope.output = response.data;
           	 }).catch(function (response) {
	            ErrorService.handleHttpError(response);
	         });

            }
        
         //Test function for state
         $scope.testStateTransposeFunc = function(deviceUid,deviceName,deviceStates)
           {
              DeviceService.teststatetranspose($scope.textToSave,deviceUid,deviceName,deviceStates)
              .then(function (response) {
        		$scope.output = response.data;
           	 }).catch(function (response) {
	            ErrorService.handleHttpError(response);
	         });
          }
           
           $scope.close = function() {
               $uibModalInstance.dismiss('close');
           };
        }
]);