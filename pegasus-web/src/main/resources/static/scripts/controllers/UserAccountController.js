angular.module('pegasus').controller("UserAccountController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", "SecurityService", "ErrorService", "ToastrService", "UserService", "ApplicationService", "CompanyService", "$uibModal",
	 	function ($rootScope, $scope, $stateParams, $location, $state, SecurityService, ErrorService, ToastrService, UserService, ApplicationService, CompanyService, $uibModal) {
	 		$scope.busy = true;
	 		$scope.loadingRoles = false;

	 		$scope.statusOptions = [];
	 		$scope.companyOptions = [];
	 		$scope.companyRoleOptions = [];
	 		$scope.roleAvailableOptions = [];
	 		$scope.roleAssignedOptions = [];
	 		$scope.user = {};
            $scope.companyUsers = [];
            $scope.isAdmin = false;
            $scope.companyIdForRole = null;
            $scope.applicationOptions = [];
            $scope.applicationIdForRole = null;

            var checkedAvailableRoleIds = [];
            var checkedAssignedRoleIds = [];

            init();

	 		$scope.onCompanyChange = function() {
	 			var companyId = $scope.user.companyId;
	 			
	 			if (companyId !== undefined && companyId !== null && companyId !== '') {
                    $scope.companyUsers = getUsersByCompany(companyId);
				}
	 		};

	 		$scope.onCompanyRoleChange = function () {
                if ($scope.companyIdForRole !== undefined &&
					$scope.companyIdForRole !== null && $scope.companyIdForRole !== '') {

                    loadApplications($scope.companyIdForRole);
                }
            };

            $scope.onApplicationRoleChange = function () {
                if ($scope.companyIdForRole !== undefined &&
                    $scope.companyIdForRole !== null && $scope.companyIdForRole !== '') {
                    loadRoles($scope.applicationIdForRole);
                }
            };

            $scope.toggleAvailableRoleCheckbox = function(roleId) {
                addNewOrRemoveExistedElement(checkedAvailableRoleIds, roleId);
			};

            $scope.toggleAssignedRoleCheckbox = function(roleId) {
                addNewOrRemoveExistedElement(checkedAssignedRoleIds, roleId);
            };

			$scope.add = function() {
                addRolesToAssigned();
			};

            $scope.addAll = function() {
                checkedAvailableRoleIds = $scope.roleAvailableOptions.map(function (role) {
                	return role.id;
				});
                addRolesToAssigned();
                checkedAvailableRoleIds = [];
            };

            $scope.remove = function() {
                removeRolesFromAssigned();
            };

            $scope.removeAll = function() {
                checkedAssignedRoleIds = $scope.roleAssignedOptions.map(function (role) {
                    return role.id;
                });
                removeRolesFromAssigned();
                checkedAssignedRoleIds = [];
            };

            $scope.copyRolesFromOtherUser = function() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'partials/modals/user/copyUserRoles.html',
                    controller: 'UserCopyRolesController',
                    size: 'lg',
                    resolve: {

                    }
                });

                modalInstance.result.then(function(roles) {
                    var rolesForCopy = roles.rolesForCopy;
                    var replaceRoles = roles.replaceRoles;
                    if (replaceRoles) {
                      $scope.user.roleIds = [];
                    }

                    rolesForCopy = rolesForCopy.filter(function(role){ return role && role.id ? role : false; });
                    var roleIdsForCopy = rolesForCopy.map(function(role){ return role.id; });
                    $scope.roleAssignedOptions = rolesForCopy;
                    roleIdsForCopy.forEach(function(role){
                        if ($scope.user.roleIds.indexOf(role) === -1) {
                            $scope.user.roleIds.push(role);
                        }
                    });
                });
			};

	 		$scope.hasRole = function(roleId) {
	 			var result = false;
	 			
	 			for(var i = 0; i < $scope.user.roleIds.length; i++) {
	 				var existing = $scope.user.roleIds[i];
	 				if (existing === roleId) {
	 					result = true;
	 					break;
	 				}
	 			}
	 			
	 			return result;
	 		};
	 		
	 		$scope.toggleRole = function(roleId) {
	 			var index = -1;
	 			for(var i = 0; i < $scope.user.roleIds.length; i++) {
	 				var existing = $scope.user.roleIds[i];
	 				if (existing === roleId) {
	 					index = i;
	 					break;
	 				}
	 			}
	 			
	 			if (index != -1) {
	 				$scope.user.roleIds.splice(index, 1);
	 			} else {
	 				$scope.user.roleIds.push(roleId);
	 			}
	 		};
	 		
	 		$scope.save = function(form) {

	 			if (form.$valid) {
		 			$scope.busy = true;

                    var userProfile = {
                        login: $scope.user.login,
                        companyId: $scope.user.companyId,
                        admin: $scope.user.admin,
                        status: $scope.user.status,
                        roleIds: $scope.user.roleIds
                    };

                    UserService.updateAccount($scope.user.id, userProfile)
	 	 				.then(function(response) {
				 			ToastrService.popupSuccess($scope.user.firstName + " has been successfully saved.");
	 	 				})
	 	 				.catch(ErrorService.handleHttpError)
                        .finally(function(){
                            $scope.busy = false;
                        });
	 			} else {
	 				ToastrService.popupError("The form is invalid! Please make changes and try again.");
	 			}
	 		};	
	 		
	 		$scope.resetPassword = function(id) {
	 			$scope.busy = true;

	 			UserService.resetPassword(id)
 	 				.then(function(response) {
 	 					$scope.user = response.data;

			 			$scope.busy = false;
			 			
			 			ToastrService.popupSuccess($scope.user.firstName + "'s password has been successfully reset.");
 	 				})
 	 				.catch(function(response) {
 	 					$scope.busy = false;
 	 					ErrorService.handleHttpError(response);
 	 				});
	 		};

	 		$scope.disableAccount = function(userId) {

	 		    if (!userId) return;

	 		    $scope.busy = true;
                UserService.disableUser(userId)
                    .then(function(response) {
                        $scope.user.status = response.data.status;
                        ToastrService.popupSuccess($scope.user.firstName + " has been successfully disabled.");
                    })
                    .catch(ErrorService.handleHttpError)
                    .finally(function() {
                        $scope.busy = false;
                    })
            };

	 		$scope.changeLogin = function() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'partials/modals/user/changeLogin.html',
                    controller: 'UserChangeLoginController',
                    size: 'lg',
                    resolve: {
                        user: $scope.user
                    }
                });

                modalInstance.result.then(function(user) {

                });
            };

	 		$scope.disableUserAccount = function() {
                UserService.update(user)
                    .then(function(response) {
                        $scope.user = response.data;

                        // reset form
                        form.$setPristine();
                        form.$setUntouched();

                        $scope.busy = false;
                        ToastrService.popupSuccess($scope.user.login + " account was successfully disabled.");
                    })
                    .catch(function(response) {
                        $scope.busy = false;
                        ErrorService.handleHttpError(response);
                    });
            };

            $scope.canChangeUserTenant = function() {
                return $scope.user.id && $scope.canUpdateUserAccount();            	
            }
            
            $scope.hasAccess = function() {
                return ($scope.user.id && $scope.canUpdateUserAccount() ||
                    ($scope.user.id && $scope.user.id === $rootScope.user.id));
            };

            $scope.canAccessToResetPassword = function () {
                return $scope.canResetUserPassword();
            };

            $scope.canAccessToDisableAccount = function () {
                return $scope.canDisableUserAccount();
            };

            $scope.canAccessToChangeLogin = function () {
				return ($scope.canChangeUserLogin() || ($scope.user.id && $scope.user.id === $rootScope.user.id));
            };

            function loadApplications(companyId) {

                $scope.busy = true;
                UserService.applications(companyId)
                    .then(function(response) {
                        $scope.applicationOptions = response.data;
                        if (response.data.length > 0) {
                            $scope.applicationIdForRole = response.data[0].id;
                            loadRoles($scope.applicationIdForRole);
                        }
                    })
                    .catch(function(response) {
                        ErrorService.handleHttpError(response);
                    }).finally(function () {
                    $scope.busy = false;
                });
            }

            function loadRoles(companyId) {
                $scope.loadingRoles = true;
                $scope.roleOptions = [];
                if (companyId === "") {
                    roleSortBetweenAvailableAndAssigned([]);
                } else {
                    UserService.roleOptions(companyId)
                        .then(function (response) {
                            roleSortBetweenAvailableAndAssigned(response.data);
                            $scope.loadingRoles = false;
                        })
                        .catch(function (response) {
                            $scope.loadingRoles = false;
                            ErrorService.handleHttpError(response);
                        });
                }
            }

            function roleSortBetweenAvailableAndAssigned(roles) {

                if (!roles instanceof Array) return;

                $scope.roleAssignedOptions = [];
                $scope.roleAvailableOptions = [];

                roles.forEach(function (role) {
                    if ($scope.user.roleIds.indexOf(role.id) !== -1) {
                        $scope.roleAssignedOptions.push(role);
                    } else {
                        $scope.roleAvailableOptions.push(role);
                    }
                });
            }

            function init() {
                if ($stateParams.userId) {
                    $scope.busy = true;
                    UserService.getUserAccount($stateParams.userId)
                        .then(function (response) {
                            $scope.user = response.data.user;
                            $scope.companyIdForRole = $scope.user.companyId;
                            $scope.statusOptions = response.data.statusOptions;
                            $scope.companyOptions = response.data.companyOptions;
                            $scope.companyRoleOptions = response.data.companyRoleOptions;
                            $scope.isAdmin = $scope.user.admin;
                            loadApplications($scope.companyIdForRole);
                        })
                        .catch(function (response) {
                            ErrorService.handleHttpError(response);
                        })
                        .finally(function () {
                            $scope.busy = false;
                        });
                }
            }

            function addRolesToAssigned() {
                checkedAvailableRoleIds.forEach(function (roleId) {

                    var roleForAssigned = $scope.roleAvailableOptions.filter(function (role) {
                        return role.id === roleId ? role : false;
                    });
                    roleForAssigned = roleForAssigned.length > 0 ? roleForAssigned[0] : null;

                    var roleIdIndex = $scope.roleAvailableOptions.indexOf(roleForAssigned);
                    if (roleForAssigned !== null) {
                        $scope.roleAvailableOptions.splice(roleIdIndex, 1);
                        $scope.roleAssignedOptions.push(roleForAssigned);
                        $scope.user.roleIds.push(roleId);
                    }
                });
                checkedAvailableRoleIds = [];
            }

            function removeRolesFromAssigned() {
                checkedAssignedRoleIds.forEach(function (roleId) {

                    var roleForAvailable = $scope.roleAssignedOptions.filter(function (role) {
                        return role.id === roleId ? role : false;
                    });
                    roleForAvailable = roleForAvailable.length > 0 ? roleForAvailable[0] : null;

                    var roleIdIndex = $scope.roleAssignedOptions.indexOf(roleForAvailable);
                    if (roleForAvailable !== null) {
                        $scope.roleAssignedOptions.splice(roleIdIndex, 1);
                        $scope.roleAvailableOptions.push(roleForAvailable);
                        $scope.user.roleIds.splice($scope.user.roleIds.indexOf(roleId), 1);
                    }
                });
              checkedAssignedRoleIds = [];
            }

            function addNewOrRemoveExistedElement(array, element) {
                if (array.indexOf(element) === -1) {
                    array.push(element);
                } else {
                    array.splice(array.indexOf(element), 1);
                }
            }
	 	}
	]
);

