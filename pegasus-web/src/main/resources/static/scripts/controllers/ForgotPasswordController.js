angular.module('pegasus').controller("ForgotPasswordController",
    [
        "$rootScope", "$scope", "$stateParams", "$location", "$state", "ErrorService", "ToastrService", "UserService", "SecurityService",
        function ($rootScope, $scope, $stateParams, $location, $state, ErrorService, ToastrService, UserService, SecurityService) {

            $scope.busy = false;

            $scope.form = {
                login: SecurityService.getUser() || ''
            };

            $scope.send = function (form) {
                $scope.busy = true;
                UserService.forgotPassword(form.login)
                    .then(function (response) {
                        ToastrService.popupSuccess('Please check your email for temporary password for your login');
                        $location.path('/signin');
                    })
                    .catch(function (response) {
                        $scope.forgotMessage = response;
                        ToastrService.popupError('Please enter the right login');
                        $scope.busy = false;
                    })
            }
        }
    ]);

