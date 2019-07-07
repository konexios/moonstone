services.factory('ApolloBoardRuntimeService',
    [ '$http', '$q', '$rootScope',
    function ($http, $q, $rootScope) {

        function getWidgetTypeCounts() {
        	return $http.get('/api/apollo/runtime/boards/board/widgettypes/count');
        }
        
        function getWidgetTypes(category) {
        	return $http.get('/api/apollo/runtime/boards/board/' + category + '/widgettypes');
        }
        
        function getWidgetTypeSizes(widgetTypeId) {
        	return $http.get('/api/apollo/runtime/boards/board/widgettypes/' + widgetTypeId + '/sizes');
        }
        
        return {
        	getWidgetTypeCounts: getWidgetTypeCounts,
        	getWidgetTypes: getWidgetTypes,
        	getWidgetTypeSizes: getWidgetTypeSizes
        };
    }
]);
