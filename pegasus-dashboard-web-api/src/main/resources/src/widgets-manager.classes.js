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
