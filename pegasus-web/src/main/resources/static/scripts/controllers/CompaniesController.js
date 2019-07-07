angular.module('pegasus').controller("CompaniesController",
	[
	 	"$rootScope", "$scope", "$location", "$state", "SecurityService", "ErrorService", "CompanyService",
	 	function ($rootScope, $scope, $location, $state, SecurityService, ErrorService, CompanyService) {
	 		$scope.busy = false;
	 		$scope.filtersBusy = false;
	 		$scope.filters = {};
	 		$scope.sm = {
	 			opinion: "none",
	 			searchFilter: {
	 				parentCompanyIds: [],
	 				statuses: [],
	 				name: null,
	 				abbrName: null
	 			}
	 		};
	 		
//	 		$scope.pageTitle = "Companies";
//	 		$scope.pageSubTitle = "";

	 		// pagination
	 		$scope.pagination = PaginationUtil.create();

	 		// sorting
	 		$scope.pagination.sort.direction = "ASC"
	 		$scope.pagination.sort.property = "name"; 

	 		// columns
	 		$scope.columnHeaders = [
	            { label: "Tenant", value: "name", sortable: true },
	            { label: "Abbreviation", value: "abbrName", sortable: true },
	            { label: "Contact", value: "contactName", sortable: false },
	            { label: "Billing Contact", value: "billingContactName", sortable: false }, 
	            { label: "Parent Tenant", value: "parentCompanyName", sortable: false },
	            { label: "Status", value: "status", sortable: true } 
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
	 			$rootScope.companySearchSM = {
	 				opinion: $scope.sm.opinion,
	 				searchFilter: $scope.sm.searchFilter
	 			};
	 			
	 			CompanyService.find($scope.sm.searchFilter).then(function(response) {
		 			$scope.pagination = PaginationUtil.update($scope.pagination, response.data.result);
//			 		$scope.pageSubTitle = "as of " + DateUtil.formatDate(new Date());
//			 		$state.current.data.pageSubTitle = "as of " + DateUtil.formatDate(new Date());
	 			}).catch(function(response){
	 				ErrorService.handleHttpError(response);
	 			}).finally(function(){
	 				$scope.busy = false;		 				
	 			});	 			
	 		};
	 		
	 		$scope.changeOpinion = function(opinion) {
	 			$scope.sm.opinion = opinion;
	 			// store in root scope
	 			$rootScope.companySearchSM.opinion = opinion;
	 		};

	 		$scope.toggleCompanyOption = function(option) {
	 			var idx = $scope.sm.searchFilter.parentCompanyIds.indexOf(option.id);

	 			if (idx == -1) {
	 				// add
	 				$scope.sm.searchFilter.parentCompanyIds.push(option.id);
	 			} else {
	 				// remove
	 				$scope.sm.searchFilter.parentCompanyIds.splice(idx, 1);
	 			}
	 			
	 			$scope.pagination.pageIndex = 0;
	 			
	 			$scope.find();
	 		};
	 		
	 		$scope.hasCompanyOption = function(option) {
	 			var result = false;
	 			
	 			for(var i = 0; i < $scope.sm.searchFilter.parentCompanyIds.length; i++) {
	 				var existing = $scope.sm.searchFilter.parentCompanyIds[i];
	 				if (existing === option.id) {
	 					result = true;
	 					break;
	 				}
	 			}
	 			
	 			return result;
	 		};

	 		$scope.toggleStatusOption = function(option) {
	 			var idx = $scope.sm.searchFilter.statuses.indexOf(option);

	 			if (idx == -1) {
	 				// add
	 				$scope.sm.searchFilter.statuses.push(option);
	 			} else {
	 				// remove
	 				$scope.sm.searchFilter.statuses.splice(idx, 1);
	 			}
	 			
	 			$scope.pagination.pageIndex = 0;
	 			
	 			$scope.find();
	 		};
	 		
	 		$scope.hasStatusOption = function(option) {
	 			var result = false;
	 			
	 			for(var i = 0; i < $scope.sm.searchFilter.statuses.length; i++) {
	 				var existing = $scope.sm.searchFilter.statuses[i];
	 				if (existing === option) {
	 					result = true;
	 					break;
	 				}
	 			}
	 			
	 			return result;
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
		 		$state.go("company.information", { "companyId" : "new" });
		 	};

		 	$scope.loadFilters = function() {
		 		$scope.filtersBusy = true;
		 		CompanyService.filters().then(function(response){
		 			$scope.filters = response.data;
		 			
		 			if ($rootScope.companySearchSM) {
		 				applyStateMachine();
		 			} else {
						$scope.sm.searchFilter.parentCompanyIds.push($rootScope.user.companyId);
						$scope.sm.searchFilter.statuses = $scope.sm.searchFilter.statuses.concat($scope.filters.statusOptions);
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
				$scope.pagination.pageIndex = $rootScope.companySearchSM.searchFilter.pageIndex;
				$scope.pagination.itemsPerPage = $rootScope.companySearchSM.searchFilter.itemsPerPage;
				$scope.pagination.sort.property = $rootScope.companySearchSM.searchFilter.sortField;
				$scope.pagination.sort.direction = $rootScope.companySearchSM.searchFilter.sortDirection;
				$scope.sm.searchFilter.parentCompanyIds = $rootScope.companySearchSM.searchFilter.parentCompanyIds;
				$scope.sm.searchFilter.statuses = $rootScope.companySearchSM.searchFilter.statuses;
				$scope.sm.searchFilter.name = $rootScope.companySearchSM.searchFilter.name;
				$scope.sm.searchFilter.abbrName = $rootScope.companySearchSM.searchFilter.abbrName;
				$scope.sm.opinion = $rootScope.companySearchSM.opinion;
		 	}

		 	function init() {
		 		$scope.loadFilters();
		 	}
		 	
	 		// initialize the UI by call the find method
	 		init();
	 	}
	]
);