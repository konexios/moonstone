controllers.controller('ForgotPasswordController',
    [ '$rootScope', '$scope', '$q', '$location', '$routeParams', 'SecurityService', 'AuthenticationService', 'UserService', 'ErrorService',
    function ($rootScope, $scope, $q, $location, $routeParams, SecurityService, AuthenticationService, UserService, ErrorService) {
        $scope.credentials = {
            username : SecurityService.getUserLogin() || '',
            password : ''
        }

        $scope.signingMessage = '';
        if ($routeParams.reason === '401') {
            $scope.signingMessage = 'Your session has expired, please login again.';
        }

        function handleSuccess (response) {
            return AuthenticationService.selectUserApplication(response.data)
            .then (function () {
                var redirect = $routeParams.redirect;
                if (redirect && redirect.indexOf('/signin') !== 0) {
                    $location.url(redirect);
                } else {
                    $location.url('/home');
                }
            });
        }

        function handleFailure () {
            $rootScope.user = null;
            $rootScope.app = null;
            SecurityService.deleteSession();
            $scope.loginBtnDisabled = false;
        }

        $scope.login = function () {
            //$scope.loginBtnDisabled = true;
            if ($scope.credentials.username == '' || $scope.credentials.password == ''){
            		$scope.signingMessage = 'Please enter username and password to login'
            }
            else {
            	//$scope.loginBtnDisabled = false;
            $scope.signingMessage = '';
            AuthenticationService.login($scope.credentials)
            .then(handleSuccess)
            .catch (function (response) {
                handleFailure();
                if (typeof response === 'string') {
                    $scope.signingMessage = response;
                } else if (response.status === 500 && response.data && response.data.exception) {
                    var exception = response.data.exception.substring(response.data.exception.lastIndexOf('.')+1);
                    switch(exception) {
                        case 'UnverifiedAccountException':
                            $scope.signingMessage = 'Your account has not been verified.';
                            break;
                        case 'RequiredChangePasswordException':
                            SecurityService.saveSession($scope.credentials.username, null);
                            $location.url('/changepassword?reason=passwordreset');
                            break;
                        case 'LockedAccountException':
                            $scope.signingMessage = 'Your account is currently locked. Please try again later.';
                            break;
                        case 'InActiveAccountException':
                            $scope.signingMessage = 'Your account is inactive, please contact your system administrator.';
                            break;
                        case 'InvalidLoginException':
                            $scope.signingMessage = 'Either your username and/or password was either misspelled or incorrect.';
                            break;
                        default:
                            $scope.signingMessage = 'Unexpected error: '+response.data.exception;
                            break;
                    }
                } else {
                    ErrorService.handleHttpError(response);
                }
            });
           }
        }
        
        $scope.validUserMessage = '';
    	$scope.forgotpasswddiv = true;
    	$scope.emailSucccessdiv = false;
    	$scope.emailFailurediv = false;
    	$scope.authType = false;
    		$scope.getUserAutheticationType = function (username) {
            
            	console.log(username, "User name enterred");
                AuthenticationService.signInUserAuthType(username)
//            	AuthenticationService.user()
                
                .then(function(response) {
                		$scope.authType = (response.data.autheticationType == "NATIVE" ? false : true);
                		//$scope.authType = true;
                		if ($scope.authType) {
                        UserService.forgotPassword(username)
                            .then(function(response) {
	                            	$scope.forgotpasswddiv = false;
	        						$scope.emailSucccessdiv = true;
	        						$scope.emailFailurediv = false;
	        						$scope.validUserMessage = '';
                            })
                            .catch(function(response) {
	                            	$scope.forgotpasswddiv = false;
	        						$scope.emailSucccessdiv = false;
	        					    $scope.emailFailurediv = true;
	        					    $scope.errorMessage = true;
	                             $scope.validUserMessage = response;
                            })
                    }else {
	                    	$scope.forgotpasswddiv = true;
	    					$scope.emailSucccessdiv = false;
	    				    $scope.emailFailurediv = false;
	    				    $scope.errorMessage = true;
	                    $scope.validUserMessage = 'User Name is invalid. Please try again.';
                    }
                }, function(response) {
                		$scope.forgotpasswddiv = true;
					$scope.emailSucccessdiv = false;
				    $scope.emailFailurediv = false;
				    $scope.errorMessage = true;
                    $scope.validUserMessage = 'User Name is invalid. Please try again.';
                })
    		}
        $scope.forgotPassword = function () {
            if ($scope.credentials.username) {
                SecurityService.saveSession($scope.credentials.username, null);
            }
            $location.url('/forgotpassword');
        }
        var isLoggedIn = SecurityService.isLoggedIn();
        if (isLoggedIn) {
            $scope.loginBtnDisabled = true;
            AuthenticationService.user()
            .then(handleSuccess)
            .catch (handleFailure);
        } else {
            $scope.loginBtnDisabled = false;
            // get application hid, branding of login screen, handle SSO if
            // configured, etc.
        }
    }
]);
