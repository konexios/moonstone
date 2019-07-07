controllers.controller('CentralKnowledgeBankController',
    [
        '$scope', 'ErrorService', 'SpinnerService', 'DeviceService', 'ToastrService',
	    function ($scope, ErrorService, SpinnerService, DeviceService, ToastrService) {
        	$scope.pageTitle = "Central Knowledge Bank";
        	$scope.cbk ={};
        	
        	//Download file from Central knowledge bank
        	$scope.download = function(){
                    DeviceService.downloadLookup($scope.cbk)
                    .then(function(response){
                        var data =angular.toJson(response.data)
                        var blob = new Blob([data], {type: 'text/plain;charset=utf-8'});
                          var downloadUrl = URL.createObjectURL(blob);
                          var a = document.createElement("a");
                          a.href = downloadUrl;
                          a.download = response.headers('Content-Disposition') ;
                          document.body.appendChild(a);
                          a.click();
                        ToastrService.popupSuccess("File successfully downloaded.")
                    }) .catch(function (response) {
                        ErrorService.handleHttpError(response);
                    });
                }
        
        //View Central knowledge bank file content
        $scope.viewcbk = function(){
            DeviceService.downloadLookup($scope.cbk)
            .then(function(response){
              
              var data =angular.toJson(response.data)
              $uibModal.open({
                  animation: false,
                  templateUrl: 'partials/viewLookupFile.html',
                  controller: 'ViewLookupFileController',
                  size:'md',
                  resolve: {
                      fileContent: function() {
                          return  data;
                      }
                  }
              })
            }) .catch(function (response) {
                ErrorService.handleHttpError(response);
            });    
        }
     }
]);