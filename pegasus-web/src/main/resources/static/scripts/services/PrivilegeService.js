angular.module('pegasus')
	.factory("PrivilegeService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){
	
		function options(enabled) {
			return $http({
				"url": "/api/pegasus/privileges/" + enabled + "/options",
				"method": "GET"
			});		
		}
		
		function rolePrivilegeOptions(enabled, applicationId) {
			return $http({
				"url": "/api/pegasus/privileges/" + enabled + "/" + applicationId + "/options",
				"method": "GET"
			});		
		}
		
		function filters() {
			return $http({
				"url": "/api/pegasus/privileges/filters",
				"method": "GET"
			});		
		}
		
		function find(searchFilter) {
			return $http({
				"url": "/api/pegasus/privileges/find",
				"method": "POST",
				"headers": {"Content-Type" : "application/json"},
				"data": searchFilter
			});
		}
		
		function privilege(id) {
			return $http({
				"url": "/api/pegasus/privileges/" + id + "/privilege", 
				"method": "GET"
			});
		}
		
		function create(privilege) {
			return $http({
				"url": "/api/pegasus/privileges/create", 
				"method": "POST",
				"headers": {"Content-Type" : "application/json"},
				"data": privilege
			});
		}
		
		function update(privilege) {
			return $http({
				"url": "/api/pegasus/privileges/update", 
				"method": "PUT",
				"headers": {"Content-Type" : "application/json"},
				"data": privilege
			});
		}
		
		return {
			options: options,
			rolePrivilegeOptions: rolePrivilegeOptions,
			filters: filters,
			find: find,
			get: privilege,
			create: create,
			update: update
		};
	}]);