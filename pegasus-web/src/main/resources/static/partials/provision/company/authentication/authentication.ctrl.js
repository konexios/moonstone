(function() {
    'use strict';

    angular
        .module('pegasus')
        .controller('CompanySecurityController', ["$scope", "$stateParams", "$uibModal", "ToastrService", "ErrorService", "CompanyService", CompanySecurityController]);

    function CompanySecurityController($scope, $stateParams, $uibModal, ToastrService, ErrorService, CompanyService) {
        var companyId = $stateParams.companyId;
        
        $scope.busy = true;
        $scope.companySecurity = null;
        
        function init() {
        	loadCompanySecurity();
        }
        
        function loadCompanySecurity() {
            CompanyService.authentication(companyId).then(function(response) {
                $scope.companySecurity = response.data;
            }).catch(function(response){
            	ErrorService.handleHttpError(response);
            }).finally(function(){
            	$scope.busy = false;
            });        	
        }

        $scope.authModal = function(companyId, authId, type) {
        	
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/provision/company/authentication/companyAuth.html',
                controller: 'CompanyAuthController',
                size: 'lg',
                resolve: {
                	companyId: function() {
                		return companyId;
                	},
                	authId: function() {
                		return authId;
                	},
                	authType: function() {
                		return type;
                	}
                }
            });

            modalInstance.result.then(function() {
            	loadCompanySecurity();
            });        	
        }
        
        $scope.loginPolicyModal = function(companyId) {
        	
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/provision/company/authentication/loginpolicy.html',
                controller: 'CompanyLoginPolicyController',
                size: 'lg',
                resolve: {
                	companyId: function() {
                		return companyId;
                	}
                }
            });

            modalInstance.result.then(function() {
            	loadCompanySecurity();
            });        	
        }
        
        $scope.passwordPolicyModal = function(companyId) {
        	
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/provision/company/authentication/passwordpolicy.html',
                controller: 'CompanyPasswordPolicyController',
                size: 'lg',
                resolve: {
                	companyId: function() {
                		return companyId;
                	}
                }
            });

            modalInstance.result.then(function() {
            	loadCompanySecurity();
            });        	
        }
       
        init();
    }
    
    angular
		.module('pegasus')
		.controller('CompanyAuthController', ["$scope", "$uibModalInstance", "ToastrService", "ErrorService", "CompanyService", "companyId", "authId", "authType", CompanyAuthController]);
    
    function CompanyAuthController($scope, $uibModalInstance, ToastrService, ErrorService, CompanyService, companyId, authId, authType) {
    	$scope.companyId = companyId;
        $scope.authId = authId;
        $scope.authType = authType;
        
        $scope.busy = true;
        $scope.companyAuth = null;
        $scope.ldapApplications = [];
        
        function init() {
            CompanyService.auth($scope.companyId, $scope.authId, $scope.authType).then(function(response) {
                $scope.companyAuth = response.data.companyAuth;
                $scope.ldapApplications = response.data.ldapApplications;
            }).catch(function(response){
            	ErrorService.handleHttpError(response);
            }).finally(function(){
            	$scope.busy = false;
            });
        }

        $scope.save = function (form, companyAtuh) {
        	if (form.$valid) {
	 			var savePromise = null;
	 			if ($scope.companyAuth.hasOwnProperty("id") && $scope.companyAuth.id && $scope.companyAuth.id != "") {
		 			savePromise = CompanyService.updateAuth($scope.companyAuth);		 				
	 			} else {
	 				savePromise = CompanyService.createAuth($scope.companyAuth);
	 			}
	 			
	 			if (!savePromise)
	 				return;

 	 			savePromise
	 				.then(function(response) {
	 					$scope.companyAuth = response.data;

	 					// reset form
	 					form.$setPristine();
	 					form.$setUntouched();

	 					var msg = "";
	 	            	if ($scope.authType == 'LDAP')
	 	            		msg += "Active Directory has been successfully saved.";
	 	            	else if ($scope.authType == 'SAML')
	 	            		msg += "Single Sign On has been successfully saved.";
	 					
		 				ToastrService.popupSuccess(msg);
		 				
		 				$uibModalInstance.close();
	 				})
	 				.catch(function(response) {
	 					ErrorService.handleHttpError(response);
	 				}).finally(function(){
	 					$scope.busy = false;
	 				});	
            } else {
            	var msg = "";
            	if ($scope.authType == 'LDAP')
            		msg += "Active Directory cannot be saved because of invalid fields, please check errors.";
            	else if ($scope.authType == 'SAML')
            		msg += "Single Sign On cannot be saved because of invalid fields, please check errors.";
            	else 
            		msg += "Unsupport authentication type!  Type=" + $scope.authType;
                ToastrService.popupError(msg);
            }
        };        
        
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        init();
    }
    
    angular
		.module('pegasus')
		.controller('CompanyLoginPolicyController', ["$scope", "$uibModalInstance", "ToastrService", "ErrorService", "CompanyService", "companyId", CompanyLoginPolicyController]);

	function CompanyLoginPolicyController($scope, $uibModalInstance, ToastrService, ErrorService, CompanyService, companyId) {
		$scope.companyId = companyId;
	    
	    $scope.busy = true;
	    $scope.companyAuthentication = null;
	    
	    function init() {
	        CompanyService.loginPolicy($scope.companyId).then(function(response) {
	            $scope.companyAuthentication = response.data;
	        }).catch(function(response){
	        	ErrorService.handleHttpError(response);
	        }).finally(function(){
	        	$scope.busy = false;
	        });
	    }
	
	    $scope.save = function (form, companyAtuh) {
	    	if (form.$valid) {
	 			var savePromise = CompanyService.updateLoginPolicy($scope.companyId, $scope.companyAuthentication.loginPolicy);		 				
	 			savePromise
	 				.then(function(response) {
	 					$scope.companyAuthentication = response.data;
	
	 					// reset form
	 					form.$setPristine();
	 					form.$setUntouched();
	 					
		 				ToastrService.popupSuccess("Login Policy has been successfully saved.");
	 				
		 				$uibModalInstance.close();
	 				})
	 				.catch(function(response) {
	 					ErrorService.handleHttpError(response);
	 				}).finally(function(){
	 					$scope.busy = false;
	 				});	
	        } else {
	            ToastrService.popupError("Login Policy cannot be saved because of invalid fields, please check errors.");
	        }
	    };        
	    
	    $scope.cancel = function () {
	        $uibModalInstance.dismiss('cancel');
	    };
	
	    init();
	}
	
    angular
		.module('pegasus')
		.controller('CompanyPasswordPolicyController', ["$scope", "$uibModalInstance", "ToastrService", "ErrorService", "CompanyService", "companyId", CompanyPasswordPolicyController]);

	function CompanyPasswordPolicyController($scope, $uibModalInstance, ToastrService, ErrorService, CompanyService, companyId) {
		$scope.companyId = companyId;
	    
	    $scope.busy = true;
	    $scope.companyAuthentication = null;
	    
	    function init() {
	        CompanyService.passwordPolicy($scope.companyId).then(function(response) {
	            $scope.companyAuthentication = response.data;
	        }).catch(function(response){
	        	ErrorService.handleHttpError(response);
	        }).finally(function(){
	        	$scope.busy = false;
	        });
	    }
	
	    $scope.save = function (form, companyAtuh) {
	    	if (form.$valid) {
	 			var savePromise = CompanyService.updatePasswordPolicy($scope.companyId, $scope.companyAuthentication.passwordPolicy);		 				
	 			savePromise
	 				.then(function(response) {
	 					$scope.companyAuthentication = response.data;
	
	 					// reset form
	 					form.$setPristine();
	 					form.$setUntouched();
	 					
		 				ToastrService.popupSuccess("Password Policy has been successfully saved.");
	 				
		 				$uibModalInstance.close();
	 				})
	 				.catch(function(response) {
	 					ErrorService.handleHttpError(response);
	 				}).finally(function(){
	 					$scope.busy = false;
	 				});	
	        } else {
	            ToastrService.popupError("Password Policy cannot be saved because of invalid fields, please check errors.");
	        }
	    };        
	    
	    $scope.cancel = function () {
	        $uibModalInstance.dismiss('cancel');
	    };
	
	    init();
	}
})();