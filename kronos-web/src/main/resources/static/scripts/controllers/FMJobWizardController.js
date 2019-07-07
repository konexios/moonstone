controllers.controller('FMJobWizardController', ['$rootScope', '$scope', '$routeParams', '$location', 'FMJobWizardService', 'ErrorService', 'SpinnerService', 'SecurityService', 'ToastrService', FMJobWizardController]);
function FMJobWizardController($rootScope, $scope, $routeParams, $location, FMJobWizardService, ErrorService, SpinnerService, SecurityService, ToastrService) {

    var vm = this;
    var timeToExpireMinutesDefault = 10080;
    vm.model = {
        available: [],
        chosen: [],
        objectIds: [],
        requester: {
            firstName: SecurityService.getUser().firstName,
            lastName: SecurityService.getUser().lastName
        },
        timezone: Util.getTimezone(),
        notification: {
            email:""
        },
        timeToExpireSeconds: timeToExpireMinutesDefault
    };
    vm.notification = {
        emailsIsValid: true
    };
    vm.initNewSchedule = false;
    vm.filter = {
        softwareReleaseScheduleId: $routeParams.id || null
    };
    vm.filterOptions = {
        selectedDevice: null,
        selection: {},
        availableObjects: [],
        softwareReleases: []
    };
    vm.filterResult = {};
    vm.isEditJobPage = !!$routeParams.id;
    vm.steps = [
          {
              stepNum: 1,
              name: "deviceSelection",
              isFirst: true,
              isLast: false,
              urlParam: 'DEVICE-SELECTION',
              behaviour: deviceSelectionStep
          },
          {
              stepNum: 2,
              name: "scheduling",
              isFirst: false,
              isLast: false,
              urlParam: 'SCHEDULING',
              behaviour: schedulingStep
          },
          {
              stepNum: 3,
              name: "notification",
              isFirst: false,
              isLast: false,
              urlParam: 'NOTIFICATION',
              behaviour: notificationStep
          },
          {
              stepNum: 4,
              name: "summary",
              isFirst: false,
              isLast: true,
              urlParam: 'SUMMARY',
              behaviour: summaryStep
          }
      ];

     vm.currentStep = vm.steps[0];

    vm.canSchedulingOnDemand = SecurityService.canSchedulingOnDemand;
    vm.canSchedulingByDateTime = SecurityService.canSchedulingByDateTime;


    //Layout and left menu behavior
    vm.menuIsHidden = function() {
        return vm.mobileLayot ? !vm.mobileMenuShowed : vm.collapsedView;
    };
    vm.collapseMenu = function() {
        if (vm.mobileLayot) {
            vm.mobileMenuShowed = !vm.mobileMenuShowed;
        } else {
            vm.collapsedView = !vm.collapsedView;
        }
    };
    vm.startDateBeforeRender = function($dates, $view) {
        var activeDate = moment().subtract(1, $view).add(1, 'hour');

        $dates.filter(function (date) {
            return date.localDateValue() <= activeDate.valueOf()
        }).forEach(function (date) {
            date.selectable = false;
        })
    };

    vm.goToNext = function() {
        if (vm.currentStep.isLast) return false;
        vm.currentStep = vm.steps[vm.currentStep.stepNum];
        vm.currentStep.behaviour();
    };
    vm.goToBack = function() {
        if (vm.currentStep.isFirst) return false;
        vm.currentStep = vm.steps[vm.currentStep.stepNum-2];
        vm.currentStep.behaviour();
    };

    //First step (Device Selection)
    function deviceSelectionStep() {
   	
    	if ($routeParams.assetTypeId && $routeParams.firmwareVersionId) {
    		vm.filter.deviceTypeId = $routeParams.assetTypeId;
    		vm.filter.softwareReleaseId = $routeParams.firmwareVersionId;
    		//vm.find();
    	}
    	
        vm.selectionUpdated = function () {
            SpinnerService.wrap(FMJobWizardService.getSelectionOptions, vm.filter)
                .then(updateTables)
                .catch(ErrorService.handleHttpError);
        };
        vm.changeSoftwareVersion = function () {

        };
        vm.find = function () {
            var swReleaseScheduleId = vm.filter.softwareReleaseScheduleId;
            vm.model.objectIds = [];

            if (swReleaseScheduleId) {

                SpinnerService.wrap(FMJobWizardService.findOne, swReleaseScheduleId)
                    .then(function (response) {
                        angular.merge(vm.model, response.data);
                        vm.model.dateTime = vm.model.onDemand || !vm.model.scheduledDate ? null : new Date(vm.model.scheduledDate);
                        vm.filter.softwareReleaseId = vm.model.softwareReleaseId;
                        vm.filter.deviceCategory = vm.model.deviceCategory;
                        // setup notification model:
                        vm.model.notification.email = vm.model.notifyEmails;
                        vm.model.notification.sendWhenSubmitted = vm.model.notifyOnSubmitted;
                        vm.model.notification.sendWhenStarted = vm.model.notifyOnStart;
                        vm.model.notification.sendWhenCompleted = vm.model.notifyOnEnd;

                        // transfer timeToExpire from seconds to minutes () or set default value
                        vm.model.timeToExpireSeconds = response.data.timeToExpireSeconds ? Math.round(response.data.timeToExpireSeconds / 60) : timeToExpireMinutesDefault;

                        SpinnerService.wrap(FMJobWizardService.getSelectionOptions, vm.filter)
                            .then(updateTables)
                            .catch(ErrorService.handleHttpError);

                    })
                    .catch(ErrorService.handleHttpError);
            } else {
                // init options
                vm.initNewSchedule = true;
                SpinnerService.wrap(FMJobWizardService.getSelectionOptions, vm.filter)
                    .then(updateTables)
                    .catch(ErrorService.handleHttpError);
            }
        };
        vm.filterUpdate = function() {
            if (vm.model.availableObjects === undefined || vm.model.availableObjects.length === 0) return;

            var filteredDevices = vm.model.availableObjects.filter(function(device) {
                for (var propName in vm.filterResult) {
                    if (!vm.filterResult.hasOwnProperty(propName)) continue;

                    var filterProperty = vm.filterResult[propName];

                    if (filterProperty instanceof Array) {
                        if (filterProperty.length === 0) continue;

                        var foundEq = false;

                        for (var i = 0; filterProperty.length > i; i++) {
                            if (filterProperty[i] !== device[propName]) {
                                continue;
                            }
                            foundEq = true;
                            break;
                        }

                        if (!foundEq) return false;
                    } else {
                        if (filterProperty === undefined || filterProperty.length === 0) continue;

                        if ((typeof device[propName] !== 'string' && filterProperty !== device[propName]) || (typeof device[propName] === 'string' && device[propName].toLowerCase().indexOf(filterProperty.toLowerCase()) === -1)) {
                            return false;
                        }
                    }
                }
                return device;
            });

            vm.model.available = [];

            filteredDevices.forEach(function(device) {
                if (vm.model.objectIds.indexOf(device.id) < 0) {
                    vm.model.available.push(device);
                }
            });

            $rootScope.$broadcast('wizardDevice_receivedData', vm.model);
        };

        function loadParamsForDeviceSelection(response) {
            vm.filterOptions = response.data;
            
            if ($routeParams.assetTypeId && $routeParams.firmwareVersionId) {
            	vm.find();
            }
        }

        function loadSoftwareReleases(response) {
            vm.filterOptions = response.data;
            vm.filter.softwareReleaseId = null;
            vm.blocked = true;
        }

        function updateTables(response) {
            var data = response.data;

            vm.filterOptions = data;
            // drop once the options has been received
            vm.filter.softwareReleaseScheduleId = undefined; 
            vm.filter.deviceTypeId = data.selection.deviceTypeId;
            vm.filter.softwareReleaseId = data.selection.softwareReleaseId;

            vm.model.availableObjects = data.availableObjects || [];
            vm.model.deviceTypes = data.deviceTypes;
            vm.model.selection = data.selection;
            vm.model.softwareReleases = data.softwareReleases || [];
            vm.model.timezones = data.timezones;
            vm.model.softwareReleaseId = vm.filter.softwareReleaseId;
            vm.model.deviceCategory = vm.filter.deviceCategory;


            if (vm.initNewSchedule) {
                vm.initNewSchedule = false;
                vm.model.notifyEmails = vm.filterOptions.defaultSoftwareReleaseEmails;
            }

            vm.model.available = [];
            vm.model.chosen = [];
            
            vm.model.availableObjects.forEach(function (device) {

                device.type = getDeviceType(device);
                device.group = getGroupName(device);

                device.latestSoftwareUpgrade = device.latestSoftwareUpgrade === null ? device.latestSoftwareUpgrade : "---";

                if (vm.model.objectIds.indexOf(device.id) >= 0) {
                    vm.model.chosen.push(device);
                } else {
                    vm.model.available.push(device);
                }
            });

            //set selected swVersion
            vm.model.softwareRelease = vm.model.softwareReleases.filter(function (softwareRelease) {
                return softwareRelease.id === vm.filter.softwareReleaseId;
            })[0];

            $rootScope.$broadcast("wizardDevice_receivedData", vm.model);
        }

        function getDeviceType(device) {

            if (device.typeId) {
                var types = vm.model.deviceTypes.filter(function (t) {
                    return t.id === device.typeId;
                });
                return types.length > 0 ? types[0] : null;
            }

            return null;
        }

        function getGroupName(device) {

            if (device.nodeName) {
                return device.nodeName;
            }

            if (vm.model.deviceCategory === "GATEWAY") {
                return device.type.name;
            }

            return "---";
        }

        var available = vm.model.available;
        var chosen = vm.model.chosen;

        if ((available === undefined || available.length === 0) && (chosen === undefined || chosen.length === 0)) {
            // SpinnerService.wrap(FMJobWizardService.getSelectionOptions, vm.filter)
            //     .then(loadParamsForDeviceSelection)
            //     .catch(ErrorService.handleHttpError);
            vm.find();
        }
    }

    //Second step
    function schedulingStep() {

    }

    //Third step
    function notificationStep() {

        vm.hasNotifications = function() {

            if (vm.model.notification === undefined) {
                return true;
            }

            var hasEmails = vm.model.notification.email !== undefined && vm.model.notification.email.length > 0;

            var isCheckedAny =
                vm.model.notification.sendWhenSubmitted === true ||
                vm.model.notification.sendWhenStarted === true ||
                vm.model.notification.sendWhenCompleted === true;

            return (hasEmails && isCheckedAny) || (!hasEmails && !isCheckedAny);
        };

        vm.checkValidEmails = function() {

            var emails = vm.model.notification.email;

            if (emails === undefined || emails.length === 0) {
                vm.notification.emailsIsValid = true;
                return;
            }

            if (emails.length < 6) {
                vm.notification.emailsIsValid = false;
                return;
            }

            var EMAIL_REGEXP = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;

            var emailList = emails
                .replace(/\s/g, '')
                .toLowerCase()
                .split(",");

            for (var i = 0; emailList.length > i; i++) {
                if (!EMAIL_REGEXP.test(emailList[i])) {
                    vm.notification.emailsIsValid = false;
                    return;
                }
            }
            vm.notification.emailsIsValid = true;
        }
    }

    //Fourth step
    function summaryStep() {

        vm.summary = {
            devicesByType: [],
            softwareRelease: {},
            busy: false
        };

        vm.model.chosen.forEach(function(item){

            var typeList = vm.summary.devicesByType.filter(function (deviceType) {
                return deviceType.typeId === item.type.id;
            });

            var type = null;

            if (typeList === undefined || typeList === null || typeList.length === 0) {
                type = {
                    typeId: item.type.id,
                    typeName: item.type.name,
                    hwVersionName: item.hwVersionName,
                    devices: []
                };
                vm.summary.devicesByType.push(type)
            } else {
                type = typeList[0];
            }

            type.devices.push(item);
        });

        vm.submit = function() {

            var swReleaseSchedule = {
                id: vm.model.id,
                jobName: vm.model.jobName,
                deviceTypeId: vm.model.selection.deviceTypeId,
                deviceCategory: vm.model.selection.deviceCategory,
                localTimezone: Util.getTimezone(),
                notifyEmails: vm.model.notification.email,
                notifyOnSubmitted: vm.model.notification.sendWhenSubmitted === undefined ? false : vm.model.notification.sendWhenSubmitted,
                notifyOnStart: vm.model.notification.sendWhenStarted === undefined ? false : vm.model.notification.sendWhenStarted,
                notifyOnEnd: vm.model.notification.sendWhenCompleted === undefined ? false : vm.model.notification.sendWhenCompleted,
                timeToExpireSeconds: vm.model.timeToExpireSeconds ? vm.model.timeToExpireSeconds * 60 : timeToExpireMinutesDefault * 60,  // transfer timeToExpire from minutes to seconds or set default value
                objectIds: vm.model.objectIds,
                scheduledDate: !vm.model.onDemand ? vm.model.dateTime : null,
                onDemand: vm.model.onDemand,
                softwareReleaseId: vm.model.softwareReleaseId,
                targetTimezone: vm.model.timezone,
                comments: vm.model.comments
            };

            vm.summary.busy = true;

            SpinnerService.wrap(FMJobWizardService.save, swReleaseSchedule)
                .then(function() {
                    ToastrService.popupSuccess('Scheduled job has been saved successfully.');
                    $location.url("/fmsummary/SCHEDULED");
                })
                .catch(ErrorService.handleHttpError);
        };

        vm.makeChanges = function() {
            vm.currentStep = vm.steps[0];
        }
    }

    //init
    vm.currentStep.behaviour();
}

