/**
 * definition of all ootb properties views
 */

/**
 * Directive for transforming "app-property" attribute to implemented field
 */
angular.module('widgets').directive('addProperty', function($parse, $compile) {
    return {
        compile: function compile(tElement, tAttrs) {
            var directiveGetter = $parse(tAttrs.addProperty);

            return function postLink(scope, element) {
                element.removeAttr('add-property');

                var directive = directiveGetter(scope);
                element.attr(directive, '');

                $compile(element)(scope);
            };
        }
    };
});

angular.module('widgets').directive('simpleString', function() {
    return {
        scope: {
            property: '='
        },
        controller: 'simpleStringController',
        templateUrl: '/views/property-types/simpleString.html'
    };
});

angular.module('widgets').controller('simpleStringController', ['$scope', function($scope) {}]);



angular.module('widgets').directive('simpleBoolean', function() {
    return {
        scope: {
            property: '='
        },
        controller: 'simpleBooleanController',
        templateUrl: '/views/property-types/simpleBoolean.html'
    };
});

angular.module('widgets').controller('simpleBooleanController', ['$scope', function($scope) {}]);

angular.module('widgets').directive('simpleInteger', function() {
    return {
        scope: {
            property: '='
        },
        controller: 'simpleIntegerController',
        templateUrl: '/views/property-types/simpleInteger.html'
    };
});

angular.module('widgets').controller('simpleIntegerController', ['$scope', function($scope) {}]);

angular.module('widgets').directive('choiceString', function() {
    return {
        scope: {
            property: '='
        },
        controller: 'choiceStringController',
        templateUrl: '/views/property-types/choiceString.html'
    };
});

angular.module('widgets').controller('choiceStringController', ['$scope', function($scope) {}]);

angular.module('widgets').directive('choiceKeyValueString', function() {
    return {
        scope: {
            property: '='
        },
        controller: 'choiceKeyValueStringController',
        templateUrl: '/views/property-types/choiceKeyValueString.html'
    };
});

angular.module('widgets').controller('choiceKeyValueStringController', ['$scope', function($scope) {}]);

angular.module('widgets').directive('choiceKeyValueInteger', function() {
    return {
        scope: {
            property: '='
        },
        controller: 'choiceKeyValueIntegerController',
        templateUrl: '/views/property-types/choiceKeyValueString.html'
    };
});

angular.module('widgets').controller('choiceKeyValueIntegerController', ['$scope', function($scope) {}]);

angular.module('widgets').directive('choiceKeyValueListString', function() {
    return {
        scope: {
            property: '='
        },
        controller: 'choiceKeyValueListStringController',
        controllerAs: 'vm',
        templateUrl: '/webjars/pegasus-dashboard-web-api/src/property-types/choiceKeyValueListString.html'
    };
});

angular.module('widgets').controller('choiceKeyValueListStringController', [
    '$scope',
    function($scope) {
        vm = this;

        vm.selectedAvailableItems = [];
        vm.selectedSelectedItems = [];

        vm.pushSelected = function() {
            if (!$scope.property.value || !($scope.property.value instanceof Array)) {
                $scope.property.value = [];
            }

            // push items to selected list
            vm.selectedAvailableItems.forEach(function(item) {
                // check on already exist
                if ($scope.property.value.indexOf(item) === -1) {
                    $scope.property.value.push(item);
                }
            });

            // clear selections
            vm.selectedAvailableItems = [];
        };

        vm.removeSelected = function() {
            if ($scope.property.value && $scope.property.value.length > 0) {
                $scope.property.value = $scope.property.value.filter(function(item) {
                    return vm.selectedSelectedItems.indexOf(item) < 0;
                });
            }

            // clear selections
            vm.selectedSelectedItems = [];
        };

        vm.filterAvailable = function(item, interation, list) {
            return $scope.property.value && $scope.property.value.length > 0 && $scope.property.value.indexOf(item.key) >= 0 ? false : true;
        };

        vm.filterSelected = function(item, interation, list) {
            return !$scope.property.value || $scope.property.value.length < 1 ? false : $scope.property.value.length > 0 && $scope.property.value.indexOf(item.key) === -1 ? false : true;
        };
    }
]);

angular.module('widgets').directive("picurlString", function() {
    return {
        scope: {
            property: "="
        },
        controller: "picUrlStringController",
        templateUrl: '/views/property-types/picUrlString.html'
    };
}
);

angular.module('widgets').controller("picUrlStringController", ['$scope', function($scope) {}]);
