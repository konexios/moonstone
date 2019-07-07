controllers.controller('LoginController',
    [ '$rootScope', '$scope', '$q', '$location', '$routeParams', 'SecurityService', 'AuthenticationService', 'ErrorService',
    function ($rootScope, $scope, $q, $location, $routeParams, SecurityService, AuthenticationService, ErrorService) {
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

        $scope.login = function() {
            $scope.loginBtnDisabled = true;
            $scope.signingMessage = '';
            AuthenticationService.login($scope.credentials)
                .then(handleSuccess)
                .catch(function(response) {
                    handleFailure();
                    if (typeof response == 'string') {
                        $scope.signingMessage = response;
                    } else if (response.status === 500 && response.data && response.data.exception) {
                        var exception = response.data.exception.substring(response.data.exception.lastIndexOf('.') + 1);
                        switch (exception) {
                            case 'UnverifiedAccountException':
                                $scope.signingMessage = 'Your account has not been verified.';
                                break;
                            case 'RequiredChangePasswordException':
                            	console.log("tessssssssss");
                                SecurityService.saveSession($scope.credentials.username, null);
                                $location.url('/changepassword?reason=passwordreset&login='+$scope.credentials.username);
                               // $location.url("/changepassword?reason=passwordreset", { "login": $scope.credentials.username });
                                break;
                            case 'LockedAccountException':
                                $scope.signingMessage = 'Your account is currently locked. Please try again later.';
                                break;
                            case 'InActiveAccountException':
                                $scope.signingMessage = 'Your account is inactive, please contact your system administrator.';
                                break;
                            case 'InvalidLoginException':
                                $scope.signingMessage = 'Wrong username or password has been entered.';
                                break;
                            default:
                                $scope.signingMessage = 'Unexpected error: ' + response.data.exception;
                                break;
                        }
                    } else {
                        ErrorService.handleHttpError(response);
                    }
                });
        };
        
        $scope.getUserAutheticationType = function (username) {
            if (username != '') {
                AuthenticationService.signInUserAuthType(username)
                .then (function (response) {
                			$scope.authType = (response.data.autheticationType == "NATIVE" ? true : false);
                    },function error (response) {
                     	$scope.authType=false;
                    })
            }
            else{
            		$scope.authType=false;
            }
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
