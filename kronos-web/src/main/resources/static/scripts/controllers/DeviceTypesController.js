controllers.controller('DeviceTypesController', ['$location', '$uibModal', 'DeviceTypeService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', DeviceTypesController]);

function DeviceTypesController($location, $uibModal, DeviceTypeService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Asset Types',
        resultTitle: 'Asset Type',
        columnHeaders: [
            {label: 'Name', value: 'name', sortable: true},
            {label: 'Description', value: 'description', sortable: true},
            {label: 'Asset Category', value: 'deviceCategory', sortable: true},
            {label: 'Editable', value: 'editable', sortable: false, renderFunc: function(val) { return val ? 'Yes': 'No'; }},
            {label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val) { return val ? 'Yes': 'No'; }},
            {label: 'In Use', value: 'numDevices', sortable: false, renderFunc: function(val) { return val > 0 ? 'Yes (' + val + ')': 'No'; }},
            {label: 'Last Modified', value: 'lastModifiedDate', sortable: false},
            {label: 'Last Modified By', value: 'lastModifiedBy', sortable: false, renderFunc: function(val) { return val === "" ? 'Unknown': val;}}
        ],
        tfootTopic: 'asset types',
        canAdd: SecurityService.canCreateDeviceType(),
        canEdit: true, // user can always open device type details to see device telemetries
        openDetails: openDetails
    });

    function openDetails(deviceType) {
        var deviceTypeId = deviceType != null ? deviceType.id : 'add';
            $location.path('/devicetype/' + deviceTypeId);
    }

    vm.find = function() {
        SpinnerService.wrap(DeviceTypeService.find, vm.pagination)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.find();
}
DeviceTypesController.prototype = Object.create(ListController.prototype);

DeviceTypesController.prototype.getCellText = function (item, column) {
    if (column.value == 'lastModifiedDate') {
        return moment(new Date(item.lastModifiedDate)).format('MM/DD/YYYY hh:mm:ss A');
    } else {
        return ListController.prototype.getCellText.call(this, item, column);
    }
};

