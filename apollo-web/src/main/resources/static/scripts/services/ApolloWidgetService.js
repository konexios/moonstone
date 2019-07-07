services.factory('ApolloWidgetService',
    [ '$http', '$q', '$rootScope',
    function ($http, $q, $rootScope) {

    	function getWidget(apolloWidgetId) {
            return $http.get('/api/apollo/settings/widgets/' +apolloWidgetId+ '/widget', {});    		
    	}
    	
        function getAll() {
            return $http.get('/api/apollo/settings/widgets/all', {});
        }
        
        function create(widget) {
        	return $http.post('/api/apollo/settings/widgets/widget/create', widget);
        }
        
        function update(widget) {
        	return $http.put('/api/apollo/settings/widgets/'+widget.id+'/widget/update', widget);
        }
        
        return {
        	getAll: getAll,
        	getWidget: getWidget,
        	create: create,
        	update: update
        };
    }
]);
