services.factory('AuthenticationService', 
    [ '$http', 
    function($http) {

        function serializeData(data) {
            // If this is not an object, defer to native stringification.
            if (!angular.isObject(data)) {
                return ((data == null) ? "" : data.toString());
            }
            var buffer = [];
            // Serialize each key in the object.
            for ( var name in data) {
                if (!data.hasOwnProperty(name)) {
                    continue;
                }
                var value = data[name];
                buffer.push(encodeURIComponent(name) + "=" + encodeURIComponent((value == null) ? "" : value));
            }
            // Serialize the buffer and clean it up for transportation.
            var source = buffer.join("&").replace(/%20/g, "+");
            return (source);
        }
        
        function login(credentials) {
            return $http({
                method : "post",
                url : "/api/selene/security/login",
                transformRequest : serializeData,
                data : {
                    username : credentials.username,
                    password : credentials.password
                },
                headers : {
                    "Content-type" : "application/x-www-form-urlencoded; charset=utf-8"
                }
            });
        }
        
        function logout() {
            return $http.post('/logout', {});
        }
        
        function user() {
            return $http.get('/api/selene/security/user', {});
        }
        
        return {
            login: login,
            logout: logout,
            user: user
        };
    }
]);
