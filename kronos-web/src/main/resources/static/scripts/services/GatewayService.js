/* global services */

services.factory('GatewayService',
    [ '$http', '$q',
    function($http, $q) {
        function find(page, filter, bulkEdit) {
            return $http.post('/api/kronos/gateway/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                uid: filter.uid,
		hid: filter.hid,
                deviceTypeIds: filter.deviceTypeIds,
                gatewayTypes: filter.gatewayTypes,
                osNames: filter.osNames,
                softwareNames: filter.softwareNames,
                softwareVersions: filter.softwareVersions,
                userIds: filter.userIds,
                nodeIds: filter.nodeIds,
                enabled: filter.enabled
            }, {
                params: {
                    bulkEdit: bulkEdit
                }
            });
        }

        function newGateway() {
            return $http.get('/api/kronos/gateway/new');
        }

        function create(gateway) {
            return $http.post("/api/kronos/gateway/create", gateway);
        }

        function getGatewayDetails(gatewayId) {
            return $http.get('/api/kronos/gateway/'+gatewayId);
        }

        function getGatewayDetailsOptions(deviceTypeId) {
            return $http.get('/api/kronos/gateway/options', { params: { deviceTypeId: deviceTypeId }});
        }

        function updateGatewayLocation(gatewayId, lastLocation) {
            return $http.post('/api/kronos/gateway/'+gatewayId+'/lastLocation', lastLocation);
        }

        function enableGateway(gatewayId) {
            return $http.post('/api/kronos/gateway/'+gatewayId+'/enable');
        }

        function disableGateway(gatewayId) {
            return $http.post('/api/kronos/gateway/'+gatewayId+'/disable');
        }

        function saveGatewaySettings(gateway) {
            return $http.put('/api/kronos/gateway/'+gateway.id, {
                id: gateway.id,
                hid: gateway.hid,
                name: gateway.name,
                deviceTypeId: gateway.deviceTypeId,
                user: gateway.user,
                node: gateway.node,
                softwareRelease: gateway.softwareRelease,
                assignOwnerToDevices: gateway.assignOwnerToDevices,
                properties: gateway.properties
            });
        }

        function deleteGateway(gatewayId) {
            return $http.delete('/api/kronos/gateway/'+gatewayId);
        }

        function deleteGateways(gatewayIds) {
            return $http.post('/api/kronos/gateway/bulkDelete', gatewayIds);
        }

        function findGatewaysByUid(uid) {
            return $http.get('/api/kronos/gateway/uid/'+encodeURIComponent(uid));
        }

        function moveGateway(gatewayId, useExistingTypes) {
            var params = {};
            if (useExistingTypes != null) {
                params.useExisting = useExistingTypes;
            }
            return $http.post('/api/kronos/gateway/move/'+gatewayId, null, {
                params: params
            });
        }

        function findLogs(gatewayId, page, filter) {
            return $http.post('/api/kronos/gateway/'+gatewayId+'/logs', {
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
            return $http.get('/api/kronos/gateway/log/options');
        }

        function getAuditLog(auditLogId) {
            return $http.get('/api/kronos/gateway/log/'+auditLogId);
        }

        function getSearchCriteria() {
            return $http.get('/api/kronos/gateway/filter');
        }

        function getLastHeartbeat(gatewayId) {
            // create abort promise and expose function resolving it
            var abortFunc;
            var abortPromise = $q(function(resolve) {
                abortFunc = resolve;
            });
            // get XHR promise
            var result = $http.get('/api/kronos/gateway/'+gatewayId+'/lastheartbeat', {
                // setting timeout to promise will abort the XHR when the promise has been resolved
                timeout: abortPromise
            });
            // modify the XHR promise with the function aborting the XHR
            result.abort = abortFunc;
            return result;
        }

        function getGatewayPri(gatewayId) {
            return $http.get('/api/kronos/gateway/'+gatewayId+'/pri');
        }

        function findSoftwareReleaseTrans(gatewayId, page, filter) {
            return $http.post('/api/kronos/gateway/' + gatewayId + '/softwareReleaseTrans', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
            });
        }

        function getBulkUpdateFields() {
            return $http.get('/api/kronos/gateway/bulkEditOptions');
        }

        function bulkUpdate(gatewayIds, updateOptions) {
            return $http.post('/api/kronos/gateway/bulkEdit', { ids: gatewayIds, editModel: updateOptions });
        }

        return {
            find: find,
            newGateway: newGateway,
            create: create,
            getGatewayDetails: getGatewayDetails,
            getGatewayDetailsOptions: getGatewayDetailsOptions,
            enableGateway: enableGateway,
            disableGateway: disableGateway,
            saveGatewaySettings: saveGatewaySettings,
            deleteGateway: deleteGateway,
            deleteGateways: deleteGateways,
            findGatewaysByUid: findGatewaysByUid,
            moveGateway: moveGateway,
            findLogs: findLogs,
            getLogOptions: getLogOptions,
            getAuditLog: getAuditLog,
            getSearchCriteria: getSearchCriteria,
            getLastHeartbeat: getLastHeartbeat,
            updateGatewayLocation: updateGatewayLocation,
            getGatewayPri: getGatewayPri,
            findSoftwareReleaseTrans: findSoftwareReleaseTrans,
            getBulkUpdateFields: getBulkUpdateFields,
            bulkUpdate: bulkUpdate
        };
    }
]);
