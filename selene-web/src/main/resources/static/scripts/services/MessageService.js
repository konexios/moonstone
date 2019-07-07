services.factory('MessageService', ['$http', function ($http) {
  function all() {
    return $http({
      'url': '/api/selene/messages/all',
      'method': 'GET'
    });
  }

  function message(messageId) {
    return $http({
      'url': '/api/selene/messages/' + messageId + '/message',
      'method': 'GET'
    });
  }

  return {
    all: all,
    message: message
  };
}]);