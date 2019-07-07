services.factory('KioskService', ['$http',
    function($http) {

		function signup(data) {
		    return $http.post('/api/kronos/kiosk/signup', data);
		}
		
		function resend(data) {
		    return $http.post('/api/kronos/kiosk/resend', data);
		}
	
        return {
        	signup: signup,
        	resend: resend
        };
    }
]);