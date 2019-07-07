services.factory('SoftwareProductService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/rhea/software-products/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                softwareVendorIds: filter.softwareVendorIds,
                enabled: filter.enabled,
                editable: filter.editable
            });
        }

        function save(softwareProduct) {
            if (softwareProduct.id) {
                return $http.put('/api/rhea/software-products/'+softwareProduct.id, softwareProduct);
            } else {
                return $http.post('/api/rhea/software-products', softwareProduct);
            }
        }

        function options() {
            return $http.get('/api/rhea/software-products/options');
        }

        return {
            find: find,
            save: save,
            options: options
        };
    }
]);
