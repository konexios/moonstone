services.factory('SoftwareReleaseScheduleService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/kronos/softwarereleaseschedule/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                scheduledDateFrom: filter.scheduledDateFrom != null ? filter.scheduledDateFrom.getTime() : null,
                scheduledDateTo: filter.scheduledDateTo != null ? filter.scheduledDateTo.getTime() : null,
                deviceCategories: filter.deviceCategories,
                statuses: filter.statuses
            });
        }

        function filterOptions() {
            return $http.get('/api/kronos/softwarereleaseschedule/filter-options');
        }

        function getSelectionOptions(selection) {
            return $http.post('/api/kronos/softwarereleaseschedule/selection-options', selection);
        }

        function findOne(swReleaseScheduleId) {
            return $http.get('/api/kronos/softwarereleaseschedule/' + swReleaseScheduleId);
        }

        function save(swReleaseSchedule) {
            swReleaseSchedule = angular.merge({}, swReleaseSchedule);
            swReleaseSchedule.scheduledDate = swReleaseSchedule.scheduledDate.getTime();
            if (swReleaseSchedule.id) {
                return $http.put('/api/kronos/softwarereleaseschedule/'+swReleaseSchedule.id, swReleaseSchedule);
            } else {
                return $http.post('/api/kronos/softwarereleaseschedule', swReleaseSchedule);
            }
        }

        return {
            find: find,
            filterOptions: filterOptions,
            getSelectionOptions: getSelectionOptions,
            findOne: findOne,
            save: save
        };
    }
]);
