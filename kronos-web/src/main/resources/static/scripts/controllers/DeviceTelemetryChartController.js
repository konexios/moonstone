/* global controllers */

(function () {
    'use strict';

    controllers.controller("DeviceTelemetryChartController", DeviceTelemetryChartController);

    DeviceTelemetryChartController.$inject = [
        "$scope", "$timeout", "$routeParams",
        "DeviceService", "ErrorService", "googleChartApiPromise", "WebSocketsService"];

    function DeviceTelemetryChartController($scope, $timeout, $routeParams,
        DeviceService, ErrorService, googleChartApiPromise, WebSocketsService) {

        /* PRIVATE OBJECTS*/

        var timer = null;
        var request = null;

        var DEVICES_WS_DESTINATION = "/api/kronos/ws/device";

        /* PUBLIC OBJECTS*/
        var vm = this;

        vm.telemetry = null;
        vm.telemetryChartType = 'current'; // or 'range'

        vm.range = {
            from: null,
            to: null
        };

        vm.intervalOptions = [
            {name:'1 min', value: 60000},
            {name:'5 min', value: 300000},
            {name:'15 min', value: 900000},
            {name:'30 min', value: 1800000},
            {name:'1 hour', value: 3600000}
        ];
        vm.refreshOptions = [
            {name:'1 sec', value: 1000},
            {name:'2 sec', value: 2000},
            {name:'3 sec', value: 3000},
            {name:'5 sec', value: 5000},
            {name:'10 sec', value: 10000},
            {name:'On Demand', value: -1}
        ];

        vm.current = {
            interval: 60000, // 1 min
            refresh: -1 // On Demand
        };
        vm.showChart = false;
        var wsConnection = null;

        /* PRIVATE METHODS */

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

        function _processData(result) {
            var data = result.data,
                datum,
                i=0,
                length = result.data.length,
                rows = new Array(length);

            for(; i<length; i++) {
                datum = data[i];
                rows[i] = {c:[{v: new Date(datum.t)}, {v: +datum.v}]};
            }
            vm.chartObject.data.rows = rows;
        }

        function _processWsData(result) {
            if(!(result instanceof Array)) result = [result];

            var len = result.length,
                to = new Date(),
                from = new Date(to - vm.current.interval);
            _moveChart(from, to);

            // let's check possible data intersections
            var lastTimeStamp = 0;
            var curDataLength = vm.chartObject.data.rows.length;
            if (curDataLength > 0) {
                lastTimeStamp = vm.chartObject.data.rows[curDataLength-1].c[0].v;
            }

            if (result && len) {
                var datum,
                    i=0,
                    rows = [];

                for(; i<result.length; i++) {
                    datum = result[i];
                    if (new Date(datum.t) > lastTimeStamp) {
                        rows.push({
                            c: [
                                {v: new Date(datum.t)},
                                {v: +datum.v}
                            ]
                        });
                    }
                }

                var newRows = vm.chartObject.data.rows.slice(); // slice() - fast creating a copy of an array

                // let's remove obsolete unnecessary data from chart (by date)
                i = 0;
                for (i = 0; i < newRows.length; i++) {
                    if (newRows[i].c[0].v >= from) {
                        break;
                    }
                }
                newRows.splice(0, i);

                newRows = newRows.concat(rows);
                vm.chartObject.data.rows = newRows;
            }

            $scope.$apply();
        }

        function _moveChart(from, to) {
            vm.chartObject.options.hAxis.viewWindow = {
                min: from,
                max: to
            };
        }

        function getXHRTelemetries(from, to) {
            var _thisRequest = DeviceService.getTelemetries($routeParams.deviceId, vm.telemetry.name, from, to);
            request = _thisRequest;
            return request.then(function(result) {
                _processData(result);
            }, function(reason) {
                if (request === _thisRequest) {
                    ErrorService.handleHttpError(reason);
                }
                // else - request has been aborted
                throw reason; // reject subsequent promise
            });
        }

        function init() {

            vm.chartObject = {
                type: 'LineChart',
                options: {
                    title: '',
                    colors: ['#00a19b'],
                    displayExactValues: true,
                    width: '100%',
                    height: 300,
                    legend: {
                        position: 'none'
                    },
                    hAxis: {},
                    chartArea: {
                        right: 50,
                        left: 50
                    },
                    pointSize: 7
                },
                data: {
                    cols: [{
                        id: 'time',
                        label: 'Time',
                        type: 'datetime'
                    }, {
                        id: 'id1',
                        label: '',
                        type: 'number'
                    }],
                    rows: []
                }
            };

            // ws push state machine
            var wsState;
            function resetWsState() {
                wsState = {
                    wsSubscription: null,
                    destination: '',
                    telemetryName: '',
                    interval: 0,
                    refresh: 0
                };
            }
            resetWsState();

            vm.onChange = function() {
                cleanup();

                vm.showChart = true;
                if (vm.telemetry) {
                    vm.chartObject.options.title = vm.chartObject.data.cols[1].label = vm.telemetry.description;

                    if (vm.telemetryChartType == 'range' && vm.range.from != null && vm.range.to != null
                         && !isNaN(vm.range.from.getTime()) && !isNaN(vm.range.to.getTime()) && vm.range.from.getTime() < vm.range.to.getTime()) {
                        _moveChart(vm.range.from, vm.range.to);
                        getXHRTelemetries(vm.range.from, vm.range.to);
                    } else if (vm.telemetryChartType == 'current') {
                        // handle later
                    } else {
                        vm.showChart = false;
                    }
                } else {
                    vm.showChart = false;
                }

                if (vm.showChart && vm.telemetryChartType == 'current') {
                    // periodical chart update
                    var to = new Date(), from = new Date(to - vm.current.interval);
                    _moveChart(from, to);

                    if (!wsConnection.isConnected()) {
                        // switch to XHR polling
                        wsState.wsSubscription = null;
                    }
                    if (wsState.wsSubscription) {
                        if (wsState.telemetryName != vm.telemetry.name || wsState.interval < vm.current.interval) {
                            // re-init required
                            wsConnection.unsubscribe(wsState.wsSubscription);
                            wsState.wsSubscription = null;
                        } else if (wsState.refresh != vm.current.refresh) {
                            if (wsState.refresh == -1 || vm.current.refresh == -1) {
                                // re-init required
                                wsConnection.unsubscribe(wsState.wsSubscription);
                                wsState.wsSubscription = null;
                            } else {
                                // just update refresh interval
                                wsConnection.send(wsState.destination, {
                                    refreshInterval: vm.current.refresh
                                }, {
                                    subscriptionId: wsState.wsSubscription.id
                                });
                            }
                        }
                    }
                    if (!wsState.wsSubscription) {
                        // get data for whole chart from XHR
                        getXHRTelemetries(from, to)
                        .then(function() {
                            if (wsConnection.isConnected()) { // WS
                                if (vm.current.refresh != -1) {
                                    // server-side polling
                                    // subscribe
                                    wsState.wsSubscription = wsConnection.subscribe("/user/queue/device/" + $routeParams.deviceId + "/telemetry/" + vm.telemetry.name, _processWsData);
                                    // request info
                                    wsState.destination = "/app" + DEVICES_WS_DESTINATION + "/" + $routeParams.deviceId + "/telemetry/" + vm.telemetry.name;
                                    wsConnection.send(wsState.destination, {
                                        "refreshInterval": vm.current.refresh
                                    }, {
                                        subscriptionId: wsState.wsSubscription.id
                                    });
                                } else {
                                    // subscribe for telemetry broadcasting from rabbitmq
                                    wsState.wsSubscription = wsConnection.subscribe("/topic/device/" + $routeParams.deviceId + "/telemetry/" + vm.telemetry.name, _processWsData);
                                }
                            } else { // XHR
                                if (vm.current.refresh != -1) {
                                    // schedule another call
                                    timer = $timeout(vm.onChange, vm.current.refresh);
                                    wsState.wsSubscription = null;
                                }
                            }
                        });
                    }

                    // update state
                    wsState.telemetryName = vm.telemetry.name;
                    wsState.interval = vm.current.interval;
                    wsState.refresh = vm.current.refresh;
                } else {
                    if (wsState.wsSubscription) {
                        wsConnection.unsubscribe(wsState.wsSubscription);
                    }
                    resetWsState();
                }
            };

            wsConnection = WebSocketsService.connect(DEVICES_WS_DESTINATION, {
                onConnect: function() {
                    if (vm.showChart && vm.telemetryChartType == 'current') {
                        // switch to WS if running
                        vm.onChange();
                        $scope.$apply();
                    }
                },
                onDisconnect: function() {
                    if (vm.showChart && vm.telemetryChartType == 'current') {
                        // switch to XHR polling if running
                        timer = $timeout(vm.onChange, 5000);
                    }
                }
            });

        }

        /* EVENTS */

        $scope.$on("$destroy", function () {
            // abort XHR and cancel timer when controller's scope is destroyed
            cleanup();
            // disconnect from web sockets
            if (wsConnection) {
                wsConnection.disconnect();
            }
        });

        /* ON LOAD ACTIONS */

        googleChartApiPromise.then(init);
    }

})();
