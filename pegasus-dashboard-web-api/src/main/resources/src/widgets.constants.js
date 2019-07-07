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
