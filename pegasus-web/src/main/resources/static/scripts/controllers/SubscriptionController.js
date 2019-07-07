angular.module('pegasus').controller("SubscriptionController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state", "SecurityService", "ErrorService", "ToastrService", "CompanyService", "SubscriptionService",
	 	function ($rootScope, $scope, $stateParams, $location, $state, SecurityService, ErrorService, ToastrService, CompanyService, SubscriptionService) {
			$scope.busy = true;
	 		$scope.companyOptions = [];
	 		$scope.subscription = {};
	 		
	 		if ($stateParams.subscriptionId) {
                $scope.subscriptionId = $stateParams.subscriptionId;
	 			$scope.busy = true;
		 		SubscriptionService.get($stateParams.subscriptionId)
		 			.then(function(response) {
						setSubscription(response.data.subscription);
		 				$scope.companyOptions = response.data.companyOptions;
						$scope.busy = false;
		 			})
		 			.catch(function(response) {
		 				$scope.busy = false;
		 				ErrorService.handleHttpError(response);
		 			});	 			
			 }
			 
			function setSubscription(value) {
				$scope.subscription = value;
				$scope.subscription.startDate = deserializeDate($scope.subscription.startDate);
				$scope.subscription.endDate = deserializeDate($scope.subscription.endDate);
				// set subscription menu item name
				$scope.$parent.subscriptionNameMI = $scope.subscription.name;
			}
			 
			function deserializeDate(value) {
				if (!value || value === '') {
					return value;
				}

				var date = value.split('-');
				
				return new Date(date[0], date[1] - 1, date[2]);
			}
			
	 		function serializeDate(value) {
	 			var nValue = new Date(value);
	 			
	 			var month = nValue.getMonth() + 1;
	 			var date = nValue.getDate();
	 			var year = nValue.getFullYear();
	 			
	 			if (month < 10)
	 				month = "0" + month;
	 			
	 			if (date < 10)
	 				date = "0" + date;
	 			
	 			return year + "-" + month + "-" + date;
	 		}
	 		
	 		$scope.save = function(form, subscription) {
				form.$setSubmitted();
	 			if (form.$valid) {
		 			$scope.busy = true;

					var subscriptionToSave = angular.copy(subscription);
		 			subscriptionToSave.startDate = serializeDate(subscriptionToSave.startDate);
					subscriptionToSave.endDate = serializeDate(subscriptionToSave.endDate);
		 			
		 			var savePromise = null;
		 			if (subscriptionToSave.id && subscriptionToSave.id != "") {
			 			savePromise = SubscriptionService.update(subscriptionToSave);		 				
		 			} else {
		 				savePromise = SubscriptionService.create(subscriptionToSave);
		 			}
		 			
		 			if (!savePromise)
		 				return;
		 			
	 	 			savePromise
	 	 				.then(function(response) {
							setSubscription(response.data);
	
				 			// reset form
			 				form.$setPristine();
			 				form.$setUntouched();
				 			
				 			$scope.busy = false;
				 			
							ToastrService.popupSuccess($scope.subscription.name + " has been successfully saved.");
							 
							$state.go("subscription.information", { "subscriptionId" : $scope.subscription.id });
	 	 				})
	 	 				.catch(function(response) {
	 	 					$scope.busy = false;
	 	 					ErrorService.handleHttpError(response);
	 	 				});		 				
	 			} else {
	 				ToastrService.popupError("The form is invalid! Please make changes and try again.");
	 			}
	 		};

			$scope.startDateOnSetTime = function() {
                $scope.$broadcast('start-date-changed');
            };

            $scope.endDateOnSetTime = function() {
                $scope.$broadcast('end-date-changed');
            };

            $scope.startDateBeforeRender = function($dates) {
                if ($scope.subscription.endDate) {
                    var activeDate = moment($scope.subscription.endDate);

                    $dates
                        .filter(function(date) {
                            return date.localDateValue() >= activeDate.valueOf();
                        })
                        .forEach(function(date) {
                            date.selectable = false;
                        });
                }
            };

            $scope.endDateBeforeRender = function($view, $dates) {
                if ($scope.subscription.startDate) {
                    var activeDate = moment($scope.subscription.startDate).subtract(1, $view).add(1, 'minute');

                    $dates
                        .filter(function(date) {
                            return date.localDateValue() <= activeDate.valueOf();
                        })
                        .forEach(function(date) {
                            date.selectable = false;
                        });
                }
            };

            $scope.hasAccess = function() {
                return (!$scope.subscription.id && ($scope.canCreateSubscription() || $scope.isSuperAdministrator())) ||
                    ($scope.subscription.id && ($scope.canUpdateSubscription() || $scope.isSuperAdministrator()));
            };
	 	}
	]
);