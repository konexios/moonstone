directives.directive('name', function() {
	return {
		scope:{},
		restrict: 'E',
		replace: true,
		link: function(s, e, a) {
			s.fullName = a.first + ' ' + a.last;
		},
		template: "<h1>Hello {{fullName}}!!!</h1>"
	}
});

directives.directive('arrowItemsPerPageDropdown', function() {
    return {
        restrict: 'E',
        replace: true,
        scope: {
            itemsPerPage: "=",
            totalItems: "=",
            change: "&",
            classes: "@"
        },
        link: function(scope, element, attributes) {
            scope.itemsPerPageOptions = [];
            scope.itemsPerPageOptions.push(10);

            scope.$watch('totalItems', function() {
                scope.itemsPerPageOptions = [];
                scope.itemsPerPageOptions.push(10);

                var total = 10;
                if (scope.totalItems > 10) {
                    while (total < scope.totalItems) {
                        total += 10;
                        scope.itemsPerPageOptions.push(total);
                    }
                }
            });
        },
        templateUrl: "/scripts/rhea/tpl/ItemsPerPageDropdown.html"
    }
});

directives.directive('arrowPagination', function() {
    return {
        restrict: 'E',
        replace: true,
        scope: {
            pageIndex: "=",
            totalPages: "=",
            first: "=",
            last: "=",
            previous: "&",
            next: "&",
            go: "&"
        },
        link: function(scope, element, attributes) {
            // set up pagination
            scope.pages = [];

            var calculateRange = function(boundry, fixedUnits, firstPageIndex, lastPageIndex, currentPageIndex, lastPageNumber, diffFromFirstPageIndex, diffFromLastPageIndex) {

                var minLimit = (boundry + fixedUnits + 1);
                var first = firstPageIndex;
                var last = lastPageIndex;

                var numberOfPages = lastPageNumber;
                var diffFromFirst = diffFromFirstPageIndex;
                var diffFromLast = diffFromLastPageIndex;

                var targetStart = currentPageIndex;
                var targetEnd = (currentPageIndex + boundry);

                if (targetStart < first) {
                    targetStart = first;
                }

                if (targetEnd > last)
                {
                    targetEnd = last;
                }

                if (numberOfPages <= minLimit) {
                    // shall return a dynamic number of items based on first and last
                    targetStart = (first + 1);
                    targetEnd = (last - 1);
                } else if (currentPageIndex >= boundry && diffFromLast >= boundry
                        && numberOfPages > minLimit) {
                    // shall always be 3
                    targetStart = (currentPageIndex - 1);
                    targetEnd = (currentPageIndex + 1);
                } else if (targetStart == first || diffFromFirst < boundry) {
                    // shall always be 4
                    targetStart = (first + 1);
                    targetEnd = (first + boundry);
                } else if (targetEnd == last) {
                    // shall always be 4
                    targetStart = (last - boundry);
                    targetEnd = (targetEnd - 1);
                }

                var list = [];

                var count = 0;
                while (targetStart <= targetEnd) {
                    if (targetStart > first && targetStart < last)
                    {
                        list.push(targetStart);
                        count++;
                    }
                    targetStart++;
                }

                return list;
            };

            var updatePagination = function() {

                var pageBoundry = 3;
                var fixedUnits = 2;
                var minLimit = (pageBoundry + fixedUnits + 1);

                // page indexes
                var firstPageIndex = 0;
                var previousPageIndex = (scope.pageIndex - 1);
                var nextPageIndex = scope.pageIndex + 1;
                var lastPageIndex = (scope.totalPages - 1);

                // page numbers
                var firstPageNumber = (firstPageIndex + 1);
                var currentPageNumber = (scope.pageIndex + 1);
                var lastPageNumber = (lastPageIndex + 1);
                var numberOfPages = scope.totalPages;

                // calculations
                var diffFromFirstPageIndex = (scope.pageIndex - firstPageIndex);
                var diffFromLastPageIndex = (lastPageIndex - scope.pageIndex);
                var boundPageIndexList = calculateRange(pageBoundry, fixedUnits, firstPageIndex, lastPageIndex, scope.pageIndex, lastPageNumber, diffFromFirstPageIndex, diffFromLastPageIndex);

                scope.pages = [];
                if (scope.totalPages > 0) {

                    // first page
                    if (scope.totalPages > 1) {
                        scope.pages.push({label: 1, value: 0, disabled: false});
                    } else {
                        scope.pages.push({label: "1/1", value: null, disabled: true});
                    }

                    // dots
                    if (numberOfPages > minLimit && scope.pageIndex >= pageBoundry) {
                        scope.pages.push({label: "...", value: null, disabled: true});
                    }

                    // pages
                    for (var i = 0; i < boundPageIndexList.length; i++) {
                        var item = boundPageIndexList[i];
                        scope.pages.push({label: item + 1, value: (item), disabled: false});
                    }

                    // dots
                    if (numberOfPages > minLimit && diffFromLastPageIndex >= pageBoundry) {
                        scope.pages.push({label: "...", value: null, disabled: true});
                    }

                    // last page
                    if (scope.totalPages > 1) {
                        scope.pages.push({label: scope.totalPages, value: (scope.totalPages - 1), disabled: false});
                    }
                }
            };

            scope.$watch('pageIndex', function() {
                updatePagination();
            });

            scope.$watch('totalPages', function() {
                updatePagination();
            });
        },
        templateUrl: "/scripts/rhea/tpl/Pagination.html"
    }
});

