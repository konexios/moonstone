angular.module('pegasus').controller("ZoneController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", "SecurityService", "ErrorService", "ToastrService", "ZoneService",
	 	function ($rootScope, $scope, $stateParams, $location, $state, SecurityService, ErrorService, ToastrService, ZoneService) {
	 		$scope.busy = true;
	 		
	 		$scope.zone = {};
	 		$scope.regionOptions = [];
	 		
	 		if ($stateParams.zoneId) {
	 			$scope.busy = true;
 			
		 		ZoneService.get($stateParams.zoneId)
		 			.then(function(response) {
		 				$scope.zone = response.data.zone;
		 				$scope.regionOptions = response.data.regionOptions;

		 				$scope.busy = false;
		 			})
		 			.catch(function(response) {
		 				$scope.busy = false;
		 				ErrorService.handleHttpError(response);
		 			});	 			
	 		}
	 		
	 		$scope.save = function(form, zone) {
				form.$setSubmitted();
	 			if (form.$valid) {
		 			$scope.busy = true;

		 			var savePromise = null;
		 			if ($scope.zone.id && $scope.zone.id != "") {
			 			savePromise = ZoneService.update(zone);		 				
		 			} else {
		 				savePromise = ZoneService.create(zone);
		 			}
		 			
		 			if (!savePromise)
		 				return;
		 			
	 	 			savePromise
	 	 				.then(function(response) {
	 	 					$scope.zone = response.data;
	
				 			// reset form
			 				form.$setPristine();
			 				form.$setUntouched();
				 			
				 			$scope.busy = false;
				 			
				 			ToastrService.popupSuccess($scope.zone.name + " has been successfully saved.");
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
                return (!$scope.zone.id && ($scope.canCreateZone() || $scope.isSuperAdministrator())) ||
                    ($scope.zone.id && ($scope.canUpdateZone() || $scope.isSuperAdministrator()));
            };
	 	}
	]
);