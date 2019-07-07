services.factory('RightToUseRequestsService',
  [ '$http',
    function($http) {
      function find(page, filter) {
        return $http.post('/api/rhea/rtu-requests/find', {
          // pagination
          pageIndex: page.pageIndex,
          itemsPerPage: page.itemsPerPage,
          // sorting
          sortDirection: page.sort.direction,
          sortField: page.sort.property,
          // filter
          enabled: filter.enabled,
          companyIds: filter.companyIds,
          statuses: filter.statuses
        });
      }

      function filterOptions() {
        return $http.get('/api/rhea/rtu-requests/options');
      }

      function updateStatus(rightToUseRequestId, status) {
        return $http.put('/api/rhea/rtu-requests/' + rightToUseRequestId, status);
      }

      return {
        find: find,
        filterOptions: filterOptions,
        updateStatus: updateStatus
      };
    }
  ]);
