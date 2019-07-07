controllers.controller('DeviceAccessKeysController', ['$uibModal', '$routeParams', 'DeviceService', 'AccessKeyService', 'ErrorService', 'SpinnerService', 'SecurityService', 'ToastrService', DeviceAccessKeysController]);
function DeviceAccessKeysController($uibModal, $routeParams, DeviceService, AccessKeyService, ErrorService, SpinnerService, SecurityService, ToastrService) {
    var vm = this;
    vm.deviceId = $routeParams.deviceId;
    vm.pri = null;

    function init() {
        vm.filter = {
            pri: [vm.pri]
        };
        AccessKeysController.call(vm, $uibModal, AccessKeyService, ErrorService, SpinnerService, SecurityService, ToastrService);
    }

    SpinnerService.wrap(DeviceService.getDevicePri, vm.deviceId)
    .then(function(response) {
        vm.pri = response.data;
        init();
    })
    .catch(ErrorService.handleHttpError);
}
DeviceAccessKeysController.prototype = Object.create(AccessKeysController.prototype);

DeviceAccessKeysController.prototype.getDetailsObject = function(accessKey) {
    var detailsObject = AccessKeysController.prototype.getDetailsObject.call(this, accessKey);
    if (!accessKey) {
        // find device object with the same id
        var deviceOption = null;
        for(var i=0; i<this.editOptions.devices.length; i++) {
            if(this.deviceId == this.editOptions.devices[i].id) {
                deviceOption = this.editOptions.devices[i];
                break;
            }
        }
        detailsObject.privileges.push({
            level: 'READ',
            pri: this.pri,
            device: deviceOption
        });
    }
    return detailsObject;
};
