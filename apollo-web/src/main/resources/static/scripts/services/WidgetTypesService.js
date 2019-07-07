services.factory('WidgetTypesService',
    [ '$http', '$q', '$rootScope',
    function ($http, $q, $rootScope) {

        function getAll() {
            return $http.get('/api/apollo/settings/widgettypes/all', {});
        }
        
        return {
        	getAll: getAll
        };
    }
]);
