angular.module('pegasus').controller("RegionController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", "SecurityService", "ErrorService", "ToastrService", "RegionService",
	 	function ($rootScope, $scope, $stateParams, $location, $state, SecurityService, ErrorService, ToastrService, RegionService) {
	 		$scope.busy = true;
	 		
	 		$scope.region = {};
	 		
	 		if ($stateParams.regionId) {
	 			$scope.busy = true;
 			
		 		RegionService.get($stateParams.regionId)
		 			.then(function(response) {
		 				$scope.region = response.data.region;
		 				$scope.busy = false;
		 			})
		 			.catch(function(response) {
		 				$scope.busy = false;
		 				ErrorService.handleHttpError(response);
		 			});	 			
	 		}
	 		
	 		$scope.save = function(form, region) {
				form.$setSubmitted();
	 			if (form.$valid) {
		 			$scope.busy = true;

		 			var savePromise = null;
		 			if ($scope.region.id && $scope.region.id != "") {
			 			savePromise = RegionService.update(region);		 				
		 			} else {
		 				savePromise = RegionService.create(region);
		 			}
		 			
		 			if (!savePromise)
		 				return;
		 			
	 	 			savePromise
	 	 				.then(function(response) {
	 	 					$scope.region = response.data;
	
				 			// reset form
			 				form.$setPristine();
			 				form.$setUntouched();
				 			
				 			$scope.busy = false;
				 			
				 			ToastrService.popupSuccess($scope.region.name + " has been successfully saved.");
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
                return (!$scope.region.id && ($scope.canCreateRegion() || $scope.isSuperAdministrator())) ||
                    ($scope.region.id && ($scope.canUpdateRegion() || $scope.isSuperAdministrator()));
            };
	 	}
	]
);