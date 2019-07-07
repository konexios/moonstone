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
