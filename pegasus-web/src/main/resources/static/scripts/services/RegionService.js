angular.module('pegasus').factory("RegionService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){
	
	function options(enabled) {
		return $http({
			"url": "/api/pegasus/regions/" + enabled + "/options",
			"method": "GET"
		});		
	}
	
	function find(searchFilter) {
		return $http({
			"url": "/api/pegasus/regions/find",
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": searchFilter
		});
	}
	
	function region(id) {
		return $http({
			"url": "/api/pegasus/regions/" + id + "/region", 
			"method": "GET"
		});
	}
	
	function create(region) {
		return $http({
			"url": "/api/pegasus/regions/create", 
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": region
		});
	}
	
	function update(region) {
		return $http({
			"url": "/api/pegasus/regions/update", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": region
		});
	}
	
	return {
		options: options,
		find: find,
		get: region,
		create: create,
		update: update
	};
}]);