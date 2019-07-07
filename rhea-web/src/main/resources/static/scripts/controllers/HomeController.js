/* global controllers */

(function() {
	'use strict';

	controllers.controller("HomeController", HomeController);

	HomeController.$inject = [
		"$scope", "$timeout", "$uibModal", "HomeService", "ErrorService", "SpinnerService", "SecurityService", "WebSocketsService"
	];

	function HomeController($scope, $timeout, $uibModal, HomeService, ErrorService, SpinnerService, SecurityService, WebSocketsService) {
		var vm = this;
	}

})();
