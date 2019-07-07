services.factory('BoardService',
    [ '$http', '$q', '$rootScope',
    function ($http, $q, $rootScope) {

        function createBoard(board) {
        	return $http.post('/api/apollo/boards/board/create', board);
        }

        function userDefaultBoard() {
        	return $http.get('/api/apollo/boards/default/board');
        }
        
        function readBoard(boardId) {
        	return $http.get('/api/apollo/boards/'+boardId+'/board');
        }
        
        function updateBoard(board) {
        	return $http.put('/api/apollo/boards/'+board.id+'/board/update', board);
        }
        
        function deleteBoard(boardId) {
        	return $http.delete('/api/apollo/boards/'+boardId+'/board/delete');
        }
        
        function assignAsUserDefault(boardId) {
        	return $http.get('/api/apollo/boards/'+boardId+'/board/user-default');
        }
        
        function assignAsFavorite(boardId) {
        	return $http.get('/api/apollo/boards/'+boardId+'/board/favorite');
        }
        
        function assignAsCustom() {
        }
        
        function copyBoard(boardId) {
        	return $http.get('/api/apollo/boards/'+boardId+'/board/copy');
        }
        
        function findArrowCertifiedDevices(boardId) {
        	return $http.get('/api/apollo/boards/'+boardId+'/board/certified/devices');
        }

        function copyArrowCertifiedBoard(board) {
        	return $http.post('/api/apollo/boards/board/copy/certified', board);
        }

        return {
        	createBoard: createBoard,
        	readBoard: readBoard,
        	updateBoard: updateBoard,
        	deleteBoard: deleteBoard,
        	assignAsUserDefault: assignAsUserDefault,
        	assignAsFavorite: assignAsFavorite,
        	assignAsCustom: assignAsCustom,
        	copyBoard: copyBoard,
        	findArrowCertifiedDevices: findArrowCertifiedDevices,
        	copyArrowCertifiedBoard: copyArrowCertifiedBoard,
        	userDefaultBoard: userDefaultBoard
        };
    }
]);
