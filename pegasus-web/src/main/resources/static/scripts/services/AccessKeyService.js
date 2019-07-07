angular.module('pegasus').factory('AccessKeyService',  ["$rootScope", "$location", "$http", function($rootScope, $location, $http) {

        function find(searchFilter, entity) {
            return $http({
                "url": "/api/pegasus/" + entity.types + "/" + entity.id + "/accesskeys",
                "method": "POST",
                "headers": {"Content-Type" : "application/json"},
                "data": searchFilter
            });
        }

        function getPriForType(type, id) {
            return $http.get('/api/pegasus/accesskey/' + type + '/' + id + '/pri');
        }

        function get(accessKeyId, entityType, entityId) {
            return $http.get("/api/pegasus/" + entityType + "/" + entityId + "/accesskey/" + accessKeyId);
        }

        function save(accessKey, entity) {
            if (accessKey.id) {
                return $http.put('/api/pegasus/' + entity.entityTypes + '/' + entity.entityId + '/accesskey', accessKey);
            } else {
                return $http.post('/api/pegasus/' + entity.entityTypes + '/' + entity.entityId + '/accesskey', accessKey);
            }
        }

        return {
            find: find,
            getPriForType: getPriForType,
            get: get,
            save: save
        };
    }
]);