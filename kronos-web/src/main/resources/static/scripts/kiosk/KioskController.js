controllers.controller('KioskController',
    [ '$rootScope', '$scope', '$location', 'ErrorService', 'KioskService',
    function($rootScope, $scope, $location, ErrorService, KioskService) {

    	$rootScope.currentDate = new Date();

        $scope.isActivePath = function() {
            var path = $location.path();
            for(var i=0; i<arguments.length; i++) {
                if(path.indexOf(arguments[i]) == 0) return 'active';
            }
            return '';
        };
    }
]);
