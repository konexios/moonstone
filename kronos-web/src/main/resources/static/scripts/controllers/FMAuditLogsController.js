controllers.controller('FMAuditLogsController', ['$routeParams', '$uibModal', '$filter', '$sce', 'FMJobWizardService', 'CommonService', 'SecurityService', 'ErrorService', 'SpinnerService', FMAuditLogsController]);

function FMAuditLogsController($routeParams, $uibModal, $filter, $sce, FMJobWizardService, CommonService, SecurityService, ErrorService, SpinnerService) {
    var vm = this;
    var options = {
        assets: [],
        fromDate: null,
        types: [],
        users: []
    };
    var type2display = {
        CreateSoftwareReleaseSchedule: 'Created',
        UpdateSoftwareReleaseSchedule: 'Updated',
        SoftwareReleaseScheduleAssetStarted: 'Asset In Progress',
        SoftwareReleaseScheduleAssetReceived: 'Asset Received',
        SoftwareReleaseScheduleAssetManuallyReceived: 'Asset Manually Received',
        SoftwareReleaseScheduleAssetSucceeded: 'Asset Succeeded',
        SoftwareReleaseScheduleAssetFailed: 'Asset Failed',
        SoftwareReleaseScheduleAssetManuallyFailed: 'Asset Manually Failed',
        SoftwareReleaseScheduleAssetExpired: 'Asset Expired',
        SoftwareReleaseScheduleAssetRetried: 'Asset Retried',
        SoftwareReleaseScheduleAssetManuallyRetried: 'Asset Manually Retried',
        SoftwareReleaseScheduleAssetCancelled: 'Asset Cancelled',
        SoftwareReleaseScheduleAssetFirmwareDownload: 'Firmware Download',
        SoftwareReleaseScheduleAssetTempTokenCreated: 'Temp Token Created',
        SoftwareReleaseScheduleAssetTempTokenExpired: 'Temp Token Expired',
        SoftwareReleaseScheduleAssetCommandSentToGateway: 'Sent Command To Gateway',
        SoftwareReleaseScheduleAlert: 'Alerts'
    };
    var display2type = Object.keys(type2display).reduce(function(acc, type) {
        acc[type2display[type]] = type;
        return acc;
    }, {});

    LogsController.call(vm, $uibModal, $filter);

    // overrides
    vm.columnHeaders = [
        { label: 'Action', value: 'type', sortable: false, renderFunc: function(type) {
            return type2display[type];
        } },
        { label: 'Who', value: 'createdBy', sortable: false, renderFunc: function(who) {
            return who || '---';
        } },
        { label: 'Date/Time', value: 'createdDate', sortable: true, renderFunc: function(when) {
            return $filter('date')(when*1000, 'M/d/yyyy h:mm:ss a');
        } },
        { label: 'Log', value: 'logMessage', sortable: false, renderFunc: function(msg) {
        	if (msg === null || msg === undefined)
        		return "---";
        	
        	var htmlMsg = msg.replace(/\r\n/g, "<br>");
        	var trustedObject = $sce.trustAsHtml(htmlMsg);
        	var trustedHtml = $sce.getTrustedHtml(trustedObject);
            
            return trustedHtml;
        } }
//        ,
//        { label: 'Changes Made', value: 'parameters', sortable: false, renderFunc: function(parameters) {
//            var msg = '';
//            if (parameters instanceof Array) {
//                for(var i=0; i<parameters.length; i++) {
//                    if (msg.length > 0) {
//                        msg += '. ';
//                    }
//                    var entry = parameters[i];
//                    switch(entry.parameter) {
//                        case 'scheduledDate':
//                            msg += 'Scheduled date changed from '+($filter('date')(entry.oldValue, 'M/d/yyyy h:mm a'))+' to '+($filter('date')(entry.newValue, 'M/d/yyyy h:mm a'));
//                            break;
//                        case 'status':
//                            msg += 'Status changed from '+entry.oldValue+' to '+entry.newValue;
//                            break;
//                        case 'assetAdded':
//                            msg += 'Asset '+entry.newValue+' was added to job';
//                            break;
//                        case 'assetRemoved':
//                            msg += 'Asset '+entry.oldValue+' was removed from job';
//                            break;
//                        case 'assetName':
//                            msg += 'Asset '+entry.newValue+' Firmware Upgrade failed';
//                            break;
//                    }
//                }
//            }
//            return msg; // TODO
//        } }
    ];

    vm.find = function() {
        var filter = angular.extend({}, vm.filter);
        filter.types = filter.types.map(function(display) {
            return display2type[display];
        });
        // user model can contain several ids (API 'user')
        var userIds = [];
        filter.userIds.forEach(function(user) {
            user.ids.forEach(function(id) {
                userIds.push(id);
            });
        });
        filter.userIds = userIds;
        SpinnerService.wrap(FMJobWizardService.findJobAuditLogs, $routeParams.id, vm.pagination, filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.getAuditLog = function(log) {
        return SpinnerService.wrap(FMJobWizardService.getJobAuditLog, log.id)
        .catch(ErrorService.handleHttpError);
    };

    vm.find();
    
    // override LogsController, needed to present a custom UI to support custom filters
    vm.open = function(size) {
    	
        if (!vm.filter.hasOwnProperty('assetIds')) {
        	vm.filter.assetIds = [];
        }
    	
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/firmwaremanagement/software-release-schedule-log-filter.html',
            controller: 'SoftwareReleaseScheduleLogFilterController',
            controllerAs: 'vm',
            size: size,
            resolve: {
                filter: vm.filter,
                type2display: type2display,
                options: options,
                routeId: {
                    value: $routeParams.id
                }
            }
        });

        modalInstance.result.then(
            function (result) {
                vm.filter = result.filter;
                options = result.options;
                vm.pagination.pageIndex = 0;
                vm.find();
            }
        );
    };
}
FMAuditLogsController.prototype = Object.create(LogsController.prototype);

