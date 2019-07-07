services.factory("BingMapService", ["$http", "$q", "BingMapKey", function ($http, $q, BingMapKey) {

    function MapInstance() {
        var map = null;
        var searchManager = null;

        this.getMap = function (mapId, options) {
            if (angular.isUndefined(options))
                options = {};

            // check default options
            options.credentials = angular.isDefined(options.credentials) ? options.credentials : BingMapKey;
            options.disableUserInput = angular.isDefined(options.disableUserInput) ? options.disableUserInput : false;
            options.disableZooming = angular.isDefined(options.disableZooming) ? options.disableZooming : true;
            options.enableClickableLogo = angular.isDefined(options.enableClickableLogo) ? options.enableClickableLogo : false;
            options.enableHighDpi = angular.isDefined(options.enableHighDpi) ? options.enableHighDpi : true;
            options.enableSearchLogo = angular.isDefined(options.enableSearchLogo) ? options.enableSearchLogo : false;
            options.enableSearchLogo = angular.isDefined(options.enableSearchLogo) ? options.enableSearchLogo : false;
            options.mapTypeId = angular.isDefined(options.mapTypeId) ? options.mapTypeId : Microsoft.Maps.MapTypeId.birdseye;
            options.showBreadcrumb = angular.isDefined(options.showBreadcrumb) ? options.showBreadcrumb : false;
            options.showCopyright = angular.isDefined(options.showCopyright) ? options.showCopyright : false;
            options.showDashboard = angular.isDefined(options.showDashboard) ? options.showDashboard : false;
            options.showLogo = angular.isDefined(options.showLogo) ? options.showLogo : false;
            options.showMapTypeSelector = angular.isDefined(options.showMapTypeSelector) ? options.showMapTypeSelector : false;
            options.showScalebar = angular.isDefined(options.showScalebar) ? options.showScalebar : false;
            options.tileBuffer = angular.isDefined(options.tileBuffer) ? options.tileBuffer : 1;
            options.zoom = angular.isDefined(options.zoom) ? options.zoom : 0;

            map = new Microsoft.Maps.Map(document.getElementById(mapId), options);
        };

        this.getZoom = function () {
            return map.getZoom();
        };

        this.getCenter = function () {
            return map.getCenter();
        };

        function LoadSearchModule() {
            return searchManager !== null ? $q.resolve(searchManager) : $q(function(resolve) {
                Microsoft.Maps.loadModule('Microsoft.Maps.Search', {
                    callback: function() {
                        searchManager =  new Microsoft.Maps.Search.SearchManager(map);
                        resolve(searchManager);
                    }
                })
            });
        }

        function search_showInfoBox(result) {
            var currInfobox;
            if (currInfobox) {
                currInfobox.setOptions({visible: true});
                map.entities.remove(currInfobox);
            }
            currInfobox = new Microsoft.Maps.Infobox(
                result.location,
                {
                    title: result.name,
                    description: [result.address, result.city, result.state, result.country, result.phone].join(' '),
                    showPointer: true,
                    titleAction: null,
                    titleClickHandler: null
                });
            currInfobox.setOptions({visible: true});
            map.entities.push(currInfobox);
        }

        this.createMapPin = function(loc, opt) {
            var pin = null;
            if (loc) {
                pin = new Microsoft.Maps.Pushpin(loc, opt);
                map.entities.push(pin);
            }
            return pin;
        };

        this.createMapPinFromSearchResult = function(result) {
            var pin = null;
            if (result) {
                pin = new Microsoft.Maps.Pushpin(result.location, null);
                Microsoft.Maps.Events.addHandler(pin, 'click', function () {
                    search_showInfoBox(result)
                });
                map.entities.push(pin);
            }
            return pin;
        };

        this.removeMapEntity = function(entity) {
            map.entities.remove(entity);
        };

        this.removeMapEntities = function(entities) {
            entities.forEach(function(entity) {
                map.entities.remove(entity);
            });
        };

        this.removeAllMapEntities = function() {
            map.entities.clear();
        };

        this.find = function(queryStr) {
            return LoadSearchModule().then(function(searchManager) {
                return $q(function(resolve, reject) {
                    var request = {
                        query: query,
                        count: 5,
                        startIndex: 0,
                        bounds: map.getBounds(),
                        callback: resolve,
                        errorCallback: reject
                    };
                    searchManager.search(request);
                });
            });
        };


        this.geocode = function(address) {
            return LoadSearchModule().then(function(searchManager) {
                return $q(function(resolve, reject) {
                    var request = {
                        where: address,
                        count: 1,
                        bounds: map.getBounds(),
                        callback: resolve,
                        errorCallback: reject
                    };
                    searchManager.geocode(request);
                });
            });
        };

        this.getToPoint = function(latitude, longitude, zoom) {
            map.setView({
                center: {
                    latitude: parseFloat(latitude),
                    longitude: parseFloat(longitude)
                },
                zoom: parseInt(zoom, 10)
            })
        };

        this.getToBounds = function(bounds) {
            map.setView({bounds:bounds});
        };

        this.addEventHandler = function(eventName, callback) {
            // returns handlerId which can be used to remove event handler
            return Microsoft.Maps.Events.addHandler(map, eventName, callback);
        };

        this.removeEventHandler = function(handlerId) {
            return Microsoft.Maps.Events.removeHandler(handlerId);
        };

        this.destroy = function() {
            map.dispose();
        };
    }

    return {
        MapInstance: MapInstance
    };
}]);
