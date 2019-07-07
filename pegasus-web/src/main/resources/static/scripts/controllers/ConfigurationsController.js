angular.module('pegasus').controller("ConfigurationsController",
    [
        "$rootScope", "$scope", "$stateParams", "$location", "$state", "SecurityService", "ErrorService", "ToastrService",
        function ($rootScope, $scope, $stateParams) {

            bindEntityBetweenControllers();

            $scope.configurationFilters = {
                visible: false,
                name: "",
                category: ""
            };

            $scope.toggleFilters = function() {
                $scope.configurationFilters.visible = !$scope.configurationFilters.visible;
            };

            $scope.addConfiguration = function() {
                var configuration = {
                    name: null,
                    category: $scope.categoryOptions[0],
                    dataType: $scope.dataTypeOptions[0],
                    jsonClass: null,
                    value: null
                };
                $scope.entity.configurations.push(configuration);
            };

            $scope.cloneConfiguration = function(index) {
                var configuration = $scope.entity.configurations[index];
                var clone = {
                    name: "Clone of " + configuration.name,
                    category: configuration.category,
                    dataType: configuration.dataType,
                    jsonClass: configuration.jsonClass,
                    value: configuration.value
                };
                $scope.entity.configurations.push(clone);
            };

            $scope.removeConfiguration = function(index) {
                if (index >= 0)
                    $scope.entity.configurations.splice(index, 1);
            };

            function bindEntityBetweenControllers() {

                var entityName = $scope.$parent.entityName.toLowerCase();

                if (entityName === "product") {
                    $scope.$parent.$watch('product', function(newVal){
                        $scope.entity = newVal
                    }, true);
                } else if (entityName === "application") {
                    $scope.$parent.$watch('application', function(newVal){
                        $scope.entity = newVal
                    }, true);
                } else {
                    console.log("NOT IMPLEMENTED CONFIGURATIONS TYPE")
                }
            }
        }
    ]
);