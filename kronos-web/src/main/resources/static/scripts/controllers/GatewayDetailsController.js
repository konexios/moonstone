controllers.controller('GatewayDetailsController', [
    '$route', '$scope', '$timeout', '$routeParams', '$uibModal', '$location', 'GatewayService', 'SecurityService', 'ErrorService', 'SpinnerService', 'ToastrService', 'ConfigurationService', 'EventService',
    function ($route, $scope, $timeout, $routeParams, $uibModal, $location, GatewayService, SecurityService, ErrorService, SpinnerService, ToastrService, ConfigurationService, EventService) {
        var HEARTBEAT_UPDATE_INTERVAL = 1*60*1000; // 1 minute
        var HEARTBEAT_THRESHOLD = 5*60*1000; // 5 minutes

        $scope.pageTitle = 'Gateway Details';
        $scope.pageSubTitle = '';
        $scope.loading = true;
        $scope.activeTab = 0;
        $scope.activeSubTab = 0;
        $scope.isAlive = null;

        $scope.canEditGateway = SecurityService.canEditGateway();
        $scope.canDeleteGateway = SecurityService.canDeleteGateway();
        $scope.canViewLogs = SecurityService.canViewGatewayAuditLogs();
        $scope.canViewAccessKeys = SecurityService.canReadAccessKeys();
        $scope.canReadSoftwareReleaseTrans = SecurityService.canReadSoftwareReleaseTrans();
        $scope.canReadGatewayTestResults = SecurityService.canReadGatewayTestResults();

        $scope.getGatewayDetailsOptions = getGatewayDetailsOptions;

        EventTrackerMixin.call($scope, $scope, $timeout, EventService, ToastrService);

        $scope.enableGateway = function() {
            SpinnerService.wrap(GatewayService.enableGateway, $scope.gateway.id)
            .then(function(response) {
                $scope.gateway.enabled = response.data;
                ToastrService.popupSuccess('Gateway ' + $scope.gateway.name + ' has been enabled.');
            })
            .catch(ErrorService.handleHttpError);
        };

        $scope.disableGateway = function() {
            SpinnerService.wrap(GatewayService.disableGateway, $scope.gateway.id)
            .then(function(response) {
                $scope.gateway.enabled = response.data;
                ToastrService.popupSuccess('Gateway ' + $scope.gateway.name + ' has been disabled.');
            })
            .catch(ErrorService.handleHttpError);
        };

        $scope.deleteGateway = function() {
            $uibModal.open({
                animation: false,
                templateUrl: 'partials/confirm-popup.html',
                controller: 'ConfirmPopupController',
                size: 'lg',
                resolve: {
                    options: {
                        title: 'Confirm deleting gateway '+$scope.gateway.name,
                        message: 'WARNING: THIS ACTION IS NOT RECOVERABLE! This gateway and all devices that are connected to it will be deleted from the system. Are you sure you want to proceed?'
                    }
                }
            }).result.then(function() {
                SpinnerService.wrap(GatewayService.deleteGateway, $scope.gateway.id)
                .then(function(response) {
                    ToastrService.popupSuccess('Gateway '+$scope.gateway.name+' has been deleted successfully');
                    $location.path('/gateways');
                })
                .catch(ErrorService.handleHttpError);
            });
        };

        $scope.save = function(form) {
            if (form.$invalid) {
                ToastrService.popupError('Gateway ' + $scope.gateway.name + ' cannot be saved because of invalid fields, please check errors.');
            } else {
                SpinnerService.wrap(GatewayService.saveGatewaySettings, $scope.gateway)
                .then(function(response) {
                    angular.extend($scope.gateway, response.data);
                    form.$setPristine();
                    ToastrService.popupSuccess('Gateway ' + $scope.gateway.name + ' has been saved successfully');
                })
                .catch(ErrorService.handleHttpError);
            }
        };

        $scope.syncConfigToGateway = function () {
            SpinnerService.wrap(ConfigurationService.syncConfigToGateway, $routeParams.gatewayId)
                .then(function (response) {
                    if (response.data.status === 'ERROR') {
                        ToastrService.popupError(response.data.message);
                    } else {
                        ToastrService.popupSuccess('Cloud gateway configuration event has been queued for the gateway ' + $scope.gateway.name);
                        $scope.trackEvent(response.data.message, 'syncConfigToGateway', $scope.gateway);
                    }
                })
                .catch(ErrorService.handleHttpError);
        };

        $scope.syncGatewayConfigToCloud = function () {
            SpinnerService.wrap(ConfigurationService.syncGatewayConfigToCloud, $routeParams.gatewayId)
                .then(function (response) {
                    if (response.data.status === 'ERROR') {
                        ToastrService.popupError(response.data.message);
                    } else {
                        ToastrService.popupSuccess('Current gateway configuration event has been queued for the gateway ' + $scope.gateway.name);
                        $scope.trackEvent(response.data.message, 'syncGatewayConfigToCloud', $scope.gateway);
                    }
                })
                .catch(ErrorService.handleHttpError);
        };

        var timer = null;
        var request = null;
        function cleanup() {
            if (request) {
                request.abort && request.abort();
                request = null;
            }
            if (timer) {
                $timeout.cancel(timer);
                timer = null;
            }
        }

        // abort XHR and cancel timer when controller's scope is destroyed
        $scope.$on('$destroy', cleanup);

        function checkAlive(lastHeartbeatTime) {
            if($scope.gateway && lastHeartbeatTime != null) {
                if ($scope.gateway.lastHeartbeatTime != null && $scope.gateway.lastHeartbeatTime < lastHeartbeatTime) {
                    // if heartbeat time has been increased - the gateway is alive
                    $scope.isAlive = true;
                } else {
                    // heartbeat time has not been increased - check it against current time and threshold
                    $scope.isAlive = lastHeartbeatTime > (Date.now() - HEARTBEAT_THRESHOLD);
                }
                $scope.gateway.lastHeartbeatTime = lastHeartbeatTime;
            } else {
                // heartbeat time is undefined - hide the icon
                $scope.isAlive = null;
            }
        }

        function updateLastHeartbeat() {
            cleanup();
            request = GatewayService.getLastHeartbeat($routeParams.gatewayId);
            request
            .then(function(result) {
                checkAlive(result.data.timestamp);
            }, function(reason) {
                if (request) {
                    console.error('failed to get last heartbeat', reason); // log only
                    checkAlive($scope.gateway && $scope.gateway.lastHeartbeatTime); // update the heart icon
                    return true; // resolve the promise to schedule another update
                }
                // else - request has been aborted
                throw reason; // reject the promise to cancel another update
            })
            .then(function() {
                timer = $timeout(updateLastHeartbeat, HEARTBEAT_UPDATE_INTERVAL);
            });
        }

        function getGatewayDetails(gatewayId) {
            $scope.loading = true;
            SpinnerService.wrap(GatewayService.getGatewayDetails, gatewayId)
                .then(function(response) {
                    $scope.gateway = angular.extend({
                        assignOwnerToDevices: false
                    }, response.data);
                    
                    $scope.gatewayCustomProp = [];
                    angular.forEach($scope.gateway.properties, function (value, key) {
                        $scope.gatewayCustomProp.push({
                            key: key,
                            value: value
                        });
                    });
                    
                    checkAlive($scope.gateway.lastHeartbeatTime);
                    getGatewayDetailsOptions($scope.gateway.deviceTypeId);
                })
                .catch(ErrorService.handleHttpError)
                .finally(function() {
                    $scope.loading = false;
                });
        }

        function getGatewayDetailsOptions(deviceTypeId) {
            SpinnerService.wrap(GatewayService.getGatewayDetailsOptions, deviceTypeId)
                .then(function(response) {
                    $scope.options = response.data;
                })
                .catch(ErrorService.handleHttpError);
        }

        // initialization
        getGatewayDetails($routeParams.gatewayId);
        timer = $timeout(updateLastHeartbeat, HEARTBEAT_UPDATE_INTERVAL);


        $scope.collapseMenu = function(){
            if($scope.mobileLayot){
                $scope.mobileMenuShowed=!$scope.mobileMenuShowed
            }else{
                $scope.collapsedView=!$scope.collapsedView;
            }
        };

        $scope.$on('resize', function (event, data) {
            if (data.width < 992) {
                $scope.mobileLayot = true;
           } else {
               $scope.mobileLayot = false;
               $scope.mobileMenuShowed = false;
            }
            $timeout(function () {
                $scope.$apply();
            }, 0, false);
        });
        $scope.mobileMenuShowed = false;

        $scope.openObjectLocation = function(size) {
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/object-location-popup.html',
                controller: 'ObjectLocationController',
                size: size,
                resolve: {
                    lastLocation: function() { return $scope.gateway.lastLocation; }
                }
            });

            modalInstance.result.then(
                function (lastLocation) {
                    SpinnerService.wrap(GatewayService.updateGatewayLocation, $scope.gateway.id, lastLocation)
                        .then(function(response) {
                            $scope.gateway.lastLocation = response.data;
                            ToastrService.popupSuccess('Gateway last location has been updated');
                        })
                        .catch(function() {
                            ToastrService.popupError('Failed to update gateway last location');
                        });
                }
            );
        };
        
        $scope.isObjectEmpty = function (gateway) {
        	if (gateway == undefined || gateway == null)
        		return false;
        	
            return Object.keys(gateway).length === 0;
        };
    }
]);
