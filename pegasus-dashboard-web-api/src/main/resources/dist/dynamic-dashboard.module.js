(function() {
    'use strict';

    angular.module('widgets', []);
})();

angular.module('widgets').run(['$templateCache', function($templateCache) {$templateCache.put('/views/property-types/choiceKeyValueListString.html','<div class="row"><div class="col-xs-5"><label for="availableList">Available:</label> <select ng-model="vm.selectedAvailableItems" name="availableList" id="availableList" class="form-control" multiple="multiple" size="15"><option ng-value="device.key" ng-repeat="device in property.view.possibleValues | filter: vm.filterAvailable" title="{{device.value}}">{{device.value}}</option></select></div><div class="col-xs-2"><div class="arrows-group text-center"><div class="arrows-links-group"><a ng-click="vm.pushSelected()" class="arrow-link" title="Add selected"><span class="glyphicon glyphicon-circle-arrow-right"></span></a></div><div class="arrows-links-group"><a ng-click="vm.removeSelected()" class="arrow-link" title="Remove selected"><span class="glyphicon glyphicon-circle-arrow-left"></span></a></div></div></div><div class="col-xs-5"><label for="selectedList">Selected:</label> <select ng-model="vm.selectedSelectedItems" name="selectedList" id="selectedList" class="form-control" multiple="multiple" size="15"><option ng-value="device.key" ng-repeat="device in property.view.possibleValues | filter: vm.filterSelected" title="{{device.value}}">{{device.value}}</option></select></div></div>');
$templateCache.put('/views/property-types/choiceKeyValueString.html','<select class="form-control" ng-model="property.value" ng-options="x.key as x.value for x in property.view.possibleValues"></select>');
$templateCache.put('/views/property-types/choiceString.html','<select class="form-control" ng-model="property.value" ng-options="x for x in property.view.possibleValues"></select>');
$templateCache.put('/views/property-types/picUrlString.html','<!-- TODO: better using validation on whole page in configuration steps (framework) --><!-- in this case is not possible to put two properties on one view --><div class="form-group" ng-form="urlValidationForm" ng-class="{\'has-error\': urlValidationForm.url.$touched && !urlValidationForm.url.$valid}"><label class="control-label" for="url">URL (https):</label> <input type="url" name="url" class="form-control" ng-model="property.value" required ng-pattern="/^(https:\\/\\/){1}(www\\.){0,1}[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,5}[\\.]{0,1}/"><div class="help-block" ng-show="urlValidationForm.url.$touched && !urlValidationForm.url.$valid"><div ng-show="urlValidationForm.url.$error.required">Url required</div><div ng-show="urlValidationForm.url.$error.url || urlValidationForm.url.$error.pattern">Please enter correct https URL</div></div></div><div class="event-logo-preview" ng-if="!!property.value"><img ng-src="{{property.value}}"></div>');
$templateCache.put('/views/property-types/simpleBoolean.html','<label><input type="radio" ng-model="property.value" ng-value="true"> {{property.view.trueLabel}}</label><br><label><input type="radio" ng-model="property.value" ng-value="false"> {{property.view.falseLabel}}</label>');
$templateCache.put('/views/property-types/simpleInteger.html','<input type="number" class="form-control" ng-model="property.value" id="input" name="input">');
$templateCache.put('/views/property-types/simpleString.html','<input type="text" class="form-control" ng-model="property.value">');
$templateCache.put('/views/dashboard/widget-body/widget-body.tmpl.html','<div class="widget-panel" ng-class="{ \'edit-mode\': vm.editMode }"><!-- widget action icons --><div uib-dropdown dropdown-append-to-body="true" ng-repeat="widgetActionIcon in vm.widget.widgetActionIcons" ng-show="vm.editMode"><button type="button" id="{{widgetActionIcon.id}}" class="btn btn-link btn-xs" uib-dropdown-toggle><i ng-class="widgetActionIcon.iconClass"></i></button><ul class="dropdown-menu" uib-dropdown-menu aria-labelledby="{{widgetActionIcon.id}}"><li ng-repeat="menuItem in widgetActionIcon.menuItems"><a href ng-click="menuItem.action(vm.widget)">{{menuItem.name}}</a></li></ul></div></div><div class="widget-content"></div><div class="widget-moving-overlay" ng-if="vm.editMode"></div>');
$templateCache.put('/views/dashboard/widgets-dashboard/dashboard.tmpl.html','<!--h3>Dashboard</h3>\r\n<pre>{{vm.dashboard | json}}</pre--><div gridster="vm.options.gridster" ng-style="vm.gridStyles"><ul><li gridster-item="widgetPos" ng-repeat="widgetPos in vm.widgetsGrid.widgetPositions"><widget-body widget="vm.widgets[widgetPos.id]" ng-class="vm.widgets[widgetPos.id].directive" edit-mode="vm.editMode"></widget-body></li></ul></div><!--h3>Options</h3>\r\n<pre>{{vm.options | json}}</pre>\r\n<h3>Widgets</h3>\r\n<pre>{{vm.widgets | json}}</pre-->');
$templateCache.put('/views/widgets-types/widget-user/widget-user.tmpl.html','<h4>User info</h4><hr size="30"><p class="user-name">{{vm.user.name}}</p><p class="user-company">{{vm.user.company}}</p><p class="user-application">{{vm.user.application}}</p>');}]);
(function() {
    'use strict';

    angular.module('widgets').component('widgetUser', {
        // TODO: change path
        templateUrl: '/views/widgets-types/widget-user/widget-user.tmpl.html',
        controller: widgetUserController,
        controllerAs: 'vm',
        bindings: {
            widget: '='
        }
    });

    function widgetUserController($scope) {
        var vm = this;

        vm.user = { name: '-' };

        vm.$onInit = function() {
            vm.widget.subscribe('/user', function(user) {
                vm.user = JSON.parse(user);
                $scope.$apply();
            });
        };
    }
})();

