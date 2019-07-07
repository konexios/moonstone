angular.module('pegasus').factory("UserService", ["$rootScope", "$location", "$http", "SecurityService",
	function($rootScope, $location, $http, SecurityService){

	function roleOptions(applicationId) {
		return $http({
			"url": "/api/pegasus/users/" + applicationId + "/roles",
			"method": "GET"
		});		
	}
	
	function options(enabled) {
		return $http({
			"url": "/api/pegasus/users/options",
			"method": "GET"
		});		
	}
	
	function filters() {
		return $http({
			"url": "/api/pegasus/users/filters",
			"method": "GET"
		});		
	}
	
	function find(searchFilter) {
		return $http({
			"url": "/api/pegasus/users/find",
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": searchFilter
		});
	}
	
	function user(id) {
		return $http({
			"url": "/api/pegasus/users/" + id + "/user", 
			"method": "GET"
		});
	}
	
	function create(user) {
		return $http({
			"url": "/api/pegasus/users/create", 
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": user
		});
	}
	
	function update(user) {
		return $http({
			"url": "/api/pegasus/users/update", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": user
		});
	}

	function getUserAuthentications(userId) {
        return $http({
            "url": "/api/pegasus/users/" + userId + "/authentication",
            "method": "GET"
        });
    }

    function disableUser(userId) {
        return $http({
            "url": "/api/pegasus/users/" + userId + "/disable",
            "method": "PUT",
            "headers": {"Content-Type" : "application/json"}
        });
	}

	function updateProfile(userId, userProfile) {
        return $http({
            "url": "/api/pegasus/users/" + userId + "/profile",
            "method": "PUT",
            "headers": {"Content-Type" : "application/json"},
            "data": userProfile
        });
	}

	function updateAccount(userId, userAccount) {
		return $http({
			"url": "/api/pegasus/users/" + userId + "/account",
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": userAccount
		});
	}

	function addUserAuthentication(userId, userAuthentication) {

		return $http({
            "url": "/api/pegasus/users/" + userId + "/authentication",
            "method": "POST",
            "headers": {"Content-Type" : "application/json"},
            "data": userAuthentication
        });
	}

	function updateUserAuthentication(userId, userAuthentication) {
		return $http({
			"url": "/api/pegasus/users/" + userId + "/authentication",
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": userAuthentication
		});
	}

	function disableAllAuthentications(userId) {
		return $http({
			"url": "/api/pegasus/users/" + userId + "/authentications/disable/all",
			"method": "PATCH",
			"headers": {"Content-Type" : "application/json"}
		});
	}

	function resetPassword(id) {
		return $http({
			"url": "/api/pegasus/users/" + id + "/password/reset", 
			"method": "PUT"
		});
	}

    function forgotPassword(login) {
        return $http({
            "url": "/api/pegasus/users/password/forgot",
            "method": "PUT",
			"data": {"login": login}
        });
    }

	function changePassword(changePasswordModel) {
        return $http({
            "url": "/api/pegasus/users/password/change",
            "method": "PUT",
            "headers": {"Content-Type" : "application/json"},
			"data": changePasswordModel
        });
    }

	function changePasswordNotAuthorized(login, changePasswordModel) {
		return $http({
			"url": "/api/pegasus/users/password/change/"  + encodeURIComponent(login),
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": changePasswordModel
		});
	}

    function getPasswordPolicy() {
        var url = '/api/pegasus/users/password-policy';
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
        var url = '/api/pegasus/users/password-policy/' + encodeURIComponent(login);
        return $http.get(url);
    }

    function getUserProfile(userId) {
        return $http({
            "url": "/api/pegasus/users/" + userId + "/profile",
            "method": "GET"
        });
	}

	function getUserAccount(userId) {
		return $http({
			"url": "/api/pegasus/users/" + userId + "/account",
			"method": "GET"
		});
	}
	
	function applications(companyId) {
		return $http({
			"url": "/api/pegasus/users/" + companyId + "/applications", 
			"method": "GET"
		});
	}

	return {
		roleOptions: roleOptions,
		options: options,
		filters: filters,
		find: find,
		get: user,
		create: create,
		update: update,
		getUserAuthentications:getUserAuthentications,
		disableUser: disableUser,
		updateProfile: updateProfile,
        updateAccount: updateAccount,
		addUserAuthentication: addUserAuthentication,
		updateUserAuthentication: updateUserAuthentication,
		disableAllAuthentication: disableAllAuthentications,
        forgotPassword: forgotPassword,
		resetPassword: resetPassword,
        changePassword: changePassword,
        changePasswordNotAuthorized: changePasswordNotAuthorized,
        getPasswordPolicy: getPasswordPolicy,
        getPasswordPolicyByLogin: getPasswordPolicyByLogin,
		getUserProfile: getUserProfile,
        getUserAccount: getUserAccount,
        applications: applications
	};
}]);