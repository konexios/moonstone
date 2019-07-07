services.factory('DeviceProductService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/rhea/device-products/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                enabled: filter.enabled,
                deviceCategories: filter.deviceCategories,
                deviceManufacturerIds: filter.deviceManufacturerIds
            });
        }

        function save(deviceProduct) {
            if (deviceProduct.id) {
                return $http.put('/api/rhea/device-products/'+deviceProduct.id, deviceProduct);
            } else {
                return $http.post('/api/rhea/device-products', deviceProduct);
            }
        }

        function options() {
            return $http.get('/api/rhea/device-products/options');
        }

        return {
            find: find,
            save: save,
            options: options
        };
    }
]);