directives.directive('arrowShowingItems', function() {
    return {
        restrict: 'E',
        replace: true,
        scope: {
            pageIndex: "=",
            itemsPerPage: "=",
            totalItems: "=",
            topic: "@"
        },
        link: function(scope, element, attributes) {
            var calculate = function() {
                scope.showingFrom = (scope.totalItems > 0 ? (scope.pageIndex * scope.itemsPerPage) + 1 : 0);
                var to = (scope.pageIndex + 1) * scope.itemsPerPage;
                if (to > scope.totalItems)
                    to = scope.totalItems;
                scope.showingTo = (scope.totalItems > 0 ? to : 0);
            };

            scope.$watch('pageIndex', function() {
                calculate();
            });

            scope.$watch('itemsPerPage', function() {
                calculate();
            });

            scope.$watch('totalItems', function() {
                calculate();
            });
        },
        templateUrl: "/scripts/rhea/tpl/ShowingItems.html"
    }
});

directives.directive('arrowColumnHeader', function() {
    return {
        restrict: 'AC',
        replace: true,
        scope: {
            label: "=",
            value: "=",
            sortField: "=",
            sortDirection: "=",
            sortable: "=",
            sort: "&"
        },
        link: function(scope, element, attributes) {
            // do nothing
        },
        templateUrl: "/scripts/rhea/tpl/ColumnHeader.html"
    }
});

directives.directive('arrowColumnHeaderSmallDesktop', function() {
    return {
        restrict: 'AC',
        replace: true,
        scope: {
            label: "=",
            value: "=",
            sortField: "=",
            sortDirection: "=",
            sortable: "=",
            sort: "&"
        },
        templateUrl: "/scripts/rhea/tpl/ColumnHeaderSmallDesktop.html"
    }
});

directives.directive('equals', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attributes, model) {
            if (!attributes.equals) {
                console.error('equals expects a model as an argument');
                return;
            }
            scope.$watch(attributes.equals, model.$validate);
            model.$validators.equals = function(modelValue, viewValue) {
                return viewValue === scope.$eval(attributes.equals);
            }
        }
    }
});

directives.directive('passwordPolicy', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        scope: {
            passwordPolicy: '='
        },
        link: function(scope, element, attributes, model) {
            function notDefined(property) {
                return !scope.passwordPolicy || scope.passwordPolicy[property] == null;
            }

            scope.$watch('passwordPolicy', model.$validate);

            model.$validators.minLength = function(modelValue, viewValue) {
                return notDefined('minLength') || viewValue.length >= scope.passwordPolicy.minLength;
            };

            model.$validators.maxLength = function(modelValue, viewValue) {
                return notDefined('maxLength') || viewValue.length <= scope.passwordPolicy.maxLength;
            };

            model.$validators.minLowerCase = function(modelValue, viewValue) {
                if (notDefined('minLowerCase')) return true;
                var matches = viewValue.match(/[a-z]/g);
                var count = matches ? matches.length : 0;
                return count >= scope.passwordPolicy.minLowerCase;
            };

            model.$validators.minUpperCase = function(modelValue, viewValue) {
                if (notDefined('minUpperCase')) return true;
                var matches = viewValue.match(/[A-Z]/g);
                var count = matches ? matches.length : 0;
                return count >= scope.passwordPolicy.minUpperCase;
            };

            model.$validators.minDigit = function(modelValue, viewValue) {
                if (notDefined('minDigit')) return true;
                var matches = viewValue.match(/[0-9]/g);
                var count = matches ? matches.length : 0;
                return count >= scope.passwordPolicy.minDigit;
            };

            model.$validators.minSpecial = function(modelValue, viewValue) {
                if (notDefined('minSpecial')) return true;
                var matches = viewValue.match(/[^a-zA-Z0-9 ]/g);
                var count = matches ? matches.length : 0;
                return count >= scope.passwordPolicy.minSpecial;
            };

            model.$validators.allowWhitespace = function(modelValue, viewValue) {
                return notDefined('allowWhitespace') || scope.passwordPolicy.allowWhitespace || viewValue.indexOf(' ') < 0;
            }
        }
    }
});

