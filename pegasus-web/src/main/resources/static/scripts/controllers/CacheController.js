angular.module('pegasus').controller("CacheController",
	[
	 	"$rootScope", "$scope", "$location", "$state", "SecurityService", "ErrorService", "CacheService",
	 	function ($rootScope, $scope, $location, $state, SecurityService, ErrorService, CacheService) {
	 		$scope.busy = false;
	 		
	 		$scope.remoteCache = {};
	 		
	 		$scope.onCacheKeyClick = function(key) {
	 			console.log(key);
	 			/*
	 			var promise = CacheService.clearRemoteCache(key)
	 				.then(function(response) {
	 					console.log(response);
	 				})
	 				.catch(function(response) {
	 					$scope.busy = false;
		 				ErrorService.handleHttpError(response);
	 				});
	 			*/
	 		};
	 		
	 		/**
	 		 * Call the REST Service to retrieve the list of cached objects
	 		 * @return void
	 		 */
	 		$scope.loadRemoteCache = function() {
	 			$scope.busy = true;
	 			
	 			var promise = CacheService.remoteCache()
	 				.then(function(response) {
	 					console.log(response);
	 					$scope.remoteCache = response.data;
			 			$state.current.data.pageSubTitle = "as of " + DateUtil.formatDate(new Date());
			 			$scope.busy = false;
	 				})
	 				.catch(function(response) {
	 					$scope.busy = false;
		 				ErrorService.handleHttpError(response);
	 				});	 			
	 		};
	 		
	 		// initialize the UI by call the find method
	 		$scope.loadRemoteCache();
	 	}
	]
);