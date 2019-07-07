angular.module('pegasus').controller("UsersController",
	[
	 	"$rootScope", "$scope", "$location", "$state", "SecurityService", "ErrorService", "UserService", "$uibModal",
	 	function ($rootScope, $scope, $location, $state, SecurityService, ErrorService, UserService, $uibModal) {
	 		$scope.busy = false;
	 		$scope.filtersBusy = false;
	 		$scope.filters = {};
	 		$scope.sm = {
	 			opinion: "none",
	 			searchFilter: {
	 				companyIds: [],
	 				statuses: [],
	 				firstName: null,
	 				lastName: null,
	 				login: null
	 			}
	 		};
	 		
	 		// pagination
	 		$scope.pagination = PaginationUtil.create();

	 		// sorting
	 		$scope.pagination.sort.direction = "ASC";
	 		$scope.pagination.sort.property = "fullName"; 

	 		// columns
	 		$scope.columnHeaders = [
	            { label: "User", value: "fullName", sortable: true },
	            { label: "Login", value: "login", sortable: false },
	            { label: "Tenant", value: "companyName", sortable: false },
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
	 			$rootScope.userSearchSM = {
	 				opinion: $scope.sm.opinion,
	 				searchFilter: $scope.sm.searchFilter
	 			};
	 			
	 			UserService.find($scope.sm.searchFilter).then(function(response) {
		 			$scope.pagination = PaginationUtil.update($scope.pagination, response.data.result);
		 			//$state.current.data.pageSubTitle = "as of " + DateUtil.formatDate(new Date());
 				}).catch(function(response) {
	 				ErrorService.handleHttpError(response);
 				}).finally(function(){
 					$scope.busy = false;	 					
 				});	 			
	 		};

	 		$scope.changeOpinion = function(opinion) {
	 			$scope.sm.opinion = opinion;
	 			// store in root scope
	 			$rootScope.userSearchSM.opinion = opinion;
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
		 	
		 	$scope.loadFilters = function() {
		 		$scope.filtersBusy = true;
		 		UserService.filters().then(function(response){
		 			$scope.filters = response.data;
		 			
		 			if ($rootScope.userSearchSM) {
		 				applyStateMachine();
		 			} else {
						$scope.sm.searchFilter.companyIds.push($rootScope.user.companyId);
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

            /**
             * Navigate user to the create UI
             * @return void
             */
            $scope.add = function() {

                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'partials/modals/user/createUser.html',
                    controller: 'UserCreateController',
                    size: 'lg',
                    resolve: {

                    }
                });

                modalInstance.result.then(function(user) {
                    console.log(user);
                    $scope.find();
                });
            };
		 	
		 	function applyStateMachine() {
				$scope.pagination.pageIndex = $rootScope.userSearchSM.searchFilter.pageIndex;
				$scope.pagination.itemsPerPage = $rootScope.userSearchSM.searchFilter.itemsPerPage;
				$scope.pagination.sort.property = $rootScope.userSearchSM.searchFilter.sortField;
				$scope.pagination.sort.direction = $rootScope.userSearchSM.searchFilter.sortDirection;
				$scope.sm.searchFilter.companyIds = $rootScope.userSearchSM.searchFilter.companyIds;
				$scope.sm.searchFilter.statuses = $rootScope.userSearchSM.searchFilter.statuses;
				$scope.sm.searchFilter.firstName = $rootScope.userSearchSM.searchFilter.firstName;
				$scope.sm.searchFilter.lastName = $rootScope.userSearchSM.searchFilter.lastName;
				$scope.sm.searchFilter.login = $rootScope.userSearchSM.searchFilter.login;	
				$scope.sm.opinion = $rootScope.userSearchSM.opinion;
		 	}

		 	function init() {
		 		$scope.loadFilters();
		 	}
		 	
	 		// initialize the UI by call the find method
	 		init();
	 	}
	]
);

angular.module('pegasus').controller('UserCreateController', [
        '$scope', '$rootScope', '$uibModalInstance', 'UserService', 'ErrorService', 'ToastrService',
        function ($scope, $rootScope, $uibModalInstance, UserService, ErrorService, ToastrService) {

			$scope.busy = true;
			$scope.user = {};

            UserService.get('new')
				.then(function (response) {
					$scope.companies = response.data.companyOptions;
                })
				.catch(function(response){
                    ErrorService.handleHttpError(response);
				})
				.finally(function () {
					$scope.busy = false;
                });

            $scope.save = function(form, user) {
				form.$setSubmitted();
            	if (form.$valid) {
                    $scope.busy = true;

                    var savePromise = UserService.create(user);

                    if (!savePromise) return;

                    savePromise
                        .then(function(response) {
                            $scope.user = response.data;

                            // reset form
                            form.$setPristine();
                            form.$setUntouched();

                            $scope.busy = false;

                            ToastrService.popupSuccess($scope.user.firstName + " has been successfully saved.");

                            $uibModalInstance.close($scope.user);
                        })
                        .catch(function(response) {
                            $scope.busy = false;
                            ErrorService.handleHttpError(response);
                        });
                } else {
                    ToastrService.popupError("The form is invalid! Please make changes and try again.");
                }
            };

            $scope.cancel = function() {
                $uibModalInstance.close();
            };
        }
    ]
);