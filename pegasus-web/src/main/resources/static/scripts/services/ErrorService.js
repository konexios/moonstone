services.factory("ErrorService", ["$rootScope", "$location", function($rootScope, $location) {

    function handleHttpError(response) {
        console.error('handleHttpError', response);

        var msg = "Status Text: " + response.statusText + " - " + response.status;

        if (response.data && typeof response.data === "string") {
            msg += "\n\nMessage:\n" + response.data;
        } else if (response.data) {
            if (response.data.error)
                msg += "\n\nError:\n" + response.data.error;
            if (response.data.exception)
                msg += "\n\nException:\n" + response.data.exception;
            if (response.data.message)
                msg += "\n\nMessage:\n" + response.data.message;
            if (response.data.path)
                msg += "\n\npath:\n" + response.data.path;
        }

        this.showModal("An unexpected error occurred!", msg);
    }

    function showModal(title, message) {
        var myModal = $("#errorModal");

        myModal.find("#title").text(title);
        myModal.find("#message").text(message);

        myModal.modal("show");
    }

    function hideModal() {
        $("#errorModal").modal("hide");
    }

    return {
        handleHttpError: handleHttpError,
        showModal: showModal,
        hideModal: hideModal
    };
}]);