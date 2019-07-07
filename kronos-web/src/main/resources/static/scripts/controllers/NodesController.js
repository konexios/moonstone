controllers.controller('NodesController', ['$location', '$uibModal', 'NodeService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', NodesController]);

function NodesController($location, $uibModal, NodeService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    vm.toolbar = [];
    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Groups',
        resultTitle: 'Group',
        columnHeaders: [
            {label: 'Name', value: 'name', sortable: true},
            {label: 'Description', value: 'description', sortable: true},
            {label: 'Group Type', value: 'nodeType.name', sortable: false},
            {label: 'Parent Group', value: 'parentNode.name', sortable: false},
            {label: 'Enabled', value: 'enabled', sortable: false},
            {label: 'Last Modified', value: 'lastModifiedDate', sortable: false},
            {label: 'Last Modified By', value: 'lastModifiedBy', sortable: false, renderFunc: function(val) { return val === "" ? 'Unknown': val;}}
        ],
        tfootTopic: 'groups',
        canAdd: SecurityService.canCreateNode(),
        canEdit: SecurityService.canEditNode(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = {
        nodeTypeIds: [],
        enabled: true
    };
    vm.filterOptions = {
        nodeTypes: []
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/node/node-filter.html',
            controller: 'NodesFilterController',
            size: 'lg',
            resolve: {
                filter: vm.filter,
                filterOptions: vm.filterOptions
            }
        });

        modalInstance.result.then(
            function (filter) {
                vm.filter = filter;
                vm.pagination.pageIndex = 0;
                vm.find();
            }
        );
    }

    function openDetails(node) {
        node = node || {
            name: '',
            description: '',
            nodeType: null,
            parentNode: null,
            enabled: true
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/node/node-details.html',
            controller: 'NodeDetailsController',
            size: 'lg',
            resolve: {
                node: node
            }
        });

        modalInstance.result.then(function(node) {
            SpinnerService.wrap(NodeService.save, node)
            .then(function(response) {
                ToastrService.popupSuccess('Group ' + node.name + ' has been saved successfully');
                // re-read the current page
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(NodeService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    SpinnerService.wrap(NodeService.getNodeTypes)
    .then(function(response) {
        vm.filterOptions.nodeTypes = response.data;
    });

    vm.find();
}
NodesController.prototype = Object.create(ListController.prototype);

NodesController.prototype.getCellText = function (item, column) {
    if (column.value == 'lastModifiedDate') {
        return moment(new Date(item.lastModifiedDate)).format('MM/DD/YYYY hh:mm:ss A');
    } else {
        return ListController.prototype.getCellText.call(this, item, column);
    }
};

controllers.controller('NodesFilterController',
    [
        '$scope', '$uibModalInstance', 'filter', 'filterOptions',
        function ($scope, $uibModalInstance, filter, filterOptions) {
            $scope.filter = angular.merge({}, filter);
            $scope.filterOptions = filterOptions;

            $scope.ok = function () {
                $uibModalInstance.close($scope.filter);
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

controllers.controller('NodeDetailsController',
    [
        '$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'SpinnerService', 'NodeService', 'node',
        function ($scope, $uibModalInstance, SecurityService, ToastrService, SpinnerService, NodeService, node) {
            $scope.node = angular.merge({}, node);

            $scope.canSaveNode = function() {
                return ($scope.node.id && SecurityService.canEditNode())
                    || (!$scope.node.id && SecurityService.canCreateNode());
            };

            $scope.save = function () {
                if ($scope.nodeForm.$valid) {
                    $uibModalInstance.close($scope.node);
                } else {
                    ToastrService.popupError('Group cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

            SpinnerService.wrap(NodeService.getNodeOptions)
            .then(function(response) {
                $scope.options = response.data;
            });
        }
    ]
);

