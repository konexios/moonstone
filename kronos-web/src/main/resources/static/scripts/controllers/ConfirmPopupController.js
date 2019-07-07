controllers.controller('ConfirmPopupController',
    [
        '$scope', '$uibModalInstance', 'options',
        function ($scope, $uibModalInstance, options) {
            $scope.options = angular.extend({
                title: 'Confirm',
                message: '',
                ok: 'OK',
                cancel: 'Cancel',
                html: false
            }, options);

            $scope.ok = function () {
                $uibModalInstance.close();
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);
