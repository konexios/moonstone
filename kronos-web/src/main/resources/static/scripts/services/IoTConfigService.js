services.factory('IoTConfigService',
    [ '$http',
    function($http) {

        function getIoTConfiguration(userIoTConfig) {
            return $http.get('/api/kronos/iot/'+(userIoTConfig ? 'user-' : '')+'configuration');
        }

        function saveIoTConfiguration(userIoTConfig, config) {
            return $http.put('/api/kronos/iot/'+(userIoTConfig ? 'user-' : '')+'configuration', config);
        }

        function testAWSConfiguration(awsConfig) {
            return $http.post('/api/kronos/iot/aws/connect', awsConfig);
        }

        function testIBMConfiguration(ibmConfig) {
            return $http.post('/api/kronos/iot/ibm/connect', ibmConfig);
        }

        function testAzureConfiguration(azureConfig) {
            return $http.post('/api/kronos/iot/azure/connect', azureConfig);
        }

        return {
            getIoTConfiguration: getIoTConfiguration,
            testAWSConfiguration: testAWSConfiguration,
            testIBMConfiguration: testIBMConfiguration,
            testAzureConfiguration: testAzureConfiguration,
            saveIoTConfiguration: saveIoTConfiguration
        };
    }
]);