// custom controller to support custom filters
controllers.controller('SoftwareReleaseScheduleLogFilterController', ['$scope','$uibModalInstance', 'filter', 'type2display', 'routeId', 'options', 'FMJobWizardService', 'SpinnerService', 'ErrorService', SoftwareReleaseScheduleLogFilterController]);
function SoftwareReleaseScheduleLogFilterController($scope, $uibModalInstance, filter, type2display, routeId, options, FMJobWizardService, SpinnerService, ErrorService) {
    var vm = this;
    vm.filter = angular.merge({}, filter);
    vm.options = options;
    var type2display = type2display;

    function init() {
        if (options.assets.length !== 0 || options.types.length !== 0 || options.users.length !== 0 || options.fromDate != null) {
            vm.options = options;
        } else {
            $scope.getOptions(routeId.value);
        }
    }

    $scope.getOptions = function(id) {
        SpinnerService.wrap(FMJobWizardService.getJobAuditLogOptions, id)
        .then(function(response) {
            vm.options = response.data;
            vm.options.types = vm.options.types.map(function(type) {
                return type2display[type];
            });
            startDateOnSetTime();
        })
        .catch(ErrorService.handleHttpError);
    }

    init();

    vm.ok = function () {
        var result = {
            filter: vm.filter,
            options: vm.options
        };
        $uibModalInstance.close(result);
    };
    
    vm.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    // Date range picker with validation controls
    vm.startDateOnSetTime = startDateOnSetTime;
    vm.endDateOnSetTime = endDateOnSetTime;
    vm.startDateBeforeRender = startDateBeforeRender;
    vm.endDateBeforeRender = endDateBeforeRender;

    function startDateOnSetTime() {
        $scope.$broadcast('start-date-changed');
    }

    function endDateOnSetTime() {
        $scope.$broadcast('end-date-changed');
    }

    function startDateBeforeRender($view, $dates) {

        // nth: refactoring, calculate unavailable dates in one cycle
        if (vm.filter.createdDateTo) {
            var activeDate = moment(vm.filter.createdDateTo);

            $dates.filter(function(date) {
                return date.localDateValue() >= activeDate.valueOf();
            })
            .forEach(function(date) {
                date.selectable = false;
            });
        }

        if (vm.options.fromDate) {
            var fromDate = moment(vm.options.fromDate).startOf('day').subtract(1, $view);

            $dates.filter(function(date) {
                return date.localDateValue() <= fromDate.valueOf();
            })
            .forEach(function(date) {
                date.selectable = false;
            });
        }

        var now = moment();
        $dates.filter(function(date) {
            return date.localDateValue() >= now.valueOf();
        })
        .forEach(function(date) {
            date.selectable = false;
        });
    }

    function endDateBeforeRender($view, $dates) {
        if (vm.filter.createdDateFrom) {
            var activeDate = moment(vm.filter.createdDateFrom).subtract(1, $view).add(1, 'minute');

            $dates.filter(function(date) {
                return date.localDateValue() <= activeDate.valueOf();
            })
            .forEach(function(date) {
                date.selectable = false;
            });
        }
        
        var now = moment().endOf("day");
        $dates.filter(function(date) {
            return date.localDateValue() >= now.valueOf();
        })
        .forEach(function(date) {
            date.selectable = false;
        });
    }
    
}
