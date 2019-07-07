controllers.controller('LoginController',
    [ '$rootScope', '$scope', '$q', '$location', '$routeParams', 'SecurityService', 'AuthenticationService', 'RheaService', 'ErrorService', 'SpinnerService',
    function($rootScope, $scope, $q, $location, $routeParams, SecurityService, AuthenticationService, RheaService, ErrorService, SpinnerService) {
        $scope.credentials = {
            username : SecurityService.getUserLogin() || '',
            password : ''
        };

        init();

        function init() {
        }

        $scope.signingMessage = '';
        if ($routeParams.reason === '401') {
            $scope.signingMessage = 'Your session has expired, please login again.';
        }

        function handleSuccess(response) {
            return AuthenticationService.selectUserApplication(response.data)
            .then(function() {
                var redirect = $routeParams.redirect;
                if (redirect && redirect.indexOf('/signin') != 0) {
                    $location.url(redirect);
                } else {
                    $location.url('/home');
                }
            });
        }

        function handleFailure() {
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
                    var exception = response.data.exception.substring(response.data.exception.lastIndexOf('.')+1);
                    switch(exception) {
                        case 'UnverifiedAccountException':
                            $scope.signingMessage = 'Your account has not been verified.';
                            break;
                        case 'RequiredChangePasswordException':
                            $scope.signingMessage = 'Your password has to be changed, use another webapp for that.';
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
        };

        var isLoggedIn = SecurityService.isLoggedIn();
        if (isLoggedIn) {
            $scope.loginBtnDisabled = true;
            AuthenticationService.user()
            .then(handleSuccess)
            .catch(handleFailure);
        } else {
            $scope.loginBtnDisabled = false;
            // get application hid, branding of login screen, handle SSO if
            // configured, etc.
        }
    }
]);
