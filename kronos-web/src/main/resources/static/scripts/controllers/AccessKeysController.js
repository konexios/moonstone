controllers.controller('AccessKeysController', ['$uibModal', 'AccessKeyService', 'ErrorService', 'SpinnerService', 'SecurityService', 'ToastrService', AccessKeysController]);
function AccessKeysController($uibModal, AccessKeyService, ErrorService, SpinnerService, SecurityService, ToastrService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        pageTitle: 'Access Keys',
        resultTitle: 'Access Key',
        columnHeaders: [
            {label: 'Raw Api Key', value: 'rawApiKey', sortable: false, style:{'text-overflow': 'ellipsis', 'word-break': 'normal'}},
            {label: 'Name', value: 'name', sortable: true},
            {label: 'Owner', value: 'ownerDisplayName', sortable: false},
            {label: 'Expired', value: 'expired', sortable: false},
            {label: 'Expiration Date', value: 'expiration', sortable: true}
        ],
        tfootTopic: 'access keys',
        // Temporarily remove ability to add and modify access key until the logic is re-designed
        /*canAdd: SecurityService.canCreateAccessKey(),*/
        canAdd: false,
        canEdit: true,
        openFilter: openFilter,
        openDetails: openDetails
    });

    vm.filter = angular.extend({
        name: null,
        accessLevels: null,
        expirationDateFrom: null,
        expirationDateTo: null
    }, vm.filter);
    vm.filterOptions = {
        accessLevels: ['OWNER', 'WRITE', 'READ']
    };
    vm.editOptions = {};

    function openFilter() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/accesskey/accesskey-filter.html',
            controller: 'AccessKeysFilterController',
            size: 'lg',
            resolve: {
                filter: vm.filter,
                filterOptions: vm.filterOptions
            }
        });
        modalInstance.result.then(function(filter) {
            vm.filter = filter;
            vm.pagination.pageIndex = 0;
            vm.find();
        });
    }

    function openDetails(accessKey) {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/accesskey/accesskey-details.html',
            controller: 'AccessKeyDetailsController',
            size: 'lg',
            resolve: {
                accessKey: vm.getDetailsObject(accessKey),
                editOptions: vm.editOptions
            }
        });
       /* modalInstance.result.then(function(accessKey) {
            SpinnerService.wrap(AccessKeyService.save, accessKey)
            .then(function(response) {
                ToastrService.popupSuccess('Access Key ' + response.data.name + ' has been saved successfully');
                // re-read the current page
                vm.find();
            })
            .catch(ErrorService.handleHttpError);
        });*/
    }

    vm.find = function() {
        SpinnerService.wrap(AccessKeyService.find, vm.pagination, vm.filter)
        .then(function(response) {
            vm.update(response.data.result);
        })
        .catch(ErrorService.handleHttpError);
    };

    // SpinnerService.wrap(AccessKeyService.getOptions)
    // .then(function(response) {
    //     vm.editOptions = response.data;
    // });

    vm.find();
}
AccessKeysController.prototype = Object.create(ListController.prototype);

AccessKeysController.prototype.getCellText = function(item, column) {
    if (column.value == 'expiration') {
        return moment(new Date(item.expiration)).format('MM/DD/YYYY hh:mm:ss A');
    } else if (column.value == 'expired') {
        return (new Date(item.expiration)).getTime() < Date.now();
    } else {
        return ListController.prototype.getCellText.call(this, item, column);
    }
};

AccessKeysController.prototype.getDetailsObject = function(accessKey) {
    if (accessKey) {
        return accessKey;
    } else {
        // new object as a template
        var expiration = new Date();
        expiration.setFullYear(expiration.getFullYear() + 20); // now + 20 years
        return {
            name: null,
            expiration: expiration,
            privileges: []
        };
    }
};

