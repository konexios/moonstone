services.factory('NodeService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/kronos/node/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                nodeTypeIds: filter.nodeTypeIds,
                enabled: filter.enabled
            });
        }

        function findChildNodes(parentNodeId) {
            return $http.get('/api/kronos/node/find'+(parentNodeId ? '/'+parentNodeId : ''));
        }

        function getNodeTypes() {
            return $http.get('/api/kronos/node/nodetypes');
        }

        function getNodeOptions() {
            return $http.get('/api/kronos/node/nodeoptions');
        }

        function save(node) {
            if (node.id) {
                return $http.put('/api/kronos/node/'+node.id, node);
            } else {
                return $http.post('/api/kronos/node', node);
            }
        }

        return {
            getNodeTypes: getNodeTypes,
            getNodeOptions: getNodeOptions,
            find: find,
            findChildNodes: findChildNodes,
            save: save
        };
    }
]);
