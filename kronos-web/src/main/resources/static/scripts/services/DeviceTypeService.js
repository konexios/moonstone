services.factory('DeviceTypeService', ['$http',
    function($http) {
        function find(page) {
            return $http.post('/api/kronos/devicetype/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property
            });
        }

        function options(selection) {
            return $http.post('/api/kronos/devicetype/options', selection);
        }

        function findOne(deviceTypeId) {
            return $http.get('/api/kronos/devicetype/' + deviceTypeId);
        }

        function save(deviceType) {
            if (deviceType.id) {
                return $http.put('/api/kronos/devicetype/' + deviceType.id + '/edit/general', deviceType);
            } else {
                return $http.post('/api/kronos/devicetype/create', deviceType);
            }
        }

        function syncDeviceType(deviceId) {
            return $http.post('/api/kronos/devicetype/device/' + deviceId);
        }

        function enableDeviceType(deviceTypeId) {
            return $http.post('/api/kronos/devicetype/' + deviceTypeId + '/enable');
        }

        function disableDeviceType(deviceTypeId) {
            return $http.post('/api/kronos/devicetype/' + deviceTypeId + '/disable');
        }

        function saveDeviceTypeTelemetry(deviceType) {
            return $http.put('/api/kronos/devicetype/' + deviceType.id + '/edit/telemetry', deviceType);
        }

        function saveDeviceTypeStateMetadata(deviceType) {
            return $http.put('/api/kronos/devicetype/' + deviceType.id + '/edit/statemetadata', deviceType);
        }

        function saveDeviceTypeActions(deviceType) {
            return $http.put('/api/kronos/devicetype/' + deviceType.id + '/edit/actions', deviceType);
        }

        function saveDeviceTypeFirmware(deviceType) {
            return $http.put('/api/kronos/devicetype/' + deviceType.id + '/edit/firmware', deviceType);
        }

        return {
            find: find,
            findOne: findOne,
            options: options,
            save: save,
            syncDeviceType: syncDeviceType,
            enableDeviceType: enableDeviceType,
            disableDeviceType: disableDeviceType,
            saveDeviceTypeTelemetry: saveDeviceTypeTelemetry,
            saveDeviceTypeStateMetadata: saveDeviceTypeStateMetadata,
            saveDeviceTypeActions: saveDeviceTypeActions,
            saveDeviceTypeFirmware: saveDeviceTypeFirmware
        };
    }
]);