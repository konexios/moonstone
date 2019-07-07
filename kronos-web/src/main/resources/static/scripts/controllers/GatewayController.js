controllers.controller('GatewayController', [
    '$scope', '$uibModalInstance', 'GatewayService', 'ErrorService', 'SpinnerService', 'ToastrService',
    function ($scope, $uibModalInstance, GatewayService, ErrorService, SpinnerService, ToastrService) {

        $scope.gateway = {};
        $scope.options = {
            types: [],
            users: []
        };

        $scope.save = function(form) {
            if (form.$valid) {
                SpinnerService.wrap(GatewayService.create, $scope.gateway)
                .then(function(response) {
                    $scope.gateway = response.data;
                    ToastrService.popupSuccess('Gateway ' + $scope.gateway.name + ' has been created successfully');
                    $uibModalInstance.close($scope.gateway);
                })
                .catch(ErrorService.handleHttpError);
            } else {
                ToastrService.popupError('Gateway cannot be created because of invalid fields, please check errors.');
            }
        };

        $scope.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };

        SpinnerService.wrap(GatewayService.newGateway)
        .then(function(response){
            $scope.gateway = response.data.gateway;
            $scope.options = response.data.options;
        }).catch(ErrorService.handleHttpError);
    }
]);
