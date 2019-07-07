controllers.controller('DevicesController',
    [
        '$scope', '$location','$uibModal' , 'ErrorService', 'DeviceService', 'ToastrService','TransposeService','WebSocketsService','SpinnerService',
	    function ($scope, $location, $uibModal, ErrorService, DeviceService, ToastrService,TransposeService,WebSocketsService,SpinnerService) {
		    $scope.pageTitle = 'Devices';
			$scope.pageSubTitle = '';
	
			$scope.busy = true;
			$scope.devices = [];
			$scope.device_list = [];
			$scope.topic_list = [];
			
			// Get transpose object form Registration State and Telemetry
			$scope.telTransposeObj = TransposeService.getTelTransposeObj();
            $scope.stateTransposeObj = TransposeService.getStateTransposeObj();
            $scope.regTransposeObj = TransposeService.getRegTransposeObj();
            
            // Get device object
            $scope.device = TransposeService.get();
            
            $scope.showDate = function (timestamp) {
                return moment(timestamp).format('MM/DD/YYYY hh:mm:ss A');
              };
              
             
            // columns
             $scope.columnHeaders = [
                 {label: 'Device', value: 'name', sortable: false,style:"width:250px;cursor: pointer"},
                 {label: 'UID', value: 'uid', sortable: false,style:"width:250px;cursor: pointer"},
                 {label: 'Type', value: 'type', sortable: false,style:"width:180px;cursor: pointer"},
                 {label: 'Gateway', value: 'gateway', sortable: false,style:"width:150px;cursor: pointer"},
                 {label: 'Group', value: 'group', sortable: false,style:"width:100px;cursor: pointer"},
                 {label: 'Owner', value: 'owner', sortable: false,style:"width:100px;cursor: pointer"},
                 {label: 'Enabled', value: 'enabled', sortable: false,style:"width:80px;cursor: pointer"},
                 {label: 'Status', value: 'status', sortable: false,style:"width:80px;cursor: pointer"},
                 {label: 'Last Telemetry', value: 'timestamp', sortable: false,style:"width:200px;cursor: pointer"}
             ];

             var sortBy = '';

             $scope.currentPage = 0;
             $scope.pageSize = 10;
             $scope.pages = [1];
             $scope.firstPage = 1;
             $scope.lastPage = 1;

             $scope.changeCurrentPage = function(page) {
                 if (page < 0 || page > $scope.pages.length) return;
                 $scope.currentPage = page;
             };

             $scope.gotoPreviousPage = function() {
                 var currentPage = $scope.currentPage;
                 $scope.changeCurrentPage(currentPage - 1);
             };

             $scope.gotoNextPage = function() {
                 var currentPage = $scope.currentPage;
                 $scope.changeCurrentPage(currentPage + 1);
             };

             $scope.changePageSize = function(pageSize) {
                 $scope.pageSize = pageSize;
                 $scope.pages = [1];
                 for (var i = 2; i <= Math.ceil($scope.devices.length / pageSize); i++) {
                     $scope.pages.push(i);
                 }
                 $scope.lastPage = $scope.pages.length;
             };

             $scope.sortBy = function(columnName, e) {
                 var compareStrings = function(obj1, obj2) {
                     return ('' + obj1[columnName]).toLowerCase() > ('' + obj2[columnName]).toLowerCase() ? 1 : -1;
                 };
                 var needSort = false;
                 for (var i = 1; i < $scope.devices.length; i++) {
                     if ($scope.devices[i - 1][columnName] !== $scope.devices[i][columnName]) {
                         needSort = true;
                         break;
                     }
                 }
                 if (!needSort) return;
                 if (sortBy !== columnName) {
                     $scope.devices.sort(compareStrings);
                     $scope.sortAsc = columnName;
                     $scope.sortDesc = '';
                 } else {
                     $scope.devices.reverse();
                     var buf = $scope.sortAsc;
                     $scope.sortAsc = $scope.sortDesc;
                     $scope.sortDesc = buf;
                 }
                 sortBy = columnName;
             };
             
             var wsConnection = WebSocketsService.connect('/websocket', {
                 onConnect: function () {
                		 wsConnection.subscribe('/topic/telemetry', function (message) {
                        	 for(i=0;i<$scope.devices.length;i++){
                        		 if($scope.devices[i].hid == message.deviceHid){
                        			 var telemetryTimestamp = Number(message.timestamp);
                               	  	var telemetryTimestamp =$scope.showDate(telemetryTimestamp)
                        			 $scope.devices[i].timestamp = telemetryTimestamp;
                        		 }
                        	 }
                        	 
                        	 $scope.$apply();
                    	 });	
                		 
                    	wsConnection.subscribe('/topic/device/create', function (message) {
                        	 SpinnerService.hide();
                           	if(message.status == "OK") {
                           		ToastrService.popupSuccess('Device has been successfully saved.');
                                $scope.busy = false;
                           	}else{
                           		ToastrService.popupError(message.message);
                           	}
                            $location.path('/devices');
                           	$scope.$apply();
             		    });

             	 		wsConnection.subscribe('/topic/device/bleDiscovery', function (message) {

             	 			SpinnerService.hide();
 	                       	
 	                       	if(message.status == "OK") {
	 	                       	$scope.data = JSON.parse(message.message);
	 	                       	var modalInstance = $uibModal.open({
	 	                            animation: false,
	 	                            templateUrl: 'partials/scanForMacAddress.html',
	 	                            controller: 'ScanMacAddressController',
	 	                            resolve:{
	 	                            	macAddress: function() {
	 	                                    return $scope.data;
	 	                                }
	 	                            }
 	                            })
 	                            modalInstance.result.then(
 	                          		  function(macId){
 	                          			// Attach value to bleAddress textbox
 	                          			// $scope.macId = macId;
 	                          			$('#bleAddress').val(macId);
 	                                   var uid =$("#bleAddress").val();
 	                                   
 	                                   for(i=0;i<uid.length;i++){
 	                                       uid= uid.replace(':','')
 	                                   }
 	                                  
 	                                  $scope.device.properties['bleAddress']=$("#bleAddress").val(); 
 	                                   $scope.device.info.uid = uid.toLowerCase();	 	                          			
 	                          		  }
 	                            );
 	                       	}else{
								// ErrorService.handleHttpError(message.message);
								// console.log(message.message);
 	                       		ToastrService.popupError(message.message);
 	                       	}
 	                       	$scope.$apply();
 	                   	 });
                 },
                 onDisconnect: function () {
            	 	// console.log("On DisConnect()");
                 }
               });
              
             // Save newly created device details
             $scope.save = function(form, formData) {
            	 // formData.properties['bleAddress']=$scope.macId;
            	 
                 if (form.$valid) {
                     $scope.busy = true;
                     var data = transformDeviceType(formData.info.type);
                     if (formData.properties === undefined) {
                         formData.properties = {};
                     }
                     formData.type = data.type;
                     
                     data.props.forEach(function (prop) {
                         if (prop.value !== '' && !(prop.name in formData.properties)) {
                             formData.properties[prop.name] = prop.value;
                             
                         }
                     });
                     defaultProps.forEach(function (prop) {
                         if (!formData.properties[prop.name]) {
                             formData.properties[prop.name] = prop.value;
                         }
                     });
                     
                     if(formData.info.type == 'mqtt-router'){
                         $scope.list =[];
                    	 $scope.device_list.forEach(function (value,key) {
                    		 $scope.list.push(value.title);
                         });
                     }
                     if(formData.info.type == 'mqtt-router'){
                    	 $scope.topicList =[];
                    	 $scope.topic_list.forEach(function (value,key) {
                    		 $scope.topicList.push(value.title);
                         });
                     }
                    
                     formData.info.deviceClass = data.deviceClass;
                     formData.name = formData.info.name;
                     formData.info.type = formData.info.type;
                     formData.uid = formData.info.uid;
                     if(formData.info.type == 'mqtt-router'){
                    	 formData.properties['deviceRegistrationOverMqtt']= formData.deviceRegistrationOverMqtt;
                         formData.properties['deviceRegistrationTopic']= formData.deviceRegistrationTopic;
                         formData.properties['mqttBrokerCertified']= formData.mqttBrokerCertified;
                         formData.properties['caCertPath']= formData.caCertPath;
                         formData.properties['clientCertPath']= formData.clientCertPath;
                         formData.properties['privateKeyPath']= formData.privateKeyPath;
                    	 formData.properties['devices'] = JSON.stringify($scope.list);
                         formData.properties['telemetryTopics'] =$scope.topicList.join();
                     }
                     
                     TransposeService.set(formData);
                     
                     DeviceService.save(formData)
                         .then(function(response) {
                        	 SpinnerService.show();	 
                         })
                         .catch(function (response) {
                             $scope.busy = false;
                             ErrorService.handleHttpError(response);
                         });
                 } else {
                     ToastrService.popupError("The form is invalid! Please make changes and try again.");
                     $scope.success = false;
                 }
             };
             
             // Scan for MAC address
             $scope.scanForMacAddress = function(form, formData) {
            	 $scope.interfaceName = formData.properties.bleInterface;
            	 $scope.discoveryTimeout = formData.properties.bleInterval;

            	 if($scope.interfaceName == '' || $scope.interfaceName == undefined){
            		 alert("To scan BLE interface and BLE Scan interval is required.")
            	 }else{
            		 DeviceService.scanMacaddress($scope.interfaceName,$scope.discoveryTimeout)
	             	 	.then(function(response) {
	             	 		 SpinnerService.show();	 		
             	 	}).catch(function(response) {
         	           ErrorService.handleHttpError(response);
         	           SpinnerService.hide();
             	 	});
            	 }
             }

             // back to list of devices page
             $scope.backAddingForm = function() {
            	 $location.path('/devices');	 
             };
             
             // Add comma separated topics
             $scope.addTopicList = function(form,formData) {
                 if(formData.newTopicList=="" || formData.newTopicList == undefined){
                     return;
                 }else{
                     $scope.topic_list.push({'title': formData.newTopicList, 'done':false})    
                 }
                  formData.newTopicList = ''
              }
             
             // Delete topics from list
              $scope.deleteTopicList = function(index) {    
                  $scope.topic_list.splice(index, 1);
                  
              }
              
              // Add in to devices list
              $scope.addDeviceList = function(form,formData) {
                  if(formData.newDeviceList=="" || formData.newDeviceList == undefined){
                    return;
                }else{
                   $scope.device_list.push({'title': formData.newDeviceList, 'done':false})
                }
                  formData.newDeviceList = ''
              }
            
            // Delete devices list
      		$scope.deleteDeviceList = function(index) {	
      			$scope.device_list.splice(index, 1);
      			
      		}

             $scope.onChangeSelectedDevice = function (type) {
                 for (var i = 0; i < deviceProps.length; i++) {
                     if (deviceProps[i].type === type) {
                         $scope.deviceProps = deviceProps[i].props.concat(defaultProps);
                         $scope.deviceInfo = deviceProps[i].info;
                         
                         return deviceProps[i].props;
                     }
                 }
                 return {};
             };
             
             $scope.isActive = function(route) {
                 return route === $location.path();
             }
             
             // Create new device
             $scope.adddev = function (){
            	 
            	 $scope.device = null;
            	 $scope.telTransposeObj = null;
                 $scope.stateTransposeObj = null;
                 $scope.regTransposeObj = null;
                 TransposeService.set($scope.device)
                 TransposeService.setRegTransposeObj($scope.regTransposeObj);
                 TransposeService.setTelTransposeObj($scope.telTransposeObj);
                 TransposeService.setStateTransposeObj($scope.stateTransposeObj);
                 $location.path('/createdevice');
             }
                          
             function loadDevices() {
                 DeviceService.all()
                     .then(function(response) {
                         $scope.devices = response.data;
                         // Get timestamp and convert it into date format
                         for(var i =0 ;i<$scope.devices.length;i++){
                        	 if($scope.devices[i].timestamp == 0){
                        		 $scope.devices[i].timestamp =  "NA";
                        	 }else{
                        		 var telemetryTimestamp = Number($scope.devices[i].timestamp);
                         	  	var telemetryTimestamp =$scope.showDate(telemetryTimestamp)
                  			 $scope.devices[i].timestamp= telemetryTimestamp;	 
                        	 }
                         }
                         
                         $scope.lastPage = Math.ceil(response.data.length / $scope.pageSize);
                         $scope.busy = false;
                     })
                     .catch(function(response) {
                         $scope.busy = false;
                         ErrorService.handleHttpError(response);
                     });
             }
               
             var deviceProps;
             DeviceService.devices()
                 .then(function (response) {
                     deviceProps = response.data;
                     $scope.deviceDetails = deviceProps;
                     if($scope.device!=null || $scope.device!=undefined){
                    	
                    	 $scope.onChangeSelectedDevice($scope.device.info.type)
                     }
                 })
                 .catch(function(response) { console.error(response); });

             var defaultProps = [
                 {
                     name: 'enabled',
                     title: 'Enabled',
                     desc: 'Enables or disables devices. Disabled devices will not be able to start',
                     value: true,
                     input: {type:"checkbox"}
                 },
                 {
                     name: 'persistTelemetry',
                     title: 'Persist telemetry',
                     desc: 'Enables or disables persisting telemetry to local database',
                     value: true,
                     input: {type:"checkbox"}
                 },
                 {
                     name: 'numThreads',
                     title: 'Count of threads',
                     desc: 'Defines size of thread pool executors. (Not used currently)',
                     value: 1,
                     input: {type:"number", min:1, max:2147483647 }
                 },
                 {
                     name: 'maxPollingIntervalMs',
                     title: 'Max polling interval',
                     desc: 'Defines interval between attempts of polling telemetry from devices',
                     value: 1000,
                     input: {type:"number", min:1, max:9223372036854775807  }
                 },
                 {
                     name: 'publishLocal',
                     title: 'Publish local',
                     desc: 'Enables or disables sending telemetry via local UDP forwarder',
                     value: false,
                     input: {type:"checkbox"}
                 },
                 {
                     name: 'externalPropertyFile',
                     title: 'External property file',
                     desc: 'Defines path to external file containing additional device properties',
                     value: '',
                     input: {type:"text"}
                 },
                 {
                     name: 'externalIniFile',
                     title: 'External ini file',
                     desc: 'Defines path to external sectioned .ini file containing additional device properties. Makes sense only in in case if externalPropertyFile is defined',
                     value: '',
                     input: {type:"text"}
                 },
                 {
                     name: 'externalIniFileSection',
                     title: 'External ini file section',
                     desc: 'Defines name of section of .ini file to be read in case if externalIniFile is defined',
                     value: '',
                     input: {type:"text"}
                 },
                 {
                     name: 'dataParsingScriptFilename',
                     title: 'Data parsing script',
                     desc: 'Defines path to external file containing JS code that should parse received data and extract telemetry form it',
                     value: '',
                     input: {type:"text"}
                 }
             ];
             
             function transformDeviceType(type) {
                 for (var i = 0; i < deviceProps.length; i++) {
                     if (deviceProps[i].type === type) {
                         return deviceProps[i];
                     }
                 }
                 return { device: type, type: '', props: {}, info: {} };
             }

             loadDevices();
             
             $scope.$on("$destroy", function () {
                 if (wsConnection) {
                     wsConnection.disconnect();
                   }
             });
        }
    ]
);