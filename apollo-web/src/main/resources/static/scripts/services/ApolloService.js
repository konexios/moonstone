services.factory('ApolloService', ['$http',
    function($http) {

        function version() {
            return $http.get('/api/apollo/webapp/version');
        }

    	function componentsVersion() {
    		return $http({
    			"url": "/api/apollo/webapp/components/version",
    			"method": "GET"
    		});		
    	}
        
        return {
            version: version,
    		componentsVersion: componentsVersion
        };
    }
]);