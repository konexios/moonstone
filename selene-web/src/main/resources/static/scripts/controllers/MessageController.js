controllers.controller('MessageController', [
  '$scope', '$routeParams', 'ErrorService', 'SpinnerService', 'MessageService',
  function ($scope, $routeParams, ErrorService, SpinnerService, MessageService) {
    $scope.pageTitle = 'Message';
    $scope.pageSubTitle = '';

    $scope.busy = true;
    $scope.message = {};

    $scope.messageHeaders = [
      {label: 'Id', value: 'id'},
      {label: 'Class name', value: 'classname'},
      {label: 'Message', value: 'message'},
      {label: 'Method name', value: 'methodname'},
      {label: 'Object id', value: 'objectid'},
      {label: 'Object name', value: 'object name'},
      {label: 'Severity', value: 'severity'},
      {label: 'Timestamp', value: 'timestamp'},
      {label: 'Device id', value: 'deviceid'}
    ];

    $scope.showDate = function (timestamp) {
      return moment(timestamp).format('MM.DD.YYYY HH:MM:ss');
    };

    if ($routeParams.messageId) {
      $scope.busy = true;
      SpinnerService.show();

      MessageService.message($routeParams.messageId)
        .then(function (response) {
          $scope.message = response.data.message;
        })
        .catch(function (response) {
          ErrorService.handleHttpError(response);
        })
        .finally(function () {
          $scope.busy = false;
          SpinnerService.hide();
        })
    }
  }
]);