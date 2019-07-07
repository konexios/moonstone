controllers.controller('ScanMacAddressController',
    [
        '$scope', '$location','$uibModalInstance','$routeParams', 'ErrorService', 'SpinnerService', 'DeviceService','macAddress','ToastrService',
	    function ($scope, $location, $uibModalInstance,$routeParams ,ErrorService, SpinnerService, DeviceService, macAddress ,ToastrService) {
        	
        	$scope.macAddress = macAddress;
    
        	//Select device function call   	 
			$scope.selectDevice = function(macId){
				
				$scope.macId = macId;
				$uibModalInstance.close($scope.macId);
			}
        	
        }
]);