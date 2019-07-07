controllers.controller('DeleteDeviceController',
    [
        '$scope', '$location','$uibModalInstance','$routeParams', 'ErrorService', 'SpinnerService', 'DeviceService', 'ToastrService', 'deviceId','deviceUID', 'WebSocketsService',
	    function ($scope, $location, $uibModalInstance,$routeParams ,ErrorService, SpinnerService, DeviceService, ToastrService, deviceId, deviceUID,WebSocketsService) {
        	
        	var wsConnection = WebSocketsService.connect('/websocket', {
        	      onConnect: function () {
        	      },	onDisconnect: function () {
  				}
  				});
        	//Delete device function call   	 
			$scope.deletedeviceFun = function(){
				//subscribe on topic to know whether device is deleted or not.
				 wsConnection.subscribe('/topic/device/' + $routeParams.deviceId, function (message) {
			       	SpinnerService.hide();
			       	if(message.status=="OK"){
			       		ToastrService.popupSuccess('Device has been successfully deleted.');
			       		$location.path('/devices');
			       	}else{
			       		ToastrService.popupError("Device has not been deleted.")
			       	}
			       	$uibModalInstance.close();
						        	
				});
				DeviceService.deletedevice(deviceId,deviceUID)
				  .then(function(response){
					  SpinnerService.show();
				  
				}).catch(function(response){ 
				        ErrorService.handleHttpError(response);
				});
			}
        	//destroy websocket connection
			$scope.$on("$destroy", function () {
				if (wsConnection) {
					wsConnection.disconnect();
				}
			});
            //close modal
			$scope.close = function() {
                 $uibModalInstance.dismiss('cancel');
             };            
        }
]);