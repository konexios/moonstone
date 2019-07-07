controllers.controller('RightToUseRequestsController', ['$uibModal', 'RightToUseRequestsService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', RightToUseRequestsController]);

function RightToUseRequestsController($uibModal, RightToUseRequestsService, ErrorService, ToastrService, SecurityService, SpinnerService) {
  var vm = this;

  vm.canUpdateRTURequests = SecurityService.canUpdateRTURequests();

  ListController.call(vm, {
    itemsPerPage: 10,
    sort: {
      property: 'tenantName'
    }
  }, {
    pageTitle: 'Right To Use Requests',
    resultTitle: 'Right To Use Requests',
    columnHeaders: [
      {label: 'Tenant Name', value: 'company.name', sortable: false},
      {label: 'Firmware Version', value: 'firmwareVersion', sortable: false},
      {label: 'Status', value: 'status', sortable: false},
      {label: 'Change Status', value: 'changeStatuses', sortable: false, renderFunc: function(val) {
        return val.length > 1 ? val[0] + ' ' + val[1] : val[0];
      }}
    ],
    tfootTopic: 'right to use requests',
    openFilter: openFilter
  });

  vm.filter = {
    enabled: true,
    companyIds: [],
    statuses: []
  };
  vm.filterOptions = {
    tenants: [],
    statuses: []
  };

  function openFilter() {
    var modalInstance = $uibModal.open({
      animation: false,
      templateUrl: 'partials/righttouserequests/righttouserequests-filter.html',
      controller: 'RightToUseRequestsFilterController',
      size: 'lg',
      resolve: {
        filter: vm.filter,
        filterOptions: vm.filterOptions
      }
    });

    modalInstance.result.then(
      function (filter) {
        vm.filter = filter;
        vm.pagination.pageIndex = 0;
        vm.find();
      }
    );
  }

  vm.find = function() {
    SpinnerService.wrap(RightToUseRequestsService.find, vm.pagination, vm.filter)
      .then(function(response) {
        vm.update(response.data.result);
      })
      .catch(ErrorService.handleHttpError);
  };

  vm.updateStatus = function(id, status) {
    SpinnerService.wrap(RightToUseRequestsService.updateStatus, id, status)
      .then(function () {
        ToastrService.popupSuccess('Right To Use Requests has been updated successfully');
        vm.find();
      })
      .catch(ErrorService.handleHttpError);
  };

  SpinnerService.wrap(RightToUseRequestsService.filterOptions)
    .then(function(response) {
      vm.filterOptions.tenants = response.data.companies;
      vm.filterOptions.statuses = response.data.statuses;
    });

  vm.find();
}

RightToUseRequestsController.prototype = Object.create(ListController.prototype);

controllers.controller('RightToUseRequestsFilterController',
  [
    '$scope', '$uibModalInstance', 'filter', 'filterOptions',
    function ($scope, $uibModalInstance, filter, filterOptions) {
      $scope.filter = angular.merge({}, filter);
      $scope.filterOptions = filterOptions;

      $scope.ok = function () {
        $uibModalInstance.close($scope.filter);
      };

      $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
      };
    }
  ]
);