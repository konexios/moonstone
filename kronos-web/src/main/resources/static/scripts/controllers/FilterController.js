controllers.controller('FilterController',
    [
        '$scope', '$uibModalInstance', 'filter',
        function ($scope, $uibModalInstance, filter) {
            $scope.filter = angular.merge({}, filter);

            $scope.ok = function () {
                $uibModalInstance.close($scope.filter);
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

