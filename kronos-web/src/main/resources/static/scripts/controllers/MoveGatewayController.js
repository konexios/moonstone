controllers.controller('MoveGatewayController', [
    '$scope', '$uibModalInstance', '$uibModal', 'GatewayService', 'ErrorService', 'SpinnerService', 'ToastrService',
    function ($scope, $uibModalInstance, $uibModal, GatewayService, ErrorService, SpinnerService, ToastrService) {

        $scope.model = {
            uid: null
        };
        $scope.gateways = [];

        function makeHtmlList(prefix, items, suffix) {
            return '<p>' + prefix + items.reduce(function(acc, item, index) {
                return acc + (index > 0 ? ', ' : '') + item.name;
            }, '') + suffix + '</p>';
        }

        $scope.associate = function(gateway, useExistingTypes) {
            SpinnerService.wrap(GatewayService.moveGateway, gateway.id, useExistingTypes)
            .then(function(response) {
                var result = response.data;
                if (result.moved) {
                    ToastrService.popupSuccess('Gateway ' + result.name + ' has been moved successfully');
                    $uibModalInstance.close(result);
                } else {
                    var message = '';
                    if (result.existingDeviceTypes && result.existingDeviceTypes.length > 0) {
                        message += makeHtmlList('WARNING: The following device types already exist in the current application - ', 
                            result.existingDeviceTypes, '. If you continue, the gateway and/or devices will be associated to the local device types.');
                    }
                    if (result.existingDeviceActionTypes && result.existingDeviceActionTypes.length > 0) {
                        message += makeHtmlList('WARNING: The following custom device action types already exist in the current application - ',
                            result.existingDeviceActionTypes, '. If you continue, the device actions will be associated to the local device action types.');
                    }
                    $uibModal.open({
                        animation: false,
                        templateUrl: 'partials/confirm-popup.html',
                        controller: 'ConfirmPopupController',
                        size: 'lg',
                        resolve: {
                            options: {
                                html: true,
                                title: 'Confirm using local objects',
                                message: message,
                                ok: 'Continue'
                            }
                        }
                    }).result.then(function() {
                        $scope.associate(gateway, true);
                    });
                }
            })
            .catch(ErrorService.handleHttpError);
        };

        $scope.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.find = function() {
            SpinnerService.wrap(GatewayService.findGatewaysByUid, $scope.model.uid)
            .then(function(response){
                $scope.gateways = response.data;
            }).catch(ErrorService.handleHttpError);
        };
    }
]);
