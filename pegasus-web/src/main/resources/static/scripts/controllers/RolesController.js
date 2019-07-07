angular.module('pegasus').controller("RolesController",
	[
	 	"$rootScope", "$scope", "$location", "$state", "SecurityService", "ErrorService", "RoleService",
	 	function ($rootScope, $scope, $location, $state, SecurityService, ErrorService, RoleService) {
	 		$scope.busy = false;
	 		$scope.filtersBusy = false;
	 		$scope.filters = {};
	 		$scope.sm = {
	 			opinion: "none",
	 			searchFilter: {
	 				productIds: [],
	 				applicationIds: [],
	 				companyIds: [],
	 				enabled: null,
	 				editable: null,
	 				name: null
	 			}
	 		};
	 		
	 		// pagination
	 		$scope.pagination = PaginationUtil.create();

	 		// sorting
	 		$scope.pagination.sort.direction = "ASC";
	 		$scope.pagination.sort.property = "name"; 

	 		// columns
	 		$scope.columnHeaders = [
	            { label: "Role", value: "name", sortable: true },
	            { label: "Description", value: "description", sortable: true },
	            { label: "Product", value: "productName", sortable: false },
	            { label: "Application", value: "applicationName", sortable: false },
	            { label: "Tenant", value: "companyName", sortable: false },
	            { label: "Enabled", value: "enabled", sortable: true }
            ];
	 		
	 		if (SecurityService.isSuperAdministrator()) {
	 			$scope.columnHeaders.push({ label: "Editable", value: "editable", sortable: true });
	 			$scope.columnHeaders.push({ label: "Private", value: "hidden", sortable: true });
	 		}

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
	 			$rootScope.roleSearchSM = {
	 				opinion: $scope.sm.opinion,
	 				searchFilter: $scope.sm.searchFilter
	 			};

	 			RoleService.find($scope.sm.searchFilter)
 				.then(function(response) {
		 			$scope.pagination = PaginationUtil.update($scope.pagination, response.data.result);
		 			//$state.current.data.pageSubTitle = "as of " + DateUtil.formatDate(new Date());
 				})
 				.catch(function(response) {
	 				ErrorService.handleHttpError(response);
 				}).finally(function() {
 					$scope.busy = false; 					
 				});
	 		};
	 		
	 		$scope.changeOpinion = function(opinion) {
	 			$scope.sm.opinion = opinion;
	 			// store in root scope
	 			$rootScope.roleSearchSM.opinion = opinion;
	 		};

	 		$scope.toggleProductOption = function(option) {
	 			$scope.toggleFilterOption($scope.sm.searchFilter.productIds, option);
	 		};
	 		
	 		$scope.hasProductOption = function(option) {
	 			return $scope.hasFilterOption($scope.sm.searchFilter.productIds, option); 
	 		};
	 		
	 		$scope.toggleCompanyOption = function(option) {
	 			$scope.toggleFilterOption($scope.sm.searchFilter.companyIds, option);
	 		};
	 		
	 		$scope.hasCompanyOption = function(option) {
	 			return $scope.hasFilterOption($scope.sm.searchFilter.companyIds, option); 
	 		};
	 		
	 		$scope.toggleApplicationOption = function(option) {
	 			$scope.toggleFilterOption($scope.sm.searchFilter.applicationIds, option);
	 		};
	 		
	 		$scope.hasApplicationOption = function(option) {
	 			return $scope.hasFilterOption($scope.sm.searchFilter.applicationIds, option); 
	 		};
	 		
	 		$scope.toggleFilterOption = function(filterOptions, option) {
	 			var idx = filterOptions.indexOf(option.id);

	 			if (idx == -1) {
	 				// add
	 				filterOptions.push(option.id);
	 			} else {
	 				// remove
	 				filterOptions.splice(idx, 1);
	 			}
	 			
	 			$scope.pagination.pageIndex = 0;
	 			
	 			$scope.find();
	 		};
	 		
	 		$scope.hasFilterOption = function(filterOptions, option) {
	 			var result = false;
	 			
	 			for(var i = 0; i < filterOptions.length; i++) {
	 				var existing = filterOptions[i];
	 				if (existing === option.id) {
	 					result = true;
	 					break;
	 				}
	 			}
	 			
	 			return result;
	 		};
	 		
	 		$scope.toggleEnabledOption = function(option) {
	 			$scope.toggleRadioOption($scope.sm.searchFilter.enabled, option);
	 		};
	 		
	 		$scope.toggleEditableOption = function(option) {
	 			$scope.toggleRadioOption($scope.sm.searchFilter.editable, option);
	 		};
	 		
	 		$scope.toggleRadioOption = function(filterOption, option) {
	 			filterOption = option.key;
	 			$scope.pagination.pageIndex = 0;
	 			
	 			$scope.find();
	 		};
	 		
	 		$scope.onFilterBlur = function() {
	 			$scope.pagination.pageIndex = 0;
	 			
	 			$scope.find();
	 		};
	 		
	 		/**
	 		 * Navigate user to the company UI to create a new Company
	 		 * @return void
	 		 */
	 		$scope.add = function() {
		 		$state.go("role", { "roleId" : "new" });
		 	};
		 	
		 	$scope.loadFilters = function() {
		 		$scope.filtersBusy = true;
		 		RoleService.filters().then(function(response){
		 			$scope.filters = response.data;
		 			
		 			if ($rootScope.roleSearchSM) {
		 				applyStateMachine();
		 			} else {
						$scope.sm.searchFilter.companyIds.push($rootScope.user.companyId);
						$scope.sm.searchFilter.enabled = "true";
						$scope.sm.searchFilter.editable = "all";
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
				$scope.pagination.pageIndex = $rootScope.roleSearchSM.searchFilter.pageIndex;
				$scope.pagination.itemsPerPage = $rootScope.roleSearchSM.searchFilter.itemsPerPage;
				$scope.pagination.sort.property = $rootScope.roleSearchSM.searchFilter.sortField;
				$scope.pagination.sort.direction = $rootScope.roleSearchSM.searchFilter.sortDirection;
				$scope.sm.searchFilter.productIds = $rootScope.roleSearchSM.searchFilter.productIds;
				$scope.sm.searchFilter.applicationIds = $rootScope.roleSearchSM.searchFilter.applicationIds;
				$scope.sm.searchFilter.companyIds = $rootScope.roleSearchSM.searchFilter.companyIds;
				$scope.sm.searchFilter.enabled = $rootScope.roleSearchSM.searchFilter.enabled;
				$scope.sm.searchFilter.editable = $rootScope.roleSearchSM.searchFilter.editable;
				$scope.sm.searchFilter.name = $rootScope.roleSearchSM.searchFilter.name;
				$scope.sm.opinion = $rootScope.roleSearchSM.opinion;
		 	}

		 	function init() {
		 		$scope.loadFilters();
		 	}
		 	
	 		// initialize the UI by call the find method
	 		init();
	 	}
	]
);