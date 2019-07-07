controllers.controller('AwsController',
	['$scope', 'ErrorService', "AwsService",
		function ($scope, ErrorService, AwsService) {
			$scope.pageTitle = 'AWS';
			$scope.pageSubscribe = '';

			$scope.aws = {};

			function loadAws() {
				AwsService.aws()
					.then(function (response) {
						$scope.aws = response.data.aws;
					})
					.catch(function (response) {
						ErrorService.handleHttpError(response)
					})
			}

			loadAws();
		}
	]
);