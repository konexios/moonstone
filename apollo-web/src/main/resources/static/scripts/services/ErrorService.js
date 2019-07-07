services.factory("ErrorService", ["$rootScope", "$location", function($rootScope, $location) {

	function handleHttpError(response) {
		var msg = "Status Text: " + response.statusText + " - " + response.status;
		if (response.data) 
		{
			if (response.data.error)
				msg += "<br\><br\>Error: <p>" + response.data.error + "</p>";			
			if (response.data.exception)
				msg += "<br\><br\>Exception: <p>" + response.data.exception + "</p>";
			if (response.data.message)
				msg += "<br\>Message: <p>" + response.data.message + "</p>";
			if (response.data.path)
				msg += "<br\>path: <p>" + response.data.path + "</p>";
		}
		
		showModal("An unexpected error occurred!", msg);					
	}

	/**
	 * Extended common http error handling, with posibility to handle http errors like 404 (NotFound), 500...
	 * @param {*} response Http response 
	 */
	function handleHttpErrorEx(response) {
		if(response.status === 404) {
			// handle NotFound
			var url =  '/404' + (response.data.message ? '?message=' + response.data.message : '');
			$location.url(url);
			$location.replace();
		} else {
			handleHttpError(response);
		}
	}

	function showModal(title, message) {
		var myModal = $("#errorModal");
		
		myModal.find("#title").html(title);
		myModal.find("#message").html(message);
		
		myModal.modal("show");
	}
	
	function hideModal() {
		$("#errorModal").modal("hide");
	}
	return {
		handleHttpError: handleHttpError,
		handleHttpErrorEx: handleHttpErrorEx,
		showModal: showModal,
 		hideModal: hideModal
	};
}]);