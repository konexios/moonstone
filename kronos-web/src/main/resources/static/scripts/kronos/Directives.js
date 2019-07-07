/**
 * Demonstration directive. We don't use it anywhere.
 * ToDo: should be removed.
 * @ngdoc directive (this tag highlight that this is angularjs directive, not a regular js function)
 * @name name
 */
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

/**
 * Multiple emails validation directive.
 * Used to validate fields with multiple emails for valid email addresses.
 * @ngdoc directive
 * @name multipleEmails
 * @returns 'multipleEmails' form validation sign.
 */
directives.directive('multipleEmails', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue) {
                if (viewValue != "") {
                    var emails = viewValue.split(',');

                    // loop that checks every email, returns undefined if one of them fails.
                    var re = /\S+@\S+\.\S+/;

                    var validityArr = emails.map(function (str) {
                        return re.test(str.trim());
                    });
                    var atLeastOneInvalid = false;
                    angular.forEach(validityArr, function (value) {
                        if (value === false)
                            atLeastOneInvalid = true;
                    });
                    if (!atLeastOneInvalid) {
                        ctrl.$setValidity('multipleEmails', true);

                        return viewValue;
                    } else {
                        ctrl.$setValidity('multipleEmails', false);
                        return undefined;
                    }
                }
                else {
                    ctrl.$setValidity('multipleEmails', true);
                    return viewValue;
                }
            });
        }
    };
});

/**
 * Common dropdown element for List pages (tables).
 * @ngdoc directive
 * @name arrowItemsPerPageDropdown
 * @param itemsPerPage
 * @param totalItems
 * @param change
 * @param classes
 * @example
 * <arrow-items-per-page-dropdown data-classes="pull-right-for-mobile"
 *  data-items-per-page="itemsPerPage" data-total-items="totalItems" data-change="vm.change(numberOfItems)">
 */
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
                        var firstDigit = +(""+total).substring(0, 1);
                        if (firstDigit == 2) {
                            total = total / 2 * 5; // 20 -> 50, 200 -> 500
                        } else {
                            total *= 2; // 10 -> 20, 50 -> 100, 100 -> 200, 500 -> 1000
                        }
                        scope.itemsPerPageOptions.push(total);
                    }
                }
            });
        },
        templateUrl: "/scripts/kronos/tpl/ItemsPerPageDropdown.html"
    }
});

/**
 * Common pagination element for List pages (tables).
 * @ngdoc directive
 * @name arrowPagination
 * @param pageIndex
 * @param totalPages
 * @param first
 * @param last
 * @param previous
 * @param next
 * @param go
 */
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
        templateUrl: "/scripts/kronos/tpl/Pagination.html"
    }
});

/**
 * Generate a label to show how many pages we have, for List pages (tables)
 * @ngdoc directive
 * @name arrowShowingItems
 * @param pageIndex
 * @param itemsPerPage
 * @param totalItems
 * @param topic
 * @returns 'Showing {{showingFrom}} to {{showingTo}} of {{totalItems}} {{topic}}'
 */
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
        templateUrl: "/scripts/kronos/tpl/ShowingItems.html"
    }
});

/**
 * Generate <th> cell for List pages (tables) with a common view and sorting logic.
 * @ngdoc directive
 * @name arrowColumnHeader
 */
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
        templateUrl: "/scripts/kronos/tpl/ColumnHeader.html"
    }
});

/**
 * Generate <th> cell for List pages (tables) with a common view and sorting logic (for mobile devices view).
 * @ngdoc directive
 * @name arrowColumnHeaderSmallDesktop
 */
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
        templateUrl: "/scripts/kronos/tpl/ColumnHeaderSmallDesktop.html"
    }
});

/**
 * Compares two fields and returns the sign of the equality.
 * Used to compare passwords in the registration\change password form.
 * @ngdoc directive
 * @name equals
 * @param ngModel first field for compare
 * @param equals second field for compare
 * @returns 'equals' validation sign
 */
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

