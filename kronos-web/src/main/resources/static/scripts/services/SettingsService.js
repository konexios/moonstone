services.factory('SettingsService',
    [ '$http',
    function($http) {

        function getSettings() {
            return $http.get('/api/kronos/settings');
        }

        function saveConfigurations(configurations) {
            return $http.put('/api/kronos/settings/configurations', configurations);
        }

        function provisionApplication() {
            return $http.post('/api/kronos/settings/provision');
        }

        function getKronosApplication() {
            return $http.get('/api/kronos/settings/kronosapplication');
        }

        function updateKronosApplication(model) {
            return $http.put('/api/kronos/settings/kronosapplication', model);
        }

        return {
            getSettings: getSettings,
            saveConfigurations: saveConfigurations,
            provisionApplication: provisionApplication,
            getKronosApplication: getKronosApplication,
            updateKronosApplication: updateKronosApplication
        };
    }
]);
