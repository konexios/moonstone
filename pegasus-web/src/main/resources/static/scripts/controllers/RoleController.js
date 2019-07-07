angular.module('pegasus').controller("RoleController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", '$uibModal', "SecurityService", "ErrorService", "ToastrService", "PrivilegeService", "ApplicationService", "RoleService",
	 	function ($rootScope, $scope, $stateParams, $location, $state, $uibModal, SecurityService, ErrorService, ToastrService, PrivilegeService, ApplicationService, RoleService) {
	 		$scope.busy = true;
	 		$scope.loadingApplications = true;
	 		$scope.loadingPrivileges = true;
	 		
	 		$scope.productOptions = [];
	 		$scope.applicationOptions = [];
	 		$scope.privilegeOptions = [];
	 		$scope.role = {};
	 		
	 		if ($stateParams.roleId) {
	 			$scope.busy = true;
	 			$scope.loadingApplications = $scope.busy;
	 			$scope.loadingPrivileges = $scope.busy;
 			
		 		RoleService.get($stateParams.roleId)
		 			.then(function(response) {
		 				$scope.role = response.data.role;
		 				$scope.productOptions = response.data.productOptions;
		 				$scope.applicationOptions = response.data.applicationOptions;
		 				$scope.privilegeOptions = response.data.privilegeOptions;

		 				$scope.busy = false;
			 			$scope.loadingApplications = $scope.busy;
		 				$scope.loadingPrivileges = $scope.busy;
		 			})
		 			.catch(function(response) {
		 				$scope.busy = false;
			 			$scope.loadingApplications = $scope.busy;
		 				$scope.loadingPrivileges = $scope.busy;
		 				ErrorService.handleHttpError(response);
		 			});	 			
	 		}
	 		
	 		$scope.save = function(form, role) {
				form.$setSubmitted();
	 			if (form.$valid) {
		 			$scope.busy = true;

		 			var savePromise = null;
		 			if ($scope.role.id && $scope.role.id != "") {
			 			savePromise = RoleService.update(role);		 				
		 			} else {
		 				savePromise = RoleService.create(role);
		 			}
		 			
		 			if (!savePromise)
		 				return;
		 			
	 	 			savePromise
	 	 				.then(function(response) {
	 	 					$scope.role = response.data;
	
				 			// reset form
			 				form.$setPristine();
			 				form.$setUntouched();
				 			
				 			$scope.busy = false;
				 			
				 			ToastrService.popupSuccess($scope.role.name + " has been successfully saved.");
	 	 				})
	 	 				.catch(function(response) {
	 	 					$scope.busy = false;
	 	 					ErrorService.handleHttpError(response);
	 	 				});		 				
	 			} else {
	 				ToastrService.popupError("The form is invalid! Please make changes and try again.");
	 			}
	 		};
	 		
	 		$scope.loadApplications = function() {
	 			console.log("loadingApplications...");
	 			
	 			$scope.applicationOptions = [];
 				$scope.loadingApplications = true;
 				
	 			RoleService.applicationOptions($scope.role.productId)
	 			.then(function(response) {
	 				$scope.applicationOptions = response.data;
	 			})
	 			.catch(function(response) {
	 				ErrorService.handleHttpError(response);
	 			})
	 			.finally(function(){
	 				$scope.loadingApplications = false;	 				
	 			});
	 		};
	 		
	 		$scope.loadPrivileges = function() {
	 			$scope.privilegeOptions = [];
	 			$scope.role.privilegeIds = [];
	 			$scope.loadingPrivileges = true;

	 			RoleService.privilegeOptions($scope.role.productId)
	 			.then(function(response) {
	 				$scope.privilegeOptions = response.data;
	 			})
	 			.catch(function(response) {
	 				ErrorService.handleHttpError(response);
	 			})
	 			.finally(function(){
	 				$scope.loadingPrivileges = false;	 				
	 			});
	 		}
	 		
	 		$scope.hasPrivilege = function(privilegeId) {
	 			var result = false;
	 			
	 			for(var i = 0; i < $scope.role.privilegeIds.length; i++) {
	 				var existing = $scope.role.privilegeIds[i];
	 				if (existing === privilegeId) {
	 					result = true;
	 					break;
	 				}
	 			}
	 			
	 			return result;
	 		};
	 		
	 		$scope.togglePrivilege = function(privilegeId) {
	 			var index = -1;
	 			for(var i = 0; i < $scope.role.privilegeIds.length; i++) {
	 				var existing = $scope.role.privilegeIds[i];
	 				if (existing === privilegeId) {
	 					index = i;
	 					break;
	 				}
	 			}
	 			
	 			if (index != -1) {
	 				$scope.role.privilegeIds.splice(index, 1);
	 			} else {
	 				$scope.role.privilegeIds.push(privilegeId);
	 			}
	 		};
	 		
	 		$scope.selectAllPrivileges = function() {
	 			$scope.deselectAllPrivileges();
	 			
	 			for(var i = 0; i < $scope.privilegeOptions.length; i++) {
	 				$scope.role.privilegeIds.push($scope.privilegeOptions[i].id);
	 			}
	 		};
	 		
	 		$scope.deselectAllPrivileges = function() {
	 			$scope.role.privilegeIds = [];
	 		};	 		
	 		
	 		$scope.cloneRole = function() {

				var modalInstance = $uibModal.open({
					animation: true,
					templateUrl: 'partials/modals/cloneRole.html',
					controller: 'CloneRoleController',
					size: 'lg',
					resolve: {
						roleId: function() { return $scope.role.id },
						applicationOptions: function() { return $scope.applicationOptions; }						
					}
				});

				modalInstance.result
					.then(function (role) {

			 			$scope.busy = true;

			 			RoleService.createClone(role)
		 	 				.then(function(response) {
		 	 					$scope.role = response.data;
					 			$scope.busy = false;
					 			
					 			ToastrService.popupSuccess("Role has been successfully created for " + $scope.application.name + ".");
		 	 				})
		 	 				.catch(function(response) {
		 	 					$scope.busy = false;
		 	 					ErrorService.handleHttpError(response);

		 	 					ToastrService.popupError("Unable to clone role! Message: " + response.data.message + " Status: " + response.data.status);
		 	 				});
					});
	 		};

            $scope.hasAccess = function() {
                return (!$scope.role.id && ($scope.canCreateRole() || $scope.isSuperAdministrator())) ||
                    ($scope.role.id && ($scope.canUpdateRole() || $scope.isSuperAdministrator()));
            };
	 	}
	]
);

angular.module('pegasus').controller('CloneRoleController', [
	'$scope', '$uibModalInstance', 'RoleService', 'roleId', 'applicationOptions',
	function ($scope, $uibModalInstance, RoleService, roleId, applicationOptions) {
		
		$scope.role = {};
		$scope.applicationOptions = applicationOptions;
		
		RoleService.clone(roleId)
		.then(function(response) {
			$scope.role = response.data.role
		})
		.catch(function(response) {
		})
		.finally(function(){
		});
		
		$scope.ok = function (form) {
			
			if (form.$valid) {
				$uibModalInstance.close($scope.role);
			} else {
				// invalid form...
			}
		};
	
		$scope.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	}
]);