/**
 * Provides password validation for compliance with certain rules.
 * @ngdoc directive
 * @name passwordPolicy
 * @param passwordPolicy object with signs that must be observed {minLength, maxLength, minLowerCase, ...}
 * @returns multiple form validation signs based on passwordPolicy object
 */
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
                var regexp = new RegExp('[' + scope.passwordPolicy.specialCharacters + ']', "g");
                var matches = viewValue.match(regexp); 
                var count = matches ? matches.length : 0;
                return count >= scope.passwordPolicy.minSpecial;
            };

            model.$validators.allowWhitespace = function(modelValue, viewValue) {
                return notDefined('allowWhitespace') || scope.passwordPolicy.allowWhitespace || viewValue.indexOf(' ') < 0;
            }
        }
    }
});

/**
 * Directive to validate JSON
 * @ngdoc directive
 * @name validJson
 * @param ngModel JSON as text
 * @returns 'validJson' form validation sign {true|false}
 */
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

/**
 * Directive to set the field as a required with the date object
 * @ngdoc directive
 * @name dateRequired
 * @param ngModel required date object
 * @returns 'dateRequired' form validation sign
 */
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

/**
 * Directive which provides validation for a range of dates
 * @ngdoc directive
 * @name dateRange
 * @param dateRange link to another range date
 * @param dateLess
 * @param dateGreater
 * @param dateMaxRange
 * @param dateMinRange
 * @param dateFromLessCurrent rule to validate that first date (from) should be less than current
 * @returns form model validation signs [] for dates range
 */
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

            if (attributes.dateFromLessCurrent != null) {
                model.$validators.dateFromLessCurrent = function(modelValue, viewValue) {
                    var currentDate = new Date();
                    if (modelValue && modelValue.getTime && !isNaN(modelValue)) {
                        return modelValue.getTime() < currentDate.getTime();
                    } else {
                        return true; //cannot compare
                    }
                };
            }
        }
    }
});

/**
 * Directive to create onResize event listener for Window and dispatch about this to inner scopes listeners.
 * @ngdoc directive
 * @name onResize
 */
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

/**
 * Directive broadcast to children scopes what the element is rendered.
 * @ngdoc directive
 * @name afterRender
 * @param afterRender
 * @returns broadcast 'onAfterRender' event
 */
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

/**
 * Email address validation directive.
 * @ngdoc directive
 * @name emailValidation
 * @param ngModel email address model
 * @return 'email' form validation sign
 */
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

/**
 * Directive to recalculate container height if 'watchedHeight' was changed.
 * @ngdoc directive
 * @name getHeight
 * @returns sets new 'top' and 'height' values to element (css)
 */
directives.directive('getHeight', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            scope.$watch('watchedHeight', function(newHeight) {
                element.css({top: newHeight + 'px', height: "calc(100% - (" + newHeight + "px)"});
            });
        }
    }
});

/**
 * Subscribe on Window onResize event to keep element height in watchedHeight variable.
 * ToDo: Document main purposes and refactoring.
 * (!) Potential negative points founded:
 * - $watch listener (second param) is not founded;
 * - can we do this with css? looks like overhead;
 * - one variable used in two directives, but scope is not guaranteed to be the same;
 * - this is special case of logic and should be processed in common controller for watchedHeight variable;
 * @ngdoc directive
 * @name watchHeight
 * @returns sets '$scope.watchedHeight' actual value
 */
directives.directive('watchHeight', [ '$rootScope', '$timeout', function($rootScope, $timeout) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            scope.$watch(function() {
                scope.watchedHeight = element[0].offsetHeight;
            });
            if (scope.watchedHeight != element[0].offsetHeight) {
                angular.element(window).on('resize', function() {
                    $timeout(function() {
                        scope.watchedHeight = element[0].offsetHeight;
                    }, 0);
                });
            }
        }
    }
}]);

