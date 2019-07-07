services.factory('SecurityService', 
    [ '$cookies', 
    function($cookies) {
        var GATEWAY_ME = 'gatewayme';
        
        function isLoggedIn() {
            return getGatewayMe() != null;
        }
        
        function getGatewayMe() {
            return $cookies.get(GATEWAY_ME) || null;
        }
        
        function saveSession(principal) {
        	deleteSession();

    		if (principal != null && principal != undefined && principal != "")
                $cookies.put(GATEWAY_ME, principal);
        }
    
        function deleteSession() {
            $cookies.remove(GATEWAY_ME);
        }
        
        return {
            isLoggedIn: isLoggedIn,
            saveSession: saveSession,
            deleteSession: deleteSession,
            getGatewayMe: getGatewayMe
        };
    }
]);
