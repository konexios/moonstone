services.factory("SpinnerService", ['$rootScope', function($rootScope){

    $rootScope.showSpinner = 0; // reset spinner counter

    function show() {
        $rootScope.showSpinner++;
    }

    function hide() {
        $rootScope.showSpinner--;
    }

    function wrap(service) {
        var args = Array.prototype.slice.call(arguments, 1);
        show();
        try {
            return service.apply(undefined, args)
            .finally(hide);
        } catch(e) {
            hide();
            throw e;
        }
    }

    return {
        show: show,
        hide: hide,
        wrap: wrap
    };
}]);