/**
 * Directive to provide special 'key=value' format for inputs (multiline),
 * with validation and display features, with saving in the valid special 'key=value' model
 * @ngdoc directive
 * @name keyValuePairs
 * @param ngModel to represent as a 'key=value'
 * @example
 * <textarea key-value-pairs class="form-control" id="" name="" ng-model="vm.model"></textarea>
 */
directives.directive('keyValuePairs', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {

            ctrl.$parsers.push(input);
            ctrl.$formatters.push(output);
            ctrl.$validators.keyValuePairs = keyValuePairsValidator;

            function input(input) {
                var obj = parseKeyValObj(input);
                return obj;
            }

            function output(obj) {
                if(!obj || angular.equals(obj, {})){
                    return '';
                }
                var str = '', propsCount = Object.keys(obj).length, index = 0;
                for (var prop in obj) {
                    index++;
                    str += prop + '=' + obj[prop] + (index == propsCount ? '' : '\n');
                }
                return str;
            }

            function keyValuePairsValidator(modelValue, viewValue) {
                if(parseKeyValObj(viewValue)){
                    return true;
                }
                return false;
            }

            function parseKeyValObj(val) {
                var obj = {};
                if(!val) {
                    // empty string == empty object
                    return obj;
                }

                var lines = val.split('\n');
                for (var i = 0; i < lines.length; i++) {
                    var pair = lines[i].split('=');
                    if(pair.length != 2){
                        return null;    // null if we have invalid data format (error)
                    }
                    obj[pair[0].trim()] = pair[1].trim();
                }
                
                return obj;
            }
        }
    };
});

/**
 * Filter to output special 'key=value' format
 * @ngdoc filter
 * @name keyValuePairsView
 * @returns formated 'key=value' string
 */
directives.filter('keyValuePairsView', function() {
  return function(object, emptySymbols) {

      if(!object || angular.equals(object, {})){
          return emptySymbols || '';
      }

      var output = '';
      for (var prop in object) {
          output += '"' + prop + '"= "' + object[prop] + '"\n';
      }

      return output;
  };
});

/**
 * Output subscription expiration screen if the subscription has expired, or message if subscription will expire soon
 * @ngdoc directive
 * @name subscriptionExpiration
 * @param $rootScope.app.subscriptionExpiration
 * @returns expiration message if subscription expiring
 */
directives.directive('subscriptionExpiration', [ '$rootScope', function($rootScope) {
  return {
    restrict: 'E',
    link: function(scope, element, attrs) {
      scope.subscriptionExpiration = $rootScope.app.subscriptionExpiration;
      if (scope.subscriptionExpiration.expired) {
        window.location.replace(window.location.origin + '/#/subscription-expired');
      }
      var endDate = new Date(scope.subscriptionExpiration.endDate);
      scope.endDate = endDate.getDate() + '/' + (endDate.getMonth() + 1) + '/' + endDate.getFullYear();
    },
    template: "<div class='subscription-expiration' ng-if='subscriptionExpiration.expiring'>Your subscription will expire {{endDate}}. Please contact Arrow Connect Support to renew.</div>"
  }
}]);

/**
 * Only validator for specific 'key=value' format.
 * ToDo: looks like validation logic duplicated in 'keyValuePairs' directive, and probably should be refactored.
 * @ngdoc directive
 * @name keyValuePairsValidation
 */
directives.directive('keyValuePairsValidation', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {

            ctrl.$validators.keyValuePairsValidation = validator;

            function validator(modelValue, viewValue) {
                if(canParseKeyValObj(viewValue)){
                    return true;
                }
                return false;
            }

            function canParseKeyValObj(val) {
                var obj = {};
                if(!val) { // empty string == empty object -> valid
                    return obj;
                }

                var lines = val.split('\n');
                for (var i = 0; i < lines.length; i++) {
                    var pair = lines[i].split('=');
                    if(pair.length != 2){
                        return null;    // null if we have invalid data format -> error
                    }
                    obj[pair[0].trim()] = pair[1].trim();
                }
                
                return obj;
            }
        }
    };
});

