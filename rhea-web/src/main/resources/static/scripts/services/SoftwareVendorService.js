services.factory('SoftwareVendorService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/rhea/software-vendors/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                enabled: filter.enabled,
                editable: filter.editable
            });
        }

        function save(softwareVendor) {
            if (softwareVendor.id) {
                return $http.put('/api/rhea/software-vendors/'+softwareVendor.id, softwareVendor);
            } else {
                return $http.post('/api/rhea/software-vendors', softwareVendor);
            }
        }

        return {
            find: find,
            save: save
        };
    }
]);
