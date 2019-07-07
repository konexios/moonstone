controllers.controller('ApolloWidgetsController',
    [ '$rootScope', '$scope', '$q', '$location', '$routeParams', '$uibModal', 'SecurityService', 'AuthenticationService', 'ErrorService', 'SpinnerService', 'ToastrService', 'ApolloWidgetService',
    function ($rootScope, $scope, $q, $location, $routeParams, $uibModal, SecurityService, AuthenticationService, ErrorService, SpinnerService, ToastrService, ApolloWidgetService) {
    	
    	var vm = this;
    	vm.widgetTypes = [];

 		// columns
 		vm.columnHeaders = [
            { label: "Widget", value: "name", sortable: false },
            { label: "Description", value: "description", sortable: false },
            { label: "Widget Type", value: "widgetTypeName", sortable: false },
            { label: "Category", value: "category", sortable: false },
            { label: "Icon Type", value: "iconType", sortable: false },
            { label: "Icon", value: "icon", sortable: false },
            { label: "Enabled", value: "enabled", sortable: false }
        ];
    	
        $scope.openWidgetDetails = function(widget) {
        	
        	if (widget == null)
        		widget = {
        			id: "new"
        		};
        	
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/modals/apollo-widget-modal.html',
                controller: 'ApolloWidgetModalController',
                size: 'lg',
                resolve: {
                	widget: widget
                }
            });
            
            modalInstance.result.then(function(widget) {
            	init();
            });
        };
 		
        function init() {
            SpinnerService.wrap(ApolloWidgetService.getAll)
                .then(function(response) {
                	vm.widgetTypes = response.data;
                })
                .catch(ErrorService.handleHttpError);
        }

        init();
    }
]);

controllers.controller('ApolloWidgetModalController',
    [
        '$scope', '$uibModalInstance', 'SpinnerService', 'ToastrService', 'SecurityService', 'ErrorService', 'ApolloWidgetService', 'widget',
        function ($scope, $uibModalInstance, SpinnerService, ToastrService, SecurityService, ErrorService, ApolloWidgetService, widget) {
       		$scope.widget = angular.merge({}, widget);
       		$scope.widgetTypes = [];
       		$scope.iconTypes = [];
       		$scope.categories = [];
        	
            $scope.save = function (form) {
            	if (form.$valid) {
            		if (widget.id == 'new') {
		            	SpinnerService.wrap(ApolloWidgetService.create, $scope.widget)
		                .then(function(response) {
		                	$uibModalInstance.close(response.data);
		                })
		                .catch(ErrorService.handleHttpError);
            		} else {
		            	SpinnerService.wrap(ApolloWidgetService.update, $scope.widget)
		                .then(function(response) {
		                	$uibModalInstance.close(response.data);
		                })
		                .catch(ErrorService.handleHttpError);
            		}
            	 } else {
            		 ToastrService.popupError('Widget can not be created because of invalid fields, please check errors.'); 
            	 }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
            
            $scope.findWidget = function() {
                SpinnerService.wrap(ApolloWidgetService.getWidget, $scope.widget.id)
                .then(function(response) {
                	$scope.widget = response.data.apolloWidget;
                	$scope.widgetTypes = response.data.widgetTypes;
               		$scope.iconTypes = response.data.iconTypes;
               		$scope.categories = response.data.categories;
                })
                .catch(ErrorService.handleHttpError);
            }
            
            $scope.findWidget();
        }
    ]
);