controllers.controller('ChangePasswordController', ['$scope', '$location', '$routeParams', 'UserService', 'ErrorService', 'SpinnerService', 'ToastrService', 'SecurityService', 'AuthenticationService',
    function($scope, $location, $routeParams, UserService, ErrorService, SpinnerService, ToastrService, SecurityService, AuthenticationService) {
    	$scope.login = $routeParams.usr;
	
    	$scope.passwordPolicy = {};

        function reset() {
            $scope.alertMessages = [];
            $scope.currentPassword = '';
            $scope.newPassword = '';
            $scope.newPasswordConfirm = '';
        }

        $scope.save = function(changePasswordForm) {
            if (changePasswordForm.$invalid) {
                ToastrService.popupError('Password cannot be changed because of invalid fields, please check errors.');
            } else {
                $scope.changePasswordLoading = true;
                UserService.changePassword($scope.login, $scope.currentPassword, $scope.newPassword)
                .then(function(response) {
                    if (response.data.ok) {
                        // success
                        var msg = 'Password has been changed successfully';
                        if (!SecurityService.isLoggedIn()) {
                        	var username = null;
                        	
                        	if ($scope.login != undefined && $scope.login != null && $scope.login != '') {
                        		username = $scope.login;	
                        	} else {
                        		username = SecurityService.getUserLogin();
                        	}
                        	
                            // try to log in using new password
                            login(username, $scope.newPassword);
                            msg += '. Logging in using the new password...';
                        } else {
                            $location.path('/home');
                        }
                        ToastrService.popupSuccess(msg);
                        reset();
                        changePasswordForm.$setPristine();
                    } else {
                        // validation errors
                        $scope.alertMessages = response.data.messages;
                        ToastrService.popupError('Failed to change the password, see errors for details');
                    }
                })
                .catch(ErrorService.handleHttpError)
                .finally(function () {
                    $scope.changePasswordLoading = false;
                });
            }
        };

        function login(username, password) {
            return AuthenticationService.login({
                username: username,
                password: password
            })
            .then(function(response) {
                return AuthenticationService.selectUserApplication(response.data);
            })
            .then(function() {
                // successful login - redirect to My Devices page
                $location.path('/home');
            })
            // Since the password has been just successfully changed, the user should login successfully.
            // Therefore no special error handling is done.
            .catch(ErrorService.handleHttpError);
        }

        $scope.init = function() {
            reset();
            
        	if($routeParams.reason == 'passwordreset') {
                $scope.alertMessages = ['You are required to change your password'];
            }
            
            if($routeParams.pwd != undefined && $routeParams.pwd != null && $routeParams.pwd != '') {
            	$scope.currentPassword = $routeParams.pwd;
            }
            
            UserService.getPasswordPolicy($scope.login)
            	.then(function(response) {
            		$scope.passwordPolicy = response.data;
            	})
            	.catch(ErrorService.handleHttpError);
        }
        
        $scope.init();
    }
]);
