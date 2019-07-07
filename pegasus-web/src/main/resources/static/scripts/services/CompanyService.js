angular.module('pegasus').factory("CompanyService", ["$rootScope", "$location", "$http", function($rootScope, $location, $http){

	function options(enabled) {
		return $http({
			"url": "/api/pegasus/companies/" + enabled + "/options",
			"method": "GET"
		});		
	}

	function filters() {
		return $http({
			"url": "/api/pegasus/companies/filters",
			"method": "GET"
		});		
	}
	
	function find(searchFilter) {
		return $http({
			"url": "/api/pegasus/companies/find",
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": searchFilter
		});
	}
	
	function company(id) {
		return $http({
			"url": "/api/pegasus/companies/" + id + "/company", 
			"method": "GET"
		});
	}
	
	function createCompany(id, company) {
		return $http({
			"url": "/api/pegasus/companies/" + id + "/company", 
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": company
		});
	}
	
	function updateCompany(id, company) {
		return $http({
			"url": "/api/pegasus/companies/" + id + "/company", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": company
		});
	}
	
	function billing(companyId) {
		return $http({
			"url": "/api/pegasus/companies/" + companyId + "/billing", 
			"method": "GET"
		});
	}
	
	function updateBilling(companyBilling) {
		return $http({
			"url": "/api/pegasus/companies/" + companyBilling.id + "/billing", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": companyBilling
		});
	}
	
	function subscriptions(companyId) {
		return $http({
			"url": "/api/pegasus/companies/" + companyId + "/subscriptions", 
			"method": "GET"
		});
	}

	function applications(companyId) {
		return $http({
			"url": "/api/pegasus/companies/" + companyId + "/applications", 
			"method": "GET"
		});
	}

	function authentication(companyId) {
		return $http({
			"url": "/api/pegasus/companies/" + companyId + "/authentication", 
			"method": "GET"
		});
	}
	
	function passwordPolicy(companyId) {
		return $http({
			"url": "/api/pegasus/companies/" + companyId + "/authentication/passwordpolicy", 
			"method": "GET"
		});
	}
	
	function updatePasswordPolicy(companyId, passwordPolicy) {
		return $http({
			"url": "/api/pegasus/companies/" + companyId + "/authentication/passwordpolicy", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": passwordPolicy
		});
	}
	
	function loginPolicy(companyId) {
		return $http({
			"url": "/api/pegasus/companies/" + companyId + "/authentication/loginpolicy", 
			"method": "GET"
		});
	}
	
	function updateLoginPolicy(companyId, loginPolicy) {
		return $http({
			"url": "/api/pegasus/companies/" + companyId + "/authentication/loginpolicy", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": loginPolicy
		});
	}
	
	function auth(companyId, authId, authType) {
		return $http({
			"url": "/api/pegasus/companies/" + companyId + "/authentication/" + authId + "/auth/" + authType, 
			"method": "GET"
		});
	}
	
	function createAuth(companyAuth) {
		return $http({
			"url": "/api/pegasus/companies/authentication/auth", 
			"method": "POST",
			"headers": {"Content-Type" : "application/json"},
			"data": companyAuth
		});
	}
	
	function updateAuth(companyAuth) {
		return $http({
			"url": "/api/pegasus/companies/authentication/auth", 
			"method": "PUT",
			"headers": {"Content-Type" : "application/json"},
			"data": companyAuth
		});
	}
	
	function hierarchy(id) {
		return $http({
			"url": "/api/pegasus/companies/" + id + "/hierarchy", 
			"method": "GET"
		});
	}
	
	return {
		options: options,
		filters: filters,
		find: find,
		company: company,
		createCompany: createCompany,
		updateCompany: updateCompany,
		billing: billing,
		updateBilling: updateBilling,
		subscriptions: subscriptions,
		applications: applications,
		authentication: authentication,
		passwordPolicy: passwordPolicy,
		updatePasswordPolicy: updatePasswordPolicy,
		loginPolicy: loginPolicy,
		updateLoginPolicy: updateLoginPolicy,
		auth: auth,
		createAuth: createAuth,
		updateAuth: updateAuth,
		hierarchy: hierarchy
	};
}]);