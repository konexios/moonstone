directives.directive("name", function () {
  return {
    scope: {},
    restrict: "E",
    replace: true,
    link: function (s, e, a) {
      s.fullName = a.first + " " + a.last;
    },
    template: "<h1>Hello {{fullName}}!!!</h1>"
  }
});

directives.directive("arrowItemsPerPageDropdown", function () {
  return {
    restrict: "E",
    replace: true,
    scope: {
      itemsPerPage: "=",
      totalItems: "=",
      change: "&"
    },
    link: function (scope, element, attributes) {
      scope.itemsPerPageOptions = [];
      scope.itemsPerPageOptions.push(10);

      scope.$watch("totalItems", function () {
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
    templateUrl: "/scripts/apollo/tpl/ItemsPerPageDropdown.html"
  }
});

directives.directive("arrowPagination", function () {
  return {
    restrict: "E",
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
    link: function (scope, element, attributes) {
      // set up pagination
      scope.pages = [];

      var calculateRange = function (boundry, fixedUnits, firstPageIndex, lastPageIndex, currentPageIndex, lastPageNumber, diffFromFirstPageIndex, diffFromLastPageIndex) {

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

        if (targetEnd > last) {
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
          if (targetStart > first && targetStart < last) {
            list.push(targetStart);
            count++;
          }
          targetStart++;
        }

        return list;
      };

      var updatePagination = function () {

        var pageBoundry = 4;
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

      scope.$watch("pageIndex", function () {
        updatePagination();
      });

      scope.$watch("totalPages", function () {
        updatePagination();
      });
    },
    templateUrl: "/scripts/apollo/tpl/Pagination.html"
  }
});

directives.directive("arrowShowingItems", function () {
  return {
    restrict: "E",
    replace: true,
    scope: {
      pageIndex: "=",
      itemsPerPage: "=",
      totalItems: "=",
      topic: "@",
      topicPlural: "@"
    },
    link: function (scope, element, attributes) {

      scope.topicValue = scope.topic;

      var calculate = function () {
        scope.topicValue = scope.topic;
        if (scope.totalItems > 1)
          scope.topicValue = scope.topicPlural;


        scope.showingFrom = (scope.totalItems > 0 ? (scope.pageIndex * scope.itemsPerPage) + 1 : 0);
        var to = (scope.pageIndex + 1) * scope.itemsPerPage;
        if (to > scope.totalItems)
          to = scope.totalItems;
        scope.showingTo = (scope.totalItems > 0 ? to : 0);
      };

      scope.$watch("pageIndex", function () {
        calculate();
      });

      scope.$watch("itemsPerPage", function () {
        calculate();
      });

      scope.$watch("totalItems", function () {
        calculate();
      });
    },
    templateUrl: "/scripts/apollo/tpl/ShowingItems.html"
  }
});

directives.directive("arrowColumnHeader", function () {
  return {
    restrict: "AC",
    replace: true,
    scope: {
      label: "=",
      value: "=",
      sortField: "=",
      sortDirection: "=",
      sortable: "=",
      sort: "&"
    },
    link: function (scope, element, attributes) {
      // do nothing
    },
    templateUrl: "/scripts/apollo/tpl/ColumnHeader.html"
  }
});

directives.directive("showIfNotEmpty", function () {
  return {
    restrict: "A",
    scope: {
      obj: "@"
    },
    link: function (scope, element, attributes) {
      var show = false;
      for (var key in scope.obj) {
        if (scope.obj.hasOwnProperty(key)) {
          show = true;
          break;
        }
      }
      if (!show) {
        angular.element(element).hide();
      }
    }
  }
});
directives.directive("showMoreLink", function () {
  return {
    restrict: "A",
    link: function (scope, element, attributes) {
      element
        .find("a")
        .on('click', function () {
          angular.element(this).toggleClass('hide-more');
          element.prev('.long-text-container').toggleClass('hide-text');
        });
    }
  }
});

directives.directive("name", function () {
	  return {
	    scope: {},
	    restrict: "E",
	    replace: true,
	    link: function (s, e, a) {
	      s.fullName = a.first + " " + a.last;
	    },
	    template: "<h1>Hello {{fullName}}!!!</h1>"
	  }
	});

	directives.directive("arrowItemsPerPageDropdown", function() {
	  return {
	    restrict: "E",
	    replace: true,
	    scope: {
	      itemsPerPage: "=",
	      totalItems: "=",
	      change: "&"
	    },
	    link: function(scope, element, attributes) {
	      scope.itemsPerPageOptions = [];
	      scope.itemsPerPageOptions.push({"key": 10, "value": 10});

	      scope.$watch("totalItems", function() {
	        scope.itemsPerPageOptions = [];
	        scope.itemsPerPageOptions.push({"key": 10, "value": 10});

	        var total = 10;
	        if (scope.totalItems > 10) {
	          while (total < scope.totalItems && total < 100) {
	            total += 10;
	            scope.itemsPerPageOptions.push({"key": total, "value": total});
	          }
	        }
	        
	        if (total < scope.totalItems)
	          scope.itemsPerPageOptions.push({"key": scope.totalItems, "value": "All"});
	      });
	    },
	    templateUrl: "/scripts/apollo/tpl/ItemsPerPageDropdown.html"
	  }
	});

	directives.directive("arrowPagination", function () {
	  return {
	    restrict: "E",
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
	    link: function (scope, element, attributes) {
	      // set up pagination
	      scope.pages = [];

	      var calculateRange = function (boundry, fixedUnits, firstPageIndex, lastPageIndex, currentPageIndex, lastPageNumber, diffFromFirstPageIndex, diffFromLastPageIndex) {

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

	        if (targetEnd > last) {
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
	          if (targetStart > first && targetStart < last) {
	            list.push(targetStart);
	            count++;
	          }
	          targetStart++;
	        }

	        return list;
	      };

	      var updatePagination = function () {

	        var pageBoundry = 4;
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

	      scope.$watch("pageIndex", function () {
	        updatePagination();
	      });

	      scope.$watch("totalPages", function () {
	        updatePagination();
	      });
	    },
	    templateUrl: "/scripts/apollo/tpl/Pagination.html"
	  }
	});

	directives.directive("arrowShowingItems", function () {
	  return {
	    restrict: "E",
	    replace: true,
	    scope: {
	      pageIndex: "=",
	      itemsPerPage: "=",
	      totalItems: "=",
	      topic: "@",
	      topicPlural: "@"
	    },
	    link: function (scope, element, attributes) {

	      scope.topicValue = scope.topic;

	      var calculate = function () {
	        scope.topicValue = scope.topic;
	        if (scope.totalItems > 1)
	          scope.topicValue = scope.topicPlural;


	        scope.showingFrom = (scope.totalItems > 0 ? (scope.pageIndex * scope.itemsPerPage) + 1 : 0);
	        var to = (scope.pageIndex + 1) * scope.itemsPerPage;
	        if (to > scope.totalItems)
	          to = scope.totalItems;
	        scope.showingTo = (scope.totalItems > 0 ? to : 0);
	      };

	      scope.$watch("pageIndex", function () {
	        calculate();
	      });

	      scope.$watch("itemsPerPage", function () {
	        calculate();
	      });

	      scope.$watch("totalItems", function () {
	        calculate();
	      });
	    },
	    templateUrl: "/scripts/apollo/tpl/ShowingItems.html"
	  }
	});

//	directives.directive("arrowColumnHeader", function () {
//	  return {
//	    restrict: "AC",
//	    replace: true,
//	    scope: {
//	      label: "=",
//	      value: "=",
//	      sortField: "=",
//	      sortDirection: "=",
//	      sortable: "=",
//	      sort: "&"
//	    },
//	    link: function (scope, element, attributes) {
//	      // do nothing
//	    },
//	    templateUrl: "/scripts/apollo/tpl/ColumnHeader.html"
//	  }
//	});

	directives.directive("showIfNotEmpty", function () {
	  return {
	    restrict: "A",
	    scope: {
	      obj: "@"
	    },
	    link: function (scope, element, attributes) {
	      var show = false;
	      for (var key in scope.obj) {
	        if (scope.obj.hasOwnProperty(key)) {
	          show = true;
	          break;
	        }
	      }
	      if (!show) {
	        angular.element(element).hide();
	      }
	    }
	  }
	});
	directives.directive("showMoreLink", function () {
	  return {
	    restrict: "A",
	    link: function (scope, element, attributes) {
	      element
	        .find("a")
	        .on('click', function () {
	          angular.element(this).toggleClass('hide-more');
	          element.prev('.long-text-container').toggleClass('hide-text');
	        });
	    }
	  }
	});


	//From Pegasus
	directives.directive("leftMenu", function() {
	  
	  function LeftMenuController($rootScope) {
	        var vm = this;

	        vm.menuIsCollapsed = vm.menuIsCollapsed || false;   // default = false (feature: can store in LS and also can grouping by name)
	        vm.scrollerConfig = $rootScope.scrollerConfig;  // todo: move scroll configurations to constants.
	    }

	  var directive = {
	    restrict: 'AE',
	    transclude: true,
	    scope: {
	      menuIsCollapsed: '=isCollapsed'
	    },
	    templateUrl: 'scripts/apollo/tpl/left-menu.tmp.html',
	    link: function(scope, el, attrs, ctrl, transcludeFn) {
	      // own transclude for no isolated scope
	      transcludeFn(scope.$parent, function(transcludedContent) {
	        el.find(".transcluded-content").append(transcludedContent);
	      });
	    },
	    controller: LeftMenuController,
	    controllerAs: 'vm',
	    bindToController: true,
	  };

	  return directive;
	});



	// Route State Load Spinner(used on page or content load)
	directives.directive('ngSpinnerBar', ['$rootScope',
	    function($rootScope) {
	        return {
	            link: function(scope, element, attrs) {
	                // by default hide the spinner bar
	                element.addClass('hide'); // hide spinner bar by default

	                // display the spinner bar whenever the route changes(the content part started loading)
	                $rootScope.$on('$stateChangeStart', function() {
	                    element.removeClass('hide'); // show spinner bar
	                });

	                // hide the spinner bar on rounte change success(after the content loaded)
	                $rootScope.$on('$stateChangeSuccess', function() {
	                    element.addClass('hide'); // hide spinner bar
	                    $('body').removeClass('page-on-load'); // remove page loading indicator
	                });

	                // handle errors
	                $rootScope.$on('$stateNotFound', function() {
	                    element.addClass('hide'); // hide spinner bar
	                });

	                // handle errors
	                $rootScope.$on('$stateChangeError', function() {
	                    element.addClass('hide'); // hide spinner bar
	                });
	            }
	        };
	    }
	])

	// Handle global LINK click
	directives.directive('a',
	    function() {
	        return {
	            restrict: 'E',
	            link: function(scope, elem, attrs) {
	                if (attrs.ngClick || attrs.href === '' || attrs.href === '#') {
	                    elem.on('click', function(e) {
	                        e.preventDefault(); // prevent link click for above criteria
	                    });
	                }
	            }
	        };
	    }
	);

	directives.directive('myTable', function() {
	  return function(scope, element, attrs) {

	    // apply DataTable options, use defaults if none specified by user
	    var options = {};
	    if (attrs.myTable.length > 0) {
	        options = scope.$eval(attrs.myTable);
	    } else {
	        options = {
	            "stateSave": true,
	            "stateDuration": 2419200, /* 1 month */
	            "jQueryUI": true,
	            "paging": false,
	            "lengthChange": false,
	            "searching": false,
	            "info": false,
	            "destroy": true
	        };
	    }

	    var explicitColumns = [];
	    element.find('th').each(function(index, elem) {
	        explicitColumns.push($(elem).text());
	    });
	    if (explicitColumns.length > 0) {
	        options["columns"] = explicitColumns;
	    } else if (attrs.columns) {
	        options["columns"] = scope.$eval(attrs.columns);
	    }

	    // aoColumnDefs is dataTables way of providing fine control over column config
	    if (attrs.columnDefs) {
	        options["columnDefs"] = scope.$eval(attrs.columnDefs);
	    }

	    // apply the plugin
	    var dataTable = element.DataTable(options);

	    // watch for any changes to our data, rebuild the DataTable
	    scope.$watch(attrs.data, function(value) {
	      console.log(value);
	      
	      var val = value || null;
	      if (val) {
	        dataTable.clear();
	        dataTable.rows.add(scope.$eval(attrs.data)).draw();
	      }
	    });
	  };
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
	                return !scope.passwordPolicy || scope.passwordPolicy[property] === null;
	            }

	            scope.$watch('passwordPolicy', model.$validate);

	            model.$validators.minLength = function(modelValue, viewValue) {
	                return notDefined('minLength') || !viewValue || viewValue.length >= scope.passwordPolicy.minLength;
	            };

	            model.$validators.maxLength = function(modelValue, viewValue) {
	                return notDefined('maxLength') || !viewValue || viewValue.length <= scope.passwordPolicy.maxLength;
	            };

	            model.$validators.minLowerCase = function(modelValue, viewValue) {
	                if (notDefined('minLowerCase') || !viewValue) return true;
	                var matches = viewValue.match(/[a-z]/g);
	                var count = matches ? matches.length : 0;
	                return count >= scope.passwordPolicy.minLowerCase;
	            };

	            model.$validators.minUpperCase = function(modelValue, viewValue) {
	                if (notDefined('minUpperCase') || !viewValue) return true;
	                var matches = viewValue.match(/[A-Z]/g);
	                var count = matches ? matches.length : 0;
	                return count >= scope.passwordPolicy.minUpperCase;
	            };

	            model.$validators.minDigit = function(modelValue, viewValue) {
	                if (notDefined('minDigit') || !viewValue) return true;
	                var matches = viewValue.match(/[0-9]/g);
	                var count = matches ? matches.length : 0;
	                return count >= scope.passwordPolicy.minDigit;
	            };

	            model.$validators.minSpecial = function(modelValue, viewValue) {
	                if (notDefined('minSpecial') || !viewValue) return true;
	                var matches = viewValue.match(/[^a-zA-Z0-9 ]/g);
	                var count = matches ? matches.length : 0;
	                return count >= scope.passwordPolicy.minSpecial;
	            };

	            model.$validators.allowWhitespace = function(modelValue, viewValue) {
	                return notDefined('allowWhitespace') || scope.passwordPolicy.allowWhitespace || !viewValue || viewValue.indexOf(' ') < 0;
	            }
	        }
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

	// Setup model for radio button in case of a value is object
	directives.directive('radioObjectModel', function() {
		return {
				restrict: 'A',
				//require: ['ngModel','ngValue'],
				scope: {
						ngModel: "=",
						ngValue: "="
				},
				link: function(scope) {
					if (angular.equals(scope.ngValue, scope.ngModel)) {                                
						scope.ngModel = scope.ngValue;
					}
				}
		};
});

/**
 * Directive for creating widget size icon from x,y size attributes
 */
directives.directive('widgetSizeIcon', function() {
    return {
        scope: {
					cols: '=',
					rows: '=',
					activeSize: '='
				},
				restrict: 'E',
				link: function (s, e, a) {
					s.$watch("rows", function () {
						if(s.rows){
							s.tRows = [];
							for (var i = 0; i < s.rows + 2; i++) {
								s.tRows.push({r: i, outer: (i === 0) || (i === s.rows + 1)});
							}
						}
					});
					s.$watch("cols", function () {
						if(s.cols){
							s.tCols = [];
							for (var i = 0; i < s.cols + 2; i++) {
								s.tCols.push({c: i, outer: (i === 0) || (i === s.cols + 1) });
							}
						}
					});
					s.$watch("activeSize", function () {
						if(s.activeSize){
							s.active = angular.equals(s.activeSize, { height: s.rows, width: s.cols });
						}
					});
				},
				template: '<table class="widget-size-table" ng-class="{active: active}"><tr ng-repeat="r in tRows"><td ng-repeat="c in tCols" ng-class="{outer: c.outer || r.outer}"></td></tr></table>'
    };
});