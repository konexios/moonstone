angular.module('pegasus').controller("ZonesController",
	[
	 	"$rootScope", "$scope", "$location", "$state", "SecurityService", "ErrorService", "ZoneService",
	 	function ($rootScope, $scope, $location, $state, SecurityService, ErrorService, ZoneService) {
	 		$scope.busy = false;
	 		$scope.filtersBusy = false;
	 		$scope.filters = {};
	 		$scope.sm = {
	 			opinion: "none",
	 			searchFilter: {
	 				
	 			}
	 		};
	 		
	 		// pagination
	 		$scope.pagination = PaginationUtil.create();

	 		// sorting
	 		$scope.pagination.sort.direction = "ASC"
	 		$scope.pagination.sort.property = "name"; 

	 		// columns
	 		$scope.columnHeaders = [
	            { label: "Zone", value: "name", sortable: true },
	            { label: "Description", value: "description", sortable: true },
	            { label: "Region", value: "regionName", sortable: false },
	            { label: "System Name", value: "systemName", sortable: true },
	            { label: "Private", value: "hidden", sortable: true },
	            { label: "Enabled", value: "enabled", sortable: true }
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
	 			$rootScope.zoneSearchSM = {
	 				opinion: $scope.sm.opinion,
	 				searchFilter: $scope.sm.searchFilter
	 			};

	 			ZoneService.find($scope.sm.searchFilter).then(function(response) {
		 			$scope.pagination = PaginationUtil.update($scope.pagination, response.data.result);
		 			// $state.current.data.pageSubTitle = "as of " + DateUtil.formatDate(new Date());
 				}).catch(function(response) {
	 				ErrorService.handleHttpError(response);
 				}).finally(function() {
 					$scope.busy = false;	 					
 				});
	 		};

	 		$scope.changeOpinion = function(opinion) {
	 			$scope.sm.opinion = opinion;
	 			// store in root scope
	 			$rootScope.zoneSearchSM.opinion = opinion;
	 		};
	 		
	 		/**
	 		 * Navigate user to the company UI to create a new Company
	 		 * @return void
	 		 */
	 		$scope.add = function() {
		 		$state.go("zone", { "zoneId" : "new" });
		 	};
		 	
		 	$scope.loadFilters = function() {
		 		$scope.filtersBusy = true;

		 		if ($rootScope.zoneSearchSM) {
		 			applyStateMachine();
		 		} else {
					$scope.sm.opinion = "list";		 			
		 		}
				
				$scope.filtersBusy = false;
				
				$scope.find();
		 	};
		 	
		 	function applyStateMachine() {
				$scope.pagination.pageIndex = $rootScope.zoneSearchSM.searchFilter.pageIndex;
				$scope.pagination.itemsPerPage = $rootScope.zoneSearchSM.searchFilter.itemsPerPage;
				$scope.pagination.sort.property = $rootScope.zoneSearchSM.searchFilter.sortField;
				$scope.pagination.sort.direction = $rootScope.zoneSearchSM.searchFilter.sortDirection;
				$scope.sm.opinion = $rootScope.zoneSearchSM.opinion;
		 	}

		 	function init() {
		 		$scope.loadFilters();
		 	}
		 	
	 		// initialize the UI by call the find method
	 		init();
	 	}
	]
);