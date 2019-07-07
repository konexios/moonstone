services.factory("UserService",[
	    "$rootScope",
	    "$location",
	    "$http",
	    "$q",
	    "SecurityService",
    function($rootScope, $location, $http, $q, SecurityService) {
	
		function getUsers() {
	    	return $http({
	            "url": "/api/apollo/users/",
	            "method": "GET"
	        });
	    }
		
        function getUser(id) {
            return $http({
                "url": "/api/apollo/users/" + id + "/user",
                "method": "GET"
            });
        }

        function create(user) {
            return $http({
                "url": "/api/apollo/users/create",
                "method": "POST",
                "headers": { "Content-Type": "application/json" },
                "data": user
            });
        }

        function update(user) {
            return $http({
                "url": "/api/apollo/users/update",
                "method": "PUT",
                "headers": { "Content-Type": "application/json" },
                "data": user
            });
        }

        function changeStatus(userIds, status) {
            return $http({
                "url": "/api/apollo/users/" + status,
                "method": "PUT",
                "headers": { "Content-Type": "application/json" },
                "data": userIds
            });
        }

        function findExistingUserName(userName) {
            return $http({
                "url": "/api/apollo/users/userExists/" + userName,
                "method": "GET"
            });
        }
        
        function roleOptions(companyId) {
    		return $http({
    			"url": "/api/apollo/users/" + companyId + "/roles",
    			"method": "GET"
    		});		
    	}

        function resetPassword(id) {
            return $http({
                "url": "/api/apollo/users/" + id + "/password/reset",
                "method": "PUT"
            });
        }

        function forgotPassword(login) {
            return $http({
                "url": "/api/apollo/users/password/forgot/" + login,
                "method": "PUT"
                	//"method": "GET"
            });
        }

        function changePassword(changePasswordModel) {
            return $http({
                "url": "/api/apollo/users/password/change",
                "method": "PUT",
                "headers": { "Content-Type": "application/json" },
                "data": changePasswordModel
            });
        }

        function changePasswordNotAuthorized(login, changePasswordModel) {
            return $http({
                "url": "/api/apollo/users/password/change/" + encodeURIComponent(login),
                "method": "PUT",
                "headers": { "Content-Type": "application/json" },
                "data": changePasswordModel
            });
        }

        function getPasswordPolicy() {
            var url = '/api/apollo/users/password-policy';
            if (!SecurityService.isLoggedIn()) {
                if (SecurityService.getUser()) {
                    url += '/' + encodeURIComponent(SecurityService.getUser());
                } else {
                    return $q.reject('Not authorized');
                }
            }
            return $http.get(url);
        }

        function getPasswordPolicyByLogin(login) {
            var url = '/api/apollo/users/password-policy/';
            url += '/' + encodeURIComponent(login);
            return $http.get(url);
        }

        function updateUserLogo(WLabelId) {
            return $http({
                "url": "/api/apollo/security/applicationInfo/" + WLabelId,
                "method": "GET",
                "headers": { "Content-Type": "application/json" }
            });
        }
        
        function saveUserLoginAudit(){
        	return $http({
                "url": "/api/apollo/users/user/audit",
                "method": "POST"
            });
        }
        
        return {
            create: create,
            update: update,
            getUser: getUser,
            getUsers: getUsers,
            changeStatus: changeStatus,
            findExistingUserName: findExistingUserName,
            roleOptions: roleOptions,
            forgotPassword: forgotPassword,
            resetPassword: resetPassword,
            changePassword: changePassword,
            changePasswordNotAuthorized: changePasswordNotAuthorized,
            getPasswordPolicy: getPasswordPolicy,
            getPasswordPolicyByLogin: getPasswordPolicyByLogin,
            updateUserLogo: updateUserLogo,
            saveUserLoginAudit : saveUserLoginAudit
        };
    }
]);