//TABLES OF DEVICES
function AbstractTableDevices($rootScope, SecurityService, resultTitle, noResultsMsg) {

    var vm = this;

    vm.paginationConfig = {
        itemsPerPage: 10,
        sort: {
            property: 'name',
            direction: 'DESC'
        }
    };
    
    vm.tableConfig = {
        resultTitle: resultTitle,
        hideResultIcon: true,
        noResultsMsg: noResultsMsg,
        columnHeaders: [
            {label: 'Asset', value: 'name', sortable: false},
            {label: 'UID', value: 'uid', sortable: false},
            {label: 'Type', value: 'type.name', sortable: false},
            {label: 'Group', value: 'group', sortable: false},
            {label: 'Owner', value: 'ownerName', sortable: false},
            {label: 'Firmware Version', value: 'softwareReleaseName', sortable: false},
            {label: 'Last Firmware Change', value: 'latestSoftwareUpgrade', sortable: false}
        ],
        tfootTopic: 'assets',
        canAdd: false,
        canEdit: false,
        bulkEdit: true,
        disabledSelectionButton: false,
        openFilter: openFilter,
        openDetails: openDetails
    };
    
    vm.filterOptions = {
        categories: [],
        statuses: []
    };
    
    vm.model = {};

    vm.updatePagination = function () {
        console.log("must be implemented");
    };
    
    vm.find = function () {
        vm.update(vm.model);
    };

    $rootScope.$on("wizardDevice_receivedData", function (event, data) {
        vm.model = data;
        vm.update(vm.model);
    });

    $rootScope.$on("wizardDevice_updatePagination", function () {

        if (vm.model.available !== undefined) {
            vm.model.available = vm.model.available.filter(function (val) {
                return val !== undefined
            });
        }
        if (vm.model.chosen !== undefined) {
            vm.model.chosen = vm.model.chosen.filter(function (val) {
                return val !== undefined
            });
        }
        vm.clearSelection();
        vm.updatePagination();
    });

    ListController.call(vm, vm.paginationConfig, vm.tableConfig);

    function openFilter() {

    }

    function openDetails(swReleaseSchedule) {

    }
}
AbstractTableDevices.prototype = Object.create(ListController.prototype);

