controllers.controller('ObjectLocationController', [
    '$scope', '$uibModalInstance', 'ToastrService', 'lastLocation', 'BingMapService', 'SpinnerService',
    function ($scope, $uibModalInstance, ToastrService, lastLocation, BingMapService, SpinnerService) {
        var DEFAULT_ZOOM_LEVEL = 16;
        var DEFAULT_CENTER = {
            latitude: 32.78306,
            longitude: -96.80667
        };

        $scope.lastLocation = angular.extend({}, lastLocation);
        $scope.searchAddress = {
            value: ''
        };

        $scope.loading = false;

        var map = null;
        var lastLocationPushpin;

        function placeLastLocationPushpin() {
            if (!map || $scope.lastLocation.latitude == null || $scope.lastLocation.longitude == null) return;
            var loc = new Microsoft.Maps.Location($scope.lastLocation.latitude, $scope.lastLocation.longitude);
            if (!lastLocationPushpin) {
                lastLocationPushpin = map.createMapPin(loc, {draggable: true});
                Microsoft.Maps.Events.addHandler(lastLocationPushpin, 'dragend', function(e) {
                    setLastLocationPosition(e.entity.getLocation());
                    $scope.$apply();
                });
            } else {
                lastLocationPushpin.setLocation(loc);
            }
        }

        function setLastLocationPosition(loc) {
            $scope.lastLocation.latitude = Math.round(loc.latitude*1000000)/1000000;
            $scope.lastLocation.longitude = Math.round(loc.longitude*1000000)/1000000;
            placeLastLocationPushpin();
        }

        $scope.$on('onAfterRender', function () {
            map = new BingMapService.MapInstance();

            var initialMapCenter;
            if ($scope.lastLocation.latitude != null && $scope.lastLocation.longitude != null) {
                initialMapCenter = $scope.lastLocation;
            } else {
                initialMapCenter = DEFAULT_CENTER;
            }

            var mapOptions = {
                disableZooming: false,
                showMapTypeSelector: true,
                showScalebar: true,
                tileBuffer: 3,
                showDashboard: true,
                mapTypeId: Microsoft.Maps.MapTypeId.road,
                zoom: DEFAULT_ZOOM_LEVEL,
                center: new Microsoft.Maps.Location(initialMapCenter.latitude, initialMapCenter.longitude)
            };

            map.getMap('lastLocationMap', mapOptions);

            placeLastLocationPushpin();

            $scope.search = function() {
                if ($scope.searchAddress.value) {
                    SpinnerService.wrap(map.geocode, $scope.searchAddress.value)
                    .then(function(result) {
                        if (result && result.results && result.results.length > 0) {
                            map.getToBounds(result.results[0].bestView);
                        } else {
                            ToastrService.popupError('Address is not found');
                        }
                    })
                    .catch(function() {
                        ToastrService.popupError('Search failed');
                    });
                }
            };

            $scope.viewLocation = function() {
                if ($scope.lastLocation.latitude != null && $scope.lastLocation.longitude != null) {
                    map.getToPoint($scope.lastLocation.latitude, $scope.lastLocation.longitude, map.getZoom());
                    placeLastLocationPushpin();
                }
            };

            $scope.setLocation = function() {
                setLastLocationPosition(map.getCenter());
            };
        });

        $scope.$on('$destroy', function () {
            if (map) {
                map.destroy();
            }
        });

        $scope.ok = function (form) {
            if (form.$valid) {
                $uibModalInstance.close($scope.lastLocation);
            } else {
                ToastrService.popupError("The form is invalid! Please make changes and try again.");
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
]);
