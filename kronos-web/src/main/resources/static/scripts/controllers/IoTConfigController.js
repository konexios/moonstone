function getIoTConfigController(userIoTConfig) {
    return function($scope, IoTConfigService, ErrorService, SpinnerService, ToastrService) {
        $scope.userIoTConfig = userIoTConfig;
        $scope.config = {};
        $scope.awsTest = false;
        $scope.ibmTest = false;
        $scope.azureTest = false;

        $scope.testConnect = function(iotConfigForm) {
            if (iotConfigForm.$invalid) {
                ToastrService.popupError('IoT Configuration cannot be tested because of invalid fields, please check errors.');
            } else {
                if ($scope.config.iotProvider == 'AWS') {
                    SpinnerService.wrap(IoTConfigService.testAWSConfiguration, $scope.config.aws)
                    .then(function(response) {
                        $scope.awsTest = response.data;
                        if ($scope.awsTest) {
                            ToastrService.popupSuccess('Successfully connected to AWS!');
                        } else {
                            ToastrService.popupError('Failed to connect to AWS, please check your settings and try again.');
                        }
                    })
                    .catch(ErrorService.handleHttpError);
                } else if ($scope.config.iotProvider == 'IBM') {
                    SpinnerService.wrap(IoTConfigService.testIBMConfiguration, $scope.config.ibm)
                    .then(function(response) {
                        $scope.ibmTest = response.data;
                        if ($scope.ibmTest) {
                            ToastrService.popupSuccess('Successfully connected to IBM!');
                        } else {
                            ToastrService.popupError('Failed to connect to IBM, please check your settings and try again.');
                        }
                    })
                    .catch(ErrorService.handleHttpError);
                } else if ($scope.config.iotProvider == 'AZURE') {
                    SpinnerService.wrap(IoTConfigService.testAzureConfiguration, $scope.config.azure)
                    .then(function(response) {
                        $scope.azureTest = response.data;
                        if ($scope.azureTest) {
                            ToastrService.popupSuccess('Successfully connected to Azure!');
                        } else {
                            ToastrService.popupError('Failed to connect to Azure, please check your settings and try again.');
                        }
                    })
                    .catch(ErrorService.handleHttpError);
                }
            }
        }

        $scope.save = function(iotConfigForm) {
            if (iotConfigForm.$invalid) {
                ToastrService.popupError('IoT Configuration cannot be saved because of invalid fields, please check errors.');
            } else {
                SpinnerService.wrap(IoTConfigService.saveIoTConfiguration, userIoTConfig, $scope.config)
                .then(function(response) {
                    $scope.config = response.data;
                    $scope.awsTest = false;
                    $scope.ibmTest = false;
                    iotConfigForm.$setPristine();
                    ToastrService.popupSuccess('IoT Configuration has been saved successfully');
                })
                .catch(ErrorService.handleHttpError);
            }
        };

        SpinnerService.wrap(IoTConfigService.getIoTConfiguration, userIoTConfig)
        .then(function(response) {
            $scope.config = response.data;
        })
        .catch(ErrorService.handleHttpError);
    }
}

controllers.controller('IoTConfigController', ['$scope', 'IoTConfigService', 'ErrorService', 'SpinnerService', 'ToastrService', getIoTConfigController(false)]);