directives.directive('validJson', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attributes, model) {
            model.$validators.validJson = function(modelValue, viewValue) {
                try {
                    JSON.parse(viewValue);
                    return true;
                } catch(e) {
                    return false;
                }
            }
        }
    }
});

directives.directive('dateRequired', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attributes, model) {
            model.$validators.dateRequired = function(modelValue, viewValue) {
                return modelValue && modelValue.getTime && !isNaN(modelValue);
            }
        }
    }
});

directives.directive('dateRange', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attributes, model) {
            if (!attributes.dateRange) {
                console.error('dateRange expects a model as an argument');
                return;
            }
            scope.$watch(attributes.dateRange, model.$validate);
            if (attributes.dateLess != null) {
                model.$validators.dateLess = function(modelValue, viewValue) {
                    var anotherModelValue = scope.$eval(attributes.dateRange);
                    if (modelValue && anotherModelValue && modelValue.getTime && anotherModelValue.getTime && !isNaN(modelValue) && !isNaN(anotherModelValue)) {
                        return modelValue.getTime() < anotherModelValue.getTime();
                    } else {
                        return true; //cannot compare
                    }
                }
            }
            if (attributes.dateGreater != null) {
                model.$validators.dateGreater = function(modelValue, viewValue) {
                    var anotherModelValue = scope.$eval(attributes.dateRange);
                    if (modelValue && anotherModelValue && modelValue.getTime && anotherModelValue.getTime && !isNaN(modelValue) && !isNaN(anotherModelValue)) {
                        return modelValue.getTime() > anotherModelValue.getTime();
                    } else {
                        return true; //cannot compare
                    }
                }
            }
            if (attributes.dateMaxRange != null) {
                scope.$watch(attributes.dateMaxRange, model.$validate);
                model.$validators.dateMaxRange = function(modelValue, viewValue) {
                    var maxRangeInMs = +scope.$eval(attributes.dateMaxRange);
                    var anotherModelValue = scope.$eval(attributes.dateRange);
                    if (modelValue && anotherModelValue && modelValue.getTime && anotherModelValue.getTime && !isNaN(modelValue) && !isNaN(anotherModelValue) && !isNaN(maxRangeInMs)) {
                        return Math.abs(modelValue.getTime() - anotherModelValue.getTime()) <= maxRangeInMs;
                    } else {
                        return true; //cannot compare
                    }
                }
            }
            if (attributes.dateMinRange != null) {
                scope.$watch(attributes.dateMinRange, model.$validate);
                model.$validators.dateMinRange = function(modelValue, viewValue) {
                    var minRangeInMs = +scope.$eval(attributes.dateMinRange);
                    var anotherModelValue = scope.$eval(attributes.dateRange);
                    if (modelValue && anotherModelValue && modelValue.getTime && anotherModelValue.getTime && !isNaN(modelValue) && !isNaN(anotherModelValue) && !isNaN(minRangeInMs)) {
                        return Math.abs(modelValue.getTime() - anotherModelValue.getTime()) >= minRangeInMs;
                    } else {
                        return true; //cannot compare
                    }
                }
            }
        }
    }
});

directives.directive('onResize', ['$window', '$rootScope', function ($window, $rootScope) {
	return {
		restrict: 'A',
		link: function () {
			$rootScope.$broadcast("resize", {
				"width": angular.element($window).width()
			});
			angular.element($window).bind('resize', function () {
				$rootScope.$broadcast("resize", {
					"width": angular.element($window).width()
				});
			})
		}
	}
}]);
directives.directive('afterRender', ['$rootScope', function ($rootScope) {
    return {
        restrict: 'A',
        terminal: true,
        transclude: false,
        link: function (scope, element, attrs) {
            if (attrs) {
                scope.$eval(attrs.afterRender)
            }
            $rootScope.$broadcast('onAfterRender')
        }
    };
}]);

directives.directive('emailValidation', function() {
    var EMAIL_REGEXP = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;

    return {
        require: '?ngModel',
        link: function(scope, elm, attrs, ctrl) {
            if (ctrl && ctrl.$validators.email) {
            ctrl.$validators.email = function(modelValue) {
                return ctrl.$isEmpty(modelValue) || EMAIL_REGEXP.test(modelValue);
            };
            }   
        }
    };

});

directives.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);