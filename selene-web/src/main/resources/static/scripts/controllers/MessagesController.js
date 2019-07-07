controllers.controller('MessagesController', [
  '$scope', 'ErrorService', 'MessageService',
  function ($scope, ErrorService, MessageService) {
    $scope.pageTitle = 'Messages';
    $scope.pageSubTitle = '';

    $scope.buse = true;
    $scope.messages = {};

    $scope.columnHeaders = [
      { label: 'Message', value: 'message', sortable: false },
      { label: 'Timestamp', value: 'timestamp', sortable: true }
    ];

    function loadErrors() {
      MessageService.all()
        .then(function (response) {
          $scope.messages = response.data;
          $scope.buse = false;
        })
        .catch(function (response) {
          $scope.buse = false;
          ErrorService.handleHttpError(response);
        });
    }

    loadErrors();
  }
]);