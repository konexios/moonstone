services.factory('FMSummaryService', ['$http', function($http) {

    return {
        statuses: statuses,
        tabs: tabs,
        summary: summary,
        filterOptions: filterOptions,
        find: find,
        eligible: eligible
    };


    function statuses() {
        return {
            'SCHEDULED': { name: 'Pending', tabId: 'SCHEDULED', tabName: 'Pending' },
            'INPROGRESS': { name: 'In progress', tabId: 'INPROGRESS', tabName: 'In progress' },
            'COMPLETE': { name: 'Processed', tabId: 'COMPLETE', tabName: 'Processed' },
            'CANCELLED': { name: 'Cancelled', tabId: 'COMPLETE', tabName: 'Processed' }
        };
    }

    function tabs() {
        return [
            { id: 'SCHEDULED', name: 'Pending', countField: 'pendingJobs' },
            { id: 'INPROGRESS', name: 'In progress', countField: 'inProgressJobs' },
            { id: 'COMPLETE', name: 'Processed', countField: 'processedJobs' }
        ];
    }

    function summary() {
        return $http.get('/api/kronos/softwarereleaseschedule/summary');
    }

    function filterOptions(status) {
        return $http.get('/api/kronos/softwarereleaseschedule/filter-options/' + status);
    }

    function find(status, page, filter) {
        return $http.post('/api/kronos/softwarereleaseschedule/find/' + status, {
            // pagination
            pageIndex: page.pageIndex,
            itemsPerPage: page.itemsPerPage,
            // sorting
            sortDirection: page.sort.direction,
            sortField: page.sort.property,
            // filter
            jobNumbers: filter.jobNumbers,
            requestors: filter.requestors,
            deviceTypes: filter.deviceTypes,
            startDates: filter.startDates,
            completedDates: filter.completedDates,
            statuses: filter.statuses
        });
    }

    function eligible() {
        return $http.get('/api/kronos/softwarereleaseschedule/eligible');
    }
}]);