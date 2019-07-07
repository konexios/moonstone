controllers.controller('TestResultsController', ['$uibModal', '$filter', '$routeParams', 'TestResultService', 'ErrorService', 'SpinnerService', 'SecurityService', 'ToastrService', TestResultsController]);

function TestResultsController($uibModal, $filter, $routeParams, TestResultService, ErrorService, SpinnerService, SecurityService, ToastrService) {
    var vm = this;

    var isObjectPage = !!($routeParams.gatewayId || $routeParams.deviceId);
    var listConfig = {
        pageTitle: 'Test Results',
        resultTitle: 'Test Results',
        columnHeaders: [
            { label: 'Test Procedure', value: 'testProcedureName', sortable: true },
            //{ label: 'Category', value: 'category', sortable: true },
            { label: 'Asset', value: 'objectName', sortable: false },
            { label: 'Started', value: 'started', sortable: true, renderFunc: function(item){ return $filter('date')(item, 'MM/dd/yyyy h:mm a'); } },
            { label: 'Ended', value: 'ended', sortable: true, renderFunc: function(item){ return $filter('date')(item, 'MM/dd/yyyy h:mm a'); } },
            { label: 'Status', value: 'status', sortable: true }
        ],
        tfootTopic: 'test results',
        canAdd: false,
        canEdit: true,
        openFilter: openFilter,
        openDetails: openDetails
    };
    // Dont show objectName on Deivce\Gateway page
    if (isObjectPage) {
        listConfig.columnHeaders.splice(1, 1);
    }
    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'started',
            direction: 'DESC'
        }
    }, listConfig);

    vm.filter = angular.extend({
        objectId: null,
        testProcedureIds: null,
        //categories: null,
        statuses: null,
        stepStatuses: null
    }, vm.filter);

    // filtering options for DEVICE and GATEWAY pages
    if (isObjectPage) {
        vm.filter.objectId = $routeParams.gatewayId || $routeParams.deviceId;
    }

    vm.filterOptions = {
        testProcedures: [],
        //categories: [],
        statuses: [],
        stepStatuses: []
    };
    vm.editOptions = {};

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/testresult/testresult-filter.html',
            controller: 'TestResultFilterController',
            size: 'lg',
            resolve: {
                filter: vm.filter,
                filterOptions: vm.filterOptions
            }
        });
        
        modalInstance.result.then(function(filter) {
            vm.filter = filter;
            vm.pagination.pageIndex = 0;
            vm.find();
        });
    }

    function openDetails(testResult) {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/testresult/testresult-details.html',
            controller: 'TestResultDetailsController',
            size: 'lg',
            resolve: {
                testResult: testResult,
                editOptions: vm.editOptions
            }
        });

        modalInstance.result.then(function(testResult) {
            SpinnerService.wrap(TestResultService.save, testResult)
                .then(function(response) {
                    ToastrService.popupSuccess('Test Result ' + response.data.name + ' has been saved successfully');
                    // re-read the current page
                    vm.find();
                })
                .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(TestResultService.find, vm.pagination, vm.filter)
            .then(function(response) {
                vm.update(response.data.result);
            })
            .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(TestResultService.options)
        .then(function(response) {
            vm.filterOptions = response.data;
        });

    vm.find();
}

TestResultsController.prototype = Object.create(ListController.prototype);

controllers.controller('TestResultFilterController', [
    '$scope', '$uibModalInstance', 'filter', 'filterOptions',
    function($scope, $uibModalInstance, filter, filterOptions) {
        $scope.filter = angular.merge({}, filter);
        $scope.filterOptions = filterOptions;

        $scope.ok = function() {
            $uibModalInstance.close($scope.filter);
        };

        $scope.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
]);

controllers.controller('TestResultDetailsController', [
    '$scope', '$uibModalInstance', 'TestResultService', 'ErrorService', 'SecurityService', 'ToastrService', 'SpinnerService', 'testResult', 'editOptions',
    function($scope, $uibModalInstance, TestResultService, ErrorService, SecurityService, ToastrService, SpinnerService, testResult, editOptions) {
        $scope.options = editOptions;
        $scope.loading = true;
        $scope.statuses = [];
        //$scope.categories = [];

        function init(testResult) {
            $scope.canSave = SecurityService.canEditTestResult();
            $scope.loading = false;

            $scope.testResult = angular.merge({}, testResult);
            $scope.editing = [];
            if ($scope.testResult.steps && $scope.testResult.steps.length > 0) {
                for (var i = 0; i < $scope.testResult.steps.length; i++) {
                    $scope.editing[i] = false;
                }
            }
        }

        SpinnerService.wrap(TestResultService.get, testResult.id)
            .then(function(response) {
                init(response.data);
            })
            .catch(function(response) {
                ErrorService.handleHttpError(response);
                $uibModalInstance.dismiss('cancel');
            });

        SpinnerService.wrap(TestResultService.options).then(function(response) {
            $scope.statuses = response.data.statuses;
            //$scope.categories = response.data.categories;
        }).catch(ErrorService.handleHttpError);

        $scope.editStep = function(index) {
            $scope.editing[index] = !$scope.editing[index];
        };

        $scope.save = function(form) {
            if (form.$valid) {
                $uibModalInstance.close($scope.testResult);
            } else {
                ToastrService.popupError('Test Result cannot be saved because of invalid fields, please check errors.');
            }
        };

        $scope.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
]);