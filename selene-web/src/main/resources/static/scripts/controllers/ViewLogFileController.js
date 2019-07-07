controllers.controller('ViewLogFileController',
    [
        '$scope', '$uibModalInstance', 'ErrorService', 'SpinnerService', 'SoftwareService', 'ToastrService','logfilename',
	    function ($scope, $uibModalInstance, ErrorService, SpinnerService, SoftwareService, ToastrService,logfilename) {
        	$scope.logfilename = logfilename;
        	
        	//View log file
        	SoftwareService.viewfile($scope.logfilename)
        	.then(function(response){
        		$scope.fileContent =response.data.message
        	})
        	.catch(function (response) {
        		ErrorService.handleHttpError(response);
        	});
        	
        	$scope.close = function() {
                $uibModalInstance.dismiss('close');
            };
        }
]);