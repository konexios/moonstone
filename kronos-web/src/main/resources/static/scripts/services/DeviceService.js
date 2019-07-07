services.factory('DeviceService',
    [ '$http', '$q',
    function($http, $q) {
        function find(page, filter, bulkEdit) {
            return $http.post('/api/kronos/device/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
				hid: filter.hid,
                uid: filter.uid,
                deviceTypeIds: filter.deviceTypeIds,
                gatewayIds: filter.gatewayIds,
                userIds: filter.userIds,
                nodeIds: filter.nodeIds,
                enabled: filter.enabled
            }, {
                params: {
                    bulkEdit: bulkEdit
                }
            });
        }

        function getSearchCriteria() {
            return $http.get('/api/kronos/device/filter');
        }

        function newDevice() {
        	return $http.get('/api/kronos/device/new');
        }

        function create(device) {
    		return $http({
    			"url": "/api/kronos/device/create",
    			"method": "POST",
    			"headers": {"Content-Type" : "application/json"},
    			"data": device
    		});
        }

        function getDeviceDetails(deviceId) {
            return $http.get('/api/kronos/device/'+deviceId);
        }

        function getDeviceStats(deviceId) {
            return $http.get('/api/kronos/device/'+deviceId+'/stats', {
                params: {
                    tz: Util.getTimezone()
                }
            });
        }

        function getDeviceLastTelemetry(deviceId) {
            return $http.get('/api/kronos/device/'+deviceId+'/lastTelemetry');
        }

        function getEditOptions(deviceTypeId) {
            return $http.get('/api/kronos/device/options', { params: { deviceTypeId: deviceTypeId }});
        }

        function saveDeviceSettings(deviceModel) {
            return $http.put('/api/kronos/device/'+deviceModel.id+'/settings', {
                id: deviceModel.id,
                hid: deviceModel.hid,
                name: deviceModel.name,
                deviceTypeId: deviceModel.deviceTypeId,
                uid: deviceModel.uid,
                enabled: deviceModel.enabled,
                user: deviceModel.user,
                node: deviceModel.node,
                softwareRelease: deviceModel.softwareRelease,
                properties: deviceModel.properties
            });
        }

        function deleteDevice(deviceId) {
            return $http.delete('/api/kronos/device/'+deviceId);
        }

        function deleteDevices(deviceIds) {
            return $http.post('/api/kronos/device/bulkDelete', deviceIds);
        }

        function saveDeviceTags(deviceModel) {
            return $http.put('/api/kronos/device/'+deviceModel.id+'/tags', deviceModel.tags);
        }

        function createDeviceTag(deviceTag) {
            return $http.post('/api/kronos/device/tags', deviceTag);
        }

        function saveDeviceAction(deviceModel, index, deviceAction) {
            if (index >= 0 && index < deviceModel.actions.length) {
                // update device action
                return $http.put('/api/kronos/device/'+deviceModel.id+'/action/'+index, deviceAction);
            } else {
                // create device action
                return $http.post('/api/kronos/device/'+deviceModel.id+'/action', deviceAction);
            }
        }

        function deleteDeviceActions(deviceModel, actionIndices) {
            return $http.post('/api/kronos/device/'+deviceModel.id+'/delete-actions', actionIndices);
        }

        function findEvents(deviceId, page, filter) {
            // check if createdDateFrom is less than createdDateTo
            if (filter.createdDateFrom != null && filter.createdDateTo != null
                && filter.createdDateFrom > filter.createdDateTo) {
                // swap
                var tmp = filter.createdDateFrom;
                filter.createdDateFrom = filter.createdDateTo;
                filter.createdDateTo = tmp;
            }
            return $http.post('/api/kronos/device/'+deviceId+'/events', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                deviceActionTypeIds: filter.deviceActionTypeIds,
                statuses: filter.statuses,
                createdDateFrom: filter.createdDateFrom ? Math.round(filter.createdDateFrom.getTime()/1000) : null,
                createdDateTo: filter.createdDateTo ? Math.round(filter.createdDateTo.getTime()/1000) : null
            });
        }

        function closeEvents(deviceId, eventIds) {
            return $http.post('/api/kronos/device/'+deviceId+'/close-events', eventIds);
        }

        function getTelemetries(deviceId, telemetryName, from, to) {
            // create abort promise and expose function resolving it
            var abortFunc;
            var abortPromise = $q(function(resolve) {
                abortFunc = resolve;
            });
            // get XHR promise
            var result = $http.get('/api/kronos/device/'+deviceId+'/telemetry/'+telemetryName, {
                params: {
                    from: from.getTime(),
                    to: to.getTime()
                },
                // setting timeout to promise will abort the XHR when the promise has been resolved
                timeout: abortPromise
            });
            // modify the XHR promise with the function aborting the XHR
            result.abort = abortFunc;
            return result;
        }

        function exportTelemetryUrl(deviceId) {
            return '/api/kronos/device/'+deviceId+'/export';
        }

        function enableDevice(deviceId) {
            return $http.post('/api/kronos/device/'+deviceId+'/enable');
        }

        function disableDevice(deviceId) {
            return $http.post('/api/kronos/device/'+deviceId+'/disable');
        }

        function updateDeviceLocation(deviceId, lastLocation) {
            return $http.post('/api/kronos/device/'+deviceId+'/lastLocation', lastLocation);
        }

        function startDevice(deviceId) {
            return $http.post('/api/kronos/device/'+deviceId+'/start');
        }

        function stopDevice(deviceId) {
            return $http.post('/api/kronos/device/'+deviceId+'/stop');
        }

        function sendCommand(deviceId, commandModel) {
            return $http.post('/api/kronos/device/'+deviceId+'/command', commandModel);
        }

        function findLogs(deviceId, page, filter) {
            return $http.post('/api/kronos/device/'+deviceId+'/logs', {
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
                types: filter.types
            });
        }

        function getLogOptions() {
            return $http.get('/api/kronos/device/log/options');
        }

        function getAuditLog(auditLogId) {
            return $http.get('/api/kronos/device/log/'+auditLogId);
        }

        function getDevicePri(deviceId) {
            return $http.get('/api/kronos/device/'+deviceId+'/pri');
        }

        function findSoftwareReleaseTrans(deviceId, page, filter) {
            return $http.post('/api/kronos/device/' + deviceId + '/softwareReleaseTrans', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
            });
        }

        function getDeviceStateTransFilterOptions() {
            return $http.get('/api/kronos/device/deviceStateTransFilterOptions');
        }

        function findDeviceStateTrans(deviceId, page, filter) {
            return $http.post('/api/kronos/device/' + deviceId + '/deviceStateTrans', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                types: filter.types,
                statuses: filter.statuses,
                createdDateFrom: filter.createdDateFrom ? Math.round(filter.createdDateFrom.getTime()/1000) : null,
                createdDateTo: filter.createdDateTo ? Math.round(filter.createdDateTo.getTime()/1000) : null,
                updatedDateFrom: filter.updatedDateFrom ? Math.round(filter.updatedDateFrom.getTime()/1000) : null,
                updatedDateTo: filter.updatedDateTo ? Math.round(filter.updatedDateTo.getTime()/1000) : null
            });
        }

        function getDeviceStateTrans(deviceId, deviceStateTransId) {
            return $http.get('/api/kronos/device/'+deviceId+'/deviceStateTrans/' + deviceStateTransId);
        }

        function getDeviceState(deviceId) {
            return $http.get('/api/kronos/device/'+deviceId+'/state');
        }

        function updateDeviceState(deviceId, deviceState) {
            return $http.post('/api/kronos/device/'+deviceId+'/state', deviceState);
        }

        function getBulkUpdateFields() {
            return $http.get('/api/kronos/device/bulkEditOptions');
        }

        function bulkUpdate(devicesIds, updateOptions) {
            return $http.post('/api/kronos/device/bulkEdit', { ids: devicesIds, editModel: updateOptions });
        }

        function removeStates(deviceId, states, removeStateDefinition) {
            return $http.put(
                '/api/kronos/device/' + deviceId + '/states/bulkDelete',
                { states: states, removeStateDefinition: removeStateDefinition }
            );
        }

        function removeTelemetry(deviceId, telemetryIds, removeTelemetryDefinition) {
            return $http
                .put(
                    '/api/kronos/device/' + deviceId + '/telemetry/bulkDelete',
                    { lastTelemetryIds: telemetryIds, removeTelemetryDefinition: removeTelemetryDefinition }
                );
        }

        return {
            getSearchCriteria: getSearchCriteria,
            find: find,
            newDevice: newDevice,
            create: create,
            getDeviceDetails: getDeviceDetails,
            getDeviceStats: getDeviceStats,
            getDeviceLastTelemetry: getDeviceLastTelemetry,
            getEditOptions: getEditOptions,
            saveDeviceSettings: saveDeviceSettings,
            deleteDevice: deleteDevice,
            deleteDevices: deleteDevices,
            saveDeviceTags: saveDeviceTags,
            createDeviceTag: createDeviceTag,
            saveDeviceAction: saveDeviceAction,
            deleteDeviceActions: deleteDeviceActions,
            findEvents: findEvents,
            closeEvents: closeEvents,
            getTelemetries: getTelemetries,
            exportTelemetryUrl: exportTelemetryUrl,
            enableDevice: enableDevice,
            disableDevice: disableDevice,
            startDevice: startDevice,
            stopDevice: stopDevice,
            sendCommand: sendCommand,
            findLogs: findLogs,
            getLogOptions: getLogOptions,
            getAuditLog: getAuditLog,
            updateDeviceLocation: updateDeviceLocation,
            getDevicePri: getDevicePri,
            findSoftwareReleaseTrans: findSoftwareReleaseTrans,
            getDeviceStateTransFilterOptions: getDeviceStateTransFilterOptions,
            findDeviceStateTrans: findDeviceStateTrans,
            getDeviceStateTrans: getDeviceStateTrans,
            getDeviceState: getDeviceState,
            updateDeviceState: updateDeviceState,
            getBulkUpdateFields: getBulkUpdateFields,
            bulkUpdate: bulkUpdate,
            removeStates: removeStates,
            removeTelemetry: removeTelemetry
        };
    }
]);
