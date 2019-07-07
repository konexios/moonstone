controllers.controller('FMRightToUseController', ['$scope', '$uibModal', '$timeout', 'FMRightToUseService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', FMRightToUseController]);

function FMRightToUseController($scope, $uibModal, $timeout, FMRightToUseService, ErrorService, ToastrService, SecurityService, SpinnerService) {
  $scope.filterOptions = {};
  $scope.filterAvailableOptions = {};

  $scope.pagination = {
    pageIndex: 0,
    itemsPerPage: 10,
    sort: {
      property: 'rtuFwVersion',
      direction: 'ASC'
    }
  };

  $scope.filter = {
    statuses: []
  };

  $scope.filterAvailable = { deviceTypeIds: [] };

  $scope.mobileLayot = false;
  $scope.collapsedView = false;
  $scope.mobileMenuShowed = false;

  $scope.eligibleGroups = [];

  $scope.tabs = [
    { id: 'requestedFirmware', name: 'Requested firmware' },
    { id: 'availableFirmware', name: 'Available firmware' }
  ];

  $scope.activeTab = 'requestedFirmware';

  $scope.eligibleColumns = [
    { label: 'RTU Firmware Version', value: 'rtuFwVersion', sortable: false },
    { label: 'Status', value: 'status', sortable: false },
    { label: 'Owner', value: 'owner', sortable: false },
    { label: 'Owner Email', value: 'ownerEmail', sortable: false }
  ];

  $scope.eligibleAvailableColumns = [
    { label: 'Asset Type', value: 'deviceType', sortable: false },
    { label: 'Assets', value: 'devices', sortable: false },
    { label: 'Hardware Version', value: 'hwVersion', sortable: false },
    { label: 'Firmware Version', value: 'fwVersion', sortable: false },
    { label: 'RTU Firmware Version', value: 'rtuFwVersion', sortable: false }
  ];

  $scope.changeActiveTab = function (id) {
    $scope.activeTab = id;
    openTab(id);
  };

  $scope.collapseMenu = function() {
    if ($scope.mobileLayot) {
      $scope.mobileMenuShowed = !$scope.mobileMenuShowed;
    } else {
      $scope.collapsedView = !$scope.collapsedView;
    }
  };

  $scope.menuIsHidden = function() {
    return $scope.mobileLayot ? !$scope.mobileMenuShowed : $scope.collapsedView;
  };

  $scope.$on('resize', function(event, data) {
    if (data.width < 992) {
      $scope.mobileLayot = true;
    } else {
      $scope.mobileLayot = false;
      $scope.mobileMenuShowed = false;
    }
    $timeout(function() {
      $scope.$apply();
    }, 0, false);
  });

  $scope.statusClickHandler = function(id, deviceType) {
    SpinnerService.wrap(FMRightToUseService.requestToDownloadFirmware, id, deviceType)
      .then(function (response) {
        if (response.data.status === 'ERROR') {
          ToastrService.popupError(response.data.message);
        } else {
          findAvailable();
          ToastrService.popupSuccess('Request has been sent');
        }
      })
      .catch(ErrorService.handleHttpError);
  };

  function find() {
    SpinnerService.wrap(FMRightToUseService.find, $scope.pagination, $scope.filter)
      .then(function (response) {
        $scope.eligibleGroups = response.data.result.content;
        $scope.pagination.first = response.data.result.first;
        $scope.pagination.last = response.data.result.last;
        $scope.pagination.pageIndex = response.data.result.number;
        $scope.pagination.itemsPerPage = response.data.result.size;
        $scope.pagination.totalItems = response.data.result.totalElements;
        $scope.pagination.totalPages = response.data.result.totalPages;
      })
      .catch(ErrorService.handleHttpError);
  }

  function findAvailable() {
    SpinnerService.wrap(FMRightToUseService.findAvailable, $scope.filterAvailable)
      .then(function (response) {
        $scope.eligibleAvailableGroups = response.data;
      })
      .catch(ErrorService.handleHttpError);
    SpinnerService.wrap(FMRightToUseService.filterAvailableOptions)
      .then(function (response) {
        $scope.filterAvailableOptions = response.data;
      });
  }

  $scope.filterUpdated = function() {
    $scope.pagination.pageIndex = 0;
    find();
  };

  $scope.filterAvailableUpdated = function () {
    findAvailable();
  };

  function openTab(id) {
    switch (id) {
      case 'availableFirmware':
        findAvailable();
        SpinnerService.wrap(FMRightToUseService.filterAvailableOptions)
          .then(function (response) {
            $scope.filterAvailableOptions = response.data;
          });
        break;
      default:
        find();
        SpinnerService.wrap(FMRightToUseService.filterOptions)
          .then(function(response) {
            $scope.filterOptions = response.data;
          });
    }
  }

  openTab($scope.activeTab);

  $scope.canSendRequest = SecurityService.canSendRequestToFirmware();

  $scope.changeItemsPerPage = function(numberOfItems) {
    $scope.pagination.itemsPerPage = numberOfItems;
    find();
  };

  $scope.previousPage = function() {
    $scope.pagination.pageIndex--;
    find();
  };

  $scope.nextPage = function() {
    $scope.pagination.pageIndex++;
    find();
  };

  $scope.gotoPage = function(pageNumber) {
    $scope.pagination.pageIndex = pageNumber;
    find();
  }
}