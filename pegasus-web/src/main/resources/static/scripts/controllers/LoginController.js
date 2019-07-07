angular.module('pegasus').controller("LoginController",
	[
	 	"$rootScope", "$scope", "$q", "$state", "$stateParams", "$location", "$cookies", "SecurityService", "AuthenticationService", "ErrorService",
	 	function ($rootScope, $scope, $q, $state, $stateParams, $location, $cookies, SecurityService, AuthenticationService, ErrorService) {
	 		$rootScope.user = null;
	 		$scope.credentials = {username: "", password: ""};
	 		$scope.busy = false;

	 		var alertMessages = {
                authFail: "Your login and/or password was either misspelled or incorrect.",
                sessionExpired: "You're session has expired, please sign in."
			};

            $scope.signinMessage = getAuthFailStatus();


	 		function successHandler(response) {
	            var user = response.data;
	            if (user && user.login) {
	                if (user.applications.length >= 1) {
	                    var app = user.applications[0];                             
	                    return AuthenticationService.application(app.id)
	                    	.then(
	                    		function(response){

	                    			$rootScope.user = user;
			                        $rootScope.app = app;
			                        SecurityService.saveSession(user.login, app.hid);
			                        
			                        var redirect = $stateParams.redirect;
			                        if (redirect && redirect.indexOf("/signin") != 0) {
                                        $location.url(redirect);
                                    } else {
                                        $location.url($location.path()); //reset query string
                                        $location.path("/dashboard");
                                    }
			                    }
	                    	);
	                } else {
	                    return $q.reject("You do not have permission to access this application.");
	                }
	            } else {
	                return $q.reject("Your login and/or password was either misspelled or incorrect.");
	            }
	 		}
	 		
	 		function failureHandler() {
	            $rootScope.user = null;
	            $rootScope.app = null;
				SecurityService.deleteSession();
				//$rootScope.clearSearchSMFilters();
	 		}
	 		
	 		$scope.login = function() {
 				$scope.signinMessage = null;
                $scope.busy = true;

 				AuthenticationService.login($scope.credentials)
 					.then(successHandler)
 					.catch(
	 					function(response) {
                            failureHandler();
	 						if (typeof response === "string") {
	 		                    $scope.signinMessage = response;
	 		                } else if (response.status === 401) {
                                $scope.signinMessage = alertMessages.authFail;
                            } else if(response.data.indexOf("Password must be changed!") !== -1) {
                                $state.go("changepassword", {"login": $scope.credentials.username});
	 		                } else {
	 		                    ErrorService.handleHttpError(response);
	 		                }
	 					}
 					)
					.finally(function() {
                        $scope.busy = false;
                    });
	 		};
	 		
	 		if (SecurityService.isLoggedIn())
	            AuthenticationService.user()
	            	.then(successHandler)
	            	.catch(failureHandler);

	 		function getAuthFailStatus() {
                if ($location.search().status === "fail-credential") {
                     return alertMessages.authFail;
                }

                if ($location.search().status === "session-expired") {
                    return alertMessages.sessionExpired;
                }

                return null;
			}
	 	}
	]
);