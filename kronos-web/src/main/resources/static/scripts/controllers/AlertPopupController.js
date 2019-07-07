controllers.controller('AlertPopupController',
    [
        '$scope', '$uibModalInstance', 'options',
        function ($scope, $uibModalInstance, options) {
            $scope.options = angular.extend({
                title: 'Alert',
                message: '',
                close: 'Close',
                html: false
            }, options);

            $scope.close = function () {
                $uibModalInstance.close();
            };
        }
    ]
);
