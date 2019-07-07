controllers.controller('SoftwareController', [
 '$rootScope', '$scope', '$uibModal', 'ErrorService', 'SpinnerService', 'SoftwareService','ToastrService',
  function ($rootScope,$scope, $uibModal, ErrorService, SpinnerService, SoftwareService,ToastrService) {
    $scope.pageTitle = 'Selene Logs';
    $scope.buse = true;
    
    $scope.columnHeaders = [	
    	{ label: 'Log FileName', value: 'logfilename', sortable: false },
    	{ label: 'Actions', value: 'view', sortable: false },
    	{ label: 'Actions', value: 'download', sortable: false },
    ];
    
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.pages = [1];
    $scope.firstPage = 1;
    $scope.lastPage = 1;
    $scope.showflag = false;

    $scope.changeCurrentPage = function(page) {
        if (page < 0 || page > $scope.pages.length) return;
        $scope.currentPage = page;
    };

    $scope.gotoPreviousPage = function() {
        var currentPage = $scope.currentPage;
        $scope.changeCurrentPage(currentPage - 1);
    };

    $scope.gotoNextPage = function() {
        var currentPage = $scope.currentPage;
        $scope.changeCurrentPage(currentPage + 1);
    };

    $scope.changePageSize = function(pageSize) {
        $scope.pageSize = pageSize;
        $scope.pages = [1];
        for (var i = 2; i <= Math.ceil($scope.logs.length / pageSize); i++) {
            $scope.pages.push(i);
        }
        $scope.lastPage = $scope.pages.length;
    };
    
    //Get list of files
    $scope.getFileList = function(month){
    	$scope.monthYear = startDate.value;
        SoftwareService.all($scope.monthYear)
        .then(function(response){
        	$scope.logs = response.data;
        	$scope.lastPage = Math.ceil(response.data.length / $scope.pageSize);
        })
        .catch(function (response) {
           ErrorService.handleHttpError(response);
         });
        
    }
     
     //View content of file
     $scope.view = function(log){
    	 $uibModal.open({
                    animation: false,
                    templateUrl: 'partials/viewLogFile.html',
                    controller: 'ViewLogFileController',
                    size:'lg',
                    resolve:{
                    	logfilename: function() {
                            return log;
                        }
                    }
         })

     }
     
     //Download log file
     $scope.download = function(log){
         SoftwareService.downloadfile(log)
         .then(function (response) {
             var blob = new Blob([response.data], {type: 'application/log'});
             var downloadUrl = URL.createObjectURL(blob);
             var a = document.getElementById("downloadTag");
             a.href = downloadUrl;
             a.download =  response.headers('Content-Disposition');
             document.body.appendChild(a);
             a.click();
        
             ToastrService.popupSuccess("Log File has been successfully downloaded.");
         })
         .catch(function (response) {
           ErrorService.handleHttpError(response);
         });
     }
     
     //JQUERY  for month and year picker
     $(function() {
    	    $('.date-picker').datepicker( {
    	        changeMonth: true,
    	        changeYear: true,
    	        showButtonPanel: true,
    	        dateFormat: 'yy-mm',
    	        onClose: function(dateText, inst) { 
    	            $(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, 1));
    	        }
    	    });
    	});

     
     //Selene Upload and download
     $scope.file = [];
     $scope.fileName = '';
     $scope.logs= {};
     
     $scope.uploadme = {};
     $scope.uploadme.src = '';
     
     $scope.openFile = function(event) {
         var input = event.target;
       
         $scope.$apply();
         var reader = new FileReader();
         reader.onload = function(){
               
           $scope.file =reader.result;//.substring(0, reader.result.length+1);
           
         
         };
         reader.readAsArrayBuffer(input.files[0]);
         $scope.fileName = input.files[0].name;
         //$('#fileDiv span').text($scope.fileName)
         $scope.$apply();
       }
//     $scope.spanData = $('#fileDiv span').text();
//     $rootScope.$watch($scope.spanData,function(old,new1){
//    	 alert("filename chaned.")
//     })
     	
     
     
     
     $scope.uploadFile = function (form, formData) {
    //	 alert("hellooooo");
    	// $scope.port = formData.port;
    	 $scope.busy = true;
    	 SpinnerService.show();
    	 if($scope.fileName || $scope.file != ''){
    		//$scope.fileName = $scope.fileName;
    		 
    		 var uarr = new Uint8Array($scope.file);
    		 var strings = [], chunksize = 0xffff;
    		 var len = uarr.length;
    		 for (var i = 0; i * chunksize < len; i++){
    			 strings.push(String.fromCharCode.apply(null, uarr.subarray(i * chunksize, (i + 1) * chunksize)));
    			 }

    		 
        	 $scope.file = btoa(strings.join(''));
        	
        	 $scope.path = formData.path + $scope.fileName;
       
        	 SoftwareService.uploadfileimport($scope.path,$scope.file)
    		 .then(function(){
    			 $scope.busy = false;
    			// alert("sdfd")
    			
    			 SpinnerService.hide();
    			 ToastrService.popupSuccess("Uploaded Successfully.")
    			 $scope.file = '';
    			 $scope.logs = {};	
    			 $scope.fileName = '';
    		 })
    		 .catch(function (response) {
    			 $scope.busy = false;
    			 $scope.file = '';
    			 $scope.logs = {};	
    			 $scope.fileName = '';	
    			 SpinnerService.hide();
    	           ErrorService.handleHttpError(response);
    	      });
    	 }
    	 else{
    		
    		 SoftwareService.uploadfileurl(formData.path,formData.url,formData.port,formData.proxy)
    		 .then(function(){
    			 $scope.busy = false;
    			 SpinnerService.hide();
    			 $scope.file = '';
    			 $scope.logs = {};
    			 $scope.fileName = '';
    			 $scope.path = {};	
    			 ToastrService.popupSuccess("Uploaded Successfully.")
    		 })
    		 .catch(function (response) {
    			 $scope.busy = false;
    			 SpinnerService.hide();
    			 $scope.logs = {};	
    	           ErrorService.handleHttpError(response);
    	      });
    	 }  
     }
     
     $scope.downloadFile = function(form,formData) {
    	 $scope.filePath = formData.downloadpath;
    	 
    	 $scope.downloadfileName = $scope.filePath.substring($scope.filePath.lastIndexOf("/") + 1, $scope.filePath.length);
    	 $scope.fileType = $scope.fileName.substring($scope.fileName.lastIndexOf(".") + 1, $scope.fileName.length);
    	
    	 SoftwareService.downloadfileurl($scope.filePath)
    	 .then(function(response){

    		 var byteCharacters = atob(response.data);
    		 var byteNumbers = new Array(byteCharacters.length);	
    		 for (var i = 0; i < byteCharacters.length; i++) {
    		     byteNumbers[i] = byteCharacters.charCodeAt(i);
    		 }
    		 
    		 var byteArray = new Uint8Array(byteNumbers);
    		 
    		 
    		 var blob = new Blob([byteArray], {type: 'application/'+$scope.fileType});
             var downloadUrl = URL.createObjectURL(blob);
             var a = document.getElementById("downloadTag");
             a.href = downloadUrl;
             a.download = $scope.downloadfileName;
             //a.download =  response.headers('Content-Disposition');
             document.body.appendChild(a);
             a.click();
             ToastrService.popupSuccess("Downloaded successfully.")
		 })
		 .catch(function (response) {
			 SpinnerService.hide();
	           ErrorService.handleHttpError(response);
	      });
     }
     
     $scope.IsFileVisible =true;
     $scope.IsURLVisible =false;
     $scope.showHide = function(value) {
    	 if(value=='file'){
    		 $scope.IsFileVisible = true;
    		 $scope.IsURLVisible =false;
    	 }
    	 else{
    		 $scope.file = [];
    		 var path = $scope.logs.path;
    	     $scope.logs= {};
    	     $scope.logs.path = path;
    	     $scope.fileName = '';
    		 $scope.IsURLVisible = true;
    		 $scope.IsFileVisible =false;
    	 }
     }
  	}
]);

