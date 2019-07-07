services.factory('ErrorService',
  ['$rootScope', '$location',
    function ($rootScope, $location) {
      /**
       *
       */
      var handleHttpError = function (response) {

        var msg = 'Status: ' + response.statusText + ' - ' + response.status;
        if (response.data != undefined && response.data != null && response.data != '') {
          var msg = 'Status: ' + response.statusText + ' - ' + response.status + ' (' + response.data.error + ')';
          msg += '<br /><br />Exception: <p>' + response.data.exception + '</p>' +
            '<br />Message: <p>' + response.data.message + '</p>';
        }

        this.showModal('An unexpected error occurred!', msg);
      };


      /**
       *
       */
      var showModal = function (title, message) {
        var myModal = $("#errorModal");

        myModal.find('#title').html(title);
        myModal.find('#message').html(message);

        myModal.modal('show');
      };

      /**
       *
       */
      var hideModal = function () {
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