/**
 * Controller for Copy user roles
 */
angular.module('pegasus').controller('UserCopyRolesController', [
        '$scope', '$rootScope', '$uibModalInstance', 'UserService', 'ErrorService', 'ToastrService',
        function ($scope, $rootScope, $uibModalInstance, UserService, ErrorService, ToastrService) {

            $scope.busy = false;
            $scope.users = [];
            $scope.user = null;
            $scope.searchUserParams = "";
            $scope.rolesForCopy = [];
            $scope.replaceExistingRoles = false;

            $scope.$watch("searchUserParams", function (newValue) {

            	if (newValue && newValue.length >= 3) {

            		var searchParams = {
                        firstNameLastNameAndLogin: newValue,
                        itemsPerPage: 100 //TODO: fixme
					};

            		$scope.busy = true;
					UserService.find(searchParams)
						.then(function (response) {
							$scope.users = response.data.result.content;
                        })
						.catch(ErrorService.handleHttpError)
						.finally(function () {
							$scope.busy = false;
                        })
				}
            });

            $scope.copy = function() {
                if (!$scope.user) return;
                $scope.busy = true;
                UserService.get($scope.user)
                    .then(function (response) {

                        if (!response.data.user) {
                            console.log("User not found");
                            return;
                        }

                        $scope.rolesForCopy = response.data.user.roleIds.map(function(roleId) {
                            var roleOptions = response.data.roleOptions;
                            for (var i = 0; i < roleOptions.length; i++) {
                                if (roleId === roleOptions[i].id) {
                                    return roleOptions[i];
                                }
                            }
                        });

                        var roles = { rolesForCopy: $scope.rolesForCopy, replaceRoles: $scope.replaceExistingRoles };

                        $uibModalInstance.close(roles);
                    })
                    .catch(ErrorService.handleHttpError)
                    .finally(function () {
                        $scope.busy = false;
                    })
            };

            $scope.cancel = function() {
                $uibModalInstance.close();
            };
        }
]);

