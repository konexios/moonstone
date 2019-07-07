services.factory('DeviceTypeService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/rhea/device-types/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                enabled: filter.enabled,
                deviceProductIds: filter.deviceProductIds
            });
        }

        function options() {
            return $http.get('/api/rhea/device-types/options');
        }

        function save(deviceType) {
            if (deviceType.id) {
                return $http.put('/api/rhea/device-types/'+deviceType.id, deviceType);
            } else {
                return $http.post('/api/rhea/device-types', deviceType);
            }
        }

        return {
            find: find,
            options: options,
            save: save
        };
    }
]);
