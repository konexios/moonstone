controllers.controller('LoginController', 
    [ '$rootScope', '$scope', '$q', '$location', '$routeParams', 'SecurityService', 'AuthenticationService', 'ErrorService', 
    function($rootScope, $scope, $q, $location, $routeParams, SecurityService, AuthenticationService, ErrorService) {
        $scope.credentials = {};
        
        $scope.signingMessage = '';
        
        function handleSuccess(response) {
            var user = response.data;
            if (user) {
                $rootScope.principal = user;
                SecurityService.saveSession($rootScope.principal.username);
                var redirect = $routeParams.redirect;
                if (redirect && redirect.indexOf('/signin') != 0) {
                    $location.url(redirect);
                } else {
                    $location.path('/home');
                }
            } else {
                return $q.reject('Your login and/or password was either misspelled or incorrect.');
            }
        }
        
        function handleFailure() {
            $rootScope.principal = null;
            SecurityService.deleteSession();
            $scope.loginBtnDisabled = false;
        }
        
        $scope.login = function() {
            $scope.loginBtnDisabled = true;
            $scope.signingMessage = '';
            AuthenticationService.login($scope.credentials)
	            .then(handleSuccess)
	            .catch(function(response) {
	                handleFailure();
	                if (typeof response == 'string') {
	                    $scope.signingMessage = response;
	                } else if (response.status === 401) {
	                    $scope.signingMessage = 'Either your username and/or password was either misspelled or incorrect.';
	                } else {
	                    ErrorService.handleHttpError(response);
	                }
	            });
        };
        
        /*
        var isLoggedIn = SecurityService.isLoggedIn();
        if (isLoggedIn) {
            $scope.loginBtnDisabled = true;
            AuthenticationService.user()
	            .then(handleSuccess)
	            .catch(handleFailure);
        } else {
            $scope.loginBtnDisabled = false;
        }
        */
    } 
]);
