angular.module('pegasus').controller("AccessKeyController",
    [
        "$rootScope", "$scope", "$stateParams", "$location", "$state", "ErrorService", "ToastrService", "SecurityService", "AccessKeyService",
        function ($rootScope, $scope, $stateParams, $location, $state, ErrorService, ToastrService, SecurityService, AccessKeyService) {

        	// pri prefixes
        	$scope.priPrefixes = {
                company: "arw:pgs:com",
                subscription: "arw:pgs:sub",
                application: "arw:pgs:app"        			
        	};
        	
            $scope.busy = true;
            $scope.entityOwner = {
                entityId: $stateParams.entityId,
                entityType: $stateParams.entityType,
                entityTypes: $stateParams.entityTypes,
                entityPri: null
            };
            $scope.accessKey = {
                privileges: []
            };
            $scope.editing = [];
            for(var i=0; i<$scope.accessKey.privileges.length; i++) {
                $scope.editing[i] = null;
            }
            
            $scope.editOptions = {};

            // find the pri for the entity type and id
            AccessKeyService.getPriForType($scope.entityOwner.entityType, $scope.entityOwner.entityId).then(function (response) {
                $scope.entityOwner.entityPri = response.data.pri;
                $scope.busy = false;
            }).catch(function(error) {
            	$scope.entityOwner.entityPri = null;
                $scope.busy = false;
            });


            if ($stateParams.accessKeyId && $stateParams.accessKeyId !== 'new') {
                $scope.busy = true;

                AccessKeyService.get(
                    $stateParams.accessKeyId, $scope.entityOwner.entityTypes, $scope.entityOwner.entityId)
                    .then(function(response) {
                        $scope.accessKey = response.data;
                        $scope.busy = false;
                    })
                    .catch(function(response) {
                        $scope.busy = false;
                        ErrorService.handleHttpError(response);
                    });
            }

            $scope.addPrivilege = function() {
                $scope.accessKey.privileges.push({level: 'READ', pri: $scope.entityOwner.entityPri});
                $scope.editing[$scope.accessKey.privileges.length - 1] = '';
            };

            $scope.editPrivilege = function(index) {
                if ($scope.editing[index] !== null) {
                    $scope.editing[index] = null;
                } else {
                    var privilege = $scope.accessKey.privileges[index];
                    if(privilege.pri.indexOf($scope.priPrefixes.company) === 0) {
                        $scope.editing[index] = $scope.priPrefixes.company;
                    } else if(privilege.pri.indexOf($scope.priPrefixes.subscription) === 0) {
                        $scope.editing[index] = $scope.priPrefixes.subscription;
                    } else if(privilege.pri.indexOf($scope.priPrefixes.application) === 0) {
                        $scope.editing[index] = $scope.priPrefixes.application;
                    } else {
                        $scope.editing[index] = '';
                    }
                }
            };

            $scope.deletePrivilege = function(index) {
                $scope.accessKey.privileges.splice(index, 1);
                $scope.editing.splice(index, 1);
            };

            $scope.save = function(form) {
                if (form.$valid) {
                    $scope.busy = true;
                    $scope.accessKey.expiration = convertToDate($scope.accessKey.expiration);
                    var savePromise = AccessKeyService.save($scope.accessKey, $scope.entityOwner);

                    if (!savePromise)
                        return;

                    savePromise
                        .then(function(response) {
                            $scope.accessKey = response.data;

                            // reset form
                            form.$setPristine();
                            form.$setUntouched();

                            $scope.busy = false;

                            ToastrService.popupSuccess($scope.accessKey.name + " has been successfully saved.");
                        })
                        .catch(function(response) {
                            $scope.busy = false;
                            ErrorService.handleHttpError(response);
                        });
                } else {
                    ToastrService.popupError("The form is invalid! Please make changes and try again.");
                }
            };

            $scope.expireNow = function() {
                $scope.accessKey.expiration = new Date();
            };

            $scope.canSave = function() {
                console.log($scope.entityOwner.entityType);
                switch ($scope.entityOwner.entityType) {

                    case 'company':
                        return ($stateParams.accessKeyId === 'new' && $scope.canCreateCompanyAccessKey()) ||
                               ($stateParams.accessKeyId !== 'new' && $scope.accessKey.id && $scope.canUpdateCompanyAccessKey());
                        break;

                    case 'application':
                        return ($stateParams.accessKeyId === 'new' && $scope.canCreateApplicationAccessKey()) ||
                            ($stateParams.accessKeyId !== 'new' && $scope.accessKey.id && $scope.canUpdateApplicationAccessKey());
                        break;

                    case 'subscription':
                        return ($stateParams.accessKeyId === 'new' && $scope.canCreateSubscriptionAccessKey()) ||
                            ($stateParams.accessKeyId !== 'new' && $scope.accessKey.id && $scope.canUpdateSubscriptionAccessKey());
                        break;
                }
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

            $scope.canEditPrivilege = function(privilege) {
                return (privilege.level === 'READ' || privilege.level === 'WRITE' || (privilege.level === 'OWNER' && SecurityService.isSuperAdministrator()));
            };

            $scope.isCompany = function(index) {
                return $scope.editing[index].indexOf($scope.priPrefixes.company) === 0;
            };

            $scope.isSubscription = function(index) {
                return $scope.editing[index].indexOf($scope.priPrefixes.subscription) === 0;
            };

            $scope.isApplication = function(index) {
                return $scope.editing[index].indexOf($scope.priPrefixes.application) === 0;
            };

            $scope.selectNode = function(index) {
                $scope.editing[index] = $scope.editOptions.nodePriPrefix;
            };

            $scope.selectGateway = function(index) {
                $scope.editing[index] = $scope.editOptions.gatewayPriPrefix;
            };

            $scope.selectDevice = function(index) {
                $scope.editing[index] = $scope.editOptions.devicePriPrefix;
            };

            $scope.onNodeSelected = function(privilege) {
                privilege.pri = $scope.editOptions.nodePriPrefix + privilege.node.hid;
                delete privilege.gateway;
                delete privilege.device;
            };

            $scope.onGatewaySelected = function(privilege) {
                privilege.pri = $scope.editOptions.gatewayPriPrefix + privilege.gateway.hid;
                delete privilege.node;
                delete privilege.device;
            };

            $scope.onDeviceSelected = function(privilege) {
                privilege.pri = $scope.editOptions.devicePriPrefix + privilege.device.hid;
                delete privilege.node;
                delete privilege.gateway;
            };

            function convertToDate(value) {

                var nValue = new Date(value);

                var month = nValue.getMonth() + 1;
                var date = nValue.getDate();
                var year = nValue.getFullYear();

                if (month < 10)
                    month = "0" + month;

                if (date < 10)
                    date = "0" + date;

                return year + "-" + month + "-" + date;
            }
        }
    ]);

