controllers.controller('KronosController',
    [ '$rootScope', '$scope', '$location', '$uibModal','$routeParams', 'SecurityService', 'AuthenticationService', 'ErrorService', 'KronosService','$cookies',
    function($rootScope, $scope, $location, $uibModal, $routeParams, SecurityService, AuthenticationService, ErrorService, KronosService,$cookies) {

    	$rootScope.logoUrl = null;
    	$rootScope.currentDate = new Date();
    	$rootScope.kronosVersion = null;

        init();

        function init(){
            KronosService.getVersion().then(function(response){
                $rootScope.kronosVersion = response.data;
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
        
        $scope.openComponentsVersion = function() {
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/modals/componentsVersion.html',
                controller: 'ComponentsVersionController',
                size: 'lg'
            });
            
            modalInstance.result.then(function() {
            });
        };

        // privileges
        $scope.isAdmin = function() {
            return SecurityService.isAdmin();
        };

        $scope.canReadGlobalActions = function() {
            return SecurityService.canReadGlobalActions();
        };

        $scope.canReadGlobalActionTypes = function() {
            return SecurityService.canReadGlobalActionTypes();
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

        $scope.canReadRightToUse = function () {
            return SecurityService.canReadRightToUse();
        };

        $scope.canReadSettings = function() {
            return SecurityService.canReadSettings();
        };

        $scope.canReadDeviceActionTypes = function() {
            return SecurityService.canReadDeviceActionTypes();
        };


        $scope.canEditIoTConfig = function() {            
            return SecurityService.canEditIoTConfig();
        }


        $scope.canEditUserIoTConfig = function() {
            return SecurityService.canEditUserIoTConfig();
        };

        $scope.canReadSoftwareReleaseSchedule = function() {
            return SecurityService.canReadSoftwareReleaseSchedule();
        };
        
        $scope.canCreateSoftwareReleaseSchedule = function() {
            return SecurityService.canCreateSoftwareReleaseSchedule();
        };

        $scope.canReadTestProcedures = function() {
            return SecurityService.canReadTestProcedures();
        };

        $scope.canReadTestResults = function() {
            return SecurityService.canReadTestResults();
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

        $scope.isSubscriptionExpired = function() {
        	
        	if ($rootScope.app == undefined) return false;
        	
            return $rootScope.app.subscriptionExpiration.expired != undefined ? $rootScope.app.subscriptionExpiration.expired : false;
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

controllers.controller("ComponentsVersionController", ["$rootScope", "$scope", "$uibModalInstance", "KronosService", function($rootScope, $scope, $uibModalInstance, KronosService) {
	
	// columns
	$scope.columnHeaders = [
        { label: "Name", value: "name", sortable: false },
        { label: "Description", value: "description", sortable: false },
        { label: "Version", value: "version", sortable: false },
        { label: "Compatible", value: "compatible", sortable: false },
        { label: "Vendor", value: "vendor", sortable: false },
        { label: "Date", value: "builtDate", sortable: false },
        { label: "JDK", value: "builtJdk", sortable: false },
        { label: "Who", value: "builtBy", sortable: false }
    ];
		
	$scope.componentsVersion = {};
	
	$scope.load = function() {
		KronosService.componentsVersion()
		.then(function(response){
			$scope.componentsVersion = response.data;
		});		
	}

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
	
	$scope.load();
}]);
