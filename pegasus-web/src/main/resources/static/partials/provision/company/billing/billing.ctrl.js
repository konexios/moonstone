(function () {
    'use strict';

    angular
        .module('pegasus')
        .controller('CompanyBillingController', ["$scope", "$stateParams", "ToastrService", "ErrorService", "CompanyService", CompanyBillingController]);

    function CompanyBillingController($scope, $stateParams, ToastrService, ErrorService, CompanyService) {
        var companyId = $stateParams.companyId;
        
        $scope.busy = true;
        $scope.companyBilling = null;
        
 		$scope.save = function(form, companyBilling) {
 			if (form.$valid) {
	 			$scope.busy = true;

	 			var savePromise = CompanyService.updateBilling(companyBilling);		 				
 	 			savePromise
 	 				.then(function(response) {
 	 					$scope.companyBilling = response.data;

			 			// reset form
		 				form.$setPristine();
		 				form.$setUntouched();
			 			
			 			ToastrService.popupSuccess("Billing information has been successfully saved.");
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
        
        function init() {
            CompanyService.billing(companyId).then(function(response) {
                $scope.companyBilling = response.data;
            }).catch(function(response){
            	ErrorService.handleHttpError(response);
            }).finally(function(){
            	$scope.busy = false;
            });
        }
        
        init();
    }

})();
