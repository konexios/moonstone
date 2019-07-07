controllers.controller('NotFoundController', ['$routeParams', 'ErrorService',
    function($routeParams, ErrorService) {
        var vm = this;

        vm.errorMessage = $routeParams.message || 'Page not found';
    }
]);
