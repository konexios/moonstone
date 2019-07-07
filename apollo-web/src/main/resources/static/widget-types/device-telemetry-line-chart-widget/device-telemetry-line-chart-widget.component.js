(function() {
    'use strict';

    angular
        .module('widgets')
        .component('deviceTelemetryLineChartWidget', {
            templateUrl: '/widget-types/device-telemetry-line-chart-widget/device-telemetry-line-chart-widget.tmpl.html',
            controller: widgetDeviceTelemetryLineChartController,
            controllerAs: 'vm',
            bindings: {
                widget: '='
            }
        });


    function widgetDeviceTelemetryLineChartController($scope, googleChartApiPromise) {
        var vm = this;
        vm.showChart = false;
        vm.data = {
        	waiting: true,
        	error: false,
        	state: null,
        	result: null
        };
        
        function _processWsData(result) {
            if(!(result instanceof Array)) result = [result];

            var len = result.length,
                to = new Date(),
                from = new Date(to - vm.data.result.displayInterval);
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
                    	
                    	var tempData = null;
                    	if (vm.data.result.type === "IntegerCube" 
                			|| vm.data.result.type === "FloatCube") {
                            tempData = [
                                {v: new Date(datum.t)},
                                {v: +datum.v},
                                {v: +datum.v2},
                                {v: +datum.v3}
                            ];                    		
                    	} else if (vm.data.result.type === "IntegerSquare" 
                			|| vm.data.result.type === "FloatSquare") {
                            tempData = [
                                {v: new Date(datum.t)},
                                {v: +datum.v},
                                {v: +datum.v2}
                            ];
                    	} else {
                            tempData = [
                                {v: new Date(datum.t)},
                                {v: +datum.v}
                            ];
                    	}
                    	
                    	if (tempData != null && tempData != undefined)
                    		rows.push( { c: tempData });
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
        
        function initChart() {
            vm.chartObject = {
	            type: 'LineChart',
	            options: {
	                title: '',
	                curveType: 'function',
	                colors: ['#00a19b', '#bbbbbb', '#29272a'],
	                displayExactValues: false,
	                width: '100%',
	                height: 215,
	                legend: {
	                    position: 'none'
	                },
	                hAxis: {},
	                chartArea: {
	                	top: 25,
	                	bottom: 25,
	                    right: 0,
	                    left: 50
	                },
	                pointSize: 0
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
        }
        
        vm.$onInit = function() {
        	vm.widget.subscribe("/widget-state", function(message) {
        		// TODO handle state change
        	});
        	
        	vm.widget.subscribe("/widget-error", function(message) {
        		// TODO handle error
        	});
        	
            vm.widget.subscribe("/widget-data", function(message) {
        		var widgetData = JSON.parse(message);
            	var json = { 
            		state: widgetData.state,
            		result: widgetData.data 
            	};
            	angular.extend(vm.data, json);
            	
                var cols = [{
	                    id: 'time',
	                    label: 'Time',
	                    type: 'datetime'
	                }, {
	                    id: 'id1',
	                    label: '',
	                    type: 'number'
	                }];
                
            	if (vm.data.result.type === "IntegerCube" 
            			|| vm.data.result.type === "FloatCube") {
	                cols = [{
	                    id: 'time',
	                    label: 'Time',
	                    type: 'datetime'
	                }, {
	                    id: 'id1',
	                    label: '',
	                    type: 'number'
	                }, {
	                    id: 'id2',
	                    label: '',
	                    type: 'number'
	                }, {
	                    id: 'id3',
	                    label: '',
	                    type: 'number'
	                }];
            	} else if (vm.data.result.type === "IntegerSquare" 
            			|| vm.data.result.type === "FloatSquare") {
	                cols = [{
	                    id: 'time',
	                    label: 'Time',
	                    type: 'datetime'
	                }, {
	                    id: 'id1',
	                    label: '',
	                    type: 'number'
	                }, {
	                    id: 'id2',
	                    label: '',
	                    type: 'number'
	                }, {
	                    id: 'id3',
	                    label: '',
	                    type: 'number'
	                }];            		
            	}
            	
            	vm.chartObject.data.cols = cols;
            	
            	if (widgetData.data.telemetryItems && widgetData.data.telemetryItems != null && widgetData.data.telemetryItems != undefined)
            		_processWsData(widgetData.data.telemetryItems);
            	
            	vm.data.waiting = false;
            	if (!vm.showChart)
            		vm.showChart = true;

            	vm.chartObject.options.title = vm.data.result.telemetryName;
            	
                $scope.$apply();
            });
            
            vm.widget.subscribeForMetaDataUpdate(function() {
            	$scope.$apply();
            });
            
            googleChartApiPromise.then(initChart);
        };
    }
})();