services.factory("SpinnerService", ['$rootScope', function($rootScope){

    $rootScope.showSpinner = 0; // reset spinner counter

    return {
        show: function() {
            $rootScope.showSpinner++;
        },
        hide: function() {
            $rootScope.showSpinner--;
        }
    };
}]);
