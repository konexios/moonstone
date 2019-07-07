controllers.controller('RheaController',
    [ '$rootScope', '$scope', '$location', 'SecurityService', 'AuthenticationService', 'ErrorService', 'RheaService',
    function($rootScope, $scope, $location, SecurityService, AuthenticationService, ErrorService, RheaService) {

    	$rootScope.logoUrl = null;
    	$rootScope.currentDate = new Date();
    	$rootScope.rheaVersion = null;

        init();

        function init(){
            RheaService.getVersion().then(function(response){
                $rootScope.rheaVersion = response.data;
            });
        }

        $scope.isLoggedIn = function() {
            return SecurityService.isLoggedIn();
        };

        $scope.isActivePath = function() {
            var path = $location.path();
            for(var i=0; i<arguments.length; i++) {
                if(path.indexOf(arguments[i]) == 0) return 'active';
            }
            return '';
        };

        // privileges
        $scope.isAdmin = function() {
            return SecurityService.isAdmin();
        };

        $scope.canReadDeviceTypes = function() {
            return SecurityService.canReadDeviceTypes();
        };

        $scope.canReadNodeTypes = function() {
            return SecurityService.canReadNodeTypes();
        };

        $scope.canReadNodes = function() {
            return SecurityService.canReadNodes();
        };

        $scope.canReadGateways = function() {
            return SecurityService.canReadGateways();
        };

        $scope.canReadAccessKeys = function() {
            return SecurityService.canReadAccessKeys();
        };

        $scope.canReadSettings = function() {
            return SecurityService.canReadSettings();
        };

        $scope.canReadDeviceActionTypes = function() {
            return SecurityService.canReadDeviceActionTypes();
        };

        $scope.canEditUserIoTConfig = function() {
            return SecurityService.canEditUserIoTConfig();
        };

        $scope.canReadManufacturers = function() {
            return SecurityService.canReadManufacturers();
        };

        $scope.canReadDeviceProducts = function() {
            return SecurityService.canReadDeviceProducts();
        };

        $scope.canReadSoftwareProducts = function() {
            return SecurityService.canReadSoftwareProducts();
        };
        
        $scope.canReadSoftwareVendors = function() {
            return SecurityService.canReadSoftwareVendors();
        };

        $scope.canReadSoftwareReleases = function() {
            return SecurityService.canReadSoftwareReleases();
        };

        $scope.canReadRightToUseRequest = function () {
            return SecurityService.canReadRightToUseRequest();
        };

        $scope.canReadSoftwareReleaseSchedule = function() {
            return SecurityService.canReadSoftwareReleaseSchedule();
        };

        $scope.canReadRTURequest = function () {
            return SecurityService.canReadRTURequest();
        };

        function clearUserSession() {
            $rootScope.app = null;
            $rootScope.user = null;
            SecurityService.deleteSession();
        }

        $scope.selectApp = function() {
            return AuthenticationService.selectUserApplication($rootScope.user, true)
            .then(function() {
                // success - redirect to default route (home)
                $location.url('/');
            }, function(response) {
                if (typeof response == 'object') {
                    // http error
                    ErrorService.handleHttpError(response);
                }// else - cancelled
            });
        };

        $scope.logout = function() {
            AuthenticationService.logout()
            .then(function(response){
                clearUserSession();
                $location.url('/signin');
            }, function(response){
                clearUserSession();
                ErrorService.handleHttpError(response);
            });
        };

    }
]);
