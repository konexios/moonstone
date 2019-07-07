services.factory('AzureService', [ '$http', function ($http) {
	function azure() {
		return $http({
			"url": "/api/selene/gateways/azure",
			"method": "GET"
		});
	}

	return { azure: azure };
}]);