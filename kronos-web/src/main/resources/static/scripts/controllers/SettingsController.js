controllers.controller('SettingsController', [
    '$scope', '$timeout', '$routeParams', '$uibModal', 'SettingsService', 'SecurityService', 'ErrorService', 'SpinnerService', 'ToastrService',
    function ($scope, $timeout, $routeParams, $uibModal, SettingsService, SecurityService, ErrorService, SpinnerService, ToastrService) {
        $scope.loading = true;

        $scope.activeTab = 0;

        $scope.canSaveConfigurations = SecurityService.canSaveSettings();
        $scope.canEditIoTConfig = SecurityService.canEditIoTConfig();
        $scope.canProvisionApplication = SecurityService.canProvisionApplication();

        $scope.model = null;

        $scope.collapseMenu = function(){
            if($scope.mobileLayot){
                $scope.mobileMenuShowed=!$scope.mobileMenuShowed
            }else{
                $scope.collapsedView=!$scope.collapsedView;
            }
        };

        $scope.$on('resize', function (event, data) {
            if (data.width < 992) {
                $scope.mobileLayot = true;
           } else {
               $scope.mobileLayot = false;
               $scope.mobileMenuShowed = false;
            }
            $timeout(function () {
                $scope.$apply();
            }, 0, false);
        });
        $scope.mobileMenuShowed = false;

        $scope.saveConfigurations = function(form) {
            if (form.$valid) {
                SpinnerService.wrap(SettingsService.saveConfigurations, $scope.model.configurations)
                .then(function(response) {
                    $scope.model.configurations = response.data;
                    ToastrService.popupSuccess('Configuration has been saved successfully.');
                })
                .catch(ErrorService.handleHttpError);
            } else {
                ToastrService.popupError('Configuration cannot be saved because of invalid fields, please check errors.');
            }
        };

        $scope.canShowSaveConfigsBtn = function(configurations) {
            // show save button if have one or more editable (not readonly) configurations
            return !!configurations && configurations.length > 0 && configurations.some(function(conf){ return !conf.readonly; });
        };

        SpinnerService.wrap(SettingsService.getSettings)
        .then(function(response) {
            $scope.model = response.data;
        })
        .catch(ErrorService.handleHttpError)
        .finally(function() {
            $scope.loading = false;
        });
    }
]);
