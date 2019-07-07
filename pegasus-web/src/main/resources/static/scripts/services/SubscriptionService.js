angular.module('pegasus').factory("SubscriptionService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){
	
	function options(enabled) {
		return $http({
			"url": "/api/pegasus/subscriptions/" + enabled + "/options",
			"method": "GET"
		});		
	}
	
	function filters() {
		return $http({
			"url": "/api/pegasus/subscriptions/filters",
			"method": "GET"
		});		
	}
	
	function find(searchFilter) {
		return $http({
			"url": "/api/pegasus/subscriptions/find",
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": searchFilter
		});
	}

    function applications(id) {
        return $http({
            "url": "/api/pegasus/subscriptions/" + id + "/applications",
            "method": "GET"
        });
    }
	
	function subscription(id) {
		return $http({
			"url": "/api/pegasus/subscriptions/" + id + "/subscription", 
			"method": "GET"
		});
	}
	
	function create(subscription) {
		return $http({
			"url": "/api/pegasus/subscriptions/create", 
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": subscription
		});
	}
	
	function update(subscription) {
		return $http({
			"url": "/api/pegasus/subscriptions/update", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": subscription
		});
	}
	
	return {
		options: options,
		filters: filters,
		find: find,
        applications: applications,
		get: subscription,
		create: create,
		update: update
	};
}]);