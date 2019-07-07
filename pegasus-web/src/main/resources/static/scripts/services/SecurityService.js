services.factory("SecurityService", ["$rootScope", "$location", "$cookies", function($rootScope, $location, $cookies){

	var PEGASUS_ME = "pegasusme";
	var PEGASUS_APP = "pegasusapp";
	
	function isLoggedIn() {
		return getPegasusMe() != null && getPegasusApp() != null;
	}

	function getPegasusMe() {
		return $cookies.get(PEGASUS_ME) || null;
	}
	
	function getPegasusApp() {
		return $cookies.get(PEGASUS_ME) || null;
	}
	
	function saveSession(login, hid) {
		deleteSession();
		
		if (login != null && login != undefined && login != "")
			$cookies.put(PEGASUS_ME, login);
		
		if (hid != null && hid != undefined && hid != "")
			$cookies.put(PEGASUS_APP, hid);
	}
	
	function deleteSession() {
		$cookies.remove(PEGASUS_ME);
		$cookies.remove(PEGASUS_APP);
	}

	function getUser() {
		return $rootScope.user;
	}
	
	function getUserId() {
		var user = getUser();
		
		return ((user != null && user != undefined) ? user.id : null);
	}

	function isSuperAdministrator() {
		var user = getUser();
		return (user != null && user != undefined && user.admin);
	}
	
	function isAdministrator() {
		var user = getUser();
		
		return (user != null && user != undefined && hasPrivilege("PEGASUS_ADMIN"));
	}
	
	function hasPrivilege(privilegeName) {
		var user = getUser();

		return (user != null && user != undefined && userHasPrivilege(user, privilegeName));
	}
	
	function userHasPrivilege(user, privilegeName) {

		if (user == null || user == undefined || privilegeName == null || privilegeName == undefined || privilegeName == "")
			return false;
		
		var applicationInstance = $rootScope.app;
		if (applicationInstance == null || applicationInstance == undefined)
			return false;

		var result = false;
		
		for (var i = 0; i < user.applications.length; i++) {
			var application = user.applications[i];
			if (application.id === applicationInstance.id) {
				for (var p = 0; p < application.privileges.length; p++) {
					var privilege = application.privileges[p];
					if (privilege.name == privilegeName) {
						result = true;
						break;
					}
				}
				if (result)
					break;
			}
		}
		
		return result;
	}
	
	return {
		isLoggedIn: isLoggedIn,
		saveSession: saveSession,
		deleteSession: deleteSession,
		getUser: getUser,
		getUserId: getUserId,
		isSuperAdministrator: isSuperAdministrator,
		isAdministrator: isAdministrator,
		hasPrivilege: hasPrivilege
	};
}]);