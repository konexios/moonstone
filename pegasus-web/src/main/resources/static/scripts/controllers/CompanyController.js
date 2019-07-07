angular.module('pegasus').controller("CompanyController",
	[
	 	"$rootScope", "$scope", "$stateParams", "$location", "$state",
	 	function ($rootScope, $scope, $stateParams, $location, $state) {
	 		$scope.companyId = $stateParams.companyId;
	 	}
	]
);