/**
 * Controller for change login
 */
angular.module('pegasus').controller('UserChangeLoginController', [
    '$scope', '$rootScope', '$uibModalInstance', 'UserService', 'ErrorService', 'ToastrService', 'user',
    function ($scope, $rootScope, $uibModalInstance, UserService, ErrorService, ToastrService, user) {

        $scope.busy = false;
        $scope.user = user;
        $scope.login = user.login;

        $scope.save = function(form) {
            if (!$scope.user) {
              ToastrService.popupError("No such user! Please re-login.");
              $uibModalInstance.close();
              return;
            }
            if (form.$valid) {
                $scope.busy = true;

                var userProfile = {
                    login: $scope.login,
                    companyId: user.companyId,
                    admin: user.admin,
                    status: user.status,
                    roleIds: user.roleIds
                };

                UserService.updateAccount($scope.user.id, userProfile)
                    .then(function(response) {
                        user.login = $scope.login;
                        ToastrService.popupSuccess($scope.user.firstName + " has been successfully saved.");
                    })
                    .catch(ErrorService.handleHttpError)
                    .finally(function(){
                        $scope.busy = false;
                    });
            } else {
                ToastrService.popupError("The form is invalid! Please make changes and try again.");
            }
            $uibModalInstance.close();
        };

        $scope.cancel = function() {
            $uibModalInstance.close();
        };
    }
]);