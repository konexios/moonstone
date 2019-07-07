(function () {
    'use strict';

    angular
        .module('pegasus')
        .controller('CompanyInformationController', ["$scope", "$state", "$stateParams", "ToastrService", "ErrorService", "CompanyService", CompanyInformationController]);

    function CompanyInformationController($scope, $state, $stateParams, ToastrService, ErrorService, CompanyService) {
        var companyId = $stateParams.companyId;
        
        $scope.busy = true;
 		$scope.parentCompanyOptions = [];
 		$scope.statusOptions = [];
        $scope.company = null;
        
 		$scope.save = function(form, company) {
			form.$setSubmitted();
 			if (form.$valid) {
	 			$scope.busy = true;

	 			var savePromise = null;
	 			if ($scope.company.id && $scope.company.id != "") {
		 			savePromise = CompanyService.updateCompany(company.id, company);		 				
	 			} else {
	 				savePromise = CompanyService.createCompany("new", company);
	 			}
	 			
	 			if (!savePromise)
	 				return;
		
 	 			savePromise
 	 				.then(function(response) {
		 				$scope.company = response.data;

			 			// reset form
		 				form.$setPristine();
						form.$setUntouched();

						// update menu item name
						setMenuItemName($scope.company);
			 			
			 			ToastrService.popupSuccess($scope.company.name + " has been successfully saved.");
                        $state.go("company.information", { "companyId" : $scope.company.id });
 	 				})
 	 				.catch(function(response) {
 	 					ErrorService.handleHttpError(response);
 	 				}).finally(function(){
 	 					$scope.busy = false;
 	 				});		 				
 			} else {
 				ToastrService.popupError("The form is invalid! Please make changes and try again.");
 			}
 		};

        $scope.hasAccess = function() {
            return (($scope.company === null || !$scope.company.id) && ($scope.canCreateCompany())) ||
                ($scope.company !== null && $scope.company.hasOwnProperty('id')
					&& $scope.company.id && ($scope.canUpdateCompany()));
		};
		
		// set\update company menu item name
		function setMenuItemName(company) {
			$scope.$parent.companyNameMI = company.name;
		}
        
        function init() {
            CompanyService.company(companyId).then(function(response) {
 				$scope.company = response.data.company;
 				$scope.parentCompanyOptions = response.data.parentCompanyOptions;
				$scope.statusOptions = response.data.statusOptions;
				setMenuItemName($scope.company);
            }).catch(function(response){
            	ErrorService.handleHttpError(response);
            }).finally(function(){
            	$scope.busy = false;
            });
        }
        
        init();
    }

})();
