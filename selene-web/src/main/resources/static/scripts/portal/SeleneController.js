controllers.controller('SeleneController', 
    [ '$rootScope', '$scope', '$location', 'SecurityService', 'AuthenticationService', 'ErrorService', 
    function($rootScope, $scope, $location, SecurityService, AuthenticationService, ErrorService) {

    	$rootScope.currentDate = new Date();
    	
      $rootScope.model = {
          navbar: {
              menu: 'signin',
              item: ''
          }
      };

      $scope.isLoggedIn = function() {
          return $rootScope.principal && SecurityService.isLoggedIn();
      };

      $scope.isActivePath = function(path) {
          for (var i = 0; i < arguments.length; i++) {
              if (arguments[i] === $location.path()) {
                  return 'active';
              }
          }
          return '';
      };

      $scope.isActiveNavbarMenu = function(menu) {
          return ($rootScope.model.navbar.menu == menu ? 'active' : '');
      };

      $scope.isActiveNavbarItem = function(item) {
          return ($rootScope.model.navbar.item == item ? 'active' : '');
      };

      function clearUserSession() {
          $rootScope.principal = null;
          SecurityService.deleteSession();
      }

      $scope.logout = function() {
          AuthenticationService.logout()
            .then(function(response){
              $location.url('/signin');
            }, function(response){
              ErrorService.handleHttpError(response);
            })
            .finally(function() {
              clearUserSession();
            });
      };

      if (SecurityService.getGatewayMe() !== null) {
        $rootScope.principal = {
          username: SecurityService.getGatewayMe()
        }
      }

    }
]);