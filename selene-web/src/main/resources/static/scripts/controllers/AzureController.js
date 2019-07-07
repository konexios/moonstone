controllers.controller('AzureController',
	['$scope', 'ErrorService', "AzureService",
		function ($scope, ErrorService, AzureService) {
			$scope.pageTitle = 'Azure';
			$scope.pageSubscribe = '';

			$scope.azure = {};

			function loadAzure() {
				AzureService.azure()
					.then(function (response) {
						$scope.azure = response.data.azure;
					})
					.catch(function (response) {
						ErrorService.handleHttpError(response)
					})
			}

			loadAzure();
		}
	]
);