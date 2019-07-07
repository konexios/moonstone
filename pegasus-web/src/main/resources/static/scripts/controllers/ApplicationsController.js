angular.module('pegasus').controller("ApplicationsController",
	[
	 	"$rootScope", "$scope", "$location", "$state", "SecurityService", "ErrorService", "ApplicationService",
	 	function ($rootScope, $scope, $location, $state, SecurityService, ErrorService, ApplicationService) {
	 		$scope.busy = false;
	 		$scope.filtersBusy = false;
	 		$scope.filters = {};
	 		$scope.sm = {
	 			opinion: "none",
	 			searchFilter: {
	 				companyIds: [],
	 				subscriptionIds: [],
	 				regionIds: [],
	 				zoneIds: [],
	 				productIds: [],
	 				apiSigningRequired: [],
	 				enabled: null,
	 				name: null,
	 				code: null
	 			}
	 		};
	 		
	 		// pagination
	 		$scope.pagination = PaginationUtil.create();

	 		// sorting
	 		$scope.pagination.sort.direction = "ASC"
	 		$scope.pagination.sort.property = "name"; 

	 		// columns
	 		$scope.columnHeaders = [
	            { label: "Application", value: "name", sortable: true },
	            { label: "Description", value: "description", sortable: true },
	            { label: "Code", value: "code", sortable: true },
	            { label: "Product", value: "productName", sortable: false },
	            { label: "Extensions", value: "productExtensionNames", sortable: false },
	            { label: "Features", value: "productFeatureNames", sortable: false },
	            { label: "Tenant", value: "companyName", sortable: false },
	            { label: "Active", value: "enabled", sortable: true },
	            { label: "Region / Zone", value: "regionName|zoneName", sortable: false },
	            { label: "Api Signing", value: "apiSigningRequired", sortable: true },
	            { label: "Pending Vault", value: "pendingVaultLogin", sortable: false }
            ];

	 		/**
	 		 * Change the column to sort on and/or toggles ASC and DESC if the 
	 		 * column currently set is the same column being passed in
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
	 			$rootScope.applicationSearchSM = {
	 				opinion: $scope.sm.opinion,
	 				searchFilter: $scope.sm.searchFilter
	 			};

	 			ApplicationService.find($scope.sm.searchFilter).then(function(response) {
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
	 			$rootScope.applicationSearchSM.opinion = opinion;
	 		};
	 		
	 		$scope.toggleCompanyOption = function(option) {
	 			$scope.toggleFilterOption($scope.sm.searchFilter.companyIds, option);
	 		};
	 		
	 		$scope.hasCompanyOption = function(option) {
	 			return $scope.hasFilterOption($scope.sm.searchFilter.companyIds, option); 
	 		};
	 		
	 		$scope.toggleSubscriptionOption = function(option) {
	 			$scope.toggleFilterOption($scope.sm.searchFilter.subscriptionIds, option);
	 		};
	 		
	 		$scope.hasSubscriptionOption = function(option) {
	 			return $scope.hasFilterOption($scope.sm.searchFilter.subscriptionIds, option); 
	 		};
	 		
	 		$scope.toggleProductOption = function(option) {
	 			$scope.toggleFilterOption($scope.sm.searchFilter.productIds, option);
	 		};
	 		
	 		$scope.hasProductOption = function(option) {
	 			return $scope.hasFilterOption($scope.sm.searchFilter.productIds, option); 
	 		};
	 		
	 		$scope.toggleApiSigningRequiredOption = function(option) {
	 			var idx = $scope.sm.searchFilter.apiSigningRequired.indexOf(option);

	 			if (idx == -1) {
	 				// add
	 				$scope.sm.searchFilter.apiSigningRequired.push(option);
	 			} else {
	 				// remove
	 				$scope.sm.searchFilter.apiSigningRequired.splice(idx, 1);
	 			}
	 			
	 			$scope.pagination.pageIndex = 0;
	 			
	 			$scope.find();
	 		};
	 		
	 		$scope.hasApiSigningRequiredOption = function(option) {
	 			var result = false;
	 			
	 			for(var i = 0; i < $scope.sm.searchFilter.apiSigningRequired.length; i++) {
	 				var existing = $scope.sm.searchFilter.apiSigningRequired[i];
	 				if (existing === option) {
	 					result = true;
	 					break;
	 				}
	 			}
	 			
	 			return result;
	 		};
	 		
	 		$scope.toggleRegionOption = function(option) {
	 			$scope.toggleFilterOption($scope.sm.searchFilter.regionIds, option);
	 		};
	 		
	 		$scope.hasRegionOption = function(option) {
	 			return $scope.hasFilterOption($scope.sm.searchFilter.regionIds, option); 
	 		};
	 		
	 		$scope.toggleZoneOption = function(option) {
	 			$scope.toggleFilterOption($scope.sm.searchFilter.zoneIds, option);
	 		};
	 		
	 		$scope.hasZoneOption = function(option) {
	 			return $scope.hasFilterOption($scope.sm.searchFilter.zoneIds, option); 
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
	 		 * Navigate user to the create UI
	 		 * @return void
	 		 */
	 		$scope.add = function() {
		 		$state.go("application.information", { "applicationId" : "new" });
		 	};
		 	
		 	$scope.loadFilters = function() {
		 		$scope.filtersBusy = true;
		 		ApplicationService.filters().then(function(response){
		 			$scope.filters = response.data;
		 			
		 			if ($rootScope.applicationSearchSM) {
		 				applyStateMachine();
		 			} else {
						//$scope.sm.searchFilter.companyIds.push($rootScope.user.companyId);
						//$scope.sm.searchFilter.apiSigningRequired = ["YES"];
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
				$scope.pagination.pageIndex = $rootScope.applicationSearchSM.searchFilter.pageIndex;
				$scope.pagination.itemsPerPage = $rootScope.applicationSearchSM.searchFilter.itemsPerPage;
				$scope.pagination.sort.property = $rootScope.applicationSearchSM.searchFilter.sortField;
				$scope.pagination.sort.direction = $rootScope.applicationSearchSM.searchFilter.sortDirection;
				$scope.sm.searchFilter.companyIds = $rootScope.applicationSearchSM.searchFilter.companyIds;
				$scope.sm.searchFilter.subscriptionIds = $rootScope.applicationSearchSM.searchFilter.subscriptionIds;
				$scope.sm.searchFilter.regionIds = $rootScope.applicationSearchSM.searchFilter.regionIds;
				$scope.sm.searchFilter.zoneIds = $rootScope.applicationSearchSM.searchFilter.zoneIds;
				$scope.sm.searchFilter.productIds = $rootScope.applicationSearchSM.searchFilter.productIds;
				$scope.sm.searchFilter.apiSigningRequired = $rootScope.applicationSearchSM.searchFilter.apiSigningRequired;
				$scope.sm.searchFilter.enabled = $rootScope.applicationSearchSM.searchFilter.enabled;
				$scope.sm.searchFilter.name = $rootScope.applicationSearchSM.searchFilter.name;
				$scope.sm.searchFilter.code = $rootScope.applicationSearchSM.searchFilter.code;
				$scope.sm.opinion = $rootScope.applicationSearchSM.opinion;
		 	}

		 	function init() {
		 		$scope.loadFilters();
		 	}
		 	
	 		// initialize the UI by call the find method
	 		init();
	 	}
	]
);