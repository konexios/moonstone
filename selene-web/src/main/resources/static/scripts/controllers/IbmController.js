controllers.controller('IbmController',
	['$scope', 'ErrorService', "IbmService",
		function ($scope, ErrorService, IbmService) {
			$scope.pageTitle = 'IBM';
			$scope.pageSubscribe = '';

			$scope.ibm = {};

			function loadIbm() {
				IbmService.ibm()
					.then(function (response) {
						$scope.ibm = response.data.ibm;
					})
					.catch(function (response) {
						ErrorService.handleHttpError(response)
					})
			}

			loadIbm();
		}
	]
);