controllers.controller('DeleteTransposeController',
    [
        '$scope', '$uibModalInstance', 'ErrorService', 'DeviceService', 'ToastrService', 'deviceId','transposeType', 
	    function ($scope, $uibModalInstance, ErrorService, DeviceService, ToastrService, deviceId, transposeType) {
        	//delete transpose file function
        	$scope.deleteTranspose = function(){
        		DeviceService.deletetransposefile(deviceId,transposeType)
        		.then( function(response) {
        			ToastrService.popupSuccess('File has been successfully deleted.');
                    $scope.busy = false;
                    $uibModalInstance.close();
                })
                .catch(function (response) {
                    $scope.busy = false;
                    ErrorService.handleHttpError(response);
                });
        	} 
        	//close modal
        	$scope.close = function() {
                $uibModalInstance.dismiss('cancel');
            };
            
        }
]);