angular.module('pegasus').controller("UserController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", "SecurityService", "ErrorService", "UserService", "$uibModal",
	 	function ($rootScope, $scope, $stateParams, $location, $state, SecurityService, ErrorService, UserService) {

		    if ($stateParams.userId) {
                $scope.busy = true;
                UserService.get($stateParams.userId)
                    .then(function(response) {
                        $scope.user = response.data.user;
                        $scope.busy = false;
                        setMenuItemName($scope.user);
                    })
                    .catch(function(response) {
                        $scope.busy = false;
                        ErrorService.handleHttpError(response);
                    });
            }

            function setMenuItemName(user) {
                $scope.userNameMI = user.firstName + ' ' + user.lastName;
            }
	 	}
	]
);