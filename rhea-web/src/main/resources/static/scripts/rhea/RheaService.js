services.factory('RheaService', ['$http',
    function($http) {

        function getVersion() {
            return $http.get('/api/rhea/webapp/version');
        }

        return {
            getVersion: getVersion
        };
    }
]);