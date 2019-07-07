services.factory('AllBoardsService',
    [ '$http', '$q', '$rootScope',
    function ($http, $q, $rootScope) {

        function getDefaultBoard() {
            return $http.get('/api/apollo/all/boards/default', {});
        }
        
        function getCustomBoards() {
            return $http.get('/api/apollo/all/boards/custom', {});
        }
        
        function getFavoriteBoards() {
            return $http.get('/api/apollo/all/boards/favorite', {});
        }
        
        function getCertifiedBoards() {
            return $http.get('/api/apollo/all/boards/certified', {});
        }
        
        function getAllBoards() {
            return $http.get('/api/apollo/all/boards/all', {});
        }

        return {
        	getDefaultBoard: getDefaultBoard,
        	getCustomBoards: getCustomBoards,
        	getFavoriteBoards: getFavoriteBoards,
        	getCertifiedBoards: getCertifiedBoards,
        	getAllBoards: getAllBoards
        };
    }
]);