(function() {
    'use strict';

    angular.module('widgets').component('dashboard', {
        // TODO: change path
        templateUrl: '/views/dashboard/widgets-dashboard/dashboard.tmpl.html',
        controller: dashboardController,
        controllerAs: 'vm',
        bindings: {
            options: '=',
            editMode: '=?'
        }
    });

    function dashboardController(WidgetOptions, WidgetsManagerService, $scope, $window) {
        var vm = this;

        vm.defaultBoardOptions = {
            gridster: WidgetOptions.gridster,
            dashboard: WidgetOptions.dashboard
        };
        vm.options = angular.merge({}, vm.defaultBoardOptions, vm.options);
        vm.dashboard = null;
        vm.widgets = {};
        vm.widgetsGrid = { boardSize: vm.options.gridster.columns, widgetPositions: [] };
        vm.gridStyles = vm.defaultBoardOptions.dashboard.backgroundGrid.getStyles(vm.editMode);

        /**
         * TODO document
         */
        vm.$onInit = function() {
            if (vm.options.boardId === undefined || vm.options.boardId === null) {
                throw 'Error: boardId is null or undefined. Put options="{ boardId: id }"';
            }

            WidgetsManagerService.onReady(registerBoard);
        };

        /**
         * TODO document
         */
        $scope.$watch('vm.editMode', function(newVal, oldVal) {
            vm.options.gridster.draggable.enabled = !!newVal;
            // update grid styles (show\hide grid)
            vm.gridStyles = vm.defaultBoardOptions.dashboard.backgroundGrid.getStyles(vm.editMode);
            // TODO: this should not be a part on watch event, something like specific success configuration event
            if (newVal == false && oldVal == true) {
                // update widget positions on close edit mode
                vm.dashboard.updatedLayout(vm.widgetsGrid);
            }
        });

        /**
         * TODO document
         */
        //        $scope.$watch('vm.widgetsGrid.widgetPositions', function(items) {
        //            if (vm.options.gridster.draggable.enabled) {
        //              	vm.dashboard.updatedLayout(vm.widgetsGrid);
        //            }
        //        }, true);

        /**
         * TODO document
         */
        function registerBoard() {
            WidgetsManagerService.registerBoard(vm.options.boardId, function(boardRuntimeInstance) {
                vm.dashboard = boardRuntimeInstance;
                vm.widgetsGrid.boardSize = boardRuntimeInstance.layout.columns;
                vm.options.gridster.columns = boardRuntimeInstance.layout.columns;

                // add widget handler
                vm.dashboard.subscribeOnAddWidget(function(widget) {
                    if (vm.options.widgetActionIcons) {
                        widget.widgetActionIcons = vm.options.widgetActionIcons;
                    }

                    var widgetPosition = {
                        id: widget.data.id,
                        sizeX: widget.data.layout.sizeX,
                        sizeY: widget.data.layout.sizeY,
                        row: widget.data.layout.row,
                        col: widget.data.layout.col
                    };
                    vm.widgets[widget.data.id] = widget;
                    vm.widgetsGrid.widgetPositions.push(widgetPosition);

                    $scope.$apply();
                });

                // update widget handler
                vm.dashboard.subscribeOnUpdateWidget(function(widget) {
                    vm.widgets[widget.id].data = widget;
                    var widgetPosition = {
                        id: widget.id,
                        sizeX: widget.layout.sizeX,
                        sizeY: widget.layout.sizeY,
                        row: widget.layout.row,
                        col: widget.layout.col
                    };

                    var i = 0;
                    for (i = vm.widgetsGrid.widgetPositions.length - 1; i >= 0; i--) {
                        if (vm.widgetsGrid.widgetPositions[i].id == widget.id) {
                            vm.widgetsGrid.widgetPositions[i] = widgetPosition;
                            break;
                        }
                    }

                    $scope.$apply();
                });

                // remove widget handler
                vm.dashboard.subscribeOnRemoveWidget(function(widgetId) {
                    delete vm.widgets[widgetId];
                    var i = 0;
                    for (i = vm.widgetsGrid.widgetPositions.length - 1; i >= 0; i--) {
                        if (vm.widgetsGrid.widgetPositions[i].id == widgetId) {
                            vm.widgetsGrid.widgetPositions.splice(i, 1);

                            break;
                        }
                    }

                    $scope.$apply();
                });

                if (vm.options.boardOnRedy) 
                	vm.options.boardOnRedy(vm.dashboard);

                // openBoard
                vm.dashboard.openBoard(vm.options.properties);

                $scope.$apply();
            });
        }

        // Section: dynamic column size approach

        // TODO: reimplement dynamic grid size

        // object for storing widgets size and positions for current grid column size
        var sizesStorage = { sizes: {} }; // e.g. currentSize: 6, sizes: { 7 : widgets[{},{}...] }

        if (vm.options.dashboard.dynamicGridSize) {
            var widgetWidth = parseInt(vm.options.gridster.colWidth);
            if (isNaN(widgetWidth)) {
                throw 'colWidth - Column width should be a number.';
            }

            angular.element($window).on('resize', onResize);
            onResize();
        }

        // ToDo: in this action should be included
        function onResize(event) {
            var oldColumns = vm.options.gridster.columns,
                newColumns = parseInt($window.innerWidth / vm.options.gridster.colWidth);
            // ToDo: design than widget size y is more than columns have
            // e.g. vm.options.gridster.sizeX = newColumns;
            var gridWasChanged = oldColumns != newColumns,
                gridIsGrow = newColumns > oldColumns;

            if (gridWasChanged) {
                sizesStorage.currentSize = newColumns;
                var widgetsSizes = [];
                vm.widgets.forEach(function(widget) {
                    // todo: store sizes in separeted widget object e.g. widget.gridSizes
                    // todo: refactoring, this is not a part of recalcSizeStorage
                    if (gridIsGrow) {
                        // restore old grid widget positions (if exest, note: size can be changed)
                        // if (sizesStorage.sizes[sizesStorage.currentSize]) {
                        //     sizesStorage.sizes[sizesStorage.currentSize].forEach(function(osWidget) {
                        //         if (osWidget.id === widget.data.id) {
                        //             widget.row = osWidget.row;
                        //             widget.col = osWidget.col;
                        //         }
                        //     });
                        // }
                    } else {
                        // fit widgets to grid columns
                        if (widget.col > newColumns - 1) {
                            widget.col = newColumns - 1;
                        }
                    }

                    widgetsSizes.push({ id: widget.data.id, row: widget.row, col: widget.col, sizeX: widget.sizeX, sizeY: widget.sizeY });
                });
                sizesStorage.sizes[sizesStorage.currentSize] = widgetsSizes;
            }

            vm.options.gridster.columns = newColumns;
        }

        /**
         * TODO document
         */
        function onDestroy() {
            if (vm.dashboard) {
                vm.dashboard.closeBoard();
                vm.dashboard = null;
            }
            $window.onbeforeunload = null; // Unregister event when the controller goes out of scope
        }

        $scope.$on('$destroy', onDestroy);
        $window.onbeforeunload = onDestroy;
    }
})();

