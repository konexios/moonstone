controllers.controller('TextModalController',
    [
        '$scope', '$uibModalInstance', 'options',
        function ($scope, $uibModalInstance, options) {
            $scope.options = angular.extend({
                title: null,
                text: null,
                close: 'Close',
            }, options);

            $scope.close = function () {
                //$uibModalInstance.close();
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);
