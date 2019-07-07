controllers.controller('NodeTypesController', ['$uibModal', 'NodeTypeService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', NodeTypesController]);

function NodeTypesController($uibModal, NodeTypeService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Group Types',
        resultTitle: 'Group Type',
        columnHeaders: [
            {label: 'Name', value: 'name', sortable: true},
            {label: 'Description', value: 'description', sortable: true},
            {label: 'Enabled', value: 'enabled', sortable: false},
            {label: 'Last Modified', value: 'lastModifiedDate', sortable: false},
            {label: 'Last Modified By', value: 'lastModifiedBy', sortable: false, renderFunc: function(val) { return val === "" ? 'Unknown': val;}}
        ],
        tfootTopic: 'group types',
        canAdd: SecurityService.canCreateNodeType(),
        canEdit: SecurityService.canEditNodeType(),
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = {
        enabled: true
    };

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/enabled-filter.html',
            controller: 'FilterController',
            size: 'lg',
            resolve: {
                filter: vm.filter
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

    function openDetails(nodeType) {
        nodeType = nodeType || {
            name: '',
            description: '',
            enabled: true
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/nodetype/node-type-details.html',
            controller: 'NodeTypeDetailsController',
            size: 'lg',
            resolve: {
                nodeType: nodeType
            }
        });

        modalInstance.result.then(function(nodeType) {
            SpinnerService.wrap(NodeTypeService.save, nodeType)
            .then(function(response) {
                ToastrService.popupSuccess('Group Type ' + nodeType.name + ' has been saved successfully');
                // re-read the current page
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(NodeTypeService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.find();
}
NodeTypesController.prototype = Object.create(ListController.prototype);

NodeTypesController.prototype.getCellText = function (item, column) {
    if (column.value == 'lastModifiedDate') {
        return moment(new Date(item.lastModifiedDate)).format('MM/DD/YYYY hh:mm:ss A');
    } else {
        return ListController.prototype.getCellText.call(this, item, column);
    }
};

controllers.controller('NodeTypeDetailsController',
    [
        '$scope', '$uibModalInstance', 'NodeTypeService', 'SecurityService', 'ToastrService', 'SpinnerService', 'nodeType',
        function ($scope, $uibModalInstance, NodeTypeService, SecurityService, ToastrService, SpinnerService, nodeType) {
            $scope.nodeType = angular.merge({}, nodeType);

            $scope.canSaveNodeType = function() {
                return ($scope.nodeType.id && SecurityService.canEditNodeType())
                    || (!$scope.nodeType.id && SecurityService.canCreateNodeType());
            };

            $scope.save = function () {
                if ($scope.nodeTypeForm.$valid) {
                    $uibModalInstance.close($scope.nodeType);
                } else {
                    ToastrService.popupError('Group Type cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

            SpinnerService.wrap(NodeTypeService.options)
            .then(function(response) {
                $scope.options = response.data;
            });
        }
    ]
);

