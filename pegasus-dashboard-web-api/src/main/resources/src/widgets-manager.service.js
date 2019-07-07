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
