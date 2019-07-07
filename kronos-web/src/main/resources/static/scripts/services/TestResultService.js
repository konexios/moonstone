services.factory('TestResultService', ['$http',
    function($http) {

        function find(page, filter) {
            return $http.post('/api/kronos/testresult/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                statuses: filter.statuses,
                stepStatuses: filter.stepStatuses,
                //categories: filter.categories,
                testProcedureIds: filter.testProcedureIds,
                objectId: filter.objectId
            });
        }

        function options() {
            return $http.get('/api/kronos/testresult/options');
        }

        function get(id) {
            return $http.get('/api/kronos/testresult/' + id);
        }

        function save(testResult) {
            if (testResult.id) {
                return $http.put('/api/kronos/testresult/' + testResult.id, testResult);
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