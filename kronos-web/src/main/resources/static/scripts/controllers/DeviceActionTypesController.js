controllers.controller('DeviceActionTypesController', ['$uibModal', 'DeviceActionTypeService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', DeviceActionTypesController]);

function DeviceActionTypesController($uibModal, DeviceActionTypeService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'systemName'
        }
    }, {
        pageTitle: 'Device Action Types',
        resultTitle: 'Device Action Type',
        columnHeaders: [
            {label: 'System Name', value: 'systemName', sortable: true},
            {label: 'Name', value: 'name', sortable: true},
            {label: 'Description', value: 'description', sortable: true},
            {label: 'Editable', value: 'editable', sortable: false},
            {label: 'Enabled', value: 'enabled', sortable: false},
            {label: 'Last Modified', value: 'lastModifiedDate', sortable: false},
            {label: 'Last Modified By', value: 'lastModifiedBy', sortable: false, renderFunc: function(val) { return val === "" ? 'Unknown': val;}}
        ],
        tfootTopic: 'device action types',
        canAdd: SecurityService.canCreateDeviceActionType(),
        canEdit: true, // user can always open device action type details to see parameters
        openDetails: openDetails
    });

    function openDetails(deviceActionType) {
        deviceActionType = deviceActionType || {
            systemName: '',
            name: '',
            description: '',
            editable: true,
            enabled: true,
            parameters: {}
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/deviceactiontype/device-action-type-details.html',
            controller: 'DeviceActionTypeDetailsController',
            size: 'lg',
            resolve: {
                deviceActionType: deviceActionType
            }
        });

        modalInstance.result.then(function(deviceActionType) {
            SpinnerService.wrap(DeviceActionTypeService.save, deviceActionType)
            .then(function(response) {
                ToastrService.popupSuccess('Device Action Type ' + deviceActionType.name + ' has been saved successfully');
                // re-read the current page
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(DeviceActionTypeService.find, vm.pagination)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.find();
}
DeviceActionTypesController.prototype = Object.create(ListController.prototype);

DeviceActionTypesController.prototype.getCellText = function (item, column) {
    if (column.value == 'lastModifiedDate') {
        return moment(new Date(item.lastModifiedDate)).format('MM/DD/YYYY hh:mm:ss A');
    } else {
        return ListController.prototype.getCellText.call(this, item, column);
    }
};

controllers.controller('DeviceActionTypeDetailsController',
    [
        '$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'deviceActionType',
        function ($scope, $uibModalInstance, SecurityService, ToastrService, deviceActionType) {
            $scope.deviceActionType = angular.merge({}, deviceActionType);

            $scope.parameters = [];
            if ($scope.deviceActionType.parameters) {
                for(var name in $scope.deviceActionType.parameters) {
                    $scope.parameters.push({
                        name: name,
                        value: $scope.deviceActionType.parameters[name],
                        editing: false
                    })
                }
            }
            

            $scope.canSaveDeviceActionType = $scope.deviceActionType.editable && (
                ($scope.deviceActionType.id && SecurityService.canEditDeviceActionType())
                || (!$scope.deviceActionType.id && SecurityService.canCreateDeviceActionType()));

            $scope.addParameter = function() {
                $scope.parameters.push({
                    name: '',
                    value: '',
                    editing: true
                });
            };

            $scope.editParameter = function(index) {
                $scope.parameters[index].editing = !$scope.parameters[index].editing;
            };

            $scope.deleteParameter = function(index) {
                $scope.parameters.splice(index, 1);
            };

            $scope.save = function(form) {
                if (form.$valid) {
                    $scope.deviceActionType.parameters = {};
                    for(var i=0; i<$scope.parameters.length; i++) {
                        $scope.deviceActionType.parameters[$scope.parameters[i].name] = $scope.parameters[i].value;
                    }
                    $uibModalInstance.close($scope.deviceActionType);
                } else {
                    ToastrService.popupError('Device Action Type cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
    }
