controllers.controller('DeviceController', [
    '$scope', '$uibModalInstance', 'DeviceService', 'ErrorService', 'SpinnerService', 'ToastrService',
    function ($scope, $uibModalInstance, DeviceService, ErrorService, SpinnerService, ToastrService) {

    	$scope.device = { enabled: true, info: {}, properties: {} };
        $scope.options = null;
        $scope.deviceTypeOptions = [];

        function loadDevice() {
            $scope.getOptions(null);
        }

        $scope.getOptions = function(deviceTypeId) {
            SpinnerService.wrap(DeviceService.getEditOptions, deviceTypeId)
                .then(function (response) {
                    $scope.options = response.data;
                })
                .catch(ErrorService.handleHttpError);
        };

        $scope.save = function(form) {
            form.$setSubmitted();
            if (form.$invalid) {
                ToastrService.popupError('Device cannot be saved because of invalid fields, please check errors.');
            } else {
                SpinnerService.wrap(DeviceService.create, $scope.device)
                .then(function(response) {
                    $scope.device = response.data;
                    ToastrService.popupSuccess('Device ' + $scope.device.name + ' has been saved successfully');
                    $uibModalInstance.close();
                })
                .catch(ErrorService.handleHttpError);
            }
        };

        $scope.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };

        loadDevice();
    }
]);