(function() {
    'use strict';

    angular.module('widgets').factory('WidgetMenuFactory', WidgetMenuFactory);

    function WidgetMenuFactory() {
        return {
            getCommonMenu: getCommonMenu
        };

        function getCommonMenu(widgetId) {
            return {
                id: 'bodyMenu' + widgetId,
                items: [
                    {
                        name: 'Remove',
                        action: function(widget) {
                            widget.deleteMe();
                        }
                    }
                ]
            };
        }
    }
})();

(function() {
    'use strict';

    angular.module('widgets').component('widgetBody', {
        // TODO: change path
        templateUrl: '/views/dashboard/widget-body/widget-body.tmpl.html',
        controller: widgetBodyController,
        controllerAs: 'vm',
        bindings: {
            widget: '=',
            editMode: '=?'
        }
    });

    function widgetBodyController($scope, $compile, $element, $sce) {
        var vm = this;

        vm.$onInit = function() {
            // with watch we can change widget type\view dynamically
            $scope.$watch('vm.widget.directive', function() {
                var html = '<' + vm.widget.directive + ' widget="vm.widget"></' + vm.widget.directive + '>';
                $element.find('.widget-content').html($compile(html)($scope));
            });
        };
    }
})();

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

(function() {
    'use strict';

    angular.module('widgets').constant('WidgetOptions', {
        dashboard: {
            dynamicGridSize: false,
            // display background grid options
            backgroundGrid: {
                show: true,
                onEditModeOnly: true,
                sizes: {
                    margins: 15,
                    colWidth: 300
                },
                getStyles: function(editMode) {
                    // show grid always or only on edit mode if it enabled
                    return this.show && (!this.onEditModeOnly || this.onEditModeOnly && !!editMode) ? {
                        'background-image': 'linear-gradient(#bbbbbb 0px, transparent 1px), linear-gradient(90deg, #bbbbbb 0px, transparent 1px), linear-gradient(#e9e9e9 1px, transparent 0px), linear-gradient(90deg, #e9e9e9 1px, transparent 0px)',
                        'background-size': this.sizes.colWidth + 'px ' + this.sizes.colWidth + 'px, ' + this.sizes.colWidth + 'px ' + this.sizes.colWidth + 'px, ' + this.sizes.margins * 2 + 'px ' + this.sizes.margins * 2 + 'px, ' + this.sizes.margins * 2 + 'px ' + this.sizes.margins * 2 + 'px',
                        'background-position': '0px ' + this.sizes.margins / 2 + 'px, ' + this.sizes.margins / 2 + 'px 0px, 0px ' + this.sizes.margins / 2 + 'px, ' + this.sizes.margins / 2 + 'px 0px'
                    } : {};
                }
            }
        },

        gridster: {
            columns: 6, // the width of the grid, in columns
            pushing: true, // whether to push other items out of the way on move or resize
            floating: false, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
            swapping: false, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
            width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
            colWidth: 300, // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
            rowHeight: 'match', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
            margins: [15, 15], // the pixel distance between each widget
            outerMargin: true, // whether margins apply to outer edges of the grid
            isMobile: false, // stacks the grid items if true
            mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
            mobileModeEnabled: false, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
            minColumns: 1, // the minimum columns the grid must have
            minRows: 2, // the minimum height of the grid, in rows
            maxRows: 100,
            defaultSizeX: 1, // the default width of a gridster item, if not specifed
            defaultSizeY: 1, // the default height of a gridster item, if not specified
            minSizeX: 1, // minimum column width of an item
            maxSizeX: null, // maximum column width of an item
            minSizeY: 1, // minumum row height of an item
            maxSizeY: null, // maximum row height of an item
            resizable: {
                enabled: false,
                handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw']
            },
            draggable: {
                enabled: false // whether dragging items is supported
                //handle: '.my-class', // optional selector for drag handle
            }
        },

        urls: {}
    });
})();

