services.factory('SoftwareService', ['$http', function ($http) {
	function viewfile(logfilename){
		return $http({
				"url": "/api/selene/log/get",
				"method": "POST",
				"headers": {"Content-Type" : "application/json"},
				"data":{
					logfilename : logfilename
				}  
		    });
	}
	
	function downloadfile(logfilename){
		return $http({
				"url": "/api/selene/log/download",
				"method": "POST",
				"headers": {"Content-Type" : "application/json"},
				"data": {
			    	logfilename : logfilename
			    }  
		    });
	}
	
	function all(monthYear){
		return $http({
				"url": "/api/selene/log/"+monthYear+"/getall",
				"method": "GET"
		    });
	}
	
	function uploadfileurl(uploadPath, url, port, proxy){
		console.log(parseInt(port));
		return $http({
				"url": "/api/selene/software/uploadFileFromUrl",
				"method": "POST",
				"headers": {"Content-Type" : "application/json"},
				"data": {
					url : url,
					uploadPath : uploadPath,
					proxy : proxy,
					port : parseInt(port)
			    }  
		    });
	}
	
	function uploadfileimport(uploadPath,inputStream){
		return $http({
				"url": "/api/selene/software/uploadFile",
				"method": "POST",
				"headers": {"Content-Type" : "application/json"},
				"data": {
					uploadPath : uploadPath,
					inputStream : inputStream
				}  
	    });
}
	
	function downloadfileurl(downloadPath){
		return $http({
				"url": "/api/selene/software/downloadFile",
				"method": "POST",
				"headers": {"Content-Type" : "application/json","accept":"text/plain"},
				"data": {
					downloadPath : downloadPath
			    }  
		    });
	}

  return {
    all: all,
    viewfile: viewfile,
    downloadfile : downloadfile,
    uploadfileurl: uploadfileurl,
    uploadfileimport : uploadfileimport,
    downloadfileurl : downloadfileurl
  };
}]);