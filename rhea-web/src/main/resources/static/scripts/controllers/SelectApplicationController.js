controllers.controller('SelectApplicationController',
    [
        '$scope', '$uibModalInstance', 'applications',
        function ($scope, $uibModalInstance, applications) {
            $scope.applications = applications;
            $scope.select = $uibModalInstance.close;
        }
    ]
);
