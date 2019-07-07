services.factory('NodeTypeService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/kronos/nodetype/find', {
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

        function save(nodeType) {
            if (nodeType.id) {
                return $http.put('/api/kronos/nodetype/'+nodeType.id, nodeType);
            } else {
                return $http.post('/api/kronos/nodetype', nodeType);
            }
        }

        function options() {
            return $http.get('/api/kronos/nodetype/options');
        }

        return {
            find: find,
            save: save,
            options: options
        };
    }
]);
