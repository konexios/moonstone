angular.module('pegasus').controller("ChangePasswordController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", '$uibModal', "SecurityService", "ErrorService", "ToastrService", "UserService", "AuthenticationService",
	 	function ($rootScope, $scope, $stateParams, $location, $state, $uibModal, SecurityService, ErrorService, ToastrService, UserService, AuthenticationService) {

            init();

            $scope.isAuthorized = SecurityService.isLoggedIn();
            $scope.passwordPolicy = {};

            $scope.save = function(changePasswordForm) {
                if (changePasswordForm.$invalid) {
                    ToastrService.popupError('Password cannot be changed because of invalid fields, please check errors.');
                } else {

                    var changePasswordModel = {
                        currentPassword: $scope.currentPassword,
                        newPassword: $scope.newPassword
                    };

                    $scope.busy = true;

                    if (SecurityService.isLoggedIn()) {

                        UserService.changePassword(changePasswordModel)
                            .then(function (response) {
                                var msg = 'Password has been changed successfully';
                                ToastrService.popupSuccess(msg);
                                reset();
                                changePasswordForm.$setPristine();
                            })
                            .catch(function (response) {
                                $scope.alertMessages = response.data.messages;
                                ToastrService.popupError('Failed to change the password! ' + response.data.message);
                            })
                            .finally(function () {
                                $scope.busy = false;
                            });
                    } else {

                        UserService.changePasswordNotAuthorized($stateParams.login, changePasswordModel)
                            .then(function (response) {
                                var msg = 'Password has been changed successfully';
                                ToastrService.popupSuccess(msg);
                                reset();
                                changePasswordForm.$setPristine();
                                login({"username": $stateParams.login, "password":changePasswordModel.newPassword});
                            })
                            .catch(function (response) {
                                $scope.alertMessages = response.data.messages;
                                ToastrService.popupError('Failed to change the password! ' + response.data.message);
                            })
                            .finally(function () {
                                $scope.busy = false;
                            });
                    }
                }
            };

            function init() {
                reset();
                $scope.busy = true;

                if (SecurityService.isLoggedIn()) {
                    UserService.getPasswordPolicy()
                        .then(function (response) {
                            $scope.passwordPolicy = response.data;
                        })
                        //.catch(ErrorService.handleHttpError)
                        .finally(function (response) {
                            $scope.busy = false;
                        });
                } else {

                    UserService.getPasswordPolicyByLogin($stateParams.login)
                    .then(function (response) {
                        $scope.passwordPolicy = response.data;
                    })
                    //.catch(ErrorService.handleHttpError)
                    .finally(function (response) {
                        $scope.busy = false;
                    });
                }
            }

            function login(credentials) {

                AuthenticationService.login(credentials)
                    .then(function(response) {
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
                                            if (redirect && redirect.indexOf("/signin") !== 0)
                                                $location.url(redirect);
                                            else
                                                $location.path("/dashboard");
                                        }
                                    );
                            } else {
                                return $q.reject("You do not have permission to access this application.");
                            }
                        } else {
                            return $q.reject("Your login and/or password was either misspelled or incorrect.");
                        }
                    })
                    .catch(
                        function(response) {
                            failureHandler();
                            if (typeof response == "string") {
                                $scope.signinMessage = response;
                            } else if (response.status === 401) {
                                $scope.signinMessage = "Your login and/or password was either misspelled or incorrect.";
                            } else if(response.data.message.indexOf("RequiredChangePassword") !== -1) {
                                $state.go("changepassword", {"login": $scope.credentials.username});
                            } else {
                                ErrorService.handleHttpError(response);
                            }
                        }
                    );
            }

            function reset() {
                $scope.currentPassword = '';
                $scope.newPassword = '';
                $scope.newPasswordConfirm = '';
            }
	 	}
	]
);