//Device available
controllers.controller('FMWizardAvailableDevicesTable', ['$rootScope', '$routeParams', 'SecurityService', 'SpinnerService', 'FMJobWizardService', 'ErrorService', FMWizardAvailableDevicesTable]);
function FMWizardAvailableDevicesTable($rootScope, $routeParams, SecurityService, SpinnerService, FMJobWizardService, ErrorService) {

    var vm = this;
    vm.resultTitle = "Eligible Assets";

    AbstractTableDevices.call(vm, $rootScope, SecurityService, "Eligible Assets", "No eligible assets were found");

    vm.update = function (data) {
        vm.allItemIds = data.available.filter(function (item) { return !item.scheduled });
        vm.pagination.totalItems = data.available.length;
        $rootScope.$broadcast("wizardDevice_updatePagination");
    };

    vm.addDevice = function () {

        if (vm.selectedItemIds.length < 1) return;

        var assetsForCheck = vm.model.available.filter(function (item) {
            return vm.selectedItemIds.indexOf(item.id) >= 0;
        });

        var softwareReleaseScheduleId = $routeParams.id || null;

        SpinnerService.wrap(FMJobWizardService.checkAssets, softwareReleaseScheduleId, assetsForCheck)
        .then(function(response) {
            if(response.data.status == 'OK') {
                vm.model.available.forEach(function (item, key) {
                    if (vm.selectedItemIds.indexOf(item.id) >= 0) {
                        vm.model.chosen.push(vm.model.available[key]);
                        vm.model.objectIds.push(vm.model.available[key].id);
                        delete vm.model.available[key];
                    }
                });
                $rootScope.$broadcast("wizardDevice_updatePagination");
            } else {
                ErrorService.showModal('Error', response.data.message);
            }
        })
        .catch(ErrorService.handleHttpError);
    };
    
    vm.updatePagination = function () {

        if (!vm.model.available) return;

        vm.model.available = vm.model.available.filter(function(item) { return !item.scheduled });
        vm.allItemIds = vm.model.available;

        var startIndexForNexPage = vm.pagination.pageIndex * vm.pagination.itemsPerPage;
        var lastPage = vm.model.available.length > 0 ? Math.ceil(vm.model.available.length / vm.pagination.itemsPerPage) : 1;

        var pagination = {
            pageIndex: vm.pagination.pageIndex,
            first: 1,
            last: lastPage,
            totalElements: vm.model.available.length,
            totalPages: lastPage,
            content: vm.model.available.slice(startIndexForNexPage, startIndexForNexPage + vm.pagination.itemsPerPage)
        };

        vm.pagination.update(pagination);
    };
    vm.selectAllItems = function () {
        vm.selectedItemIds = vm.model.available.map(function (item) {
            return item.id;
        });
    };
}
FMWizardAvailableDevicesTable.prototype = Object.create(AbstractTableDevices.prototype);


