controllers.controller('ProvisionAppController', ['$scope', 'SettingsService', 'ErrorService', 'SpinnerService', 'SecurityService', 'ToastrService',
    function($scope, SettingsService, ErrorService, SpinnerService, SecurityService, ToastrService) {

        $scope.applicationModel = {};
        $scope.isAdmin = SecurityService.isAdmin();

        init();

        function init() {
            SpinnerService.wrap(SettingsService.getKronosApplication)
                .then(function(response) {
                    $scope.applicationModel = response.data.kronosApplication;
                })
                .catch(ErrorService.handleHttpError);
        }

        $scope.saveApplicationModel = function(form) {
            if (form.$valid) {
                SpinnerService.wrap(SettingsService.updateKronosApplication, $scope.applicationModel)
                .then(function(response) {
                    $scope.applicationModel = response.data;
                    ToastrService.popupSuccess('Configuration has been saved successfully.');
                })
                .catch(ErrorService.handleHttpError);
            } else {
                ToastrService.popupError('Configuration cannot be saved because of invalid fields, please check errors.');
            }
        };

        $scope.provisionApplication = function() {
            SpinnerService.wrap(SettingsService.provisionApplication)
            .then(function(response) {
                ToastrService.popupSuccess('Application has been provisioned successfully.');
            })
            .catch(ErrorService.handleHttpError);
        };


    }
]);