controllers.controller('AccessKeysFilterController',
    [
        '$scope', '$uibModalInstance', 'filter', 'filterOptions',
        function ($scope, $uibModalInstance, filter, filterOptions) {
            $scope.filter = angular.merge({}, filter);
            $scope.filterOptions = filterOptions;

            $scope.startDateBeforeRender = startDateBeforeRender;
            $scope.endDateBeforeRender = endDateBeforeRender;
            $scope.startDateOnSetTime = startDateOnSetTime;
            $scope.endDateOnSetTime = endDateOnSetTime;

            function startDateOnSetTime() {
                $scope.$broadcast('ac-start-date-changed');
            }

            function endDateOnSetTime() {
                $scope.$broadcast('ac-end-date-changed');
            }

            function startDateBeforeRender($dates) {
                if ($scope.filter.expirationDateTo) {
                    var activeDate = moment($scope.filter.expirationDateTo);

                    $dates.filter(function(date) {
                        return date.localDateValue() >= activeDate.valueOf();
                    })
                    .forEach(function(date) {
                        date.selectable = false;
                    });
                }
            }

            function endDateBeforeRender($view, $dates) {
                if ($scope.filter.expirationDateFrom) {
                    var activeDate = moment($scope.filter.expirationDateFrom)
                        .subtract(1, $view)
                        .add(1, 'minute');

                    $dates.filter(function(date) {
                        return date.localDateValue() <= activeDate.valueOf();
                    })
                    .forEach(function(date) {
                        date.selectable = false;
                    });
                }
            }
              

            $scope.ok = function () {
                $uibModalInstance.close($scope.filter);
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }
    ]
);

controllers.controller('AccessKeyDetailsController',
    [
        '$scope', '$uibModalInstance', 'AccessKeyService', 'ErrorService', 'SecurityService', 'ToastrService', 'SpinnerService', 'accessKey', 'editOptions',
        function ($scope, $uibModalInstance, AccessKeyService, ErrorService, SecurityService, ToastrService, SpinnerService, accessKey, editOptions) {
            $scope.options = editOptions;
            $scope.loading = true;
            function init(accessKey) {
                $scope.canSave = (!accessKey.id && SecurityService.canCreateAccessKey()) || (accessKey.id && SecurityService.canEditAccessKey());
                $scope.loading = false;
                $scope.accessKey = accessKey;
                $scope.accessKey.expirationDate = new Date($scope.accessKey.expiration);
                $scope.editing = [];
                for(var i=0; i<$scope.accessKey.privileges.length; i++) {
                    $scope.editing[i] = null;
                }
            }

            $scope.isOwner = function(privilege) {
                return privilege.level == 'OWNER';
            };

            $scope.isNode = function(index) {
                return $scope.editing[index].indexOf(editOptions.nodePriPrefix) == 0;
            };

            $scope.isGateway = function(index) {
                return $scope.editing[index].indexOf(editOptions.gatewayPriPrefix) == 0;
            };

            $scope.isDevice = function(index) {
                return $scope.editing[index].indexOf(editOptions.devicePriPrefix) == 0;
            };

            $scope.selectNode = function(index) {
                $scope.editing[index] = editOptions.nodePriPrefix;
            };

            $scope.selectGateway = function(index) {
                $scope.editing[index] = editOptions.gatewayPriPrefix;
            };

            $scope.selectDevice = function(index) {
                $scope.editing[index] = editOptions.devicePriPrefix;
            };

            $scope.onNodeSelected = function(privilege) {
                privilege.pri = editOptions.nodePriPrefix + privilege.node.hid;
                delete privilege.gateway;
                delete privilege.device;
            };

            $scope.onGatewaySelected = function(privilege) {
                privilege.pri = editOptions.gatewayPriPrefix + privilege.gateway.hid;
                delete privilege.node;
                delete privilege.device;
            };

            $scope.onDeviceSelected = function(privilege) {
                privilege.pri = editOptions.devicePriPrefix + privilege.device.hid;
                delete privilege.node;
                delete privilege.gateway;
            };

            /*$scope.addPrivilege = function() {
                $scope.accessKey.privileges.push({level: 'READ', pri: ''});
                $scope.editing[$scope.accessKey.privileges.length - 1] = '';
            };*/

            $scope.editPrivilege = function(index) {
                if ($scope.editing[index] != null) {
                    $scope.editing[index] = null;
                } else {
                    var privilege = $scope.accessKey.privileges[index];
                    if(privilege.pri.indexOf(editOptions.nodePriPrefix) == 0) {
                        $scope.editing[index] = editOptions.nodePriPrefix;
                    } else if(privilege.pri.indexOf(editOptions.gatewayPriPrefix) == 0) {
                        $scope.editing[index] = editOptions.gatewayPriPrefix;
                    } else if(privilege.pri.indexOf(editOptions.devicePriPrefix) == 0) {
                        $scope.editing[index] = editOptions.devicePriPrefix;
                    } else {
                        $scope.editing[index] = '';
                    }
                }
            };

            $scope.deletePrivilege = function(index) {
                $scope.accessKey.privileges.splice(index, 1);
                $scope.editing.splice(index, 1);
            };

            $scope.getObjectName = function(privilege) {
                if (privilege.device) {
                    return 'Device: ' + privilege.device.name;
                } else if (privilege.gateway) {
                    return 'Gateway: ' + privilege.gateway.name;
                } else if (privilege.node) {
                    return 'Group: ' + privilege.node.name;
                } else {
                    return 'Unknown';
                }
            };

            $scope.expireNow = function() {
                $scope.accessKey.expirationDate = new Date();
            };

          /*  $scope.save = function (form) {
                if (form.$valid) {
                    $scope.accessKey.expiration = $scope.accessKey.expirationDate.getTime();
                    delete $scope.accessKey.expirationDate;
                    $uibModalInstance.close($scope.accessKey);
                } else {
                    ToastrService.popupError('Access Key cannot be saved because of invalid fields, please check errors.');
                }
            };
*/
            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

            if (accessKey.id) {
                SpinnerService.wrap(AccessKeyService.get, accessKey.id)
                .then(function(response) {
                    init(response.data);
                })
                .catch(function(response) {
                    ErrorService.handleHttpError(response);
                    $uibModalInstance.dismiss('cancel');
                });
            } else {
                init(accessKey);
            }
        }
    ]
);
