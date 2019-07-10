controllers.controller('AllBoardsController',
    [ '$rootScope', '$scope', '$q', '$location', '$routeParams', '$uibModal', 'SecurityService', 'AuthenticationService', 'ErrorService', 'SpinnerService', 'ToastrService', 'AllBoardsService', 'BoardService',
    function ($rootScope, $scope, $q, $location, $routeParams, $uibModal, SecurityService, AuthenticationService, ErrorService, SpinnerService, ToastrService, AllBoardsService, BoardService) {
    	
    	$scope.data = {
    		allBoards: [],
    		certifiedBoards: []
    	};
    	
    	$scope.allBoards = function() {
    		$scope.data.allBoards = [];

    		SpinnerService.wrap(AllBoardsService.getAllBoards)
            .then(function (response) {
                $scope.data.allBoards = response.data;
            })
            .catch(ErrorService.handleHttpError);
    	}
    	
    	$scope.certifiedBoards = function() {
    		$scope.data.certifiedBoards = [];
    		
            SpinnerService.wrap(AllBoardsService.getCertifiedBoards)
            .then(function (response) {
                $scope.data.certifiedBoards = response.data;
            })
            .catch(ErrorService.handleHttpError);
    	}
    	
    	$scope.isMyBoard = function(board) {
    		if (board.userId === null || board.userId === undefined)
    			return false;
    		
    		return board.userId === $rootScope.user.id;
    	};
    	
    	$scope.canEditDefaultBoard = function(board) {
    		var result = false;
    		
    		if ((board.category == 'SystemDefault' && SecurityService.canUpdateSystemDefaultBoard())
    				|| (board.category == 'UserDefault' && SecurityService.canUpdateBoard())) {
    			result = true;
    		}
    		
    		return result;
    	};
    	
    	$scope.canDeleteDefaultBoard = function(board) {
    		var result = false;
    		
    		if ((board.category == 'SystemDefault' && SecurityService.canDeleteSystemDefaultBoard())
    				|| (board.category == 'UserDefault' && SecurityService.canDeleteBoard())) {
    			result = true;
    		}
    		
    		return result;
    	};

    	$scope.goToBoard = function(board) {
    		$location.path('/board/' + board.id);
    	};
    	
        $scope.openBoard = function(board) {
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/modals/boardModal.html',
                controller: 'BoardModalController',
                size: 'lg',
                resolve: {
                    board: board
                }
            });
            
            modalInstance.result.then(function(board) {
            	init();
            });
        };
        
        $scope.deleteConfirmation = function(board) {
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/modals/deleteBoardModal.html',
                controller: 'DeleteBoardModalController',
                size: 'lg',
                resolve: {
                    board: board
                }
            });
            
            modalInstance.result.then(function(board) {
            	init();
            });
        };
        
        $scope.assignBoardAsUserDefault = function(board) {
            SpinnerService.wrap(BoardService.assignAsUserDefault, board.id)
            .then(function(response) {
                ToastrService.popupSuccess('Board "' + board.name + '" has been assigned as default board');
                init();
            })
            .catch(ErrorService.handleHttpError);
        };

        $scope.assignBoardAsFavorite = function(board) {
            SpinnerService.wrap(BoardService.assignAsFavorite, board.id)
            .then(function(response) {
                ToastrService.popupSuccess('Board "' + board.name + '" has been assigned as a favorite board');
                init();
            })
            .catch(ErrorService.handleHttpError);
        };
        
        $scope.copyBoard = function(board) {
            SpinnerService.wrap(BoardService.copyBoard, board.id)
            .then(function(response) {
            	console.log(response);
                ToastrService.popupSuccess('Board "' + board.name + '" has been copied and named "' + response.data.name + '"');
                init();
            })
            .catch(ErrorService.handleHttpError);
        };
        
        $scope.viewArrowCertifiedBoard = function(board) {
            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/modals/arrowCertifiedModal.html',
                controller: 'ArrowCertifiedBoardModalController',
                size: 'lg',
                resolve: {
                    board: board
                }
            });
            
            modalInstance.result.then(function(board) {
            	init();
            });
        };
    	
    	function init() {
    		$scope.allBoards();
    		$scope.certifiedBoards();
    	}
    	
    	init();
    }
]);

