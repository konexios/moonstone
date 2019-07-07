services.factory('ConfigurationService',
    ['$http',
        function ($http) {

            function syncConfigToGateway(gatewayId) {
                return $http.post('/api/kronos/gateway/' + gatewayId + '/configuration/cloud/restore');
            }

            function syncGatewayConfigToCloud(gatewayId) {
                return $http.post('/api/kronos/gateway/' + gatewayId + '/configuration/cloud/sync');
            }

            function syncConfigToDevice(deviceId) {
                return $http.post('/api/kronos/device/' + deviceId + '/configuration/cloud/restore');
            }

            function syncDeviceConfigToCloud(deviceId) {
                return $http.post('/api/kronos/device/' + deviceId + '/configuration/cloud/sync');
            }

            return {
                syncConfigToGateway: syncConfigToGateway,
                syncGatewayConfigToCloud: syncGatewayConfigToCloud,
                syncConfigToDevice: syncConfigToDevice,
                syncDeviceConfigToCloud: syncDeviceConfigToCloud
            };

        }
    ]);