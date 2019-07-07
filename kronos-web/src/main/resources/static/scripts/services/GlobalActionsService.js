services.factory('GlobalActionsService', ['$http', function($http) {

    function getGlobalActionTypes(page) {
        return $http.post('/api/kronos/globalactiontype/find', {
            // pagination
            pageIndex: page.pageIndex,
            itemsPerPage: page.itemsPerPage,
            // sorting
            sortDirection: page.sort.direction,
            sortField: page.sort.property
        });
    }

    function saveGlobalActionType(actionType) {
        if (actionType.id) {
            return $http.put('/api/kronos/globalactiontype/' + actionType.id, actionType);
        } else {
            return $http.post('/api/kronos/globalactiontype', actionType);
        }
    }

    function getGlobalActions(page) {
        return $http.post('/api/kronos/globalaction/find', {
            // pagination
            pageIndex: page.pageIndex,
            itemsPerPage: page.itemsPerPage,
            // sorting
            sortDirection: page.sort.direction,
            sortField: page.sort.property
        });
    }

    function getGlobalActionTypesList() {
        return $http.get('/api/kronos/globalactiontype/find');
    }


    function saveGlobalAction(globalAction) {
        if (globalAction.id) {
            return $http.put('/api/kronos/globalaction/' + globalAction.id, globalAction);
        } else {
            return $http.post('/api/kronos/globalaction', globalAction);
        }
    }

    return {
        getGlobalActionTypes: getGlobalActionTypes,
        saveGlobalActionType: saveGlobalActionType,
        getGlobalActions: getGlobalActions,
        getGlobalActionTypesList: getGlobalActionTypesList,
        saveGlobalAction: saveGlobalAction
    };
}]);