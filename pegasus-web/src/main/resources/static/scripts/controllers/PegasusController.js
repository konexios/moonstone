controllers.controller("PegasusController", ["$rootScope", "$scope", "$location", "SecurityService", "AuthenticationService", "WebAppService", "ErrorService",
    function($rootScope, $scope, $location, SecurityService, AuthenticationService, WebAppService, ErrorService) {
	    $scope.$on("$viewContentLoaded", function() {
	    });

        $scope.isShowSpinner = false;
        $rootScope.$on('loading:progress', function (){
            $scope.isShowSpinner = true;
        });
        $rootScope.$on('loading:finish', function (){
            $scope.isShowSpinner = false;
        });

		$rootScope.model = {
			navbar: {
				itemInMenu: null
			},
			footer: {
				fullYear: new Date().getFullYear(),
			}
		};
	    
		$scope.parseItemInMenu = function() {
	
			var result = "";
			var path = $location.path();
			var startIndex = path.lastIndexOf("/");
			var endIndex = path.indexOf("?");
	
			if (endIndex == -1)
				endIndex = path.length;
	
			if (startIndex != -1)
				result = path.substring(startIndex + 1, endIndex);
	
			return result;
		};

        $scope.isActivePath = function() {
            var path = $location.path();
            for(var i=0; i<arguments.length; i++) {
                if(path.indexOf(arguments[i]) == 0) return 'active';
            }
            return '';
        };		
		
		$scope.isInPath = function(path) {
			
			var fullPath = "/" + path;
			
			var result = $location.path() == fullPath;
			if (!result) {
				result = ($location.path().indexOf(fullPath) != -1)
			}		 			
				return result;
		};
		
		$scope.isActive = function(path) {
	
			var result = $scope.isInPath(path);
			if (result)
				$rootScope.model.navbar.itemInMenu = $scope.parseItemInMenu();
	
			return (result ? "active" : "");
		};
		
		$scope.isSuperAdministrator = function() {
			return SecurityService.isSuperAdministrator();
		};
		
		$scope.isAdministrator = function() {
			return SecurityService.isAdministrator();
		};
		
		$scope.isLoggedIn = function() {
			return SecurityService.isLoggedIn();
		};

		$scope.canReadProduct = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_PRODUCT");
		};
		
		$scope.canCreateProduct = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_PRODUCT");
		};
		
		$scope.canUpdateProduct = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_PRODUCT");
		};
		
		$scope.canReadCompany = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_COMPANY");
		};
		
		$scope.canCreateCompany = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_COMPANY");
		};
		
		$scope.canUpdateCompany = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_COMPANY");
		};
		
		$scope.canReadCompanyApplications = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_COMPANY_APPLICATIONS");
		};
		
		$scope.canReadCompanyBilling = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_COMPANY_BILLING");
		};
		
		$scope.canReadCompanyHierarchy = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_COMPANY_HIERARCHY");
		};
		
		$scope.canReadCompanyAuthentication = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_COMPANY_AUTHENTICATION");
		};
		
		$scope.canUpdateCompanyPasswordPolicy = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_COMPANY_PASSWORD_POLICY");
		};
		
		$scope.canUpdateCompanyLoginPolicy = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_COMPANY_LOGIN_POLICY");
		};
		
		$scope.canCreateCompanyAuth = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_COMPANY_AUTH");
		};
		
		$scope.canUpdateCompanyAuth = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_COMPANY_AUTH");
		};
		
		$scope.canReadCompanySubscriptions = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_COMPANY_SUBSCRIPTIONS");
		};
		
		$scope.canReadSubscription = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_SUBSCRIPTION");
		};
		
		$scope.canCreateSubscription = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_SUBSCRIPTION");
		};
		
		$scope.canUpdateSubscription = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_SUBSCRIPTION");
		};

        $scope.canReadSubscriptionApplications = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_READ_SUBSCRIPTION_APPLICATIONS");
        };
		
		$scope.canReadApplication = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_APPLICATION");
		};
		
		$scope.canCreateApplication = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_APPLICATION");
		};
		
		$scope.canUpdateApplication = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_APPLICATION");
		};
		
		$scope.canReadRegion = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_REGION");
		};
		
		$scope.canCreateRegion = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_REGION");
		};
		
		$scope.canUpdateRegion = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_REGION");
		};
		
		$scope.canReadZone = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_ZONE");
		};
		
		$scope.canCreateZone = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_ZONE");
		};
		
		$scope.canUpdateZone = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_ZONE");
		};
		
		$scope.canReadUser = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_USER");
		};

        $scope.canReadUserProfile = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_READ_USER_PROFILE");
        };

        $scope.canReadUserAccount = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_READ_USER_ACCOUNT");
        };
		
		$scope.canCreateUser = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_USER");
		};
		
		$scope.canUpdateUser = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_USER");
		};

        $scope.canUpdateUserProfile = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_UPDATE_USER_PROFILE");
        };

        $scope.canUpdateUserAccount = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_UPDATE_USER_ACCOUNT");
        };
		
		$scope.canResetUserPassword = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_RESET_USER_PASSWORD");
		};

		$scope.canChangeUserLogin = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CHANGE_USER_LOGIN");
		};

        $scope.canDisableUserAccount = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_DISABLE_USER_ACCOUNT");
        };
        
        $scope.canUpdateUserRoles = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_UPDATE_USER_ROLES");
        };
        
        $scope.canReadUserAuthentication = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_READ_USER_AUTHENTICATION");
        };
        
        $scope.canCreateUserAuthentication = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_CREATE_USER_AUTHENTICATION");
        };

        $scope.canUpdateUserAuthentication = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_UPDATE_USER_AUTHENTICATION");
        };
        
		$scope.canReadRole = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_ROLE");
		};
		
		$scope.canCreateRole = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_ROLE");
		};
		
		$scope.canUpdateRole = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_ROLE");
		};
		
		$scope.canReadPrivilege = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_READ_PRIVILEGE");
		};
		
		$scope.canCreatePrivilege = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_PRIVILEGE");
		};
		
		$scope.canUpdatePrivilege = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_UPDATE_PRIVILEGE");
		};
		
		$scope.canManageCache = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_MANAGE_CACHE");
		};
		
		$scope.canCreateApplicationVaultLogin = function()
		{
			return SecurityService.hasPrivilege("PEGASUS_CREATE_APPLICATION_VAULT_LOGIN");
		};

        $scope.canReadCompanyAccessKey = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_READ_COMPANY_ACCESS_KEYS");
        };

        $scope.canCreateCompanyAccessKey = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_CREATE_COMPANY_ACCESS_KEY");
        };

        $scope.canUpdateCompanyAccessKey = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_UPDATE_COMPANY_ACCESS_KEY");
        };

        $scope.canReadApplicationAccessKey = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_READ_APPLICATION_ACCESS_KEYS");
        };

        $scope.canCreateApplicationAccessKey = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_CREATE_APPLICATION_ACCESS_KEY");
        };

        $scope.canUpdateApplicationAccessKey = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_UPDATE_APPLICATION_ACCESS_KEY");
        };

        $scope.canReadApplicationConfiguration = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_READ_APPLICATION_CONFIGURATIONS");
        };

        $scope.canCreateApplicationConfiguration = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_CREATE_APPLICATION_CONFIGURATION");
        };

        $scope.canUpdateApplicationConfiguration = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_UPDATE_APPLICATION_CONFIGURATION");
        };
        
        $scope.canReadSubscriptionAccessKey = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_READ_SUBSCRIPTION_ACCESS_KEYS");
        };

        $scope.canCreateSubscriptionAccessKey = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_CREATE_SUBSCRIPTION_ACCESS_KEY");
        };

        $scope.canUpdateSubscriptionAccessKey = function()
        {
            return SecurityService.hasPrivilege("PEGASUS_UPDATE_SUBSCRIPTION_ACCESS_KEY");
        };
	
		$scope.signout = function() {
	
			var promise = AuthenticationService.logout();
	 		promise.then(function(response){
	 			var path = "/" + $rootScope.app.hid + "?signout=true";
	 			$rootScope.app = null;
				$rootScope.user = null;
				SecurityService.deleteSession();
				//$rootScope.clearSearchSMFilters();
				$rootScope.$resetScope();
				$location.path(path);
	 		}, function(response){
	 			$rootScope.app = null;
				$rootScope.user = null;
				SecurityService.deleteSession();
				//$rootScope.clearSearchSMFilters();
				$rootScope.$resetScope();
				ErrorService.handleHttpError(response);
	 		});
		};
		
		$rootScope.$resetScope = function() {
			for (var prop in $rootScope) {
			    if (prop.substring(0,1) !== '$') {
			        delete $rootScope[prop];
			    }
			}
		}

