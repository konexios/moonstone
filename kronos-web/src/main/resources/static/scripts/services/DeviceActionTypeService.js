services.factory('DeviceActionTypeService',
    [ '$http',
    function($http) {
        function find(page) {
            return $http.post('/api/kronos/deviceactiontype/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property
            });
        }

        function save(deviceActionType) {
            if (deviceActionType.id) {
                return $http.put('/api/kronos/deviceactiontype/'+deviceActionType.id, deviceActionType);
            } else {
                return $http.post('/api/kronos/deviceactiontype', deviceActionType);
            }
        }

        return {
            find: find,
            save: save
        };
    }
]);
