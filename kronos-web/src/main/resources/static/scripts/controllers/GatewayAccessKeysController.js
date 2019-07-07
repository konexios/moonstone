controllers.controller('GatewayAccessKeysController', ['$uibModal', '$routeParams', 'GatewayService', 'AccessKeyService', 'ErrorService', 'SpinnerService', 'SecurityService', 'ToastrService', GatewayAccessKeysController]);
function GatewayAccessKeysController($uibModal, $routeParams, GatewayService, AccessKeyService, ErrorService, SpinnerService, SecurityService, ToastrService) {
    var vm = this;
    vm.gatewayId = $routeParams.gatewayId;
    vm.pri = null;

    function init() {
        vm.filter = {
            pri: [vm.pri]
        };
        AccessKeysController.call(vm, $uibModal, AccessKeyService, ErrorService, SpinnerService, SecurityService, ToastrService);
    }

    SpinnerService.wrap(GatewayService.getGatewayPri, vm.gatewayId)
    .then(function(response) {
        vm.pri = response.data;
        init();
    })
    .catch(ErrorService.handleHttpError);
}
GatewayAccessKeysController.prototype = Object.create(AccessKeysController.prototype);

GatewayAccessKeysController.prototype.getDetailsObject = function(accessKey) {
    var detailsObject = AccessKeysController.prototype.getDetailsObject.call(this, accessKey);
    if (!accessKey) {
        // find gateway object with the same id
        var gatewayOption = null;
        for(var i=0; i<this.editOptions.gateways.length; i++) {
            if(this.gatewayId == this.editOptions.gateways[i].id) {
                gatewayOption = this.editOptions.gateways[i];
                break;
            }
        }
        detailsObject.privileges.push({
            level: 'READ',
            pri: this.pri,
            gateway: gatewayOption
        });
    }
    return detailsObject;
};
