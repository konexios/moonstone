controllers.controller('ViewLookupFileController',
    [
        '$scope', '$uibModalInstance', 'fileContent',
	    function ($scope, $uibModalInstance, fileContent) {

        	$scope.fileContent = fileContent;        	
        	$scope.close = function() {
                $uibModalInstance.dismiss('close');
            };
        }
]);