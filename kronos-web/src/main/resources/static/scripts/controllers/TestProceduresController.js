controllers.controller('TestProceduresController', ['$uibModal', 'TestProcedureService', 'ErrorService', 'SpinnerService', 'SecurityService', 'ToastrService', TestProceduresController]);

function TestProceduresController($uibModal, TestProcedureService, ErrorService, SpinnerService, SecurityService, ToastrService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Test Procedures',
        resultTitle: 'Test Procedures',
        columnHeaders: [
            { label: 'Name', value: 'name', sortable: true },
            { label: 'Description', value: 'description', sortable: true },
            { label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val) { return val ? 'Yes': 'No'; } },
            { label: 'Asset Type', value: 'deviceTypeName', sortable: false },
            {label: 'Last Modified', value: 'lastModifiedDate', sortable: false},
            {label: 'Last Modified By', value: 'lastModifiedBy', sortable: false, renderFunc: function(val) { return val === "" ? 'Unknown': val;}}
        ],
        tfootTopic: 'test procedures',
        canAdd: SecurityService.canCreateTestProcedure(),
        canEdit: true,
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = angular.extend({
        enabled: null,
        deviceTypeIds: null
    }, vm.filter);
    vm.filterOptions = {
        deviceTypes: []
    };
    vm.editOptions = {};

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/testprocedure/testprocedure-filter.html',
            controller: 'TestProceduresFilterController',
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

    function openDetails(testProcedure) {
        testProcedure = testProcedure || {
            name: '',
            description: '',
            enabled: false,
            steps: []
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/testprocedure/testprocedure-details.html',
            controller: 'TestProcedureDetailsController',
            size: 'lg',
            resolve: {
                testProcedure: testProcedure,
                editOptions: vm.editOptions
            }
        });

        modalInstance.result.then(function(testProcedure) {
            SpinnerService.wrap(TestProcedureService.save, testProcedure)
                .then(function(response) {
                    ToastrService.popupSuccess('Test Procedure ' + response.data.name + ' has been saved successfully');
                    // re-read the current page
                    vm.find();
                })
                .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(TestProcedureService.find, vm.pagination, vm.filter)
            .then(function(response) {
                vm.update(response.data.result);
            })
            .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(TestProcedureService.options)
        .then(function(response) {
            vm.filterOptions.deviceTypes = response.data;
        });

    vm.find();
}

TestProceduresController.prototype = Object.create(ListController.prototype);

TestProceduresController.prototype.getCellText = function (item, column) {
    if (column.value == 'lastModifiedDate') {
        return moment(new Date(item.lastModifiedDate)).format('MM/DD/YYYY hh:mm:ss A');
    } else {
        return ListController.prototype.getCellText.call(this, item, column);
    }
};

controllers.controller('TestProceduresFilterController', [
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

controllers.controller('TestProcedureDetailsController', [
    '$scope', '$uibModalInstance', 'TestProcedureService', 'ErrorService', 'SecurityService', 'ToastrService', 'SpinnerService', 'testProcedure', 'editOptions',
    function($scope, $uibModalInstance, TestProcedureService, ErrorService, SecurityService, ToastrService, SpinnerService, testProcedure, editOptions) {
        $scope.options = editOptions;
        $scope.loading = true;
        $scope.deviceTypes = [];

        function init(testProcedure) {
            $scope.canSave = (!testProcedure.id && SecurityService.canCreateTestProcedure()) || (testProcedure.id && SecurityService.canEditTestProcedure());
            $scope.loading = false;

            $scope.testProcedure = angular.merge({}, testProcedure);
            $scope.editing = [];
            if ($scope.testProcedure.steps && $scope.testProcedure.steps.length > 0) {
                for (var i = 0; i < $scope.testProcedure.steps.length; i++) {
                    $scope.editing[i] = false;
                }
            }
        }

        if (testProcedure.id) {
            SpinnerService.wrap(TestProcedureService.get, testProcedure.id)
                .then(function(response) {
                    init(response.data);
                })
                .catch(function(response) {
                    ErrorService.handleHttpError(response);
                    $uibModalInstance.dismiss('cancel');
                });
        } else {
            init(testProcedure);
        }

        SpinnerService.wrap(TestProcedureService.options).then(function(response) {
            $scope.deviceTypes = response.data;
        }).catch(ErrorService.handleHttpError);

        $scope.addStep = function() {
            $scope.testProcedure.steps.push({ name: '', description: '' }); //sortOrder, id, enabled
            $scope.editing.push(true);
        };

        $scope.editStep = function(index) {
            $scope.editing[index] = !$scope.editing[index];
        };

        $scope.deleteStep = function(index) {
            $scope.testProcedure.steps.splice(index, 1);
            $scope.editing.splice(index, 1);
        };

        $scope.save = function(form) {
            if (form.$valid) {
                $uibModalInstance.close($scope.testProcedure);
            } else {
                ToastrService.popupError('Test Procedure cannot be saved because of invalid fields, please check errors.');
            }
        };

        $scope.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
]);