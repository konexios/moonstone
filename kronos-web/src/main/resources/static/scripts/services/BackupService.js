services.factory('BackupService',
    ['$http',
        function ($http) {
            function findGatewayBackups(gatewayId, page, filter) {
                return $http.post('/api/kronos/gateway/' + gatewayId + '/backups/find', {
                    // pagination
                    pageIndex: page.pageIndex,
                    itemsPerPage: page.itemsPerPage,
                    // sorting
                    sortDirection: page.sort.direction,
                    sortField: page.sort.property,
                    // filter
                    name: filter.name
                });
            }

            function createGatewayBackup(gatewayId, backupName) {
                return $http.post('/api/kronos/gateway/' + gatewayId + '/backups/create', {
                    name: backupName
                });
            }

            function createDeviceBackup(deviceId, backupName) {
                return $http.post('/api/kronos/device/' + deviceId + '/backups/create', {
                    name: backupName
                });
            }

            function restoreDeviceBackup(deviceId, backupId) {
                return $http.post('/api/kronos/device/' + deviceId + '/backups/' + backupId + '/restore', {});
            }

            function restoreGatewayBackup(gatewayId, backupId) {
                return $http.post('/api/kronos/gateway/' + gatewayId + '/backups/' + backupId + '/restore', {});
            }

            function findDeviceBackups(deviceId, page, filter) {
                return $http.post('/api/kronos/device/' + deviceId + '/backups/find', {
                    // pagination
                    pageIndex: page.pageIndex,
                    itemsPerPage: page.itemsPerPage,
                    // sorting
                    sortDirection: page.sort.direction,
                    sortField: page.sort.property,
                    // filter
                    name: filter.name
                });
            }

            function backupGatewayConfigs(backupName, gatewayIds) {
                return $http.post('/api/kronos/gateway/backups/create', {
                    name: backupName,
                    ids: gatewayIds
                });
            }

            function backupDeviceConfigs(backupName, deviceIds) {
                return $http.post('/api/kronos/device/backups/create', {
                    name: backupName,
                    ids: deviceIds
                });
            }

            return {
                findGatewayBackups: findGatewayBackups,
                findDeviceBackups: findDeviceBackups,
                createGatewayBackup: createGatewayBackup,
                createDeviceBackup: createDeviceBackup,
                restoreDeviceBackup: restoreDeviceBackup,
                restoreGatewayBackup: restoreGatewayBackup,
                backupGatewayConfigs: backupGatewayConfigs,
                backupDeviceConfigs: backupDeviceConfigs
            };
        }
    ]);
