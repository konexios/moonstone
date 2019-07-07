services.factory('EventService',
    [ '$http',
    function($http) {
        function getStatuses(eventIds) {
            return $http.post('/api/kronos/event/statuses', eventIds);
        }

        return {
            getStatuses: getStatuses
        };
    }
]);
