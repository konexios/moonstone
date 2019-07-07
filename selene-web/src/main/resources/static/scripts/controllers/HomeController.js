controllers.controller('HomeController',
  ['$scope','ErrorService', "GatewayService", "DeviceService",
    function ($scope, ErrorService, GatewayService, DeviceService) {
	 
      $scope.widgets = {
    		  myDevices: {
                  loading: false,
              },
              myGateways: {
                  loading: false,
              }
      }
      function loadPage() {
          $scope.checkGateway();
          $scope.noDevices();
      };
      
      //Check gateway status
	  $scope.checkGateway = function(){
		  $scope.widgets.myGateways.loading = true;
		  GatewayService.checkgatewaystatus()
      		.then(function (response) {
      		$scope.gatewayStatus =response.data.status;
      		
      	})
          .catch(function (response) {
            $scope.busy = false;
            ErrorService.handleHttpError(response);
          });
	  }
	  
	  //Count for number of devices
	  $scope.noDevices = function(){
		  $scope.widgets.myDevices.loading = true;
		  DeviceService.all()
		  .then(function(response){
			  
			  $scope.devices = response.data.length
		  }).catch(function (response) {
	            $scope.busy = false;
	            ErrorService.handleHttpError(response);
	      });;
	  }
	  
	  loadPage();
  }
]);