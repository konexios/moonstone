angular.module('pegasus').factory("ProductService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){
	
	function options(enabled) {
		return $http({
			"url": "/api/pegasus/products/" + enabled + "/options",
			"method": "GET"
		});		
	}
	
	function find(searchFilter) {
		return $http({
			"url": "/api/pegasus/products/find",
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": searchFilter
		});
	}
	
	function product(id) {
		return $http({
			"url": "/api/pegasus/products/" + id + "/product", 
			"method": "GET"
		});
	}
	
	function create(product) {
		return $http({
			"url": "/api/pegasus/products/create", 
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": product
		});
	}
	
	function update(product) {
		return $http({
			"url": "/api/pegasus/products/update", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": product
		});
	}
	
	return {
		options: options,
		find: find,
		get: product,
		create: create,
		update: update
	};
}]);