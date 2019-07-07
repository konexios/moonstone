services.factory('$dialogConfirm', function($uibModal) {
    return function(message, title) {
        var modal = $uibModal.open({
            size: 'sm',
            template:
            '<div class="modal-header">' +
                '<h4 class="modal-title" ng-bind="title"></h4>' +
            '</div>' +
            '<div class="modal-body" ng-bind="message"></div>' +
            '<div class="modal-footer">' +
                '<button class="btn btn-default" ng-click="modal.dismiss()">No</button>' +
                '<button class="btn btn-primary" ng-click="modal.close()">Yes</button>' +
            '</div>',
            controller: function($scope, $uibModalInstance) {
                $scope.modal = $uibModalInstance;

                if (angular.isObject(message)) {
                    angular.extend($scope, message);
                } else {
                    $scope.message = message;
                    $scope.title = angular.isUndefined(title) ? 'Confirm' : title;
                }
            }
        });

        return modal.result;
    };
});

services.factory('$dialogAlert', function($uibModal) {
    return function(message, title) {
        var modal = $uibModal.open({
            size: 'sm',
            template:
            '<div class="modal-header">' +
                '<h4 class="modal-title" ng-bind="title"></h4></div>' +
            '<div class="modal-body" ng-bind="message"></div>' +
            '<div class="modal-footer">' +
                '<button class="btn btn-primary" ng-click="modal.close()">OK</button>' +
            '</div>',
            controller: function($scope, $uibModalInstance) {
                $scope.modal = $uibModalInstance;
                if (angular.isObject(message)) {
                    angular.extend($scope, message);
                } else {
                    $scope.message = message;
                    $scope.title = angular.isUndefined(title) ? 'Alert' : title;
                }
            }
        });

        return modal.result;
    };
});
