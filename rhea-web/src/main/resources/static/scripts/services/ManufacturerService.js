services.factory('ManufacturerService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/rhea/device-manufacturers/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                enabled: filter.enabled
            });
        }

        function save(manufacturer) {
            if (manufacturer.id) {
                return $http.put('/api/rhea/device-manufacturers/'+manufacturer.id, manufacturer);
            } else {
                return $http.post('/api/rhea/device-manufacturers', manufacturer);
            }
        }

        return {
            find: find,
            save: save
        };
    }
]);
