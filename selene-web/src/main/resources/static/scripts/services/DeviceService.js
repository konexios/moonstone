services.factory("DeviceService", ["$http", function ($http) {

  function all() {
    return $http.get("/api/selene/devices/all");
  }
  
  function device(deviceId) {
    return $http.get("/api/selene/devices/" + deviceId + "/device");
  }

  function update(device) {
	  return $http({
	      "url": "/api/selene/devices/" + device.id + "/updateDevice",
	      "method": "PUT",
	      "headers": {"Content-Type": "application/json"},
	      "data": {
	    	  id: device.id,
	          name: device.name,
	          type: device.type,
	          uid: device.uid,
	          properties: JSON.stringify(device.properties),
	          info: JSON.stringify(device.info)
	        }
	    });
  }

  function save(device) {
    return $http({
      "url": "/api/selene/devices/createDevice",
      "method": "POST",
      "headers": {"Content-Type" : "application/json"},
      "data": {
        name: device.name,
        type: device.type,
        uid: device.uid,
        properties: JSON.stringify(device.properties),
        info: JSON.stringify(device.info)
      }
    });
  }
  
  function saveTranspose(transpose){
      return $http({
          "url": "/api/selene/devices/"+transpose.deviceId+"/saveTranspose/"+transpose.transposeType,
          "method": "POST",
          "headers": {"Content-Type" : "application/json"},
          "data": JSON.stringify({
              deviceId: transpose.deviceId,
              transposeType: transpose.transposeType,
              transposeFunction: transpose.transposeFunction
          }) 
      });
 }
  
  function devices() {
    return $http.get("./devices.json");
  }

  function start(deviceId) {
    return $http.get("/api/selene/devices/" + deviceId + "/start");
  }

  function stop(deviceId) {
    return $http.get("/api/selene/devices/" + deviceId + "/stop");
  }

  function checkDeviceStatus(deviceId) {
    return $http.get("/api/selene/devices/" + deviceId + "/checkDeviceStatus");
  }
  
  function deletetransposefile(deviceId,transposeType){
	  return $http({
          "url": "/api/selene/devices/"+deviceId+"/deleteTranspose/"+transposeType,
          "method": "DELETE",
          "headers": {"Content-Type" : "application/json"}
         
        });
  }
  
  function edittransposefile(deviceId,transposeType){
	  return $http.get("/api/selene/devices/"+deviceId+"/getTranspose/"+ transposeType)
  }
  
  function testregteltranspose(input,textToSave){
	  return $http({
          "url": "/api/selene/devices/testRegTelTranspose",
          "method": "POST",
          "headers": {"Content-Type" : "application/json"},
          "data" :JSON.stringify({
        	  incomingPayload :input,
        	  transposeFunction : textToSave
         
          	})
	  })
  }
  function teststatetranspose(textToSave,deviceUid,deviceName,deviceStates){
	  return $http({
          "url": "/api/selene/devices/testStateTranspose",
          "method": "POST",
          "headers": {"Content-Type" : "application/json"},
          "data" :JSON.stringify({
        	  deviceUid :deviceUid,
        	  deviceName:deviceName,
        	  deviceStates:deviceStates,
        	  transposeFunction : textToSave
         
          	})
	  })
  }
  
  function deletedevice(deviceId,deviceUID){
	  return $http({
		"url": "/api/selene/devices/"+deviceId+"/deleteDevice",
		"method": "DELETE",
        "headers": {"Content-Type" : "application/json"},
        "data" :deviceUID
		})
  }
  
  function downloadLookup(cbk){
      return $http({
          "url": "/api/selene/centralknowledgebank/download",
          "method": "POST",
         "headers": {"Content-Type" : "application/json"},
         "data" :JSON.stringify(cbk)
      })
 }
  
 function importlookup(lookupContent,deviceId,deviceUID){
      return $http({
          "url": "/api/selene/devices/"+deviceId+"/performCommand",
          "method": "POST",
         "headers": {"Content-Type" : "application/json"},
         "data" :JSON.stringify({
             lookup:lookupContent,
             deviceUid: deviceUID
         })
         
      })
 }
 
 function scanMacaddress(interfaceName,discoveryTimeout){
	 return $http({
         "url": "/api/selene/devices/bleDiscovery",
         "method": "POST",
        "headers": {"Content-Type" : "application/json"},
        "data" :JSON.stringify({
            interfaceName:interfaceName,
            discoveryTimeout:discoveryTimeout
        })
	 });
 }   
  return {
    all: all,
    device: device,
    update: update,
    save: save,
    start: start,
    stop: stop,
    checkDeviceStatus: checkDeviceStatus,
    devices: devices,
    saveTranspose: saveTranspose,
    deletetransposefile : deletetransposefile,
    edittransposefile: edittransposefile,
    testregteltranspose:testregteltranspose,
    teststatetranspose : teststatetranspose,
    deletedevice : deletedevice,
    downloadLookup: downloadLookup,
    importlookup : importlookup,
    scanMacaddress: scanMacaddress
  };
}
]);
