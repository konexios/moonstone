angular.module('pegasus').factory("ApplicationEngineService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){
	
//	function options(enabled) {
//		return $http({
//			"url": "/api/pegasus/engines/" + enabled + "/options",
//			"method": "GET"
//		});		
//	}
	
	function optionsByProductAndZone(productId, zoneId) {
		return $http({
			"url": "/api/pegasus/engines/" + productId + "/" + zoneId + "/options",
			"method": "GET"
		});		
	}
	
//	function find(searchFilter) {
//		return $http({
//			"url": "/api/pegasus/engines/find",
//			"method": "POST",
//			"headers": {"Content-Type" : "application/json"},
//			"data": searchFilter
//		});
//	}
//	
//	function zone(id) {
//		return $http({
//			"url": "/api/pegasus/engines/" + id + "/zone", 
//			"method": "GET"
//		});
//	}
//	
//	function create(zone) {
//		return $http({
//			"url": "/api/pegasus/engines/create", 
//			"method": "POST",
//			"headers": {"Content-Type" : "application/json"},
//			"data": zone
//		});
//	}
//	
//	function update(zone) {
//		return $http({
//			"url": "/api/pegasus/engines/update", 
//			"method": "PUT",
//			"headers": {"Content-Type" : "application/json"},
//			"data": zone
//		});
//	}
	
	return {
		optionsByProductAndZone: optionsByProductAndZone
//		options: options,
//		find: find,
//		get: zone,
//		create: create,
//		update: update
	};
}]);