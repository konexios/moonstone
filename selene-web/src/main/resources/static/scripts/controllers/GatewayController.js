controllers.controller('GatewayController',
  ['$scope', 'ErrorService', 'SpinnerService', "GatewayService","DeviceService", "AwsService", "AzureService", "IbmService","WebSocketsService", "ToastrService",
    function ($scope, ErrorService, SpinnerService, GatewayService, DeviceService,AwsService, AzureService, IbmService,WebSocketsService, ToastrService) {
      $scope.pageTitle = 'Gateway';
      $scope.pageSubTitle = '';

      $scope.busy = true;
      $scope.cloudPlatformOptions = [];
      $scope.gateway = {};
      $scope.seleneProp = {};
      $scope.selene='';
      $scope.updateGateway = false;

      // other data
      $scope.aws = {};

      $scope.additiionalBlock = '';
      $scope.checkType = function (cloudPlatform) {
        switch (cloudPlatform) {
          case 'Aws':
            $scope.additiionalBlock = "Aws";
            loadAws();
            break;
          case 'Ibm':
            $scope.additiionalBlock = "Ibm";
            loadIbm();
            break;
          case 'Azure':
            $scope.additiionalBlock = "Azure";
            loadAzure();
            break;
          case 'IotConnect':
            // TODO load data IotConnect
            $scope.additiionalBlock = "IotConnect";
            break;

        }
      };
      
        $scope.deviceProp = 'ON';
        $scope.gatewayDetail = 'OFF';

        $scope.selenePropFun = function(){
            $scope.deviceProp = 'ON';
            $scope.gatewayDetail = 'OFF';
        }

        $scope.gatewayDetailFun = function(){
            $scope.deviceProp = 'OFF';
            $scope.gatewayDetail = 'ON';
            getGateway();
        }

        $scope.nextStep = function(){
            $scope.deviceProp = 'OFF';
            $scope.gatewayDetail = 'ON';
            getGateway();
            getPropertyFunction();
        }

        $scope.dropDownChange = function(){
            $("#fileCreateDiv").load("../partials/selenePropFile.html");
        }

        getPropertyFunction();
        $scope.busy = true;
        $scope.showGatewayProperties = false;
        function getGateway() {
        	GatewayService.gateway()
        	.then(function (response) {
        	    $scope.response = response;
                    if(response.status == 204){
                        $scope.updateGateway = false;
                        $scope.gateway1 = {};
                        GatewayService.loadGatewayProperties().then(function(response){
                        	if(!response.data){
                        		$scope.gateway1.cloudBatchSendingIntervalMs = 60000;
                        		$scope.gateway1.heartBeatIntervalMs = 500;

                        	}else{                        	
                        		$scope.gateway1 = response.data;
                        	}                        	
                        });
                    }
                    else{
                        $scope.cloudPlatformOptions = response.data.cloudPlatformOptions;
                        $scope.gateway = response.data.gateway;
                        
                        var length = (Object.keys($scope.gateway.properties)).length;
                        if(length>0){
                        	$scope.showGatewayProperties = true;
                        }
                        if ($scope.gateway.id && $scope.gateway.id !== "") {
                            $scope.updateGateway = true;
                        }
                        $scope.busy = false;

                        if ($scope.gateway.cloudPlatform === "Aws") {
                            $scope.additiionalBlock = "Aws";
                            loadAws();
                        } else if ($scope.gateway.cloudPlatform === 'Azure') {
                            $scope.additiionalBlock = 'Azure';
                            loadAzure();
                        } else if ($scope.gateway.cloudPlatform === 'Ibm') {
                            $scope.additiionalBlock = 'Ibm';
                            loadIbm();
                        } else if ($scope.gateway.cloudPlatform === 'IotConnect') {
                            $scope.additiionalBlock = 'IotConnect';
                            loadIotConnect();
                        }
           
                        GatewayService.checkgatewaystatus()
                        .then(function (response) {
                            $scope.gatewayStatus =response.data.status;
                        })
                        .catch(function (response) {
                            $scope.busy = false;
                        });
                    }
                })
                .catch(function (response) {
                    $scope.busy = false;
                });
        }
      
      //Get gateway details
      getGateway();

      $scope.seleneNextEnable= false;
      $scope.loadNextEnable = false;
      // Create selene property
      $scope.saveSeleneProp = function(form,formData){
          $scope.seleneProp = formData;
          if(form.$valid) {
              GatewayService.createselene($scope.seleneProp)
              .then(function(response){
                  $scope.editSeleneMode = true;
                  if(response.data.status == 'ERROR'){
                      ToastrService.popupError(response.data.message)
                      $scope.seleneNextEnable= false;
                      $scope.editSeleneMode = true;
                  }else{
                      $scope.seleneNextEnable= true;
                      $scope.editSeleneMode = true;
                      ToastrService.popupSuccess("Successfully created.");
                  }
              })
              .catch(function (response) {
                  ErrorService.handleHttpError(response);
              });
              $scope.cancelseleneEdit();
          }
          else{
              ToastrService.popupError("The form is invalid! Please make changes and try again.");
          }
      }
      $scope.seleneEdit = function() {
          $scope.editSeleneMode = true;
      };

      $scope.cancelseleneEdit = function() {
          $scope.editSeleneMode = false;
      };

      // get self property
      function getPropertyFunction(){
          GatewayService.getselene()
          .then(function (response){
              $scope.seleneProp = response.data;
              if($scope.seleneProp.propertiesFilePresent== true){
                  $scope.seleneNextEnable= true;
                  $scope.editSeleneMode = false;
                  $scope.deviceProp = 'OFF';
                  $scope.gatewayDetail = 'ON';
              }
          })
          .catch(function (response){
              ErrorService.handleHttpError(response);
          });
      }

      $scope.loadFile = function(form,formData){
          if (form.$valid) {
              $scope.loadNextEnable= true;
              GatewayService.loadselene(formData)
              .then(function (response){
                  if(response.data.status == 'ERROR'){
                      ToastrService.popupError(response.data.message)
                      $scope.loadNextEnable= false;
                  }
                  else{
                      ToastrService.popupSuccess("Successfully loaded")
                  }
              })
              .catch(function (response){
                  ErrorService.handleHttpError(response);
              });
          }
          else {
              ToastrService.popupError("The form is invalid! Please make changes and try again.");
          }
      }
      
      // Aws columns
      $scope.awsHeaders = [
        {label: 'Host', value: 'host'},
        {label: 'Port', value: 'port'},
        {label: 'Root Cert', value: 'rootCert', type:"long"},
        {label: 'Client Cert', value: 'clientCert', type:"long"},
        {label: 'Private Key', value: 'privateKey', type:"long"},
        {label: 'Enabled', value: 'enabled', type:"bool"}
      ];
      function loadAws() {
        $scope.busy = true;
        AwsService.aws()
          .then(function (response) {
            $scope.aws = response.data.aws;
            $scope.busy = false;
          })
          .catch(function (response) {
            $scope.busy = false;
            ErrorService.handleHttpError(response);
          });
      }

      // Azure columns
      $scope.azureHeaders = [
        {label: 'Host', value: 'host'},
        {label: 'Access key', value: 'accessKey'}
      ];
      function loadAzure() {
        $scope.busy = true;
        AzureService.azure()
          .then(function (response) {
            $scope.azure = response.data.azure;
            $scope.busy = false;
          })
          .catch(function (response) {
            $scope.busy = false;
            ErrorService.handleHttpError(response);
          })
      }

      // IBM columns
      $scope.ibmHeaders = [
        {label: 'Organization id', value: 'organizationId'},
        {label: 'Gateway type', value: 'gatewayType'},
        {label: 'Gateway id', value: 'gatewayId'},
        {label: 'Authorization method', value: 'authMethod'},
        {label: 'Authorization token', value: 'authToken'}
      ];
      function loadIbm() {
        $scope.busy = true;
        IbmService.ibm()
          .then(function (response) {
            $scope.ibm = response.data.ibm;
            $scope.busy = false;
          })
          .catch(function (response) {
            $scope.busy = false;
            ErrorService.handleHttpError(response);
          })
      }
      
      $scope.iotConnectHeaders = [
          {label: 'MQTT URL', value: 'iotConnectMqtt'},
          {label: 'MQTT VHost ', value: 'iotConnectMqttVHost'},
        ];
      function loadIotConnect() {
    	  $scope.busy = true;
    	  GatewayService.iotconnect()
            .then(function (response) {
              $scope.iotConnect = response.data.iotConnect;
              $scope.busy = false;
            })
            .catch(function (response) {
              $scope.busy = false;
              ErrorService.handleHttpError(response);
            })
      }

      $scope.editMode = false;

      $scope.save = function (form, formData) {
        if (form.$valid) {
          $scope.busy = true;

          var savePromise = null;
          if ($scope.gateway.id && $scope.gateway.id != "") {
            savePromise = GatewayService.update(formData);
          } else {
            savePromise = GatewayService.create(formData);
          }

          if (!savePromise)
            return;

          savePromise
            .then(function (response) {
              $scope.gateway = response.data;
              getPropertyFunction();
              ToastrService.popupSuccess($scope.gateway.name + " has been successfully saved.");

              // reset form
              form.$setPristine();
              form.$setUntouched();

              $scope.busy = false;
            })
            .catch(function (response) {
              $scope.busy = false;
              ErrorService.handleHttpError(response);
            });
        } else {
          ToastrService.popupError("The form is invalid! Please make changes and try again.");
        }
        $scope.cancelEdit();
      };

      $scope.startEdit = function() {
        $scope.editMode = true;
      };

      $scope.cancelEdit = function() {
        $scope.editMode = false;
      };
      
      var savePromise = null;
      $scope.create = function (form, formData) {
    	  
    	  
          if (form.$valid) {
        	 // $scope.IsVisible = $scope.IsVisible ? false : true;
	            $scope.busy = true;
	            SpinnerService.show();
	            if(formData.uid =="" || formData.uid==undefined){
	            	formData.uid="${mac.simple}";
	            }
   
	            savePromise = GatewayService.create(formData);	
            
            if (!savePromise)
              return;

            savePromise
              .then(function (response) {
            	 
              // $scope.gateway1 = response.data;
            	  $scope.IsVisible = true;
                // reset form
                form.$setPristine();
                form.$setUntouched();

                ToastrService.popupSuccess(formData.name + " has been successfully saved.");
              })
              .catch(function (response) {
                ErrorService.handleHttpError(response);
              })
              .finally(function () {
                $scope.busy = false;
                SpinnerService.hide();
              });
          } else {
            ToastrService.popupError("The form is invalid! Please make changes and try again.");
          }
        };
        
        //Start gateway
        $scope.startGateway = function() {
        	GatewayService.start()
              .then(function (response) {
            	  SpinnerService.show();
                
            	  setTimeout(function(){
                	SpinnerService.hide();
                	getGateway();
                	$scope.gatewayResponse = response;
                },10000)
                
                ToastrService.popupSuccess('Start event has been queued for the gateway ');
              })
              .catch(function() {
                ToastrService.popupError('An error has occurred');
              });
            $scope.lastPing = moment(new Date()).format('MM/DD/YYYY hh:mm:ss A');
        };

        var wsConnection;
        wsConnection = WebSocketsService.connect('/websocket', {
            onConnect: function () {
                wsConnection.subscribe('/topic/device/0', function (message) {
                    setTimeout(function(){
                      SpinnerService.hide();
                      ToastrService.popupSuccess('Stop event has been queued for the gateway ' );
                      getGateway();
                  },5000)
             });
            },
            onDisconnect: function () {
          }
        });
        
        //Stop gateway
        $scope.stopGateway = function() {
              DeviceService.stop("0")
              .then(function() {
                  SpinnerService.show();
                  
              })
              .catch(function () {
                ToastrService.popupError('An error has occurred');
              });
            $scope.lastPing = moment(new Date()).format('MM/DD/YYYY hh:mm:ss A');
        
      }
        
      $scope.$on("$destroy", function () {
          if (wsConnection) {
            wsConnection.disconnect();
          }
        });
        
    }
  ]);