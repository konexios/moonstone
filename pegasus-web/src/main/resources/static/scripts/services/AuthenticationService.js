services.factory("AuthenticationService", ["$http", function($http){

	function login(credentials) {
		var serializeData = function ( data ) {
            if ( !angular.isObject( data ) ) {
                return( ( data == null ) ? "" : data.toString() );
            }

            var buffer = [];
            for ( var name in data ) {
                if ( !data.hasOwnProperty( name ) ) {
                    continue;
                }
                var value = data[ name ];
                buffer.push(
                    encodeURIComponent( name ) +
                    "=" +
                    encodeURIComponent( ( value == null ) ? "" : value )
                );
            }

            var source = buffer
                .join( "&" )
                .replace( /%20/g, "+" );
            return( source );
        };

		return $http({
			method: "post",
            url: "/api/v1/core/security/login",
            transformRequest: serializeData,
            data: {
                username: credentials.username,
                password: credentials.password
            },
            headers: {
            	"Content-type": "application/x-www-form-urlencoded; charset=utf-8"
            }
        });
	}

	function logout() {
		return $http.post("/logout", {});
	}
	
	function application(applicationId) {
		return $http.get("/api/v1/core/security/application/" + applicationId, {}); 
	}
	
	function user() {
		return $http.get("/api/pegasus/security/user", {});
	}
	
	return {
 		login: login,
 		logout: logout,
 		application: application,
 		user: user
	};
}]);