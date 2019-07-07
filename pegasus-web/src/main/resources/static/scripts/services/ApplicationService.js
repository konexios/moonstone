angular.module('pegasus').factory("ApplicationService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){
	
	function options(enabled) {
		return $http({
			"url": "/api/pegasus/applications/" + enabled + "/options",
			"method": "GET"
		});		
	}

	function subscriptions(companyId) {
		return $http({
			"url": "/api/pegasus/applications/" + companyId + "/subscriptions",
			"method": "GET"
		});		
	}
	
	function productExtensions(parentProductId) {
		return $http({
			"url": "/api/pegasus/applications/" + parentProductId + "/productExtensions",
			"method": "GET"
		});		
	}
	
	function productFeatures(productId) {
		return $http({
			"url": "/api/pegasus/applications/" + productId + "/productFeatures",
			"method": "GET"
		});		
	}
	
	function filters() {
		return $http({
			"url": "/api/pegasus/applications/filters",
			"method": "GET"
		});		
	}
	
	function find(searchFilter) {
		return $http({
			"url": "/api/pegasus/applications/find",
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": searchFilter
		});
	}
	
	function application(id) {
		return $http({
			"url": "/api/pegasus/applications/" + id + "/application", 
			"method": "GET"
		});
	}
	
	function create(application) {
		return $http({
			"url": "/api/pegasus/applications/create", 
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": application
		});
	}
	
	function update(application) {
		return $http({
			"url": "/api/pegasus/applications/update", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": application
		});
	}
	
	function createVaultLogin(applicationId, adminToken) {
		
		var applicationVaultLogin = {
			"applicationId": applicationId,
			"adminToken": adminToken
		};
		
		return $http({
			"url": "/api/pegasus/applications/create/vaultlogin", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": applicationVaultLogin
		});
	}
	
	return {
		options: options,
		subscriptions: subscriptions,
		productExtensions: productExtensions,
		productFeatures: productFeatures,
		filters: filters,
		find: find,
		get: application,
		create: create,
		update: update,
		createVaultLogin: createVaultLogin
	};
}]);