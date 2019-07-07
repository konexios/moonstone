services.factory('HomeService',
    ['$http', '$q',
        function ($http, $q) {
            
	        function checkEULA() {
	            return $http.get('/api/kronos/home/checkeula');
	        }
	        
	        function updateEULA() {
	            return $http.post('/api/kronos/home/updateeula');
	        }
    	
    		function myDevices(page) {
                return $http.post('/api/kronos/home/devices', {
                    // pagination
                    pageIndex: page.pageIndex,
                    itemsPerPage: page.itemsPerPage
                });
            }

            function myGateways(page) {
                return $http.post('/api/kronos/home/gateways', {
                    // pagination
                    pageIndex: page.pageIndex,
                    itemsPerPage: page.itemsPerPage
                });
            }

            function myDeviceEvents(page) {
                return $http.post('/api/kronos/home/device/events', {
                    // pagination
                    pageIndex: page.pageIndex,
                    itemsPerPage: page.itemsPerPage
                });
            }

            return {
            	checkEULA: checkEULA,
            	updateEULA: updateEULA,
                myDevices: myDevices,
                myGateways: myGateways,
                myDeviceEvents: myDeviceEvents
            };
        }
    ]);
