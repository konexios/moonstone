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