controllers.controller('BoardModalController',
    [
        '$scope', '$uibModalInstance', 'SpinnerService', 'ToastrService', 'SecurityService', 'ErrorService', 'BoardService', 'board',
        function ($scope, $uibModalInstance, SpinnerService, ToastrService, SecurityService, ErrorService, BoardService, board) {
        	$scope.board = {
        		category: 'Custom',
        		name: null,
        		description: null
        	};

        	
        	if (board != null)
        		$scope.board = angular.merge({}, board);
        	
            $scope.save = function (form) {
                if (form.$valid) {

                	if ($scope.board.id) {
                        SpinnerService.wrap(BoardService.updateBoard, $scope.board)
                        .then(function(response) {
                            ToastrService.popupSuccess('Board "' + $scope.board.name + '" has been saved successfully');
    	                    $uibModalInstance.close($scope.board);
                        })
                        .catch(ErrorService.handleHttpError);
                	} else {
                        SpinnerService.wrap(BoardService.createBoard, $scope.board)
                        .then(function(response) {
                            ToastrService.popupSuccess('Board "' + $scope.board.name + '" has been saved successfully');
    	                    $uibModalInstance.close($scope.board);
                        })
                        .catch(ErrorService.handleHttpError);
                	}
                } else {
                    ToastrService.popupError('Board cannot be saved because of invalid fields, please check errors.');
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
            
            $scope.applyCategory = function(category) {
            	if ($scope.board.category != category) {
	            	$scope.board.category = category;
            	} else {
	            	$scope.board.category = "Custom";
            	}
            };
            
            $scope.canCreateSystemDefaultBoard = function() {
                return SecurityService.canCreateSystemDefaultBoard();
            };
            
            $scope.canUpdateSystemDefaultBoard = function() {
                return SecurityService.canUpdateSystemDefaultBoard();
            };
            
            $scope.canCreateArrowCertifiedBoard = function() {
                return SecurityService.canCreateArrowCertifiedBoard();
            };
            
            $scope.canUpdateArrowCertifiedBoard = function() {
                return SecurityService.canUpdateArrowCertifiedBoard();
            };

            $scope.canMarkAsUserDefaultBoard = function() {
                return SecurityService.canMarkAsUserDefaultBoard();
            };
            
            $scope.canMarkAsFavoriteBoard = function() {
                return SecurityService.canMarkAsFavoriteBoard();
            };
        }
    ]
);

controllers.controller('DeleteBoardModalController',
    [
        '$scope', '$uibModalInstance', 'SpinnerService', 'ToastrService', 'SecurityService', 'ErrorService', 'BoardService', 'board',
        function ($scope, $uibModalInstance, SpinnerService, ToastrService, SecurityService, ErrorService, BoardService, board) {
       		$scope.board = angular.merge({}, board);
        	
            $scope.ok = function () {
                SpinnerService.wrap(BoardService.deleteBoard, $scope.board.id)
                .then(function(response) {
                    ToastrService.popupSuccess('Board "' + board.name + '" has been successfully deleted');
                    $uibModalInstance.close($scope.board);
                })
                .catch(ErrorService.handleHttpError);
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
            
            $scope.applyCategory = function(category) {
            	if ($scope.board.category != category) {
	            	$scope.board.category = category;
            	} else {
	            	$scope.board.category = "Custom";
            	}
            }
            
            $scope.canDeleteBoard = function() {
                return SecurityService.canDeleteBoard();
            };
        }
    ]
);

controllers.controller('ArrowCertifiedBoardModalController',
    [
        '$scope', '$uibModalInstance', 'SpinnerService', 'ToastrService', 'SecurityService', 'ErrorService', 'BoardService', 'board',
        function ($scope, $uibModalInstance, SpinnerService, ToastrService, SecurityService, ErrorService, BoardService, board) {
       		$scope.board = angular.merge({}, board);
       		$scope.devices = [];
       		$scope.data = {
       			boardId: $scope.board.id,
       			deviceUid: null
       		};
        	
            $scope.ok = function (form) {
            	 if (form.$valid && $scope.data.deviceUid) {
	            	SpinnerService.wrap(BoardService.copyArrowCertifiedBoard, $scope.data)
	                .then(function(response) {
	                	$uibModalInstance.close(response.data);
	                })
	                .catch(ErrorService.handleHttpError);
            	 } else {
            		 ToastrService.popupError('Board cannot be created because of invalid fields, please check errors.'); 
            	 }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
            
            $scope.findDevices = function() {
                SpinnerService.wrap(BoardService.findArrowCertifiedDevices, $scope.board.id)
                .then(function(response) {
                	$scope.devices = response.data;
                })
                .catch(ErrorService.handleHttpError);
            }
            
            $scope.findDevices();
        }
    ]
);
