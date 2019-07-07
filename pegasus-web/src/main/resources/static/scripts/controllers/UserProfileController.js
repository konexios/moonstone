angular.module('pegasus').controller("UserProfileController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", "SecurityService", "ErrorService", "ToastrService", "UserService",
	 	function ($rootScope, $scope, $stateParams, $location, $state, SecurityService, ErrorService, ToastrService, UserService) {
	 		$scope.busy = false;
	 		$scope.loadingRoles = false;

	 		$scope.user = {};
	 		$scope.userCompanyOptions = [];
            $scope.companyUsers = [];
	 		
	 		if ($stateParams.userId) {
	 			console.log($stateParams);
	 			$scope.busy = true;
		 		UserService.getUserProfile($stateParams.userId)
		 			.then(function(response) {
		 				$scope.user = response.data.user;
                        $scope.userCompanyOptions = response.data.userCompanyOptions;
                        $scope.companyUsers = getUsersByCompany($scope.user.companyId);
						$scope.busy = false;
						setMenuItemName($scope.user);
		 			})
		 			.catch(function(response) {
		 				$scope.busy = false;
		 				ErrorService.handleHttpError(response);
		 			});	 			
	 		}

	 		function getUsersByCompany(companyId) {
				return $scope.userCompanyOptions.filter(function (user) {
					return user.companyId === companyId;
				});
			}

			function setMenuItemName(user) {
				$scope.$parent.userNameMI = user.firstName + ' ' + user.lastName;
			}
	 		
	 		$scope.updateProfile = function(form, user) {
				form.$setSubmitted();
	 			if (form.$valid) {
		 			$scope.busy = true;

                    var userProfile = {
                        firstName: user.firstName,
                        middleName: user.middleName,
                        lastName: user.lastName,
                        companyId: user.companyId,
                        office: user.office,
                        extension: user.extension,
                        fax: user.fax,
                        cell: user.cell,
                        home: user.home,
                        email: user.email,
                        address1: user.address1,
                        address2: user.address2,
                        city: user.city,
                        state: user.state,
                        postalCode: user.postalCode,
                        country: user.country,
						parentUserId: user.parentUserId
                    };

                    UserService.updateProfile(user.id, userProfile)
	 	 				.then(function(response) {
							 ToastrService.popupSuccess($scope.user.firstName + " has been successfully saved.");
							 setMenuItemName($scope.user);
	 	 				})
	 	 				.catch(ErrorService.handleHttpError)
						.finally(function(){
                            $scope.busy = false;
						});
	 			} else {
	 				ToastrService.popupError("The form is invalid! Please make changes and try again.");
	 			}
	 		};	

            $scope.hasAccess = function() {
                return (($scope.user.id && $scope.canUpdateUser()) || ($scope.user.id && $scope.user.id === $rootScope.user.id));
            };
	 	}
	]
);