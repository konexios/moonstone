angular.module('pegasus').controller("ApplicationController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", '$uibModal', "SecurityService", "ErrorService", "ToastrService", "ApplicationEngineService", "ZoneService", "CompanyService", "ProductService", "SubscriptionService", "ApplicationService",
	 	function ($rootScope, $scope, $stateParams, $location, $state, $uibModal, SecurityService, ErrorService, ToastrService, ApplicationEngineService, ZoneService, CompanyService, ProductService, SubscriptionService, ApplicationService) {

			$scope.entityName = "Application";
			$scope.busy = true;
			$scope.loadingSubscriptions = false;
	 		$scope.loadingZones = false;
	 		$scope.loadingProductExtensions = false;
	 		$scope.loadingProductFeatures = false;

	 		$scope.companyOptions = [];
	 		$scope.parentProductOptions = [];
	 		$scope.productExtensionOptions = [];
	 		$scope.productFeatureOptions = [];
	 		$scope.subscriptionOptions = [];
	 		$scope.applicationEngineOptions = [];
	 		$scope.apiSigningRequiredOptions = [];
	 		$scope.regionOptions = [];
	 		$scope.zoneOptions = [];
	 		$scope.application = {};

            $scope.categoryOptions = [];
            $scope.dataTypeOptions = [];

	 		if ($stateParams.applicationId) {
	 			$scope.applicationId = $stateParams.applicationId;
	 			$scope.busy = true;
		 		ApplicationService.get($stateParams.applicationId)
		 			.then(function(response) {
		 				$scope.application = response.data.application;
		 				$scope.companyOptions = response.data.companyOptions;
		 				$scope.parentProductOptions = response.data.parentProductOptions;
		 				$scope.productExtensionOptions = response.data.productExtensionOptions;
		 				$scope.productFeatureOptions = response.data.productFeatureOptions;
		 				$scope.subscriptionOptions = response.data.subscriptionOptions;
		 				$scope.applicationEngineOptions = response.data.applicationEngineOptions;
		 				$scope.apiSigningRequiredOptions = response.data.apiSigningRequiredOptions;
		 		 		$scope.regionOptions = response.data.regionOptions;
		 		 		$scope.zoneOptions = response.data.zoneOptions;

                        $scope.categoryOptions = response.data.categoryOptions;
						$scope.dataTypeOptions = response.data.dataTypeOptions;
						
						setMenuItemName($scope.application);
		 		 		
		 				$scope.busy = false;
		 			})
		 			.catch(function(response) {
		 				$scope.busy = false;
		 				ErrorService.handleHttpError(response);
		 			});	 			
	 		} else {
	 			$scope.busy = false;
	 		}

	 		$scope.onCompanyChange = function() {
	 			$scope.application.subscriptionId = null;
	 			
	 			var companyId = $scope.application.companyId;
	 			
	 			if (companyId != undefined && companyId != null && companyId != '') {
	 				loadSubscriptions(companyId);
	 			} else
	 				$scope.subscriptionOptions = [];
	 		};

	 		function loadSubscriptions(companyId) {
	 			$scope.loadingSubscriptions = true;
	 			$scope.subscriptionOptions = [];
		 		ApplicationService.subscriptions(companyId)
		 			.then(function(response) {
		 				$scope.subscriptionOptions = response.data;
		 				$scope.loadingSubscriptions = false;
		 			})
		 			.catch(function(response) {
		 				$scope.loadingSubscriptions = false;
		 				ErrorService.handleHttpError(response);
		 			});
	 		};
	 		
	 		$scope.onProductChange = function() {
	 			$scope.application.applicationEngineId = null;
	 			
	 			var productId = $scope.application.productId;
	 			var zoneId = $scope.application.zoneId;
	 			
	 			if (productId != undefined && productId != null && productId != '') {
	 				loadProductExtensions(productId);
	 				loadProductFeatures(productId);
	 				loadApplicationEngines(productId, zoneId);
	 			} else
	 				$scope.productExtensionOptions = [];
	 		};

	 		function loadProductExtensions(parentProductId) {
	 			$scope.loadingProductExtensions = true;
	 			$scope.productExtensionOptions = [];
		 		ApplicationService.productExtensions(parentProductId)
		 			.then(function(response) {
		 				$scope.productExtensionOptions = response.data;
		 				$scope.loadingProductExtensions = false;
		 			})
		 			.catch(function(response) {
		 				$scope.loadingProductExtensions = false;
		 				ErrorService.handleHttpError(response);
		 			});
	 		};
	 		
	 		function loadProductFeatures(productId) {
	 			$scope.loadingProductFeatures = true;
	 			$scope.productFeatureOptions = [];
		 		ApplicationService.productFeatures(productId)
		 			.then(function(response) {
		 				$scope.productFeatureOptions = response.data;
		 				$scope.loadingProductExtensions = false;
		 			})
		 			.catch(function(response) {
		 				$scope.loadingProductFeatures = false;
		 				ErrorService.handleHttpError(response);
		 			});
	 		};
	 		
	 		$scope.onRegionChange = function() {
	 			$scope.application.zoneId = null;
	 			$scope.application.applicationEngineId = null;
	 			
	 			var regionId = $scope.application.regionId;
	 			
	 			if (regionId != undefined && regionId != null && regionId != '')
	 				loadZones(regionId);
	 			else
	 				$scope.zoneOptions = [];
	 		};
	 		
	 		function loadZones(regionId) {
	 			$scope.loadingZones = true;
	 			$scope.zoneOptions = [];
		 		ZoneService.optionsByRegion(regionId, true)
		 			.then(function(response) {
		 				$scope.zoneOptions = response.data;
		 				$scope.loadingZones = false;
		 			})
		 			.catch(function(response) {
		 				$scope.loadingZones = false;
		 				ErrorService.handleHttpError(response);
		 			});
	 		}
	 		
	 		$scope.onZoneChange = function() {
	 			$scope.application.applicationEngineId = null;
	 			
	 			var productId = $scope.application.productId;
	 			var zoneId = $scope.application.zoneId;
	 			
 				loadApplicationEngines(productId, zoneId);
	 		};
	 		
	 		function loadApplicationEngines(productId, zoneId) {
	 			$scope.applicationEngineOptions = [];
		 		
	 			if (productId != undefined 
	 					&& productId != null 
	 					&& productId != '' 
	 					&& zoneId != undefined 
	 					&& zoneId != null 
	 					&& zoneId != '') {
		 			$scope.loadingEngines = true;
		 			ApplicationEngineService.optionsByProductAndZone(productId, zoneId)
			 			.then(function(response) {
			 				$scope.applicationEngineOptions = response.data;
			 				$scope.loadingEngines = false;
			 			})
			 			.catch(function(response) {
			 				$scope.loadingEngines = false;
			 				ErrorService.handleHttpError(response);
			 			});
	 			}
			 }
			 
			 // set\update application menu item name
			function setMenuItemName(application) {
				$scope.$parent.applicationNameMI = application.name;
			}
	 		
	 		$scope.save = function(form, application) {
				form.$setSubmitted();
	 			if (form.$valid) {
		 			$scope.busy = true;

					 var savePromise = null,
					 	 isEditDialog = !!($scope.application.id && $scope.application.id != "");
		 			if (isEditDialog) {
			 			savePromise = ApplicationService.update(application);		 				
		 			} else {
		 				savePromise = ApplicationService.create(application);
		 			}
		 			
		 			if (!savePromise)
		 				return;
		 			
	 	 			savePromise
	 	 				.then(function(response) {
							$scope.application = response.data;
							  
							setMenuItemName($scope.application);
	
							form.$setPristine();
							form.$setUntouched();
				 			
							ToastrService.popupSuccess($scope.application.name + " has been successfully saved.");

							$state.go("application.information", { "applicationId" : $scope.application.id });
	 	 				})
	 	 				.catch(function(response) {
	 	 					ErrorService.handleHttpError(response);
	 	 				}).finally(function() {
							$scope.busy = false;
						});
	 			} else {
	 				ToastrService.popupError("The form is invalid! Please make changes and try again.");
	 			}
	 		};
	 		
	 		$scope.openVaultLogin = function() {

				var modalInstance = $uibModal.open({
					animation: true,
					templateUrl: 'partials/modals/vaultLogin.html',
					controller: 'VaultLoginController',
					size: 'lg'
				});

				modalInstance.result
					.then(function (adminToken) {
						console.log("adminToken: " + adminToken);
						
			 			$scope.busy = true;

			 			ApplicationService.createVaultLogin($scope.application.id, adminToken)
		 	 				.then(function(response) {
		 	 					$scope.application = response.data;
					 			$scope.busy = false;
					 			
					 			ToastrService.popupSuccess("Vault Login has been successfully created for " + $scope.application.name + ".");
		 	 				})
		 	 				.catch(function(response) {
		 	 					$scope.busy = false;
		 	 					ErrorService.handleHttpError(response);
		 	 					
		 	 					ToastrService.popupError("Unable to create Vault Login! Message: " + response.data.message + " Status: " + response.data.status);
		 	 				});
					});
	 		};

            $scope.hasAccess = function() {
                return (!$scope.application.id && ($scope.canCreateApplication() || $scope.isSuperAdministrator())) ||
                    ($scope.application.id && ($scope.canUpdateApplication() || $scope.isSuperAdministrator()));
            };
	 	}
	]
);

angular.module('pegasus').controller('VaultLoginController', [
	'$scope', '$uibModalInstance',
	function ($scope, $uibModalInstance) {
		
		$scope.ok = function (form) {
			
			if (form.$valid) {
				$uibModalInstance.close($scope.adminToken);
			} else {
				// invalid form...
			}
		};

		$scope.cancel = function () {
			$uibModalInstance.dismiss('cancel');
	  		};
	  	}
]);