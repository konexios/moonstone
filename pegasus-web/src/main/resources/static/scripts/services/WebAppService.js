angular.module('pegasus').factory("WebAppService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){
	
	function version() {
		return $http({
			"url": "/api/pegasus/webapp/version",
			"method": "GET"
		});		
	}
	
	function componentsVersion() {
		return $http({
			"url": "/api/pegasus/webapp/components/version",
			"method": "GET"
		});		
	}
	
	return {
		version: version,
		componentsVersion: componentsVersion
	};
}]);