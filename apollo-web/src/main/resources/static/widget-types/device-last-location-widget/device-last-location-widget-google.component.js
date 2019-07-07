(function() {
    'use strict';

    angular
        .module('widgets')
        .component('deviceLastLocationWidget', {
            templateUrl: '/widget-types/device-last-location-widget/device-last-location-widget-google.tmpl.html',
            controller: widgetDeviceLastLocationController,
            controllerAs: 'vm',
            bindings: {
                widget: '='
            }
        });


    function widgetDeviceLastLocationController($scope) {
        var vm = this;
        vm.data = {
        	waiting: true,
        	error: false,
        	state: null,
            mapData: {
            	map: null,
            	marker: null,
            	infoWindow: null,
            	infoWindowOpen: false
            },
        	result: null
        };
        
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
                $scope.$apply();

                if (vm.data.result == undefined) {
                	
                } else {
	                vm.data.result.latlng = { lat: parseFloat(vm.data.result.latitude), lng: parseFloat(vm.data.result.longitude) };
	                if (vm.data.mapData.map == null) {
	                    vm.initMap(vm.data);
	                    vm.createMarker(vm.data);
	                } else {
	                    vm.updateMarker(vm.data);
	                    vm.setMapCenter(vm.data.result.latlng);
	                    
	                    // TODO revisit temporary fix
	                    google.maps.event.trigger(vm.data.mapData.map,'resize');
	                }
                }

                vm.data.waiting = false;
                $scope.$apply();
            });
            
            vm.widget.subscribeForMetaDataUpdate(function() {
            	$scope.$apply();
            });
        };
        
        vm.initMap = function(data) {
            vm.data.mapData.map = new google.maps.Map(document.getElementById('widget-map-' + data.result.id), {
              zoom: 16,
              center: data.result.latlng
            });
        };

        vm.createMarker = function(data) {
        	var contentString = "<strong>" + data.result.name + "</strong>";
        	contentString += "<br/>Latitude: " + data.result.latitude;
        	contentString += "<br/>Longitude: " + data.result.longitude;
        	contentString += "<br/>Time: " + data.result.timestamp;
        	
        	// marker
        	vm.data.mapData.marker = new google.maps.Marker({
                map: vm.data.mapData.map,
                position: data.result.latlng
             });

            // info window
        	vm.data.mapData.infowindow = new google.maps.InfoWindow({ content: contentString });
            
            google.maps.event.addListener(vm.data.mapData.marker, 'click', function() {
            	vm.data.mapData.infoWindowOpen = true;
            	vm.data.mapData.infowindow.open(vm.data.mapData.map, vm.data.mapData.marker);
            });
            
            google.maps.event.addListener(vm.data.mapData.infowindow,'closeclick',function(){
            	vm.data.mapData.infoWindowOpen = false;
            });
        };
        
        vm.updateMarker = function(data) {
        	vm.data.mapData.marker.position = data.result.latlng;
        };
        
        vm.setMapCenter = function(latlng) {
        	if (!vm.data.mapData.infoWindowOpen)
        		vm.data.mapData.map.setCenter(latlng);
        };
    }
})();