(function() {
    'use strict';

    angular.module('widgets').factory('WidgetsManagerService', WidgetsManagerService);

    function WidgetsManagerService(WidgetsManagerClasses, $http) {
        var WMServiceInstance = {
            state: 'off', // off pending ready
            id: null
        };

        // web socket connecting to defined endpoint: UrlUtils.MESSAGE_ENDPOINT
        var webSocket = null;
        var stompClient = null;
        var onReadyHandlers = [];

        var previouslyRegisteredBoard = null;
        
        WMServiceInstance.onReady = function(handler) {

        	var start = new Date();
        	
        	
            if (WMServiceInstance.state == 'ready') {
                handler();
            }

            if (WMServiceInstance.state == 'off') {
                WMServiceInstance.state = 'pending';

                initialize(function() {
                    WMServiceInstance.state = 'ready';
                    // run all onReady call backs
                    while (onReadyHandlers.length > 0) {
                        var hendler = onReadyHandlers.shift();
                        hendler();
                    }
                });
            }

            onReadyHandlers.push(handler);
            
        	var end = new Date();
        	var duration = end - start; //milliseconds interval
        	console.log("WMServiceInstance.onReady| duration: " + duration);
        };

        /**
         * TODO document
         */
        function initialize(callback) {
            
        	var start = new Date();
        	
        	webSocket = new SockJS('/endpoint');
            stompClient = Stomp.over(webSocket);

            stompClient.connect({}, function(frame) {
                // subscribe to CoreBoardRuntimeController

                /**
                 * TODO document
                 */
//                stompClient.subscribe('/topic/acs/dd/runtime/boards/registered', function(response) {
//                    var registerBoardResponse = JSON.parse(response.body);
//                    
//                    console.log("WMServiceInstance.initialize|registered| activeBoardId: " + activeBoardId + " registerBoardResponse.boardId: " + registerBoardResponse.boardId + " same? " + (registerBoardResponse.boardId === activeBoardId));
//                    
//                    if (registerBoardResponse.boardId === activeBoardId) {
//                        var handler = activeBoardRegisterCallback;
//
//                        var boardRuntimeInstance = WidgetsManagerClasses.createBoardRuntimeInstance(registerBoardResponse, WMServiceInstance);
//                        boardRuntimeInstance.init(function() {
//                            handler(boardRuntimeInstance);
//                        });
//                    } else {
//                        // do nothing, received message that is not the
//                        // actors active board
//                    }
//                });
            	
                /**
                 * TODO document
                 */
//                stompClient.subscribe('/topic/acs/dd/runtime/boards/opened', function(response) {
//                    var openBoardResponse = JSON.parse(response.body);
//                    
//                    console.log("WMServiceInstance.initialize|opened| activeBoardId: " + activeBoardId + " openBoardResponse.boardId: " + openBoardResponse.boardId + " same? " + (openBoardResponse.boardId === activeBoardId));
//                    
//                    if (openBoardResponse.boardRuntimeId === activeBoardId) {
//                        // may need to do something...
//                    } else {
//                        // do nothing, received message that is not the
//                        // actors active board
//                    }
//                });

                /**
                 * TODO document
                 */
//                stompClient.subscribe('/topic/acs/dd/runtime/boards/closed', function(response) {
//                    var closeBoardResponse = JSON.parse(response.body);
//                    
//                    console.log("WMServiceInstance.initialize|opened| activeBoardId: " + activeBoardId + " closeBoardResponse.boardId: " + closeBoardResponse.boardId + " same? " + (closeBoardResponse.boardId === activeBoardId));
//                    
//                    if (closeBoardResponse.boardRuntimeId === activeBoardId) {
//                        WMServiceInstance.unregisterBoard(closeBoardResponse.boardRuntimeId);
//                    } else {
//                        // do nothing, received message that is not the
//                        // actors active board
//                    }
//                });

                /**
                 * TODO document
                 */
//                stompClient.subscribe('/topic/acs/dd/runtime/boards/unregistered', function(response) {
//                    var unregisteredBoardResponse = JSON.parse(response.body);
//                    if (unregisteredBoardResponse.boardId === activeBoardId) {
//                        // may need to do something...
//                    } else {
//                        // do nothing, received message that is not the
//                        // actors active board
//                    }
//                });

                callback();
            });
            
            
        	var end = new Date();
        	var duration = end - start; //milliseconds interval
        	console.log("WMServiceInstance.initialize| duration: " + duration);
        }

        // CoreRuntimeBoardController

        var activeBoardId = null;
        var activeBoardRegisterCallback = null;

        /**
         * registerBoard
         *
         * TODO document
         */
        WMServiceInstance.registerBoard = function(boardId, callback) {
        	
        	var start = new Date();
        	
//        	activeBoardId = boardId;
//        	activeBoardRegisterCallback = callback;
        	
        	// request
            var registerBoardRequest = { boardId: boardId };
//            stompClient.send('/app/acs/dd/runtime/boards/register', {}, JSON.stringify(registerBoardRequest));
            
            $http
	            .post('/acs/dd/runtime/boards/register', registerBoardRequest)
	            .then(function(response) {
	                var registerBoardResponse = response.data;
	                
	            	activeBoardId = registerBoardResponse.boardRuntimeId;
	                
                    var boardRuntimeInstance = WidgetsManagerClasses.createBoardRuntimeInstance(registerBoardResponse, WMServiceInstance);
                    boardRuntimeInstance.init(function() {
                    	callback(boardRuntimeInstance);
                    });
	            })
	            .catch(function(response) {
	            });
            
        	var end = new Date();
        	var duration = end - start; //milliseconds interval
        	console.log("WMServiceInstance.registerBoard| duration: " + duration);
        };

        /**
         * openBoard
         *
         * TODO document
         */
        WMServiceInstance.openBoard = function(boardRuntimeId) {
            
        	var start = new Date();
        	
        	// request
            var openBoardRequest = { boardRuntimeId: boardRuntimeId };

            stompClient.send('/app/acs/dd/runtime/boards/open', {}, JSON.stringify(openBoardRequest));

        	var end = new Date();
        	var duration = end - start; //milliseconds interval
        	console.log("WMServiceInstance.openBoard| duration: " + duration);
        };
        
        /**
         * openBoard with configuration patch.
         * patch for now: key - value pairs
         *
         * TODO document
         */
        WMServiceInstance.openBoardWithPatch = function(boardRuntimeId, properties) {
        	
        	var start = new Date();
        	
            // request
            var openBoardRequest = { 
            		boardRuntimeId: boardRuntimeId,
            		configurationPatch: {
            			properties: properties
            		}
            }

            stompClient.send('/app/acs/dd/runtime/boards/open', {}, JSON.stringify(openBoardRequest));

        	var end = new Date();
        	var duration = end - start; //milliseconds interval
        	console.log("WMServiceInstance.openBoardWithPatch| duration: " + duration);
        };
        
        
        /**
         * closeBoard
         *
         * TODO document
         */
        WMServiceInstance.closeBoard = function(boardRuntimeId) {
            
        	var start = new Date();
        	
        	// request
            var closeBoardRequest = { boardRuntimeId: boardRuntimeId };

            // stompClient.send('/app/acs/dd/runtime/boards/close', {}, JSON.stringify(closeBoardRequest));
            
            $http
            .post('/acs/dd/runtime/boards/close', closeBoardRequest)
            .then(function(response) {
                var closeBoardResponse = response.data;
                
                WMServiceInstance.unregisterBoard(closeBoardResponse.boardRuntimeId);
            })
            .catch(function(response) {
            });
            
        	var end = new Date();
        	var duration = end - start; //milliseconds interval
        	console.log("WMServiceInstance.closeBoard| duration: " + duration);
        };

        /**
         * unregisterBoard
         *
         * TODO document
         */
        WMServiceInstance.unregisterBoard = function(boardRuntimeId) {
            
        	var start = new Date();
        	
        	// request
            var unregisterBoardRequest = { boardRuntimeId: boardRuntimeId };

            stompClient.send('/app/acs/dd/runtime/boards/unregister', {}, JSON.stringify(unregisterBoardRequest));
            
        	var end = new Date();
        	var duration = end - start; //milliseconds interval
        	console.log("WMServiceInstance.unregisterBoard| duration: " + duration);
        };

        // CoreRuntimeWidgetController

        /**
         * newWidget
         *
         * method to handle the actor requesting to add a new widget
         */
        WMServiceInstance.newWidget = function(widgetTypeId, handler) {
            $http
                .get('/acs/dd/runtime/boards/generate/board/runtime/id', {})
                .then(function(response) {
                    // TODO rename boardRuntimeId to parentRuntimeId
                    WMServiceInstance.newWidgetOnBoard(response.data.boardRuntimeId, widgetTypeId, handler);
                })
                .catch(function(response) {
                });
        };

        /**
         * TODO document
         */
        WMServiceInstance.newWidgetOnBoard = function(parentRuntimeId, widgetTypeId, handler) {
            var registerBoardResponse = {
                boardRuntimeId: parentRuntimeId,
                boardId: parentRuntimeId,
                boardName: 'New Widget Board',
                boardDescription: 'A temporary board that allows the creation of a new widget'
            };

            var boardRuntimeInstance = WidgetsManagerClasses.createBoardRuntimeInstance(registerBoardResponse, WMServiceInstance);

            var addWidgetCallback = function(widget) {
                handler(widget);
            };

            // set handler
            boardRuntimeInstance.subscribeOnAddWidget(addWidgetCallback);

            var initCallback = function() {
                var newWidgetRequest = {
                    widgetTypeId: widgetTypeId,
                    generatedParentRuntimeId: parentRuntimeId
                };

                stompClient.send('/app/acs/dd/runtime/boards/widgets/new', {}, JSON.stringify(newWidgetRequest));
            };

            // initialize
            boardRuntimeInstance.init(initCallback);
        };

        /**
         * cancelNewWidget
         *
         * method to handle if the actor cancels the creation of a new widget
         */
        WMServiceInstance.cancelNewWidget = function(widget) {
            var cancelNewWidgetRequest = {
                widgetRuntimeId: widget.data.id
            };

            stompClient.send('/app/acs/dd/runtime/boards/widgets/cancel', {}, JSON.stringify(cancelNewWidgetRequest));
        };

        /**
         * registerWidgetToParent
         *
         * TODO document
         */
        WMServiceInstance.registerWidgetToParent = function(parentRuntimeId, parentId, widget, widgetMetaData, configuration) {
            var registerWidgetToParentRequest = {
                parentRuntimeId: parentRuntimeId,
                parentId: parentId,
                widgetRuntimeId: widget.data.id,
                widgetMetaData: widgetMetaData,
                configuration: configuration
            };

            // after this request, target board will got this widget
            stompClient.send('/app/acs/dd/runtime/boards/widgets/register', {}, JSON.stringify(registerWidgetToParentRequest));

            // no callback with status right now
            // if any error - target board will(should) receive it
        };

        return WMServiceInstance;
    }
})();

