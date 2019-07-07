services.factory('FMRightToUseService', ['$http', function($http) {

  function find(page, filter) {
    return $http.post('/api/kronos/rtu/find', {
      pageIndex: page.pageIndex,
      itemsPerPage: page.itemsPerPage,
      statuses: filter.statuses
    });
  }

  function findAvailable(filter) {
    return $http.post('/api/kronos/rtu/find/available', filter);
  }

  function filterOptions() {
    return $http.get('/api/kronos/rtu/options');
  }

  function filterAvailableOptions() {
    return $http.get('/api/kronos/rtu/options/available');
  }
  
  function requestToDownloadFirmware(id, deviceType) {
    return $http.put('/api/kronos/rtu/request/' + id, deviceType);
  }

  return {
    find: find,
    findAvailable: findAvailable,
    filterOptions: filterOptions,
    filterAvailableOptions: filterAvailableOptions,
    requestToDownloadFirmware: requestToDownloadFirmware
  };
}]);