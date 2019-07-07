/* global controllers */

(function () {
    'use strict';

    controllers.controller("HomeController", HomeController);

    HomeController.$inject = [
        "$rootScope", "$scope", "$location", "$timeout", "$uibModal", "HomeService", "ErrorService", "SpinnerService", 'AuthenticationService', "SecurityService", "WebSocketsService"
    ];

    function HomeController($rootScope, $scope, $location, $timeout, $uibModal, HomeService, ErrorService, SpinnerService, AuthenticationService, SecurityService, WebSocketsService) {

        var vm = this;
        this.paginationDevice = new Util.Pagination();
        this.paginationDeviceEvents = new Util.Pagination();
        this.paginationGateways = new Util.Pagination();
        var HOMEPAGE_WS_DESTINATION = "/api/kronos/ws/home";

        $scope.timer = null;
        $scope.refreshRate = (5 * 60 * 1000);

        $scope.widgets = {
            myDevices: {
                loading: true,
                lastLoad: null,
                data: []
            },
            myGateways: {
                loading: true,
                lastLoad: null,
                data: []
            },
            myDeviceEvents: {
                loading: true,
                lastLoad: null,
                data: []
            }
        };

        $scope.loadPage = function () {
            $scope.loadMyDevices();
            $scope.loadMyGateways();
            $scope.loadMyDeviceEvents();
            $scope.timer = $timeout($scope.loadPage, $scope.refreshRate);
        };

        $scope.loadMyDevices = function () {
            $scope.widgets.myDevices.loading = true;

            HomeService.myDevices(vm.paginationDevice)
                .then(function (response) {
                    $scope.widgets.myDevices.data = response.data;
                    vm.paginationDevice.update(response.data.result);
                }).catch(ErrorService.handleHttpError)
                .finally(function () {
                    $scope.widgets.myDevices.loading = false;
                    $scope.widgets.myDevices.lastLoad = new Date();
                });
        };

        $scope.loadMyGateways = function () {
            $scope.widgets.myGateways.loading = true;

            HomeService.myGateways(vm.paginationGateways)
                .then(function (response) {
                    $scope.widgets.myGateways.data = response.data;
                    vm.paginationGateways.update(response.data.result);
                }).catch(ErrorService.handleHttpError)
                .finally(function () {
                    $scope.widgets.myGateways.loading = false;
                    $scope.widgets.myGateways.lastLoad = new Date();
                });
        };

        $scope.loadMyDeviceEvents = function () {
            $scope.widgets.myDeviceEvents.loading = true;

            HomeService.myDeviceEvents(vm.paginationDeviceEvents)
                .then(function (response) {
                    $scope.widgets.myDeviceEvents.data = response.data;
                    vm.paginationDeviceEvents.update(response.data.result);
                }).catch(ErrorService.handleHttpError)
                .finally(function () {
                    $scope.widgets.myDeviceEvents.loading = false;
                    $scope.widgets.myDeviceEvents.lastLoad = new Date();
                });
        };

        $scope.$on('$destroy', function () {
            // cancel all current XHR requests
            if ($scope.timer) {
                $timeout.cancel($scope.timer);
                $scope.timer = null;
            }
            vm.wsConnection.disconnect();
        });

        function _processWsData(data) {

            // On first loading turn off the loading state
            if(($scope.widgets.myDevices.lastLoad === null && $scope.widgets.myDevices.loading) ||
                ($scope.widgets.myGateways.lastLoad  === null && $scope.widgets.myGateways.loading) ||
                ($scope.widgets.myDeviceEvents.lastLoad === null && $scope.widgets.myDeviceEvents.loading))
            {
                $scope.widgets.myDevices.loading = false;
                $scope.widgets.myGateways.loading = false;
                $scope.widgets.myDeviceEvents.loading = false;
            }

            // Let's ignore WS updates if user requested manual XHR update (any "Refresh" button on "Home" page)
            if ($scope.widgets.myDevices.loading || $scope.widgets.myGateways.loading || $scope.widgets.myDeviceEvents.loading)
                return;
            
            var lastLoad = new Date();
            // set data
            $scope.widgets.myDevices.data = data.myDevices;
            $scope.widgets.myGateways.data = data.myGateways;
            $scope.widgets.myDeviceEvents.data = data.myDeviceEvents;

            // set last update time
            $scope.widgets.myDevices.lastLoad = lastLoad;
            $scope.widgets.myGateways.lastLoad = lastLoad;
            $scope.widgets.myDeviceEvents.lastLoad = lastLoad;

            vm.paginationDevice.update(data.myDevices.result);
            vm.paginationGateways.update(data.myGateways.result);
            vm.paginationDeviceEvents.update(data.myDeviceEvents.result);

            $scope.$apply();
        }

        function connectSocket() {
            vm.wsConnection = WebSocketsService.connect(HOMEPAGE_WS_DESTINATION, {
                onConnect: function () {
                    if ($scope.timer) {
                        $timeout.cancel($scope.timer);
                        $scope.timer = null;
                    }

                    var wsSubscription = vm.wsConnection.subscribe("/user/queue/home/page", _processWsData);
                    // NOTE: do not send content prop back to the server
                    var paginationDevice = angular.extend({}, vm.paginationDevice, { content: null });
                    var paginationGateways = angular.extend({}, vm.paginationGateways, { content: null });
                    var paginationDeviceEvents = angular.extend({}, vm.paginationDeviceEvents, { content: null });
                    // NOTE: we may use default $scope.refreshRate (5 minutes) but is is too long for websockets.
                    vm.wsConnection.send("/app/api/kronos/ws/home/page", {
                        "refreshInterval": 10000, // 10 seconds, see comment above
                        "paginationDevice": paginationDevice,
                        "paginationGateways": paginationGateways,
                        "paginationDeviceEvents": paginationDeviceEvents
                    }, {
                        subscriptionId: wsSubscription.id
                    });
                },
                onDisconnect: function () {
                    if (!$scope.timer) {
                        $scope.timer = $timeout($scope.loadPage, 5000);
                    }
                }
            });
        }

        function disconnectSocket() {
            if ($scope.timer) {
                $timeout.cancel($scope.timer);
                $scope.timer = null;
            }
            vm.wsConnection.disconnect();
        }

        vm.changeItemsDevicePerPage = function (numberOfItems) {
            if(numberOfItems != vm.paginationDevice.itemsPerPage) {
                vm.paginationDevice.pageIndex = 0;
                vm.paginationDevice.itemsPerPage = numberOfItems;
                $scope.widgets.myDevices.loading = true;
                disconnectSocket();
                $scope.loadMyDevices();
                connectSocket();
            }
        };

        vm.previousDevicePage = function () {
            vm.paginationDevice.pageIndex--;
            $scope.widgets.myDevices.loading = true;
            disconnectSocket();
            $scope.loadMyDevices();
            connectSocket();
        };
        vm.gotoDevicePage = function (pageNumber) {
            vm.paginationDevice.pageIndex = pageNumber;
            $scope.widgets.myDevices.loading = true;
            disconnectSocket();
            $scope.loadMyDevices();
            connectSocket();
        };

        vm.nextDevicePage = function () {
            vm.paginationDevice.pageIndex++;
            $scope.widgets.myDevices.loading = true;
            disconnectSocket();
            $scope.loadMyDevices();
            connectSocket();
        };

        vm.changeItemsDeviceEventsPerPage = function (numberOfItems) {
            if(numberOfItems != vm.paginationDeviceEvents.itemsPerPage) {
                vm.paginationDeviceEvents.pageIndex = 0;
                vm.paginationDeviceEvents.itemsPerPage = numberOfItems;
                $scope.widgets.myDeviceEvents.loading = true;
                disconnectSocket();
                $scope.loadMyDeviceEvents();
                connectSocket();
            }
        };

        vm.previousDeviceEventsPage = function () {
            vm.paginationDeviceEvents.pageIndex--;
            $scope.widgets.myDeviceEvents.loading = true;
            disconnectSocket();
            $scope.loadMyDeviceEvents();
            connectSocket();
        };
        vm.gotoDeviceEventsPage = function (pageNumber) {
            vm.paginationDeviceEvents.pageIndex = pageNumber;
            $scope.widgets.myDeviceEvents.loading = true;
            disconnectSocket();
            $scope.loadMyDeviceEvents();
            connectSocket();
        };

        vm.nextDeviceEventsPage = function () {
            vm.paginationDeviceEvents.pageIndex++;
            $scope.widgets.myDeviceEvents.loading = true;
            disconnectSocket();
            $scope.loadMyDeviceEvents();
            connectSocket();
        };

        vm.changeItemsGatewaysPerPage = function (numberOfItems) {
            if(numberOfItems != vm.paginationGateways.itemsPerPage) {
                vm.paginationGateways.pageIndex = 0;
                vm.paginationGateways.itemsPerPage = numberOfItems;
                $scope.widgets.myGateways.loading = true;
                disconnectSocket();
                $scope.loadMyGateways();
                connectSocket();
            }
        };

        vm.previousGatewaysPage = function () {
            vm.paginationGateways.pageIndex--;
            $scope.widgets.myGateways.loading = true;
            disconnectSocket();
            $scope.loadMyGateways();
            connectSocket();
        };
        vm.gotoGatewaysPage = function (pageNumber) {
            vm.paginationGateways.pageIndex = pageNumber;
            $scope.widgets.myGateways.loading = true;
            disconnectSocket();
            $scope.loadMyGateways();
            connectSocket();
        };

        vm.nextGatewaysPage = function () {
            vm.paginationGateways.pageIndex++;
            $scope.widgets.myGateways.loading = true;
            disconnectSocket();
            $scope.loadMyGateways();
            connectSocket();
        };

        connectSocket();

        function checkEULA () {

            HomeService.checkEULA()
            .then(function (response) {
            	var eulaModel = response.data.data;
            	if (eulaModel.needsToAgree)
            		openEULA(eulaModel);
            })
            .catch(ErrorService.handleHttpError)
            .finally(function () {
            });
        }
        
        function openEULA(eulaModel) {
        	// do not allow this window to be closed
            var modalInstance = $uibModal.open({
                animation: false,
                backdrop: 'static',
                keyboard: false,
                templateUrl: 'partials/eula.html',
                controller: 'EULAController',
                size: 'lg',
                resolve: {
                	eulaModel: eulaModel
                }
            });

            modalInstance.result.then(function(agreed) {
            	if (agreed) {
                  SpinnerService.wrap(HomeService.updateEULA)
                  .then(function(response) {
                  })
                  .catch(ErrorService.handleHttpError);
            	} else {
                    AuthenticationService.logout()
                    .then(function(response){
                        clearUserSession();
                        $location.url('/signin');
                    }, function(response){
                        clearUserSession();
                        ErrorService.handleHttpError(response);
                    });
            	}
            });
        }
        
        function clearUserSession() {
            $rootScope.app = null;
            $rootScope.user = null;
            SecurityService.deleteSession();
        }
        
        checkEULA();
    }

})();

controllers.controller('EULAController',
	    [
	        '$scope', '$sce', '$uibModalInstance', 'eulaModel',
	        function ($scope, $sce, $uibModalInstance, eulaModel) {
	            $scope.eulaModel = eulaModel;
	            $scope.eulaUrl = $sce.trustAsResourceUrl(eulaModel.url);

	            $scope.agree = function() {
	            	$uibModalInstance.close(true);
	            };

	            $scope.disagree = function () {
	            	$uibModalInstance.close(false);
	            };
	        }
	    ]
	);
