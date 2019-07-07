services.factory("GatewayService", [ "$http", function($http) {

		function gateway() {
			return $http({
				"url": "/api/selene/gateways/gateway",
				"method": "GET"
			});
		}
		
		function iotconnect() {
			return $http({
				"url": "/api/selene/gateways/iotconnect",
				"method": "GET"
			})
		}

		function loadGatewayProperties(){
			return $http({
				"url": "/api/selene/gateways/loadgatewayproperties",
				"method": "GET"
			});
		}

		function update(gateway) {
			return $http({
				"url": "/api/selene/gateways/update", 
				"method": "PUT",
				"headers": {"Content-Type" : "application/json"},
				"data": gateway
			});
		}
		
		function create(gateway1){
			return $http({
			      "url": "/api/selene/gateways/creategateway",
			      "method": "POST",
			      "headers": {"Content-Type" : "application/json"},
			      "data": JSON.stringify({
			    	deviceClass : "com.arrow.selene.device.self.SelfModule",
			        name: gateway1.name,
			        uid: gateway1.uid,
			        enabled: true,
			        cloudTransferMode : gateway1.cloudTransferMode,
			        apiKey: gateway1.apiKey,
			        secretKey: gateway1.secretKey,
			        heartBeatIntervalMs: gateway1.heartBeatIntervalMs,
			        iotConnectUrl: gateway1.iotConnectUrl,
			        iotConnectMqtt: gateway1.iotConnectMqtt,
			        iotConnectMqttVHost : gateway1.iotConnectMqttVHost
			      })
			    });
			  }
		
		function start() {
		    return $http.get("/api/selene/gateways/start");
		 }

		function checkgatewaystatus(){
            return $http.get("/api/selene/gateways/checkGatewayStatus");
        } 
		
		function createselene(selene){
			return $http({
			      "url": "/api/selene/gateways/createseleneproperties",
			      "method": "POST",
			      "headers": {"Content-Type" : "application/json"},
			      "data": selene
			    });
		}
		
		function getselene(){
			return $http.get("/api/selene/gateways/getseleneproperties");
		}
		
		function loadselene(path){
			return $http({
			      "url": "/api/selene/gateways/loadpersistentseleneproperties",
			      "method": "POST",
			      "headers": {"Content-Type" : "application/json"},
			      "data": {
			    	  homeDirectory: path
			      }
			    });
		}
		
		return {
			gateway: gateway,
			update: update,
			create: create,
			start: start,
			checkgatewaystatus: checkgatewaystatus,
		    	getselene:getselene,
		    	createselene:createselene,
		    	loadselene:loadselene,
		    	iotconnect: iotconnect,
		    	loadGatewayProperties:loadGatewayProperties
		};
    }
]);
