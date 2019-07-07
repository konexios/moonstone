services.factory('AccessKeyService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/kronos/accesskey/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                name: filter.name,
                accessLevels: filter.accessLevels,
                pri: filter.pri,
                expirationDateFrom: filter.expirationDateFrom && filter.expirationDateFrom.getTime(),
                expirationDateTo: filter.expirationDateTo && filter.expirationDateTo.getTime()
            });
        }

        function getOptions() {
            return $http.get('/api/kronos/accesskey/options');
        }

        function get(accessKeyId) {
            return $http.get('/api/kronos/accesskey/'+accessKeyId);
        }

        function save(accessKey) {
            if (accessKey.id) {
                return $http.put('/api/kronos/accesskey/'+accessKey.id, accessKey);
            } else {
                return $http.post('/api/kronos/accesskey', accessKey);
            }
        }

        return {
            find: find,
            getOptions: getOptions,
            get: get,
            save: save
        };
    }
]);
