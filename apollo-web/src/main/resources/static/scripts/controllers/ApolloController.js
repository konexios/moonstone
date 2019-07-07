controllers.controller('ApolloController', 
    [ '$rootScope', '$scope', '$location', '$uibModal', 'SecurityService', 'AuthenticationService', 'ErrorService', 'ApolloService','WidgetsManagerService',
    function ($rootScope, $scope, $location, $uibModal, SecurityService, AuthenticationService, ErrorService, ApolloService, WidgetsManagerService) {

    	$rootScope.currentDate = new Date();
    	$rootScope.apolloVersion = null;
    	
        $rootScope.model = {
            navbar: {
                menu: 'signin',
                item: ''
            }
        };

        init();

        function init (){
            ApolloService.version().then( function (response){
                $rootScope.apolloVersion = response.data;
            });
        }
        
        $scope.isLoggedIn = function () {
        	return SecurityService.isLoggedIn();
        }
        
        $scope.isActivePath = function () {
            var path = $location.path();
            for (var i=0; i<arguments.length; i++) {
                if(path.indexOf(arguments[i]) === 0) return 'active';
            }
            return '';
        }
        
        $scope.isActiveNavbarMenu = function (menu) {
            return ($rootScope.model.navbar.menu === menu ? 'active' : '');
        }

        $scope.isActiveNavbarItem = function (item) {
            return ($rootScope.model.navbar.item === item ? 'active' : '');
        }
        
        function clearUserSession () {
            $rootScope.principal = null;
            SecurityService.deleteSession();
        }
        
        $scope.logout = function () {
            AuthenticationService.logout()
            	.then(function (response){
            		$location.url('/signin');
            	}, function (response) {
            		ErrorService.handleHttpError(response);
            	})
	          	.finally(function () {
	          		clearUserSession();
	          		WidgetsManagerService.state='off';
	          	});
        }
        
        $scope.isSigninPage = function () {
            if($location.path().search(/^\/signin/) == -1)   
              return true;
            else
              return false;
        }
        
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
        
        $scope.hasApolloAccess = function() {
            return SecurityService.hasApolloAccess();
        };
        
        $scope.canCreateBoard = function() {
            return SecurityService.canCreateBoard();
        };
        
        $scope.canUpdateBoard = function() {
            return SecurityService.canUpdateBoard();
        };
        
        $scope.canReadBoard = function() {
            return SecurityService.canReadBoard();
        };
        
        $scope.canDeleteBoard = function() {
            return SecurityService.canDeleteBoard();
        };
        
        $scope.canCopyBoard = function() {
            return SecurityService.canCopyBoard();
        };
        
        $scope.canCreateSystemDefaultBoard = function() {
            return SecurityService.canCreateSystemDefaultBoard();
        };
        
        $scope.canUpdateSystemDefaultBoard = function() {
            return SecurityService.canUpdateSystemDefaultBoard();
        };

        $scope.canDeleteSystemDefaultBoard = function() {
            return SecurityService.canDeleteSystemDefaultBoard();
        };
        
        $scope.canCreateArrowCertifiedBoard = function() {
            return SecurityService.canCreateArrowCertifiedBoard();
        };
        
        $scope.canUpdateArrowCertifiedBoard = function() {
            return SecurityService.canUpdateArrowCertifiedBoard();
        };

        $scope.canDeleteArrowCertifiedBoard = function() {
            return SecurityService.canDeleteArrowCertifiedBoard();
        };        
        
        $scope.canMarkAsUserDefaultBoard = function() {
            return SecurityService.canMarkAsUserDefaultBoard();
        };
        
        $scope.canMarkAsFavoriteBoard = function() {
            return SecurityService.canMarkAsFavoriteBoard();
        };
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

controllers.controller("FooterController", ["$rootScope", "$scope", "ApolloService", function($rootScope, $scope, ApolloService) {
    $scope.$on("$includeContentLoaded", function() {
    	ApolloService.version().then(function(response){
    		$rootScope.model.version = response.data;
    	});
    });
}]);

controllers.controller("ComponentsVersionController", ["$rootScope", "$scope", "$uibModalInstance", "ApolloService", function($rootScope, $scope, $uibModalInstance, ApolloService, WebAppService) {
	
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
		ApolloService.componentsVersion()
		.then(function(response){
			$scope.componentsVersion = response.data;
		});		
	}

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
	
	$scope.load();
}]);