controllers.controller('WidgetTypesController',
    [ '$rootScope', '$scope', '$q', '$location', '$routeParams', '$uibModal', 'SecurityService', 'AuthenticationService', 'ErrorService', 'SpinnerService', 'ToastrService', 'WidgetTypesService',
    function ($rootScope, $scope, $q, $location, $routeParams, $uibModal, SecurityService, AuthenticationService, ErrorService, SpinnerService, ToastrService, WidgetTypesService) {
    	
    	var vm = this;
    	vm.widgetTypes = [];

 		// columns
 		vm.columnHeaders = [
            { label: "Widget Type", value: "name", sortable: false },
            { label: "Description", value: "description", sortable: false },
            { label: "Class Name", value: "className", sortable: false },
            { label: "Directive", value: "directive", sortable: false },
            { label: "Category", value: "category", sortable: false }
        ];
    	
        function init() {
            SpinnerService.wrap(WidgetTypesService.getAll)
                .then(function(response) {
                	vm.widgetTypes = response.data;
                })
                .catch(ErrorService.handleHttpError);
        }

        init();
    }
]);