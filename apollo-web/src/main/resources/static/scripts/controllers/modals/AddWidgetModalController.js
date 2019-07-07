controllers.controller('AddWidgetModalController', [
    '$scope', '$uibModalInstance', 'SpinnerService', 'ToastrService', 'SecurityService', 'ErrorService', 'BoardService', 'ApolloBoardRuntimeService', 'WidgetsManagerService', 'boardId', 'boardRuntimeId', 'widget',
    function($scope, $uibModalInstance, SpinnerService, ToastrService, SecurityService, ErrorService, BoardService, ApolloBoardRuntimeService, WidgetsManagerService, boardId, boardRuntimeId, widget) {
        var vm = this;

        vm.isEdit = !!widget;
        vm.wizard = { step: vm.isEdit ? 1 : 0 }; // for widget Edit - skip widget type selection page
        vm.widgetSizes = {
            small: { name: 'Small', field: 'small', order: 1 },
            medium: { name: 'Medium', field: 'medium', order: 2 },
            large: { name: 'Large', field: 'large', order: 3 },
            xtraLarge: { name: 'Xtra-Large', field: 'xtraLarge', order: 4 }
        };
        vm.widgetTypeData = {
            tab: 'Device',
            widgetCounts: {},
            widgets: [],
            selected: null,
            loadingWidgets: true
        };

        vm.widgetConfiguration = null;
        vm.configuredWidget = widget || null;
        vm.addWidgetModel = {
        	size: {}
        };
        vm.supportedWidgetSizes = [];


        init();

        /**
         * TODO document
         */
        function init() {
            if (vm.isEdit) {
                vm.addWidgetModel.name = widget.metadata.name;
                vm.addWidgetModel.description = widget.metadata.description;
                vm.addWidgetModel.size.width = widget.data.layout.sizeX || 1;
                vm.addWidgetModel.size.height = widget.data.layout.sizeY || 1;
        		vm.addWidgetModel.col = widget.data.layout.col || 0;
        		vm.addWidgetModel.row = widget.data.layout.row || 0;
        		
        		loadWidgetTypeSizes(widget.data.widgetTypeId);
                
                SpinnerService.show();
                widget.subscribeForConfigurationTopic(widgetConfigurationUpdated);
                //widget.sendToConfigurationInit();
                widget.editWidget();
            } else {
                loadWidgetCounts();
                loadWidgets();
            }
        }

        /**
         * TODO document
         */
        vm.selectWidgetItem = function(widgetItem) {
            vm.addWidgetModel.type = widgetItem.widgetTypeId;
            vm.supportedWidgetSizes = widgetItem.supportedSizes;
            vm.widgetTypeData.selected = widgetItem.id;
        };

        /**
         * TODO document
         */
        vm.changeWidgetCategoryTab = function(tab) {
            vm.widgetTypeData.tab = tab;
            vm.addWidgetModel.type = null;
            vm.widgetTypeData.selected = null;
            loadWidgets();
        };

        /**
         * TODO document
         */
        function loadWidgetCounts() {
            SpinnerService.wrap(ApolloBoardRuntimeService.getWidgetTypeCounts)
                .then(function(response) {
                    vm.widgetTypeData.widgetCounts = response.data;
                })
                .catch(ErrorService.handleHttpError);
        }

        /**
         * This method loads available widget types for user selection.
         */
        function loadWidgets() {
            vm.widgetTypeData.loadingWidgets = true;
            SpinnerService.wrap(ApolloBoardRuntimeService.getWidgetTypes, vm.widgetTypeData.tab)
                .then(function(response) {
                    vm.widgetTypeData.widgets = response.data;
                    vm.widgetTypeData.loadingWidgets = false;
                })
                .catch(ErrorService.handleHttpError);
        }
        
        /**
         * TODO document
         * 
         * @param widgetTypeId
         * @returns
         */
        function loadWidgetTypeSizes(widgetTypeId) {
            SpinnerService.wrap(ApolloBoardRuntimeService.getWidgetTypeSizes, widgetTypeId)
                .then(function(response) {
                	vm.supportedWidgetSizes = response.data;
                })
                .catch(ErrorService.handleHttpError);
        }

        /**
         * TODO document
         */
        vm.goNext = function() {
            if (vm.wizard.step === 3) {
                saveWidget();
            } else if (vm.wizard.step === 2 && !vm.widgetConfiguration.closed) {
                SpinnerService.show();
                
                vm.widgetConfiguration = processConfigurationBeforeSend(vm.widgetConfiguration);
                vm.configuredWidget.sendToConfigurationProcess(vm.widgetConfiguration);
            } else {
                if (vm.wizard.step === 0 && !vm.isEdit) {
                    initializeWidget();
                }
                vm.wizard.step = vm.wizard.step > 3 ? 3 : vm.wizard.step + 1;
            }
        };

        /**
         * TODO document
         */
        vm.goBack = function() {
            // if we return back to dynamic configuration - set configuration opened again.
            if (vm.wizard.step === 3) {
                vm.widgetConfiguration.closed = false;
            }
            if (vm.wizard.step === 2 && vm.widgetConfiguration.currentPage !== 0) {
                vm.widgetConfiguration.currentPage--;
                vm.widgetConfiguration.changedPage--;
            } else {
                vm.wizard.step = vm.wizard.step < 0 ? 0 : vm.wizard.step - 1;
            }
        };

        /**
         * TODO document
         */
        vm.cancel = function() {
            if (!vm.isEdit && vm.configuredWidget) {
            	WidgetsManagerService.cancelNewWidget(vm.configuredWidget);
                vm.configuredWidget = null;
            } else if (vm.isEdit) {
            	widget.cancelEditWidget();
            }
            $uibModalInstance.dismiss('cancel');
        };

        /**
         * TODO document
         */
        function saveWidget() {
            var widgetMetaData = {
        		name: vm.addWidgetModel.name,
        		description: vm.addWidgetModel.description,
        		width: vm.addWidgetModel.size.width,
        		height: vm.addWidgetModel.size.height,
        		positionX: vm.addWidgetModel.col,
        		positionY: vm.addWidgetModel.row
            };
            
        	if (!vm.isEdit) {
        		vm.widgetConfiguration = processConfigurationBeforeSend(vm.widgetConfiguration);
                WidgetsManagerService.registerWidgetToParent(boardRuntimeId, boardId, vm.configuredWidget, widgetMetaData, vm.widgetConfiguration);
        	} else if (vm.isEdit) {
        		widget.updateWidget(widgetMetaData, vm.widgetConfiguration);
        	}

            $uibModalInstance.dismiss('cancel');
        }

        /**
         * TODO revisit, we need two objects, one for full config 
         * and a second to hold values only 
         * 
         * hack to prevent full configuration data being sent to server 
         * 
         * TODO document 
         */
        function processConfigurationBeforeSend(configuration) {
        	for (var i = 0; i < configuration.pages.length; i++) {
        		var page = configuration.pages[i];
        		
        		for (var p = 0; p < page.properties.length; p++) {
        			var property = page.properties[p];
        			var innerProperty = property.property; 
        			if (innerProperty != null && innerProperty != undefined) {
            			var view = innerProperty.view;
            			if (view != null && view != undefined) {
            				if (innerProperty.viewId === 'choice-Key-Value-String'
            					|| innerProperty.viewId === 'choice-String') {
            					// empty the possibleValues array
            					view.possibleValues = [];
            				}
            			}            				
        			}
        		}
        	}
        	
        	return configuration;
        };
        
        // widget meta data update callback handler
        function widgetMetaDataUpdatedHandler(widgetMetaData) {
            if (!vm.isEdit) {
                WidgetsManagerService.registerWidgetToParent(boardRuntimeId, boardId, vm.configuredWidget);
            }        	
        }

        /**
         * TODO document
         */
        function widgetConfigurationUpdated(configuration) {
        	console.log(configuration);
        	
            vm.widgetConfiguration = JSON.parse(configuration);
            if (vm.widgetConfiguration.closed) {
                vm.goNext();
            }
            if (vm.widgetConfiguration.error) {
                ToastrService.popupError(vm.widgetConfiguration.error);
                vm.cancel();
            }
            SpinnerService.hide();
            $scope.$apply();
        }

        /**
         * TODO document
         */
        function initializeWidget() {
            SpinnerService.show();
            
            WidgetsManagerService.onReady(function() {
                WidgetsManagerService.newWidget(
                	vm.addWidgetModel.type, 
                	function(widget) {
                		
                		console.log("**** NEW WIDGET CALL BACK HANDLER (Apollo) ****");
                		
	                    widget.subscribeForConfigurationTopic(widgetConfigurationUpdated);
	                    widget.sendToConfigurationInit();
                		vm.configuredWidget = widget;
	                    $scope.$apply();
	                    
	                    SpinnerService.hide();
                	}
                );
            });
        }
    }
]);