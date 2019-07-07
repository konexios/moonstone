angular.module('pegasus').factory("CacheService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){
	
	function remoteCache() {
		return $http({
			"url": "/api/pegasus/cache/remote",
			"method": "GET"
		});		
	}
	

	function clearRemoteCache(key) {
		return $http({
			"url": "/api/pegasus/cache/remote/" + key + "/clear", 
			"method": "GET"
		});
	}
	
	return {
		remoteCache: remoteCache,
		clearRemoteCache: clearRemoteCache
	};
}]);