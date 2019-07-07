services.factory('IbmService', ['$http', function ($http) {
	function ibm() {
		return $http({
			'url': '/api/selene/gateways/ibm',
			'method': 'GET'
		});
	}

	return { ibm: ibm };
}]);