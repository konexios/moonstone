services.factory('SecurityService',
    [ '$cookies', '$rootScope',
    function($cookies, $rootScope) {
        var APOLLO_ME = 'apollome';
        var APOLLO_APP = 'apolloapp';

        function isLoggedIn() {
            return getApolloMe() != null && getApolloApp() != null;
        }

        function getApolloMe() {
            return $cookies.get(APOLLO_ME) || null;
        }

        function getApolloApp() {
            return $cookies.get(APOLLO_APP) || null;
        }

        function saveSession(login, hid) {
            deleteSession();

            if (login != null && login != undefined && login != "")
                $cookies.put(APOLLO_ME, login);

            if (hid != null && hid != undefined && hid != "")
                $cookies.put(APOLLO_APP, hid);
        }

        function deleteSession() {
            $cookies.remove(APOLLO_ME);
            $cookies.remove(APOLLO_APP);
        }

        function getUser() {
            return $rootScope.user;
        }

        function hasPrivilege(privilegeName) {
            var user = getUser();

            return (user != null && userHasPrivilege(user, privilegeName));
        }

        function userHasPrivilege(user, privilegeName) {

            if (user == null || user == undefined || privilegeName == null || privilegeName == undefined || privilegeName == "")
                return false;

            var applicationInstance = $rootScope.app;
            if (applicationInstance == null || applicationInstance == undefined)
                return false;

            var result = false;

            outerLoop: for (var i = 0; i < user.applications.length; i++) {
                var application = user.applications[i];
                if (application.id === applicationInstance.id) {
                    for (var p = 0; p < application.privileges.length; p++) {
                        var privilege = application.privileges[p];
                        if (privilege.name == privilegeName) {
                            result = true;
                            break outerLoop;
                        }
                    }
                }
            }

            return result;
        }

        // privileges
        function hasApolloAccess() {
            return hasPrivilege('APOLLO_ACCESS');
        }
        
        // board
        function canCreateBoard() {
            return hasPrivilege('APOLLO_CREATE_BOARD');
        }
        
        function canUpdateBoard() {
            return hasPrivilege('APOLLO_UPDATE_BOARD');
        }
        
        function canReadBoard() {
            return hasPrivilege('APOLLO_READ_BOARD');
        }
        
        function canDeleteBoard() {
            return hasPrivilege('APOLLO_DELETE_BOARD');
        }
        
        function canCopyBoard() {
        	return hasPrivilege('APOLLO_COPY_BOARD');
        }
        
        // system default board
        function canCreateSystemDefaultBoard() {
            return hasPrivilege('APOLLO_CREATE_SYSTEM_DEFAULT_BOARD');
        }
        
        function canUpdateSystemDefaultBoard() {
            return hasPrivilege('APOLLO_UPDATE_SYSTEM_DEFAULT_BOARD');
        }
        
        function canDeleteSystemDefaultBoard() {
            return hasPrivilege('APOLLO_DELETE_SYSTEM_DEFAULT_BOARD');
        }
        
        // arrow certified board
        function canCreateArrowCertifiedBoard() {
            return hasPrivilege('APOLLO_CREATE_ARROW_CERTIFIED_BOARD');
        }
        
        function canUpdateArrowCertifiedBoard() {
            return hasPrivilege('APOLLO_UPDATE_ARROW_CERTIFIED_BOARD');
        }
        
        function canDeleteArrowCertifiedBoard() {
            return hasPrivilege('APOLLO_DELETE_ARROW_CERTIFIED_BOARD');
        }

        
//        function canMarkAsSystemDefaultBoard() {
//            return hasPrivilege('APOLLO_SYSTEM_DEFAULT_BOARD');
//        }
//        
//        function canMarkAsArrowCertifiedBoard() {
//            return hasPrivilege('APOLLO_ARROW_CERTIFIED_BOARD');
//        }

        function canMarkAsUserDefaultBoard() {
            return hasPrivilege('APOLLO_ASSIGN_USER_DEFAULT_BOARD');
        }
        
        function canMarkAsFavoriteBoard() {
            return hasPrivilege('APOLLO_ASSIGN_FAVORITE_BOARD');
        }

        function canMarkAsHyperionBoard() {
            return hasPrivilege('APOLLO_ASSIGN_HYPERION_BOARD');
        }
        
        return {
            isLoggedIn: isLoggedIn,
            saveSession: saveSession,
            deleteSession: deleteSession,
            getUserLogin: getApolloMe,
            getUserApp: getApolloApp,
            // privileges
            hasApolloAccess: hasApolloAccess,
            canCreateBoard: canCreateBoard,
            canUpdateBoard: canUpdateBoard,
            canReadBoard: canReadBoard,
            canDeleteBoard: canDeleteBoard,
            canCopyBoard: canCopyBoard,
            canCreateSystemDefaultBoard: canCreateSystemDefaultBoard,
            canUpdateSystemDefaultBoard: canUpdateSystemDefaultBoard,
            canDeleteSystemDefaultBoard: canDeleteSystemDefaultBoard,
            canCreateArrowCertifiedBoard: canCreateArrowCertifiedBoard,
            canUpdateArrowCertifiedBoard: canUpdateArrowCertifiedBoard,
            canDeleteArrowCertifiedBoard: canDeleteArrowCertifiedBoard,
//            canMarkAsSystemDefaultBoard: canMarkAsSystemDefaultBoard,
//            canMarkAsArrowCertifiedBoard: canMarkAsArrowCertifiedBoard,
            canMarkAsUserDefaultBoard: canMarkAsUserDefaultBoard,
            canMarkAsFavoriteBoard: canMarkAsFavoriteBoard,
            canMarkAsHyperionBoard: canMarkAsHyperionBoard
        };
    }
]);
