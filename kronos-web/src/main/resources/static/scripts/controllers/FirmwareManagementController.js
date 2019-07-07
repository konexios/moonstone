function FirmwareManagementController($location,$uibModal, CommonService) {
    var vm = this;

    vm.showUpdateNow = function(){
        // TODO: show update now
    };

    vm.showError = function(item) {
        $uibModal.open({
            animation: false,
            templateUrl: 'partials/list/list-text-modal.html',
            controller: 'TextModalController',
            controllerAs: 'vm',
            animation: !!window.MSInputMethodContext && !!document.documentMode,
            resolve: {
                options: {
                    title: 'Error',
                    text: item.error
                }
            }
        });
    };

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'started',
            direction: 'DESC'
        }
    }, {
        pageTitle: 'History',
        pageTitleIcon: 'fa-cloud-download',
        resultTitle: 'Firmware Releases',
        columnHeaders: [
            { label: 'Job Name', value: 'jobName', sortable: false},
            { label: 'From', value: 'fromVersion', sortable: false },
            { label: 'To', value: 'toVersion', sortable: false },
            { label: 'Start', value: 'started', sortable: true, renderFunc: CommonService.getFormatredDate },
            { label: 'End', value: 'ended', sortable: true, renderFunc: CommonService.getFormatredDate },
            {
                label: 'Status',
                value: 'status',
                sortable: false,
                cellAction: function(item) { return item.status == 'ERROR' ? vm.showError(item) : false; },
                renderFunc: function(val) { return val == 'ERROR' ? '! ' + val : val; }
            },
            {
                label: 'Method',
                value: 'invalid',
                sortable: false,
                renderFunc: function(val) { return val == null ? 'On Demand' : 'Scheduled'; }
            },
        ],
        canAdd: false,
        canEdit: true,
        openFilter: null,
        openDetails: function(job) {	
        	$location.url('/fmaudit/'+job.softwareReleaseScheduleId);
        }
    	//,
        //buttons: [{caption: 'Upgrade Now', icon: '', onClick: vm.showUpdateNow}]
    });

    vm.filter = {};
}
FirmwareManagementController.prototype = Object.create(ListController.prototype);

FirmwareManagementController.prototype.displayState = function(index, column, item) {
	if(column.label=='Job Name'){
		return 'edit';
	}else if (item.status !== 'ERROR' && column.label=='Status') {         
		 return 'text';     
	} else {
		 return ListController.prototype.displayState.call(this, index, column, item);   
	}
};


controllers.controller('DeviceFirmwareManagementController', ['$location','$routeParams', '$uibModal', 'DeviceService', 'CommonService', 'SecurityService', 'ErrorService', 'SpinnerService', DeviceFirmwareManagementController]);

function DeviceFirmwareManagementController($location,$routeParams, $uibModal, DeviceService, CommonService, SecurityService, ErrorService, SpinnerService) {
    var vm = this;

    FirmwareManagementController.call(vm,$location, $uibModal, CommonService);

    var canViewFirmwareManagement = SecurityService.canReadSoftwareReleaseTrans();
    
    vm.find = function() {
        if (canViewFirmwareManagement) {
            SpinnerService.wrap(DeviceService.findSoftwareReleaseTrans, $routeParams.deviceId, vm.pagination, vm.filter)
                .then(function(response) {
                    vm.update(response.data.result);
                })
                .catch(ErrorService.handleHttpError);
        }
    };
    vm.find();
}
DeviceFirmwareManagementController.prototype = Object.create(FirmwareManagementController.prototype);


controllers.controller('GatewayFirmwareManagementController', ['$location','$routeParams', '$uibModal', 'GatewayService', 'CommonService', 'SecurityService', 'ErrorService', 'SpinnerService', GatewayFirmwareManagementController]);

function GatewayFirmwareManagementController($location, $routeParams, $uibModal, GatewayService, CommonService, SecurityService, ErrorService, SpinnerService) {
    var vm = this;

    FirmwareManagementController.call(vm, $location, $uibModal, CommonService);

    var canViewFirmwareManagement = SecurityService.canReadSoftwareReleaseTrans();

    vm.find = function() {
        if (canViewFirmwareManagement) {
            SpinnerService.wrap(GatewayService.findSoftwareReleaseTrans, $routeParams.gatewayId, vm.pagination, vm.filter)
                .then(function(response) {
                    vm.update(response.data.result);
                })
                .catch(ErrorService.handleHttpError);
        }
    };

    vm.find();
}
GatewayFirmwareManagementController.prototype = Object.create(FirmwareManagementController.prototype);