controllers.controller('FMAvailableUpdatesController', ['$scope', '$timeout', '$routeParams', '$location', '$uibModal', 'FMSummaryService', 'CommonService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', FMAvailableUpdatesController]);

function FMAvailableUpdatesController($scope, $timeout, $routeParams, $location, $uibModal, FMSummaryService, CommonService, ErrorService, ToastrService, SecurityService, SpinnerService) {
  var vm = this; // controller context (vm) used basically for List operations (on this page)

  $scope.mobileLayot = false;
  $scope.collapsedView = false;
  $scope.mobileMenuShowed = false;
  $scope.summary = null;
  $scope.eligibleGroups = [];
  $scope.dateTimeFormat = CommonService.formats.dateTimeFormat;

  function init() {
    vm.find();
  }

  // Layout and left menu behavior
  $scope.menuIsHidden = function() {
    return $scope.mobileLayot ? !$scope.mobileMenuShowed : $scope.collapsedView;
  };

  $scope.collapseMenu = function() {
    if ($scope.mobileLayot) {
      $scope.mobileMenuShowed = !$scope.mobileMenuShowed;
    } else {
      $scope.collapsedView = !$scope.collapsedView;
    }
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


  // List controller:

  var paginationConfig = {
    itemsPerPage: 10,
    sort: {
      property: 'jobNumber',
      direction: 'DESC'
    }
  };

  $scope.eligibleColumns = [
    { label: 'Asset Type', value: 'deviceType', sortable: false },
    { label: 'Assets', value: 'devices', sortable: false },
    { label: 'Hardware Version', value: 'hwVersion', sortable: false },
    { label: 'Firmware Version', value: 'currentHw', sortable: false },
    { label: 'Available Firmware Versions', value: 'newSwVersion', sortable: false }
  ];

  var listConfig = {
    // set up header for list (if it's needed)
    // resultTitle: $scope.tabs.filter(function(t) { return t.id === $scope.tab; })[0].name,
    // Display specific Jobs columns for a specific tab
    columnHeaders: [],
    tfootTopic: 'jobs',
    canAdd: false,
    canEdit: true,
    openFilter: null,
    openDetails: function(job) {
      $location.url('/fmaudit/'+job.id);
    }
  };

  $scope.schedule = function(assetTypeId, firmwareVersionId) {
    $location.url('/fmschedule/?action=create&assetTypeId=' + assetTypeId + "&firmwareVersionId=" + firmwareVersionId).replace();
  };

  ListController.call(vm, paginationConfig, listConfig);

  vm.find = function() {
    SpinnerService.wrap(FMSummaryService.eligible)
      .then(function(response) {
        $scope.eligibleGroups = response.data;
      })
      .catch(ErrorService.handleHttpError);
  };

  init();

}
FMAvailableUpdatesController.prototype = Object.create(ListController.prototype);