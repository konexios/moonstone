services.factory('FMJobWizardService', ['$http', function($http) {

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

    function audit(swReleaseScheduleId) {
        return $http.get('/api/kronos/softwarereleaseschedule/' + swReleaseScheduleId + '/audit');
    }

    function checkAssets(swReleaseScheduleId, assets) {
        if(swReleaseScheduleId) {
            return $http.post('/api/kronos/softwarereleaseschedule/checkAssets/' + swReleaseScheduleId, assets);
        } else {
            return $http.post('/api/kronos/softwarereleaseschedule/checkAssets/', assets);
        }
    }

    function save(swReleaseSchedule) {
        swReleaseSchedule = angular.merge({}, swReleaseSchedule);

        if (swReleaseSchedule.scheduledDate !== null) {
            swReleaseSchedule.scheduledDate = swReleaseSchedule.scheduledDate.getTime();
        }

        if (swReleaseSchedule.id) {
            return $http.put('/api/kronos/softwarereleaseschedule/'+swReleaseSchedule.id, swReleaseSchedule);
        } else {
            return $http.post('/api/kronos/softwarereleaseschedule', swReleaseSchedule);
        }
    }

    function findJobAuditLogs(swReleaseScheduleId, page, filter) {
        return $http.post('/api/kronos/softwarereleaseschedule/'+swReleaseScheduleId+'/logs', {
            // pagination
            pageIndex: page.pageIndex,
            itemsPerPage: page.itemsPerPage,
            // sorting
            sortDirection: page.sort.direction,
            sortField: page.sort.property,
            // filter
            createdDateFrom: filter.createdDateFrom ? Math.round(filter.createdDateFrom.getTime()/1000) : null,
            createdDateTo: filter.createdDateTo ? Math.round(filter.createdDateTo.getTime()/1000) : null,
            userIds: filter.userIds,
            types: filter.types,
            assetIds: filter.assetIds
        });
    }

    function findJobAssets(swReleaseScheduleId, page, status) {
        return $http.post('/api/kronos/softwarereleaseschedule/'+swReleaseScheduleId+'/assets', {
            // pagination
            pageIndex: page.pageIndex,
            itemsPerPage: page.itemsPerPage,
            // sorting
            sortDirection: page.sort.direction,
            sortField: page.sort.property,
            // custom filters
            status: status
        });
    }

    function getJobAuditLogOptions(swReleaseScheduleId) {
        return $http.get('/api/kronos/softwarereleaseschedule/log/options/' + swReleaseScheduleId);
    }

    function getJobAuditLog(logId) {
        return $http.get('/api/kronos/softwarereleaseschedule/log/' + logId);
    }

    function cancel(swReleaseScheduleId) {
        return $http.post('/api/kronos/softwarereleaseschedule/'+swReleaseScheduleId+'/cancel', {});
    }

    function moveSoftwareReleaseTransactionsToError(softwareReleaseScheduleId, softwareReleaseTransIds) {    	
    	return $http.post('/api/kronos/softwarereleaseschedule/transactions/move-to-error', {
    			softwareReleaseScheduleId: softwareReleaseScheduleId,
    			softwareReleaseTransIds: softwareReleaseTransIds
   		});
    }
    
    // AC-864
    function retrySoftwareReleaseTransactionsOnError(softwareReleaseScheduleId, softwareReleaseTransIds) {
    	//alert('retrySoftwareReleaseTransactionsOnError');
    	return $http.post('/api/kronos/softwarereleaseschedule/transactions/retry-on-error', {
    			softwareReleaseScheduleId: softwareReleaseScheduleId,
    			softwareReleaseTransIds: softwareReleaseTransIds
   		});
    }
    
    function retrySoftwareReleaseTransactions(softwareReleaseScheduleId, softwareReleaseTransIds) {
    	return $http.post('/api/kronos/softwarereleaseschedule/transactions/retry', {
    			softwareReleaseScheduleId: softwareReleaseScheduleId,
    			softwareReleaseTransIds: softwareReleaseTransIds
   		});
    }
    
    function cancelSoftwareReleaseTransactions(softwareReleaseScheduleId, softwareReleaseTransIds) {
    	return $http.post('/api/kronos/softwarereleaseschedule/transactions/cancel', {
			softwareReleaseScheduleId: softwareReleaseScheduleId,
			softwareReleaseTransIds: softwareReleaseTransIds
		});
    }

    function start(swReleaseScheduleId) {
        return $http.post('/api/kronos/softwarereleaseschedule/'+swReleaseScheduleId+'/start', {});
    }

    return {
        find: find,
        filterOptions: filterOptions,
        getSelectionOptions: getSelectionOptions,
        findOne: findOne,
        audit: audit,
        checkAssets: checkAssets,
        save: save,
        findJobAuditLogs: findJobAuditLogs,
        findJobAssets: findJobAssets,
        getJobAuditLog: getJobAuditLog,
        getJobAuditLogOptions: getJobAuditLogOptions,
        cancel: cancel,
        retrySoftwareReleaseTransactions: retrySoftwareReleaseTransactions,
        cancelSoftwareReleaseTransactions: cancelSoftwareReleaseTransactions,
        moveSoftwareReleaseTransactionsToError: moveSoftwareReleaseTransactionsToError,
        retrySoftwareReleaseTransactionsOnError: retrySoftwareReleaseTransactionsOnError,
        start: start
    };

}]);