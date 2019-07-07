services.factory('TelemetryUnitService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/kronos/telemetryunit/find', {
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

        function save(telemetryUnit) {
            if (telemetryUnit.id) {
                return $http.put('/api/kronos/telemetryunit/'+telemetryUnit.id, telemetryUnit);
            } else {
                return $http.post('/api/kronos/telemetryunit', telemetryUnit);
            }
        }

        return {
            find: find,
            save: save
        };
    }
]);
