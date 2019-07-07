controllers.controller('DeviceStateUpdateController',
    [
        '$scope', '$uibModalInstance', 'ToastrService', 'stateMetadata',
        function ($scope, $uibModalInstance, ToastrService, stateMetadata) {
            $scope.deviceState = {};
            $scope.model = {
                name: null,
                value: null
            };

            function updateOptions() {
                $scope.options = [];
                for(var key in stateMetadata) {
                    if (!(key in $scope.deviceState)) {
                        $scope.options.push(stateMetadata[key]);
                    }
                }
            }

            $scope.add = function() {
                $scope.deviceState[$scope.model.name] = $scope.model.value;
                $scope.model = {
                    name: null,
                    value: null
                };
                updateOptions();
            }

            $scope.delete = function(name) {
                delete $scope.deviceState[name];
                updateOptions();
            }

            $scope.save = function (form) {
                if (form.$valid) {
                    $uibModalInstance.close($scope.deviceState);
                } else {
                    ToastrService.popupError('Device State cannot be updated because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

            updateOptions();
        }
    ]
);
