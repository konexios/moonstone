services.factory("ToastrService", ['toastr', function(toastr){
	var $toastlast;
	
	function popupToast(type, title, message) {
		var $toast = toastr[type](message, title);
		$toastlast = $toast;
		
		return $toast;
	}
	
	function popupSuccess(message) {
		return popupToast("success", "Success", message);
	}
	
	function popupInfo(message) {
		return popupToast("info", "Info", message);
	}
	
	function popupWarning(message) {
		return popupToast("warning", "Warning", message);
	}

	function popupError(message) {
		return popupToast("error", "Error", message);
	}
	
	return {
		popupToast: popupToast,
		popupSuccess: popupSuccess,
		popupInfo: popupInfo,
		popupWarning: popupWarning,
		popupError: popupError
	};
}]);