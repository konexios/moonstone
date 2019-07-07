controllers.controller('ForgotPasswordController', ['$scope', '$location', 'UserService', 'SecurityService', 'ErrorService', 'SpinnerService', 'ToastrService',
    function($scope, $location, UserService, SecurityService, ErrorService, SpinnerService, ToastrService) {
        $scope.login = SecurityService.getUserLogin() || '';
        $scope.errorMessage = null;

        $scope.save = function(form) {
            if (form.$invalid) {
                ToastrService.popupError('Please enter the login');
            } else {
                $scope.resetPasswordLoading = true;
                UserService.resetPassword($scope.login)
                    .then(function() {
                        SecurityService.saveSession($scope.login, null);
                        ToastrService.popupSuccess('Please check your email for temporary password for your login');
                        $location.path('/signin');
                    })
                    .catch(function(response) {
                        if (response.data.exceptionClassName === 'com.arrow.acs.AcsLogicalException') {
                            $scope.errorMessage = response.data.message;
                        } else {
                            ErrorService.handleHttpError(response);
                        }
                    })
                    .finally(function() {
                        $scope.resetPasswordLoading = false;
                    });
            }
        };

    }
]);
