angular.module('pegasus').controller("UserAuthenticationController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", "SecurityService", "ErrorService", "ToastrService", "UserService", "$uibModal",
	 	function ($rootScope, $scope, $stateParams, $location, $state, SecurityService, ErrorService, ToastrService, UserService, $uibModal) {
	 		$scope.busy = true;
	 		$scope.user = {};
	 		$scope.authentications = [];

	 		init();

	 		$scope.addOrEditAuth = function(auth) {
	 		    if ($scope.authOptions && $scope.authOptions.length > 0) {

                    $uibModal.open({
                        animation: true,
                        templateUrl: 'partials/modals/user/addOrEditAuthentication.html',
                        controller: 'UserAddOrEditAuthenticationController',
                        size: 'lg',
                        resolve: {
                            user: function () {
                                return $scope.user
                            },
                            authOptions: function () {
                                return $scope.authOptions
                            },
                            authentication: function () {
                                return auth
                            }
                        }
                    }).result.then(init);

                } else {

	 		        $uibModal.open({
                        animation: true,
                        templateUrl: 'partials/modals/user/notDefinedAuthentications.html',
                        controller: 'NotDefinedAuthenticationController',
                        size: 'lg',
                        resolve: {

                        }
                    }).result.then(function (user) {

                    });
                }
			};

	 		$scope.disableAll = function() {

                $uibModal.open({
                    animation: true,
                    templateUrl: 'partials/modals/user/disableAllAuthentications.html',
                    controller: 'DisableAllAuthenticationController',
                    size: 'lg',
                    resolve: {
                        user: function () {
                            return $scope.user
                        }
                    }
                }).result.then(init);
			};

	 		function init() {

                if ($stateParams.userId) {
                    $scope.busy = true;
                    UserService.getUserAuthentications($stateParams.userId)
                        .then(function(response) {
                            console.log(response.data);
                            $scope.user = response.data.user;
                            $scope.authentications = response.data.authentications;
                            $scope.authOptions = response.data.authOptions;
                        })
                        .catch(ErrorService.handleHttpError)
						.finally(function() {
                            $scope.busy = false;
						})
                }
			}
	 	}
	]
);

/**
 * Controller add or edit user authentication
 */
angular.module('pegasus').controller('UserAddOrEditAuthenticationController', [
    '$scope', '$rootScope', '$uibModalInstance', 'UserService', 'ErrorService', 'ToastrService', 'user', 'authentication', 'authOptions',
    function ($scope, $rootScope, $uibModalInstance, UserService, ErrorService, ToastrService, user,  authentication, authOptions) {

        $scope.busy = false;
        $scope.user = user;
        $scope.authentication = {};
        $scope.isCreation = authentication === "new";

        $scope.typeOptions = authOptions.map(function (option) { return option.type });
        $scope.providerOptions = getProvidersByType(authentication);

        if ($scope.isCreation){
            $scope.authentication.type = $scope.typeOptions[0];
            $scope.providerOptions = getProvidersByType($scope.authentication);
            $scope.authentication.provider = $scope.providerOptions[0].authId;
        } else {
            $scope.authentication = authentication;
            $scope.authentication.provider = authentication.authId;
        }


        $scope.onChangeType = function() {
            $scope.providerOptions = getProvidersByType($scope.authentication);
        };

        $scope.save = function(form) {

            if (!$scope.user) return;

            if (!form.$valid) {
                ToastrService.popupError("The form is invalid! Please make changes and try again.");
                return;
            }

            if ($scope.isCreation) {
                addUserAuthentication($scope.user.id, $scope.authentication);
            } else {
                updateUserAuthentication($scope.user.id, $scope.authentication);
            }
        };

        $scope.cancel = function() {
            $uibModalInstance.close();
        };

        function getProvidersByType(auth) {
            return authOptions
                .filter(function(option) { return option.type === auth.type });
        }

        function addUserAuthentication(userId, authentication) {

            $scope.busy = true;
            UserService.addUserAuthentication(userId, authentication)
                .then(function(response) {
                    ToastrService.popupSuccess("New authentication has been successfully saved.");
                    $uibModalInstance.close();
                })
                .catch(ErrorService.handleHttpError)
                .finally(function(){
                    $scope.busy = false;
                });
        }

        function updateUserAuthentication(userId, authentication) {

            $scope.busy = true;
            UserService.updateUserAuthentication(userId, authentication)
                .then(function(response) {
                    ToastrService.popupSuccess("Authentication has been successfully saved.");
                    $uibModalInstance.close();
                })
                .catch(ErrorService.handleHttpError)
                .finally(function(){
                    $scope.busy = false;
                });
        }
    }
]);

/**
 * Not defined authentication
 */
angular.module('pegasus').controller('NotDefinedAuthenticationController', [
    '$scope', '$rootScope', '$uibModalInstance',
    function ($scope, $rootScope, $uibModalInstance) {


        $scope.cancel = function() {
            $uibModalInstance.close();
        };
    }
]);

/**
 * Disable all Authentications
 */
angular.module('pegasus').controller('DisableAllAuthenticationController', ['$scope', '$rootScope', '$uibModalInstance', 'ToastrService', 'ErrorService', 'UserService', 'user',
    function ($scope, $rootScope, $uibModalInstance, ToastrService, ErrorService, UserService, user) {

        $scope.yes = function() {
            $scope.busy = true;
            UserService.disableAllAuthentication(user.id)
                .then(function(response) {
                    ToastrService.popupSuccess("Authentications were successfully disabled.");
                    $uibModalInstance.close();
                })
                .catch(ErrorService.handleHttpError)
                .finally(function(){
                    $scope.busy = false;
                });
        };

        $scope.cancel = function() {
            $uibModalInstance.close();
        };
    }
]);