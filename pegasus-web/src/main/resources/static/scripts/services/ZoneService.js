angular.module('pegasus').factory("ZoneService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){
	
	function options(enabled) {
		return $http({
			"url": "/api/pegasus/zones/" + enabled + "/options",
			"method": "GET"
		});		
	}
	
	function optionsByRegion(regionId, enabled) {
		return $http({
			"url": "/api/pegasus/zones/" + regionId + "/" + enabled + "/options",
			"method": "GET"
		});		
	}
	
	function find(searchFilter) {
		return $http({
			"url": "/api/pegasus/zones/find",
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": searchFilter
		});
	}
	
	function zone(id) {
		return $http({
			"url": "/api/pegasus/zones/" + id + "/zone", 
			"method": "GET"
		});
	}
	
	function create(zone) {
		return $http({
			"url": "/api/pegasus/zones/create", 
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": zone
		});
	}
	
	function update(zone) {
		return $http({
			"url": "/api/pegasus/zones/update", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": zone
		});
	}
	
	return {
		options: options,
		optionsByRegion: optionsByRegion,
		find: find,
		get: zone,
		create: create,
		update: update
	};
}]);