controllers.controller('PromptPopupController',
    [
        '$scope', '$uibModalInstance', 'ToastrService', 'options',
        function ($scope, $uibModalInstance, ToastrService, options) {
            $scope.options = angular.extend({
                title: 'Prompt',
                label: 'Enter',
                value: '',
                required: false,
                notValidMessage: 'Validation failed, please check errors',
                ok: 'OK',
                cancel: 'Cancel'
            }, options);

            $scope.submit = function (form) {
                if (form.$valid) {
                    $uibModalInstance.close($scope.options.value);
                } else {
                    ToastrService.popupError($scope.options.notValidMessage);
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);