//Device chosen
controllers.controller('FMWizardChosenDevicesTable', ['$rootScope', 'SecurityService', FMWizardChosenDevicesTable]);

function FMWizardChosenDevicesTable($rootScope, SecurityService) {

    var vm = this;
    AbstractTableDevices.call(vm, $rootScope, SecurityService, "Selected Assets", "No eligible assets have been selected");

    vm.update = function (data) {
        vm.allItemIds = data.chosen;
        vm.pagination.totalItems = data.chosen.length;
        $rootScope.$broadcast("wizardDevice_updatePagination");
    };
    vm.removeDevice = function () {

        if (vm.selectedItemIds.length < 1) return;

        vm.model.chosen.forEach(function (item, key) {
            if (vm.selectedItemIds.indexOf(item.id) >= 0) {
                vm.model.available.push(vm.model.chosen[key]);
                vm.model.objectIds.splice(vm.model.objectIds.indexOf(item));
                delete vm.model.chosen[key];
            }
        });

        $rootScope.$broadcast("wizardDevice_updatePagination");
    };
    vm.updatePagination = function () {

        if (!vm.model.chosen) return;
        vm.allItemIds = vm.model.chosen;

        var startIndexForNexPage = vm.pagination.pageIndex * vm.pagination.itemsPerPage;
        var lastPage = vm.model.chosen.length > 0 ? Math.ceil(vm.model.chosen.length / vm.pagination.itemsPerPage) : 1;

        var pagination = {
            pageIndex: vm.pagination.pageIndex,
            first: 1,
            last: lastPage,
            totalElements: vm.model.chosen.length,
            totalPages: lastPage,
            content: vm.model.chosen.slice(startIndexForNexPage, startIndexForNexPage + vm.pagination.itemsPerPage)
        };
        vm.pagination.update(pagination);
    };
    vm.selectAllItems = function () {
        vm.selectedItemIds = vm.model.chosen.map(function (item) {
            return item.id;
        });
        vm.allItemIds = vm.selectedItemIds;
    };
}
FMWizardChosenDevicesTable.prototype = Object.create(AbstractTableDevices.prototype);