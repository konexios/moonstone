angular.module('pegasus').controller("ProductController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", "SecurityService", "ErrorService", "ToastrService", "ProductService",
	 	function ($rootScope, $scope, $stateParams, $location, $state, SecurityService, ErrorService, ToastrService, ProductService) {
            $scope.entityName = "Product";
			$scope.busy = true;

	 		// data
	 		$scope.productOptions = [];
	 		$scope.product = {};
	 		$scope.categoryOptions = [];
	 		$scope.dataTypeOptions = [];

	 		if ($stateParams.productId) {
				$scope.busy = true;
				$scope.productId = $stateParams.productId;
		 		ProductService.get($stateParams.productId)
		 			.then(function(response) {
		 				$scope.productOptions = response.data.productOptions;
		 				$scope.product = response.data.product;
		 				$scope.categoryOptions = response.data.categoryOptions;
		 				$scope.dataTypeOptions = response.data.dataTypeOptions;
						$scope.busy = false;
						setMenuItemName($scope.product);
		 			})
		 			.catch(function(response) {
		 				$scope.busy = false;
		 				ErrorService.handleHttpError(response);
		 			});	 			
	 		}
	 		
	 		$scope.addFeature = function() {
	 			var feature = {
	 				name: null,
	 				description: null,
	 				systemName: null
	 			};
	 			$scope.product.features.push(feature);
	 		};
	 		
	 		$scope.removeFeature = function(index) {
	 			if (index >= 0)
	 				$scope.product.features.splice(index, 1);
	 		};

	 		$scope.save = function(form, product) {
				form.$setSubmitted();
	 			if (form.$valid) {
		 			$scope.busy = true;

		 			var savePromise = null;
		 			if ($scope.product.id && $scope.product.id != "") {
			 			savePromise = ProductService.update(product);		 				
		 			} else {
		 				savePromise = ProductService.create(product);
		 			}
		 			
		 			if (!savePromise)
		 				return;
		 			
	 	 			savePromise
	 	 				.then(function(response) {
	 	 					$scope.product = response.data;
	
				 			// reset form
			 				form.$setPristine();
			 				form.$setUntouched();
				 			
				 			$scope.busy = false;
							setMenuItemName($scope.product);
							 
							ToastrService.popupSuccess($scope.product.name + " has been successfully saved.");
							 
							$state.go("product.information", { "productId" : $scope.product.id });
	 	 				})
	 	 				.catch(function(response) {
	 	 					$scope.busy = false;
	 	 					ErrorService.handleHttpError(response);
	 	 				});		 				
	 			} else {
	 				ToastrService.popupError("The form is invalid! Please make changes and try again.");
	 			}
			 };
			 
			 // set\update product menu item name
			function setMenuItemName(product) {
				$scope.$parent.productNameMI = product.name;
			}

            $scope.hasAccess = function() {
                return (!$scope.product.id && ($scope.canCreateProduct() || $scope.isSuperAdministrator())) ||
                    ($scope.product.id && ($scope.canUpdateProduct() || $scope.isSuperAdministrator()));
            };
	 	}
	]
);