//		$rootScope.clearSearchSMFilters = function() {
//			// $rootScope.$destroy();
//			if($rootScope.applicationSearchSM) { $rootScope.applicationSearchSM = null; }
//			if($rootScope.accessKeySearchSM) { $rootScope.accessKeySearchSM = null; }
//			if($rootScope.companySearchSM) { $rootScope.companySearchSM = null; }
//			if($rootScope.privilegeSearchSM) { $rootScope.privilegeSearchSM = null; }
//			if($rootScope.productSearchSM) { $rootScope.productSearchSM = null; }
//			if($rootScope.regionsSearchSM) { $rootScope.regionsSearchSM = null; }
//			if($rootScope.roleSearchSM) { $rootScope.roleSearchSM = null; }
//			if($rootScope.subscriptionSearchSM) { $rootScope.subscriptionSearchSM = null; }
//			if($rootScope.userSearchSM) { $rootScope.userSearchSM = null; }
//			if($rootScope.zoneSearchSM) { $rootScope.zoneSearchSM = null; }
//		};
	}
]);

/***
 * Layout Partials
 * 
 * By default the partials are loaded through AngularJS ng-include directive. In case they loaded in server side(e.g: PHP include function) then below partial 
 * initialization can be disabled and Layout.init() should be called on page load complete as explained above.
 */
controllers.controller("HeaderController", ["$scope", function($scope) {
    $scope.$on("$includeContentLoaded", function() {
    	
    });
}]);

controllers.controller("FooterController", ["$rootScope", "$scope", '$uibModal', "WebAppService", function($rootScope, $scope, $uibModal, WebAppService) {
    $scope.$on("$includeContentLoaded", function() {
    	WebAppService.version().then(function(response){
    		$rootScope.model.version = response.data;
    	});
    });
    
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
}]);

controllers.controller("ComponentsVersionController", ["$rootScope", "$scope", "$uibModalInstance", "WebAppService", function($rootScope, $scope, $uibModalInstance, WebAppService) {
	
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
		WebAppService.componentsVersion()
		.then(function(response){
			$scope.componentsVersion = response.data;
		});		
	}

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
	
	$scope.load();
}]);