controllers.controller('BoardController', ['$rootScope', '$scope', '$location', '$routeParams', '$uibModal', '$dialogAlert', 'SecurityService', 'ErrorService', 'SpinnerService', 'BoardService',
    function($rootScope, $scope, $location, $routeParams, $uibModal, $dialogAlert, SecurityService, ErrorService, SpinnerService, BoardService) {

        var vm = this;
        vm.boardId = $routeParams.boardId;
        vm.board = null;
        vm.editMode = false;
        vm.loading = true;
        vm.boardIsReady = false;
        
        vm.runtime = {
        	boardRuntime: null
        };

        vm.ddBoardOptions = {
            boardId: vm.boardId,
            boardOnRedy: boardOnRedy,
            gridster: { columns: 6 },
            // TODO: probably this icons should be a part of specific widget type instance, and widget common part (edit, remove)...
            // TODO: design and move this action into widget.
            widgetActionIcons: [{
                id: "widgetOptionsMenu",
                iconClass: "fa fa-cog",
                menuItems: [
                    { name: 'Edit', action: onEditWidgetBtn },
                    { name: 'Remove', action: function(widget) { widget.deleteWidget(); } }
                ]
            }]
        };

        // permissions
        vm.canManageBoard = function() {
        	var result = false;
        	
        	if (!vm.board || !vm.board.category)
        		return false;
        	
        	if ((vm.board.category === 'SystemDefault' 
        		&& SecurityService.canUpdateSystemDefaultBoard()) 
        		|| (vm.board.category === 'ArrowCertified' 
            		&& SecurityService.canUpdateArrowCertifiedBoard())) {
        		result = true;
        	} else if (vm.board.userId === $rootScope.user.id) {
        		result = true;
        	}
        	
        	return result;
        }

        init();

        function init() {
            if (vm.boardId == "default") {
                SpinnerService.wrap(BoardService.userDefaultBoard)
                    .then(function(response) {
                        vm.board = response.data;
                        vm.boardId = vm.board.id;
                        vm.ddBoardOptions.boardId = vm.board.id;
                        // in case if we don't have a default board\
                        if(!vm.boardId){
                            $dialogAlert('You don\'t have a default board. Please add the default board on all boards page.', 'No default board').then(function(){
                                $location.path('/all-boards');
                            });
                        }
                        vm.loading = false;
                    })
                    .catch(ErrorService.handleHttpErrorEx);
            } else {
                SpinnerService.wrap(BoardService.readBoard, vm.boardId)
                    .then(function(response) {
                        vm.board = response.data.board;
                        vm.loading = false;
                    })
                    .catch(ErrorService.handleHttpErrorEx);
            }
        }

//        $scope.$on("$destroy", function(){
//        	if (vm.runtime.boardRuntime) {
//            	vm.runtime.boardRuntime.closeBoard();        		
//        	}
//        });
        
        vm.openAddWidgetModal = function(widget) {
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/modals/addWidgetModal.html',
                controller: 'AddWidgetModalController',
                controllerAs: 'vm',
                size: 'lg',
                keyboard: false,
                backdrop: 'static',
                resolve: {
                    boardId: function() {
                        return vm.runtime.boardRuntime.boardId;
                    },
                    boardRuntimeId: function() {
                        return vm.runtime.boardRuntime.boardRuntimeId;
                    },
                    widget: function() {
                        return widget;
                    }
                }
            });

            modalInstance.result.then(function() {

            });
        };

        // todo: should be another way to provide board loading include catching errors
        //SpinnerService.show();

        function boardOnRedy(dashboard) {
        	vm.runtime.boardRuntime = dashboard;
        	
            //SpinnerService.hide();
            vm.boardIsReady = true;
        }

        function onEditWidgetBtn(widget) {
            vm.openAddWidgetModal(widget);
        }
    }
]);