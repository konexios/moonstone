services.factory('KronosService', ['$http',
    function($http) {

        function getVersion() {
            return $http.get('/api/kronos/version/webapp');
        }
        
    	function componentsVersion() {
    		return $http({
    			"url": "/api/kronos/version/components",
    			"method": "GET"
    		});		
    	}

        function getPageSettings() {
            return $http.get('/api/kronos/version/settings');
        }

        return {
            getVersion: getVersion,
            componentsVersion: componentsVersion,
            getPageSettings: getPageSettings
        };
    }
]);

services.factory('CommonService', ['$filter',
    function($filter) {

        var formats = {
            dateTimeFormat: 'MM/dd/yyyy h:mm a',
            dateFormat: 'MM/dd/yyyy'
        };

        function getFormatredDate(date, displayIfNull) {
            if (displayIfNull && !date) {
                return displayIfNull;
            }
            return $filter('date')(date, formats.dateTimeFormat);
        }

        function getDuration(startDate, endDate) {
            var durationInMs = endDate - startDate;
            return $filter('duration')(durationInMs);
        }

        return {
            formats: formats,
            getFormatredDate: getFormatredDate,
            getDuration: getDuration
        };
    }
]);