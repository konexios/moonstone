angular.module('pegasus').factory("RoleService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){
	
	function options(enabled) {
		return $http({
			"url": "/api/pegasus/roles/" + enabled + "/options",
			"method": "GET"
		});		
	}
	
	function applicationOptions(productId) {
		return $http({
			"url": "/api/pegasus/roles/" + productId + "/applications",
			"method": "GET"
		});		
	}
	
	function privilegeOptions(productId) {
		return $http({
			"url": "/api/pegasus/roles/" + productId + "/privileges",
			"method": "GET"
		});		
	}

	function filters() {
		return $http({
			"url": "/api/pegasus/roles/filters",
			"method": "GET"
		});		
	}
	
	function find(searchFilter) {
		return $http({
			"url": "/api/pegasus/roles/find",
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": searchFilter
		});
	}
	
	function role(id) {
		return $http({
			"url": "/api/pegasus/roles/" + id + "/role", 
			"method": "GET"
		});
	}
	
	function create(role) {
		return $http({
			"url": "/api/pegasus/roles/create", 
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": role
		});
	}
	
	function update(role) {
		return $http({
			"url": "/api/pegasus/roles/update", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": role
		});
	}
	
	function clone(id) {
		return $http({
			"url": "/api/pegasus/roles/" + id + "/clone", 
			"method": "GET"
		});
	}
	
	function createClone(role) {
		return $http({
			"url": "/api/pegasus/roles/createClone", 
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": role
		});
	}
	
	return {
		options: options,
		applicationOptions: applicationOptions,
		privilegeOptions: privilegeOptions,
		filters: filters,
		find: find,
		get: role,
		create: create,
		update: update,
		clone: clone,
		createClone: createClone
	};
}]);