controllers.controller("DeviceController",
  function ($scope, $location, $uibModal , $routeParams, ErrorService, SpinnerService, DeviceService, ToastrService, TelemetryService, TransposeService, WebSocketsService) {
    $scope.pageTitle = "Device";
    $scope.pageSubTitle = "";

    $scope.busy = true;
    $scope.device = {};
    $scope.showDate = function (timestamp) {
      return moment(timestamp).format('MM/DD/YYYY hh:mm:ss A');
    };
    // Telemetry columns
    $scope.telemetryHeaders = [
      {label: 'Name', value: 'name'},
      {label: 'Value', value: 'value'},
      {label: 'Timestamp', value: 'timestamp'}
    ];

    var deviceStarted = false;
    var deviceOnline = false;
    var command = '';
    $scope.errorMessage = '';
    $scope.lastPing = '';
    $scope.command = '';
    $scope.deviceOnline = deviceOnline;
    $scope.deviceStarted = deviceStarted;
    $scope.lastSuccessfulPing = '';

    $scope.telTransposeObj = TransposeService.getTelTransposeObj();
    $scope.stateTransposeObj = TransposeService.getStateTransposeObj();
    $scope.regTransposeObj = TransposeService.getRegTransposeObj();
   
    var wsConnection;

    $scope.activeTab = 0;
    $scope.activeSubTab = 2;

    wsConnection = WebSocketsService.connect('/websocket', {
        onConnect: function () {
        	  if ($routeParams.deviceId) {
        	      $scope.busy = true;
        	      SpinnerService.show();

        	      deviceDetails();

        	      DeviceService.devices()
        	          .then(function (response) {
        	              $scope.options = {
        	                  deviceTypes: response.data
        	              };
        	          })
        	          .catch(function (response) { ErrorService.handleHttpError(response); });
        	  }
              wsConnection.subscribe('/topic/device/' + $routeParams.deviceId, function (message) {
            	var messageStatus = message.status.toLowerCase();
                
                // Success response based on message received on topic
                if(message.message =="Failed to start device"){
                        ToastrService.popupError("Device can not be started.")
                }
                else if(message.message == "Failed to stop device"){
                        ToastrService.popupError("Device can not be stopped.")
                }
                else if(message.message =="Device started"){
                        ToastrService.popupSuccess("Device started successfully.");
                }
                else if(message.message =="Device stopped"){
                        ToastrService.popupSuccess("Device stopped successfully.");
                }
                else if(message.message =="Gateway stopped"){
                        ToastrService.popupSuccess("Gateway stopped successfully.");
                }
                else if(message.message=="Command Received"){
                            ToastrService.popupSuccess('File has been successfully imported.');
                }
				
                var deviceId = message.deviceId;
                if (deviceId !== $routeParams.deviceId) return;
                deviceOnline = true;
                $scope.errorMessage = '';

                $scope.deviceOnline = deviceOnline;
                if (messageStatus !== 'error' && messageStatus !== 'ok') {
                  deviceStarted = deviceStarted = messageStatus === 'starting' || messageStatus === 'started' || messageStatus !== 'created';
                } else if (messageStatus === 'ok') {
                  deviceStarted = command === 'start';
                } else if (messageStatus === 'error') {
                  deviceStarted = command === 'start';
                  $scope.errorMessage = message.message;
                }
                $scope.deviceStarted = deviceStarted;
                deviceOnline = false;
                $scope.lastSuccessfulPing = moment(new Date()).format('MM/DD/YYYY hh:mm:ss A');
                $scope.$apply();
              })
        },
        onDisconnect: function () {
        }
      });
    
    // Get device details function
    function deviceDetails(){

    	DeviceService.device($routeParams.deviceId)
        .then(function (response) {
          $scope.device = response.data.device;
          $scope.deviceId = $scope.device.id
          $scope.deviceProperties = Object.keys(response.data.device.properties).sort(function(a, b) {
              return ('' + a.toLowerCase()) > ('' + b.toLowerCase()) ? 1 : -1;
          });
          $scope.lastPing = moment(new Date()).format('MM/DD/YYYY hh:mm:ss A');
          $scope.telemetryList = []
          wsConnection.subscribe('/topic/telemetry', function (message) {
        	  // add telemetry timestamp if device hid is matched
        	  if($scope.device.hid==message.deviceHid){
	        	  var telemetryTimestamp = Number(message.timestamp);
	        	  var telemetryTimestamp =$scope.showDate(telemetryTimestamp)

	        	  delete message.timestamp;
	        	  delete message.deviceHid;
	              for (var key in message) {
	            	  $scope.telemetryList.push({"name":key, "value":message[key],"timestamp":telemetryTimestamp});
	            	  $scope.$apply();
	              }
        	  }
            })
        })
        .catch(function (response) {
          ErrorService.handleHttpError(response);
        })
        .finally(function () {
          $scope.busy = false;
          SpinnerService.hide();
        });
    }
  
    // To create registration transpose
    $scope.createRegistration = function (){
   		 $scope.regTransposeObj={
   				 deviceId: $scope.deviceId,
   				 transposeType : "registration",
   	 }
      TransposeService.setRegTransposeObj($scope.regTransposeObj);
      $location.path("/device/"+$scope.deviceId+"/registrationtranspose");   	 
    }
    // To edit registration file
    $scope.editRegistration = function(){
    	$scope.regTransposeObj={
  				deviceId: $scope.deviceId,
  				transposeType : "registration"
  		 }
    	TransposeService.setRegTransposeObj($scope.regTransposeObj);
    	
    	DeviceService.edittransposefile($scope.deviceId,"registration")
    	.then(function (response) {
    		
    		$scope.regTransposeObj = TransposeService.getRegTransposeObj();
            $scope.regTransposeObj["responseReg"] = response;
            TransposeService.setRegTransposeObj($scope.regTransposeObj);
            $location.path("/device/"+$scope.deviceId+"/registrationtranspose");            
        })
        .catch(function (response) { ErrorService.handleHttpError(response); });
    }
    // To create registration transpose
    $scope.deleteRegistration = function(){
          $scope.deviceId = $scope.device.id
          $scope.transposeType = "registration"; 
                                       
          var modalInstance = $uibModal.open({
                    animation: false,
                    templateUrl: 'partials/deleteTranspose.html',
                    controller: 'DeleteTransposeController',
                    resolve:{
                    	deviceId: function() {
                            return $scope.deviceId;
                        },
                        transposeType : function(){
                            return $scope.transposeType;
                        }
                    }
                    })
          modalInstance.result.then(
        		  function(){
        			  $scope.regTransposeObj={
        					  responseReg: null
        			  }
        	          TransposeService.setRegTransposeObj($scope.regTransposeObj);
        			  deviceDetails()
        		  }
          );
                 
       }
    // To create registration transpose
    $scope.createTelemetry = function (){
      		$scope.telTransposeObj={
  				 deviceId: $scope.deviceId,
  				 transposeType :"telemetry",
  		 }
        TransposeService.setTelTransposeObj($scope.telTransposeObj);
        $location.path("/device/"+$scope.deviceId+"/telemetrytranspose");      	      	 
       }

    // To create registration transpose
    $scope.editTelemetry = function(){
    	$scope.telTransposeObj={
 				 deviceId: $scope.deviceId,
 				 transposeType : "telemetry",
 		 }
    	TransposeService.setTelTransposeObj($scope.telTransposeObj);
    	
    	DeviceService.edittransposefile($scope.deviceId,"telemetry")
    	.then(function (response) {
    		$scope.telTransposeObj = TransposeService.getTelTransposeObj();
    		
            $scope.telTransposeObj["responseTel"]= response;
            
            TransposeService.setTelTransposeObj($scope.telTransposeObj);
            $location.path("/device/"+$scope.deviceId+"/telemetrytranspose");
        })
        .catch(function (response) { ErrorService.handleHttpError(response); });
    }
    
    
    // To create registration transpose
    $scope.deleteTelemetry = function(){
   	 
          $scope.deviceId = $scope.device.id;
          $scope.transposeType = "telemetry";
          var modalInstance = $uibModal.open({
                    animation: false,
                    templateUrl: 'partials/deleteTranspose.html',
                    controller: 'DeleteTransposeController',
                    resolve:{
                        deviceId: function() {
                            return $scope.deviceId;
                        },
                        transposeType : function(){
                            return $scope.transposeType;
                        }
                    }
                    })
                    modalInstance.result.then(
                  		  function(){
                  			$scope.telTransposeObj={
                  					responseTel: null
                  			}
                            TransposeService.setTelTransposeObj($scope.telTransposeObj);
                			  deviceDetails()
                		  }
                  );
    }
    
    // To create State transpose
    $scope.createState = function (){
   		 $scope.stateTransposeObj={
     				 deviceId: $scope.deviceId,
     				 transposeType : "state",
     	 }
       TransposeService.setStateTransposeObj($scope.stateTransposeObj);
       $location.path("/device/"+$scope.deviceId+"/statetranspose");
      }
    
    // To edit State transpose
    $scope.editState = function(){
	   $scope.stateTransposeObj={
				 deviceId: $scope.deviceId,
				 transposeType : "state",
		 }
	   TransposeService.setStateTransposeObj($scope.stateTransposeObj);
	   
	   DeviceService.edittransposefile($scope.deviceId,"state")
   		.then(function (response) {
   			$scope.stateTransposeObj = TransposeService.getStateTransposeObj();
   			$scope.stateTransposeObj["responseState"]=response;
           
         TransposeService.setStateTransposeObj($scope.stateTransposeObj);
         $location.path("/device/"+$scope.deviceId+"/statetranspose");
   		})
       .catch(function (response) { ErrorService.handleHttpError(response); });
    }
   
   	// To delete State transpose
    $scope.deleteState = function(){
   	 
    	$scope.deviceId = $scope.device.id;
        $scope.transposeType = "state";
                          
        var modalInstance = $uibModal.open({
                    animation: false,
                    templateUrl: 'partials/deleteTranspose.html',
                    controller: 'DeleteTransposeController',
                    resolve:{
                    	deviceId: function() {
                            return $scope.deviceId;
                        },
                        transposeType : function(){
                            return $scope.transposeType;
                        }
                    }
                    })
                    modalInstance.result.then(
                    	function(){
                    		$scope.stateTransposeObj={
                    				responseState : null
                    		}
                    	    TransposeService.setStateTransposeObj($scope.stateTransposeObj);
                  			deviceDetails()
                  		  }
                    );

    }
    
    // Delete device Function
    $scope.deleteDevice = function(){
    	var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/deleteDevice.html',
            controller: 'DeleteDeviceController',
            size:'md',
            resolve: {
           	 deviceUID: function() {
                    return $scope.device.info.uid;
                },
                deviceId: function() {
                    return $routeParams.deviceId;
                }
            }
        })
            	
    }
    
    // Update device function
    $scope.saveDeviceSettings = function(form, device) {
      if (form.$invalid) {
        ToastrService.popupError('The form is invalid! Please make changes and try again.');
        return;
      }
      $scope.busy = true;
      SpinnerService.show();
      device.info.name = device.name;
          
      DeviceService.update(device)
        .then(function (response) {
          $scope.device = response.data;
          $scope.device.info = JSON.parse($scope.device.info);
          $scope.device.properties = JSON.parse($scope.device.properties);
          $scope.deviceProperties = Object.keys($scope.device.properties).sort(function(a, b) {
            return ('' + a.toLowerCase()) > ('' + b.toLowerCase()) ? 1 : -1;
          });

          form.$setPristine();
          form.$setUntouched();
          ToastrService.popupSuccess($scope.device.name + " has been successfully saved.");
        })
        .catch(function (response) {
        	if(response.data.message.indexOf("java.lang.Exception: ") != -1){
        		ToastrService.popupError(response.data.message.split("java.lang.Exception: ")[1]);	
        	} else if(response.data.message.indexOf("com.arrow.selene.SeleneException: ") != -1){
          		ToastrService.popupError(response.data.message.split("com.arrow.selene.SeleneException: ")[1]);	
            } else{
        		ToastrService.popupError(response.data.message);
        	}
        })
        .finally(function() {
          $scope.busy = false;
          SpinnerService.hide();
        });
    };

    // Save device details
    $scope.save = function (form, device) {
      if (form.$valid) {
        $scope.busy = true;
        SpinnerService.show();

        var savePromise = null;
        if ($scope.device.id && $scope.device.id != "") {
          savePromise = DeviceService.update(device);
        } else {
          savePromise = DeviceService.create(device);
        }

        if (!savePromise)
          return;

        savePromise
          .then(function (response) {
            $scope.device = response.data;

            // reset form
            form.$setPristine();
            form.$setUntouched();

            ToastrService.popupSuccess($scope.device.name + " has been successfully saved.");
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

    // Start device
    $scope.startDevice = function(deviceId) {
    	if(deviceId == 0){
    		ToastrService.popupError('Cannot start a gateway device!');
    	}
    	else{
            DeviceService.start(deviceId)
              .then(function () {
            	})
              .catch(function() {
                ToastrService.popupError('An error has occurred');
              });
            $scope.lastPing = moment(new Date()).format('MM/DD/YYYY hh:mm:ss A');	
    	}
    };

    // Stop device
    $scope.stopDevice = function(deviceId) {
    	if(deviceId == 0){
    		ToastrService.popupError('Cannot stop a gateway device!');
    	}
    	else{
            DeviceService.stop(deviceId)
              .then(function() {
            	
              })
              .catch(function () {
               ToastrService.popupError('An error has occurred');
              });
            $scope.lastPing = moment(new Date()).format('MM/DD/YYYY hh:mm:ss A');
    	}
    };

    
    $scope.$on("$destroy", function () {
      if (wsConnection) {
        wsConnection.disconnect();
      }
    });
    
    // Telemetry transpose format
    $scope.telemetryTransposeExample = {
     		    "f|temperature"    : "26.2",
     		    "b|switch"            : "true",
     		    "s|alarm"        : "Intruder Detected"
     }
   
     // Registration transpose format
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
     
    // State transpose format
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
     
     // Visible view button only when file is selected
     $scope.visible=false;
     // Open selected file
     $scope.openFile = function(event) {
 	    var input = event.target;
 	    $scope.visible=!$scope.visible;
 	    $scope.$apply();
 	    var reader = new FileReader();
 	    reader.onload = function(){
 	      var text = reader.result;
 	      $scope.fileContent =reader.result.substring(0, reader.result.length+1);
 	     
 	    };
 	    reader.readAsText(input.files[0]);
 	  }
     
     // view file content
     $scope.viewFile = function(){
    	 
    	 $uibModal.open({
             animation: false,
             templateUrl: 'partials/viewLookupFile.html',
             controller: 'ViewLookupFileController',
             size:'md',
             resolve: {
                 deviceUID: function(){
                     return  $scope.device.info.uid;
                 },
                 deviceId: function(){
                    return $routeParams.deviceId;
                 },
                 fileContent: function() {
                     return  $scope.fileContent;
                 }
             }
         })
     }
     
     // Import file
     $scope.importLookup = function(){
       DeviceService.importlookup($scope.fileContent,$routeParams.deviceId,$scope.device.info.uid)
       .then(function(response){
       })
       .catch(function (response) {
           ErrorService.handleHttpError(response);
       });
   }
  },
  ["$scope","$uibModal","$location", "$routeParams", "ErrorService", "SpinnerService", "DeviceService", "ToastrService", "TelemetryService", "TransposeService"]);