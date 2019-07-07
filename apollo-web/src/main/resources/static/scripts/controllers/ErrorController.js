controllers.controller('ErrorController',
	[ '$scope', 'ErrorService',
 	function ($scope, ErrorService) {
 		$scope.showModal = function(title, message) {
	 		ErrorService.showModal(title, message);	 			
 		};
 		$scope.hideModal = function() {
 			ErrorService.hideModal();
 		}
 	}
]);
