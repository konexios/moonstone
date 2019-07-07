angular.module('pegasus').controller("PrivilegeController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", "SecurityService", "ErrorService", "ToastrService", "ProductService", "PrivilegeService",
	 	function ($rootScope, $scope, $stateParams, $location, $state, SecurityService, ErrorService, ToastrService, ProductService, PrivilegeService) {
	 		$scope.busy = true;
	 		
	 		$scope.productOptions = [];
	 		$scope.privilege = {};
	 		
	 		if ($stateParams.privilegeId) {
	 			$scope.busy = true;
		 		PrivilegeService.get($stateParams.privilegeId)
		 			.then(function(response) {
		 				$scope.privilege = response.data.privilege;
		 				$scope.productOptions = response.data.productOptions;
		 				$scope.busy = false;
		 			})
		 			.catch(function(response) {
		 				$scope.busy = false;
		 				ErrorService.handleHttpError(response);
		 			});	 			
	 		}
	 		
	 		$scope.save = function(form, privilege) {
				form.$setSubmitted();
	 			if (form.$valid) {
		 			$scope.busy = true;

		 			var savePromise = null;
		 			if ($scope.privilege.id && $scope.privilege.id != "") {
			 			savePromise = PrivilegeService.update(privilege);		 				
		 			} else {
		 				savePromise = PrivilegeService.create(privilege);
		 			}
		 			
		 			if (!savePromise)
		 				return;
		 			
	 	 			savePromise
	 	 				.then(function(response) {
	 	 					$scope.privilege = response.data;
	
				 			// reset form
			 				form.$setPristine();
			 				form.$setUntouched();
				 			
				 			$scope.busy = false;
				 			
				 			ToastrService.popupSuccess($scope.privilege.name + " has been successfully saved.");
	 	 				})
	 	 				.catch(function(response) {
	 	 					$scope.busy = false;
	 	 					ErrorService.handleHttpError(response);
	 	 				});		 				
	 			} else {
	 				ToastrService.popupError("The form is invalid! Please make changes and try again.");
	 			}
	 		};

            $scope.hasAccess = function() {
                return (!$scope.privilege.id && ($scope.canCreatePrivilege() || $scope.isSuperAdministrator())) ||
                    ($scope.privilege.id && ($scope.canUpdatePrivilege() || $scope.isSuperAdministrator()));
            };
	 	}
	]
);