angular.module('pegasus').controller("SubscriptionsController",
	[
	 	"$rootScope", "$scope", "$location", "$state", "SecurityService", "ErrorService", "SubscriptionService",
	 	function ($rootScope, $scope, $location, $state, SecurityService, ErrorService, SubscriptionService) {
	 		$scope.busy = false;
	 		$scope.filtersBusy = false;
	 		$scope.filters = {};
	 		$scope.sm = {
	 			opinion: "none",
	 			searchFilter: {
	 				companyIds: [],
	 				enabled: null,
	 				name: null
	 			}
	 		};

	 		// pagination
	 		$scope.pagination = PaginationUtil.create();

	 		// sorting
	 		$scope.pagination.sort.direction = "ASC"
	 		$scope.pagination.sort.property = "name"; 

	 		// columns
	 		$scope.columnHeaders = [
	            { label: "Subscription", value: "name", sortable: true },
	            { label: "Description", value: "description", sortable: true },
	            { label: "Tenant", value: "companyName", sortable: false },
	            { label: "Start", value: "startDate", sortable: true },
	            { label: "End", value: "endDate", sortable: true },
	            { label: "Active", value: "enabled", sortable: true }
            ];

	 		/**
	 		 * Change the column to sort on and/or toggles ASC and DESC if the column currently set is the same column being passed in
	 		 * @param {String} column 
	 		 * @return void
	 		 */
	 		$scope.sortColumn = function(column) {
	 			$scope.pagination = PaginationUtil.sort($scope.pagination, column);
	 			$scope.find();
	 		};

	 		/**
	 		 * Change the number of items to be displayed and re-queries the last search
	 		 * @param {Number} numberOfItems 
	 		 * @return void
	 		 */
	 		$scope.changeItemsPerPage = function(numberOfItems) {
	 			if ($scope.pagination.itemsPerPage != numberOfItems) {
		 			$scope.pagination = PaginationUtil.changeItemsPerPage($scope.pagination, numberOfItems);
		 			$scope.find();
	 			}
	 		};
	 		
	 		/**
	 		 * Decrease the page index by 1 and re-query the last search
	 		 * @return void
	 		 */
	 		$scope.previousPage = function() {
	 			$scope.pagination = PaginationUtil.previousPage($scope.pagination);
	 			$scope.find();
	 		};

	 		/**
	 		 * Set the page index to the passed in pageNumber and re-query the last search
	 		 * @param {Number} pageNumber
	 		 * @return void
	 		 */
	 		$scope.gotoPage = function(pageNumber) {
	 			$scope.pagination = PaginationUtil.gotoPage($scope.pagination, pageNumber);
	 			$scope.find();
	 		};

	 		/**
	 		 * Increase the page index by 1 and re-query the last search
	 		 * @return void
	 		 */
	 		$scope.nextPage = function() {
	 			$scope.pagination = PaginationUtil.nextPage($scope.pagination);
	 			$scope.find();
	 		};

	 		/**
	 		 * Call the REST Service to retrieve the data based on the searchFilter's properties
	 		 * @return void
	 		 */
	 		$scope.find = function() {
	 			$scope.busy = true;

	 			$scope.sm.searchFilter['pageIndex'] = $scope.pagination.pageIndex;
	 			$scope.sm.searchFilter['itemsPerPage'] = $scope.pagination.itemsPerPage;
	 			$scope.sm.searchFilter['sortField'] = $scope.pagination.sort.property;
	 			$scope.sm.searchFilter['sortDirection'] = $scope.pagination.sort.direction;
	 			
	 			// store in root scope
	 			$rootScope.subscriptionSearchSM = {
	 				opinion: $scope.sm.opinion,
	 				searchFilter: $scope.sm.searchFilter
	 			};
	 			
	 			SubscriptionService.find($scope.sm.searchFilter).then(function(response) {
		 			$scope.pagination = PaginationUtil.update($scope.pagination, response.data.result);
		 			//$state.current.data.pageSubTitle = "as of " + DateUtil.formatDate(new Date());
 				}).catch(function(response) {
	 				ErrorService.handleHttpError(response);
 				}).finally(function() {
 					$scope.busy = false;	 					
 				});	 			
	 		};

	 		$scope.changeOpinion = function(opinion) {
	 			$scope.sm.opinion = opinion;
	 			// store in root scope
	 			$rootScope.subscriptionSearchSM.opinion = opinion;
	 		};
	 		
	 		$scope.toggleCompanyOption = function(option) {
	 			var idx = $scope.sm.searchFilter.companyIds.indexOf(option.id);

	 			if (idx == -1) {
	 				// add
	 				$scope.sm.searchFilter.companyIds.push(option.id);
	 			} else {
	 				// remove
	 				$scope.sm.searchFilter.companyIds.splice(idx, 1);
	 			}
	 			
	 			$scope.pagination.pageIndex = 0;
	 			
	 			$scope.find();
	 		};
	 		
	 		$scope.hasCompanyOption = function(option) {
	 			var result = false;
	 			
	 			for(var i = 0; i < $scope.sm.searchFilter.companyIds.length; i++) {
	 				var existing = $scope.sm.searchFilter.companyIds[i];
	 				if (existing === option.id) {
	 					result = true;
	 					break;
	 				}
	 			}
	 			
	 			return result;
	 		};
	 		
	 		$scope.toggleEnabledOption = function(option) {
	 			$scope.sm.searchFilter.enabled = option.key;
	 			$scope.pagination.pageIndex = 0;
	 			
	 			$scope.find();
	 		};
	 		
	 		$scope.onFilterBlur = function() {
	 			$scope.pagination.pageIndex = 0;
	 			
	 			$scope.find();
	 		};
	 		
	 		/**
	 		 * Navigate user to the create UI
	 		 * @return void
	 		 */
	 		$scope.add = function() {
		 		$state.go("subscription.information", { "subscriptionId" : "new" });
		 	};
		 	
		 	$scope.loadFilters = function() {
		 		$scope.filtersBusy = true;
		 		SubscriptionService.filters().then(function(response){
		 			$scope.filters = response.data;
		 			
		 			if ($rootScope.subscriptionSearchSM) {
		 				applyStateMachine();
		 			} else {
						$scope.sm.searchFilter.companyIds.push($rootScope.user.companyId);
						$scope.sm.searchFilter.enabled = "true";
						$scope.sm.opinion = "list";
		 			}
		 			
		 			$scope.find();
		 		}).catch(function(response){
	 				ErrorService.handleHttpError(response);
		 		}).finally(function(){
		 			$scope.filtersBusy = false;
		 		});		 		
		 	};
		 	
		 	function applyStateMachine() {
				$scope.pagination.pageIndex = $rootScope.subscriptionSearchSM.searchFilter.pageIndex;
				$scope.pagination.itemsPerPage = $rootScope.subscriptionSearchSM.searchFilter.itemsPerPage;
				$scope.pagination.sort.property = $rootScope.subscriptionSearchSM.searchFilter.sortField;
				$scope.pagination.sort.direction = $rootScope.subscriptionSearchSM.searchFilter.sortDirection;
				$scope.sm.searchFilter.companyIds = $rootScope.subscriptionSearchSM.searchFilter.companyIds;
				$scope.sm.searchFilter.enabled = $rootScope.subscriptionSearchSM.searchFilter.enabled;
				$scope.sm.searchFilter.name = $rootScope.subscriptionSearchSM.searchFilter.name;
				$scope.sm.opinion = $rootScope.subscriptionSearchSM.opinion;
		 	}
		 	
		 	function init() {
		 		$scope.loadFilters();
		 	}
		 	
	 		// initialize the UI by call the find method
	 		init();
	 	}
	]
);