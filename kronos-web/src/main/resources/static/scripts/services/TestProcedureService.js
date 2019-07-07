services.factory('TestProcedureService', ['$http',
    function($http) {

        function find(page, filter) {
            return $http.post('/api/kronos/testprocedure/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                enabled: filter.enabled,
                deviceTypeIds: filter.deviceTypeIds
            });
        }

        function options() {
            return $http.get('/api/kronos/testprocedure/options');
        }

        function get(id) {
            return $http.get('/api/kronos/testprocedure/' + id);
        }

        function save(testProcedure) {
            if (testProcedure.id) {
                return $http.put('/api/kronos/testprocedure/' + testProcedure.id, testProcedure);
            } else {
                return $http.post('/api/kronos/testprocedure', testProcedure);
            }
        }

        return {
            find: find,
            options: options,
            get: get,
            save: save
        };
    }
]);