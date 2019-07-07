services.factory('ErrorService',
    [ '$rootScope', '$location',
    function($rootScope, $location){
    	/**
    	 *
    	 */
    	var handleHttpError = function(response) {
    		console.error('handleHttpError', response);

            var msg = 'Status: ';
            if (typeof response == 'object') {
                // skip display HTTP errors for 401 since the whole web page will be reloaded
                if (response.status == 401) return;
        		msg += response.statusText + ' - ' + response.status;
        		if (response.data)
        		{
        			msg = 'Status: ' + response.statusText + ' - ' + response.status + ' (' + response.data.error + ')';
        			msg += '<br\><br\>Exception: <p>' + response.data.exception + '</p>';
        			msg += '<br\>Message: <p>' + response.data.message + '</p>';
        		}
            } else {
                msg += response;
            }

    		showModal('An unexpected error occurred!', msg);
    	};



    	/**
    	 *
    	 */
    	var showModal = function(title, message) {
    		var myModal = $("#errorModal");

    		myModal.find('#title').html(title);
    		myModal.find('#message').html(message);

    		myModal.modal('show');
    	};

    	/**
    	 *
    	 */
    	var hideModal = function() {
    		$("#errorModal").modal('hide');
    	};

    	/**
    	 *
    	 */
    	return {
    		handleHttpError: handleHttpError,
    		showModal: showModal,
     		hideModal: hideModal
    	};
    }
]);
