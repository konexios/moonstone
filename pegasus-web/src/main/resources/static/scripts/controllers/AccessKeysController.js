angular.module('pegasus').controller("AccessKeysController",
    [
        "$uibModal", "$stateParams", "$scope", "$location", "$state", "SecurityService", "ErrorService", "AccessKeyService",
        function ($uibModal, $stateParams, $scope, $location, $state, SecurityService, ErrorService, AccessKeyService) {

            $scope.busy = false;
            $scope.filtersBusy = false;

            $scope.pagination = PaginationUtil.create();

            // sorting
            $scope.pagination.sort.direction = "ASC";
            $scope.pagination.sort.property = "name";

            // columns
            $scope.columnHeaders = [
                {label: 'Raw Api Key', value: 'rawApiKey', sortable: false, style:{'text-overflow': 'ellipsis', 'word-break': 'normal'}},
                {label: 'Name', value: 'name', sortable: true},
                {label: 'Owner', value: 'ownerDisplayName', sortable: false},
                {label: 'Expired', value: 'expired', sortable: false},
                {label: 'Expiration Date', value: 'expiration', sortable: true}
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
                if ($scope.pagination.itemsPerPage !== numberOfItems) {
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
                $scope.accessKeySearchSM = {
                    opinion: $scope.sm.opinion,
                    searchFilter: $scope.sm.searchFilter,
                    optionFilter: $scope.sm.optionFilter
                };

                $scope.accessKeySearchSM.searchFilter.expirationDateFrom =
                    convertDateToTimestamp($scope.accessKeySearchSM.searchFilter.expirationDateFrom);

                $scope.accessKeySearchSM.searchFilter.expirationDateTo =
                    convertDateToTimestamp($scope.accessKeySearchSM.searchFilter.expirationDateTo);

                AccessKeyService.find($scope.sm.searchFilter, $scope.accessKeysOwner).then(function(response) {
                    $scope.pagination = PaginationUtil.update($scope.pagination, response.data.result);
                }).catch(function(response){
                    ErrorService.handleHttpError(response);
                }).finally(function(){
                    $scope.busy = false;
                });
            };

            $scope.changeOpinion = function(opinion) {
                $scope.sm.opinion = opinion;
                // store in root scope
                $rootScope.accessKeySearchSM.opinion = opinion;
            };

            $scope.onFilterBlur = function() {
                $scope.pagination.pageIndex = 0;
                $scope.find();
            };

            $scope.canCreateAccessKey = function () {
                switch ($scope.accessKeysOwner.types) {

                    case 'applications':
                        return $scope.canCreateApplicationAccessKey();

                    case 'companies':
                        return false;
                        //return $scope.canCreateCompanyAccessKey();

                    case 'subscriptions':
                        return false;
                        //return $scope.canCreateSubscriptionAccessKey();

                }
            };

            $scope.canUpdateAccessKey = function () {
                switch ($scope.accessKeysOwner.types) {

                    case 'companies':
                        return $scope.canUpdateCompanyAccessKey();

                    case 'applications':
                        return $scope.canUpdateApplicationAccessKey();

                    case 'subscriptions':
                        return $scope.canUpdateSubscriptionAccessKey();
                }
            };

            $scope.canReadAccessKey = function () {
                switch ($scope.accessKeysOwner.types) {

                    case 'companies':
                        return $scope.canReadCompanyAccessKey();

                    case 'applications':
                        return $scope.canReadApplicationAccessKey();

                    case 'subscriptions':
                        return $scope.canReadSubscriptionAccessKey();
                }
            };

            /**
             * Navigate user to the company UI to create a new Company
             * @return void
             */
            $scope.add = function() {
                $state.go("accesskey", {
                    "entityId": $scope.accessKeysOwner.id,
                    "entityTypes": $scope.accessKeysOwner.types,
                    "entityType": $scope.accessKeysOwner.type,
                    "accessKeyId": "new"});
            };

            $scope.loadFilters = function() {
                $scope.filtersBusy = true;
                AccessKeyService.filters().then(function(response){
                    $scope.filters = response.data;

                    if ($rootScope.accessKeySearchSM) {
                        applyStateMachine();
                    } else {
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

            $scope.showFilters = function(entityType) {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'partials/modals/accessKeyFilter.html',
                    controller: 'AccessKeyFilterController',
                    size: 'lg',
                    resolve: {
                        accessKeySearchSM: function() {
                            return $scope.accessKeySearchSM;
                        },
                        entityType: function() {
                            return entityType;
                        }
                    }
                });

                modalInstance.result.then(function (filter) {

                    $scope.accessKeySearchSM.searchFilter.name = filter.name;
                    $scope.accessKeySearchSM.searchFilter.accessLevels = filter.accessLevels;
                    $scope.accessKeySearchSM.searchFilter.expirationDateFrom= filter.expirationDateFrom;
                    $scope.accessKeySearchSM.searchFilter.expirationDateTo = filter.expirationDateTo;
                    $scope.accessKeySearchSM.searchFilter.relationEntityType = filter.relationEntityType;
                    $scope.find();
                });
            };

            function init() {
                $scope.accessKeysOwner = {};

                $scope.filters = {};
                $scope.sm = {
                    opinion: "list",
                    searchFilter: {
                        name: null,
                        accessLevels: null,
                        expirationDateFrom: null,
                        expirationDateTo: null,
                        relationEntityType: 'MY_KEYS'
                    },
                    optionFilter: {
                        accessLevels: ['OWNER', 'WRITE', 'READ']
                    }
                };

                switch($state.current.name) {

                    case 'company.accesskeys':
                        $scope.accessKeysOwner.id = $state.params.companyId;
                        $scope.accessKeysOwner.types = 'companies';
                        $scope.accessKeysOwner.type  = 'company';
                        break;

                    case 'application.accesskeys':
                        $scope.accessKeysOwner.id = $state.params.applicationId;
                        $scope.accessKeysOwner.types = 'applications';
                        $scope.accessKeysOwner.type  = 'application';
                        break;

                    case 'subscription.accesskeys':
                        $scope.accessKeysOwner.id = $state.params.subscriptionId;
                        $scope.accessKeysOwner.types = 'subscriptions';
                        $scope.accessKeysOwner.type  = 'subscription';
                        break;
                }

                $scope.find();
            }

            function convertDateToTimestamp(value) {

                if (!value) {
                    return value;
                }

                return new Date(value).getTime();
            }

            init();
        }
    ]);

angular.module('pegasus').controller('AccessKeyFilterController', [
    '$scope', '$rootScope', '$uibModalInstance', 'accessKeySearchSM', 'entityType',
    function ($scope, $rootScope, $uibModalInstance, accessKeySearchSM, entityType) {

        $scope.entityType = entityType;
        $scope.model = {
            name: accessKeySearchSM.searchFilter.name,
            accessLevels: accessKeySearchSM.searchFilter.accessLevels,
            expirationDateFrom: accessKeySearchSM.searchFilter.expirationDateFrom,
            expirationDateTo: accessKeySearchSM.searchFilter.expirationDateTo,
            relationEntityType: accessKeySearchSM.searchFilter.relationEntityType,
            optionFilter: accessKeySearchSM.optionFilter
        };

        $scope.search = function() {
            $uibModalInstance.close($scope.model);
        };

        $scope.cancel = function() {
            $uibModalInstance.close();
        };
    }
  ]
);