/**
 * Directive to bind html with angular compiled code
 * @ngdoc directive
 * @name bindHtmlCompile
 * @param bindHtmlCompile string with html\angular code to compile and bind
**/
directives.directive('bindHtmlCompile', ['$compile', function ($compile) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            scope.$watch(function () {
                return scope.$eval(attrs.bindHtmlCompile);
            }, function (value) {
                // In case value is a TrustedValueHolderType, sometimes it
                // needs to be explicitly called into a string in order to
                // get the HTML string.
                element.html(value && value.toString());
                // If scope is provided use it, otherwise use parent scope
                var compileScope = scope;
                if (attrs.bindHtmlScope) {
                    compileScope = scope.$eval(attrs.bindHtmlScope);
                }
                $compile(element.contents())(compileScope);
            });
        }
    };
}]);

/**
 * Provides from template to inject custom validator function
 * @ngdoc directive
 * @name injectCustomValidator
 * @param injectCustomValidator object with validation key and function `{ key: string, func: function {} }`
 * @example
 * <input type="text" id="name" name="name" ng-model="name" inject-custom-validator="validatonObject" />
 */
directives.directive('injectCustomValidator', function() {
    return {
        restrict: 'A',
        scope: {
            injectCustomValidator: '='
        },
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            ctrl.$validators[scope.injectCustomValidator.key] = scope.injectCustomValidator.func;
        }
    };
});

/**
 * Display loading element instead of content if the loading condition happen,
 * relation support with http request (all types and url params), no any actions in view\controller required,
 * @ngdoc directive
 * @name loading
 * @param loading url string or an array of url strings associated with condition request, or bool as a condition
 * @param loadingParams if url contains params, it can be specified here
 * @param loadingMethod like GET [default], POST, PUT, DELETE
 */
directives.directive('loading', ['$http', '$interpolate', function($http, $interpolate) {
    return {
        restrict: 'A',
        scope: {
            loadingURL: "=loading",
            loadingPARAMS: "=loadingParams",
            loadingMETHOD: "=loadingMethod"
        },
        link: function(scope, element, attrs, ctrl) {
            var loadingElement = angular.element("<div class='loading-indicator'><div class='loader'><div class='bar'></div><div class='bar'></div><div class='bar'></div><div class='bar'></div><div class='bar'></div></div></div>");

            scope.isLoading = function () {
                scope.loading = false;

                if (angular.isArray(scope.loadingURL)) {
                    angular.forEach(scope.loadingURL, function(value, key) {
                        if (checkIfLoading(value)) {
                            scope.loading = true;
                        }
                    });
                } else if (angular.isString(scope.loadingURL)) {
                    scope.loading = checkIfLoading(scope.loadingURL);
                } else {
                    scope.loading = !!scope.loadingURL;
                }

                return scope.loading;
            };

            scope.$watch(scope.isLoading, function (value, oldValue) {
                if (!value) {
                    element.show();
                    loadingElement.remove();
                    scope.loading = false;
                } else {
                    element.hide();
                    element.parent().append(loadingElement);
                }
            });

            function checkIfLoading(url) {
                var loading = false;
                if (angular.isDefined(url) && $http.pendingRequests.length !== 0) {
                    angular.forEach($http.pendingRequests, function (value, key) {
                        if(angular.isDefined(scope.loadingPARAMS)) {
                            url = $interpolate(url, false, null, true)(scope.loadingPARAMS);
                        }

                        if(url === value.url || url === value.url) {
                            loading = (angular.isUndefined(scope.loadingMETHOD)) || (angular.isDefined(scope.loadingMETHOD) && scope.loadingMETHOD === value.method);
                        }
                    });
                }

                return loading;
            }
        }
    };
}]);