(function() {
    'use strict';

    angular.module('widgets').factory('WidgetsManagerClasses', WidgetsManagerClasses);

    function WidgetsManagerClasses() {
        return {
            createBoardRuntimeInstance: createBoardRuntimeInstance,
            createContainer: createContainerRuntimeInstance,
            createWidget: createWidgetRuntimeInstance
        };

        function createBoardRuntimeInstance(boardRuntimeInstance, WMServiceInstance) {
            return new BoardRuntimeInstance(boardRuntimeInstance, WMServiceInstance);
        }

        function createWidgetRuntimeInstance(widgetRuntimeInstance, boardRuntimeInstance) {
            return new WidgetRuntimeInstance(widgetRuntimeInstance, boardRuntimeInstance);
        }

        function createContainerRuntimeInstance(containerRuntimeInstance) {
            return new ContainerRuntimeInstance(containerRuntimeInstance);
        }


        /**
         * BoardRuntimeInstance
         *
         * TODO document
         */
        function BoardRuntimeInstance(boardRuntimeInstance, WMServiceInstance) {
            var instance = boardRuntimeInstance;
            var wms = WMServiceInstance;

            // widget handlers
            var addWidgetHandler;
            var updateWidgetHandler;
            var removeWidgetHandler;
            var subscriptions = [];
            var widgets = [];

            // socket to communicate
            var webSocket = new SockJS('/endpoint');
            var stompClient = Stomp.over(webSocket);
            
            instance.stompClient = stompClient; // saving link on stomp client instance for reusing sockets in witgets level
            this.boardRuntimeId = boardRuntimeInstance.boardRuntimeId;
            this.boardId = boardRuntimeInstance.boardId;
            this.name = boardRuntimeInstance.boardName;
            this.description = boardRuntimeInstance.boardDescription;
            this.layout = boardRuntimeInstance.boardLayout;


            this.init = function(callback) {
                // NOTE: check stompClient on existing connection state, can it be already connected?
                stompClient.connect({}, function(frame) {
                    /**
                     * TODO document
                     */
                    subscriptions[subscriptions.length] = stompClient.subscribe('/topic/acs/dd/runtime/boards/' + instance.boardRuntimeId + '/widgets/add', function(response) {
                        console.log("**** WIDGET ADDED TO BOARD " + instance.boardRuntimeId + " ****");

                        var widgetRuntimeInstance = JSON.parse(response.body);
                        if (addWidgetHandler !== undefined) {
                            // create widget runtime instance
                            var widget = new WidgetRuntimeInstance(widgetRuntimeInstance, instance);

                            // store widget
                            widgets[widgets.length] = widget;

                            // initialize widget
                            widget.init(function() {
                                addWidgetHandler(widget);
                            });
                        }
                    });

                    /**
                     * TODO document
                     */
                    subscriptions[subscriptions.length] = stompClient.subscribe('/topic/acs/dd/runtime/boards/' + instance.boardRuntimeId + '/widgets/remove', function(response) {
                        console.log("**** WIDGET REMOVED FROM BOARD " + instance.boardRuntimeId + " ****");
                        var widgetRuntimeId = response.body;

                        for (var i = 0; i < widgets.length; i++) {
                            var widget = widgets[i];
                            if (widget.data.id == widgetRuntimeId) {
                                // close widget
                                widget.destroyWidget();

                                // remove widget
                                widgets.splice(i, 1);
                                break;
                            }
                        }

                        if (removeWidgetHandler !== undefined) removeWidgetHandler(widgetRuntimeId);
                    });

                    /**
                     * TODO document
                     */
                    subscriptions[subscriptions.length] = stompClient.subscribe('/topic/acs/dd/runtime/boards/' + instance.boardRuntimeId + '/widgets/update', function(response) {
                        console.log("**** WIDGET UPDATED ON BOARD " + instance.boardRuntimeId + " ****");

                        var widgetRuntimeInstance = JSON.parse(response.body);
                        if (updateWidgetHandler !== undefined) {
                            updateWidgetHandler(widgetRuntimeInstance);
                        }
                    });

                    // call the callback
                    callback();
                });
            };

            /**
             * TODO document
             */
            this.openBoard = function(patchProperties) {
            	if (patchProperties === undefined) {
            		wms.openBoard(this.boardRuntimeId);
            	} else {
                    wms.openBoardWithPatch(this.boardRuntimeId, patchProperties);
            	}
            };

            /**
             * TODO document
             */
            this.updatedLayout = function(layout) {
                stompClient.send('/app/acs/dd/runtime/boards/' + instance.boardRuntimeId + '/layout/update', {}, JSON.stringify(layout));
            };

            /**
             * TODO document
             */
            this.closeBoard = function() {
                // close widgets
                for (var i = 0; i < widgets.length; i++) 
                	widgets[i].destroyWidget();

                // unsubscribe
                for (var i = 0; i < subscriptions.length; i++) 
                	subscriptions[i].unsubscribe();

                wms.closeBoard(this.boardRuntimeId);
                //wms.unregisterBoard(this.boardRuntimeId);
                
                webSocket.close();
            };

            /**
             * TODO document
             */
            this.subscribeOnAddWidget = function(handler) {
                addWidgetHandler = handler;
            };

            /**
             * TODO document
             */
            this.subscribeOnUpdateWidget = function(handler) {
                updateWidgetHandler = handler;
            };

            /**
             * TODO document
             */
            this.subscribeOnRemoveWidget = function(handler) {
                removeWidgetHandler = handler;
            };
        }


        /**
         * WidgetRuntimeInstance
         *
         * TODO document
         */
        function WidgetRuntimeInstance(widgetRuntimeInstance, boardRuntimeInstance) {
            // this part for front end widget
            this.directive = widgetRuntimeInstance.directive;
            this.data = widgetRuntimeInstance;
            this.metadata = widgetRuntimeInstance.widgetMetaData;

            // using of one stomp client for board and widget instances
            var stompClient = boardRuntimeInstance.stompClient;
            var subscriptions = [];

            // callbacks for subscriptions
            var subscriptionsMap = {};
            var configurationTopicHandlers = {};
            var metaDataTopicHandler;
            var instance = this;

            this.init = function(callback) {
                angular.forEach(widgetRuntimeInstance.topicProviders, function(url, topic) {
                    subscriptions[subscriptions.length] = stompClient.subscribe(url, function(response) {
                        var handler = subscriptionsMap[topic];
                        if (handler !== undefined) {
                            handler(response.body);
                        }
                    });
                });

                subscriptions[subscriptions.length] = stompClient.subscribe(widgetRuntimeInstance.configurationTopic, function(response) {
                    var handler = configurationTopicHandlers['config'];
                    handler(response.body);
                });

                subscriptions[subscriptions.length] = stompClient.subscribe(widgetRuntimeInstance.fastConfigurationTopic, function(response) {
                    var handler = configurationTopicHandlers['fastConfig'];
                    handler(response.body);
                });

                subscriptions[subscriptions.length] = stompClient.subscribe(widgetRuntimeInstance.metaDataUpdateTopic, function(response) {
                    instance.metadata = JSON.parse(response.body);
                    if (metaDataTopicHandler !== undefined) {
                        metaDataTopicHandler();
                    }

                    if (instance.myMetaDataTopicHandler !== undefined) {
                        instance.myMetaDataTopicHandler(instance.metadata);
                    }
                });

                callback();
            };

            this.mySubscriptions = subscriptionsMap;
            this.mystompClient = stompClient;
            this.myMetaDataTopicHandler = metaDataTopicHandler;
            this.myConfigurationTopicHandlers = configurationTopicHandlers;

            /**
             * TODO document
             */
            this.subscribe = function(topic, handler) {
                this.mySubscriptions[topic] = handler;
            };

            /**
             * TODO document
             */
            this.subscribeForConfigurationTopic = function(handler) {
                this.myConfigurationTopicHandlers['config'] = handler;
            };

            /**
             * TODO document
             */
            this.subscribeForFastConfigurationTopic = function(handler) {
                this.myConfigurationTopicHandlers['fastConfig'] = handler;
            };

            /**
             * TODO document
             */
            this.subscribeForMetaDataUpdate = function(handler) {
                this.myMetaDataTopicHandler = handler;
            };

            // calls for methods
            this.send = function(endpoint, parameter) {
                var url = this.data.messageEndpoints[endpoint];
                if (parameter == null) {
                    this.mystompClient.send(url, {}, null);
                } else {
                    var param = JSON.stringify(parameter);
                    this.mystompClient.send(url, {}, param);
                }
            };

            /**
             * TODO document
             */
            this.sendToConfigurationInit = function() {
                var url = this.data.configurationInit;
                this.mystompClient.send(url, {}, null);
            };

            /**
             * TODO revisit, we need two objects, one for full config
             * and a second to hold values only
             *
             * hack to prevent full configuration data being sent to server
             *
             * TODO document
             */
            this.processConfigurationBeforeSend = function(configuration) {
                for (var i = 0; i < configuration.pages.length; i++) {
                    var page = configuration.pages[i];

                    for (var p = 0; p < page.properties.length; p++) {
                        var property = page.properties[p];
                        var innerProperty = property.property;
                        if (innerProperty != null && innerProperty != undefined) {
                            var view = innerProperty.view;
                            if (view != null && view != undefined) {
                                if (innerProperty.viewId === 'choice-Key-Value-String' || innerProperty.viewId === 'choice-String') {
                                    // empty the possibleValues array
                                    view.possibleValues = [];
                                }
                            }
                        }
                    }
                }

                return configuration;
            };

            /**
             * TODO document
             */
            this.sendToConfigurationProcess = function(configuration) {
                configuration = this.processConfigurationBeforeSend(configuration);

                var url = this.data.configurationProcess;
                var param = JSON.stringify(configuration);
                this.mystompClient.send(url, {}, param);
            };

            /**
             * TODO document
             */
            this.sendToConfigurationSave = function(configuration) {
                configuration = this.processConfigurationBeforeSend(configuration);

                var url = this.data.saveConfiguration;
                var param = JSON.stringify(configuration);
                this.mystompClient.send(url, {}, param);
            };

            /**
             * TODO document
             */
            this.sendToFastConfigurationInit = function() {
                var url = this.data.fastConfigurationInit;
                this.mystompClient.send(url, {}, null);
            };

            /**
             * TODO document
             */
            this.sendToFastConfigurationProcess = function(sockParam, parameter) {
                var url = this.data.fastConfigurationProcess;
                var param = JSON.stringify(parameter);
                this.mystompClient.send(url, sockParam, param);
            };

            /**
             * TODO document
             */
            this.editWidget = function() {
                var url = this.data.editWidgetUrl;
                this.mystompClient.send(url, {}, null);
            };

            /**
             * TODO document
             */
            this.updateWidget = function(widgetMetaData, configuration) {
                configuration = this.processConfigurationBeforeSend(configuration);

                var url = this.data.updateWidgetUrl;
                var updateWidgetRequest = {
                    widgetMetaData: widgetMetaData,
                    configuration: configuration
                };
                var param = JSON.stringify(updateWidgetRequest);
                this.mystompClient.send(url, {}, param);
            };

            /**
             * TODO document
             */
            this.cancelEditWidget = function() {
                var url = this.data.cancelEditWidgetUrl;
                this.mystompClient.send(url, {}, null);
            };

            /**
             * TODO document
             */
            this.deleteWidget = function() {
                var removeWidgetRequest = {
                    widgetRuntimeId: this.data.id
                };

                var url = this.data.deleteUrl;
                this.mystompClient.send(url, {}, JSON.stringify(removeWidgetRequest));
            };

            /**
             * TODO document
             */
            this.refreshMetaData = function() {
                var url = this.data.metaDataUpdate;
                this.mystompClient.send(url, {}, null);
            };

            /**
             * TODO document
             */
            this.updateMetaData = function(metadata) {
                var url = this.data.metaDataUpdate;
                var param = JSON.stringify(metadata);
                this.mystompClient.send(url, {}, param);
            };

            this.destroyWidget = function() {
                // unsubscribe
                for (var i = 0; i < subscriptions.length; i++) subscriptions[i].unsubscribe();
            };
        }


        /**
         * ContainerRuntimeInstance
         *
         * TODO document
         */
        function ContainerRuntimeInstance(containerRuntimeInstance) {
            this.init = function(callback) {
                // TODO
            };
        }
    }
})();
