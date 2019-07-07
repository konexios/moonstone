services.factory("AwsService", [ "$http", function($http) {

		function aws() {
			return $http({
				"url": "/api/selene/gateways/aws", 
				"method": "GET"
			});
		}
	
		return {
			aws: aws
		};
    }
]);
