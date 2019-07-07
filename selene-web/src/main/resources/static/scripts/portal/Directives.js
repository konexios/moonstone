directives.directive("name", function() {
	return {
		scope : {},
		restrict : "E",
		replace : true,
		link : function(s, e, a) {
			s.fullName = a.first + " " + a.last;
		},
		template : "<h1>Hello {{fullName}}!!!</h1>"
	}
});

directives.directive("arrowItemsPerPageDropdown", function() {
	return {
		restrict : "E",
		replace : true,
		scope : {
			itemsPerPage : "=",
			totalItems : "=",
			change : "&"
		},
		link : function(scope, element, attributes) {
			scope.itemsPerPageOptions = [];
			scope.itemsPerPageOptions.push(10);

			scope.$watch("totalItems", function() {
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
		templateUrl : "/scripts/portal/tpl/ItemsPerPageDropdown.html"
	}
});

directives
		.directive(
				"arrowPagination",
				function() {
					return {
						restrict : "E",
						replace : true,
						scope : {
							pageIndex : "=",
							totalPages : "=",
							first : "=",
							last : "=",
							previous : "&",
							next : "&",
							go : "&"
						},
						link : function(scope, element, attributes) {
							// set up pagination
							scope.pages = [];

							var calculateRange = function(boundry, fixedUnits,
									firstPageIndex, lastPageIndex,
									currentPageIndex, lastPageNumber,
									diffFromFirstPageIndex,
									diffFromLastPageIndex) {

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
									// shall return a dynamic number of items
									// based on first and last
									targetStart = (first + 1);
									targetEnd = (last - 1);
								} else if (currentPageIndex >= boundry
										&& diffFromLast >= boundry
										&& numberOfPages > minLimit) {
									// shall always be 3
									targetStart = (currentPageIndex - 1);
									targetEnd = (currentPageIndex + 1);
								} else if (targetStart == first
										|| diffFromFirst < boundry) {
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
									if (targetStart > first
											&& targetStart < last) {
										list.push(targetStart);
										count++;
									}
									targetStart++;
								}

								return list;
							};

							var updatePagination = function() {

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
								var boundPageIndexList = calculateRange(
										pageBoundry, fixedUnits,
										firstPageIndex, lastPageIndex,
										scope.pageIndex, lastPageNumber,
										diffFromFirstPageIndex,
										diffFromLastPageIndex);

								scope.pages = [];
								if (scope.totalPages > 0) {

									// first page
									if (scope.totalPages > 1) {
										scope.pages.push({
											label : 1,
											value : 0,
											disabled : false
										});
									} else {
										scope.pages.push({
											label : "1/1",
											value : null,
											disabled : true
										});
									}

									// dots
									if (numberOfPages > minLimit
											&& scope.pageIndex >= pageBoundry) {
										scope.pages.push({
											label : "...",
											value : null,
											disabled : true
										});
									}

									// pages
									for (var i = 0; i < boundPageIndexList.length; i++) {
										var item = boundPageIndexList[i];
										scope.pages.push({
											label : item + 1,
											value : (item),
											disabled : false
										});
									}

									// dots
									if (numberOfPages > minLimit
											&& diffFromLastPageIndex >= pageBoundry) {
										scope.pages.push({
											label : "...",
											value : null,
											disabled : true
										});
									}

									// last page
									if (scope.totalPages > 1) {
										scope.pages.push({
											label : scope.totalPages,
											value : (scope.totalPages - 1),
											disabled : false
										});
									}
								}
							};

							scope.$watch("pageIndex", function() {
								updatePagination();
							});

							scope.$watch("totalPages", function() {
								updatePagination();
							});
						},
						templateUrl : "/scripts/portal/tpl/Pagination.html"
					}
				});

directives
		.directive(
				"arrowShowingItems",
				function() {
					return {
						restrict : "E",
						replace : true,
						scope : {
							pageIndex : "=",
							itemsPerPage : "=",
							totalItems : "=",
							topic : "@",
							topicPlural : "@"
						},
						link : function(scope, element, attributes) {

							scope.topicValue = scope.topic;

							var calculate = function() {
								scope.topicValue = scope.topic;
								if (scope.totalItems > 1)
									scope.topicValue = scope.topicPlural;

								scope.showingFrom = (scope.totalItems > 0 ? (scope.pageIndex * scope.itemsPerPage) + 1
										: 0);
								var to = (scope.pageIndex + 1)
										* scope.itemsPerPage;
								if (to > scope.totalItems)
									to = scope.totalItems;
								scope.showingTo = (scope.totalItems > 0 ? to
										: 0);
							};

							scope.$watch("pageIndex", function() {
								calculate();
							});

							scope.$watch("itemsPerPage", function() {
								calculate();
							});

							scope.$watch("totalItems", function() {
								calculate();
							});
						},
						templateUrl : "/scripts/portal/tpl/ShowingItems.html"
					}
				});

directives.directive("arrowColumnHeader", function() {
	return {
		restrict : "AC",
		replace : true,
		scope : {
			label : "=",
			value : "=",
			sortField : "=",
			sortDirection : "=",
			sortable : "=",
			sort : "&"
		},
		link : function(scope, element, attributes) {
			// do nothing
		},
		templateUrl : "/scripts/portal/tpl/ColumnHeader.html"
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

directives.directive('fileDropzone', function() {
return {
	      restrict: 'A',
	      scope: {
	        file: '=',
	        fileName: '=',
	        showFlag: '='
	      },
	      link: function(scope, element, attrs) {
	        var processDragOverOrEnter;
	        processDragOverOrEnter = function(event) {
	          if (event != null) {
	            event.preventDefault();
	          }
	          event.originalEvent.dataTransfer.effectAllowed = 'copy';
	          return false;
	        };
	        
	        element.bind('dragover', processDragOverOrEnter);
	        element.bind('dragenter', processDragOverOrEnter);
	        
	        return element.bind('drop', function(event) {
	        	console.log(event)
	          var file, name, reader, size, type;
	          if (event != null) {
	            event.preventDefault();
	          }
	          reader = new FileReader();
	          file = event.originalEvent.dataTransfer.files[0];
	          name = file.name;
	          type = file.type;
	          size = file.size;
	         console.log(size)
	        
	          reader.onload = function(evt) {
	        	  console.log($('#viewclick'))
	            	$('#viewclick').click();
	              return scope.$apply(function() {
	               // scope.file = evt.target.result;
	            	scope.file= reader.result;
	            	scope.showFlag = true
	               // console.log(reader.result)
	                if (angular.isString(scope.fileName)) {
	                	scope.fileName = name
	                	//console.log(scope.fileName)
	                	$('#fileDiv span').text(scope.fileName)
	                	console.log(scope.file)
	                  return scope.fileName = name;
	                }
	            	
	            	
	                return scope.file;
	              });
	         
	          };
	          
	          reader.readAsArrayBuffer(file);
	          return false;
	        });
	      }
	    };
});

directives.directive("fileread", [function () {
    return {
        scope: {
        	fileread: "="
        },
        link: function (scope, element, attributes) {
            element.bind("change", function (changeEvent) {
                var reader = new FileReader();
                reader.onload = function (loadEvent) {
                	console.log("---")
                    scope.$apply(function () {
                        scope.fileread = loadEvent.target.result;
                        
                    });
                }
                reader.readAsArrayBuffer(changeEvent.target.files[0]);
            });
        }
    }
}]);
