function LogsController($uibModal, $filter) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'createdDate',
            direction: 'DESC'
        }
    }, {
        columnHeaders: [
            {label: 'Type', value: 'type', sortable: true},
            {label: 'When', value: 'createdDate', sortable: true, renderFunc: function(when) {
                return $filter('date')(when*1000, 'MM/dd/yyyy hh:mm:ss a');
            }},
            {label: 'Who', value: 'createdBy', sortable: false, renderFunc: function(who) {
                return who || 'Unknown';
            }}
        ],
        canEdit: true
    });

    vm.filter = {
        createdDateFrom: null,
        createdDateTo: null,
        userIds: [],
        types: []
    };
    vm.options = {
        users: [],
        types: []
    };

    vm.open = function(size) {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/log-filter.html',
            controller: 'LogFilterController',
            controllerAs: 'vm',
            size: size,
            resolve: {
                filter: vm.filter,
                options: vm.options
            }
        });

        modalInstance.result.then(
            function (filter) {
                vm.filter = filter;
                vm.pagination.pageIndex = 0;
                vm.find();
            }
        );
    };

    vm.openDetails = function(log) {
        if (vm.getAuditLog) {
            vm.getAuditLog(log)
            .then(function(response) {
                $uibModal.open({
                    animation: false,
                    templateUrl: 'partials/log-popup.html',
                    controller: 'LogPopupController',
                    controllerAs: 'vm',
                    size: 'lg',
                    resolve: {
                        auditLog: response.data
                    }
                });
            });
        }
    };
}
LogsController.prototype = Object.create(ListController.prototype);

controllers.controller('LogFilterController', ['$scope','$uibModalInstance', 'filter', 'options', LogFilterController]);
function LogFilterController($scope,$uibModalInstance, filter, options) {
    var vm = this;
    vm.filter = angular.merge({}, filter);
    vm.options = options;

    vm.ok = function () {
        $uibModalInstance.close(vm.filter);
    };

    vm.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

controllers.controller('LogPopupController', ['$uibModalInstance', 'auditLog', LogPopupController]);
function LogPopupController($uibModalInstance, auditLog) {
    var vm = this;

    vm.auditLog = auditLog;

    vm.isParametersSpecified = function() {
        return vm.auditLog.parameters != null && Object.keys(vm.auditLog.parameters).length > 0;
    };

    vm.close = function () {
        $uibModalInstance.dismiss('cancel');
    };
}
