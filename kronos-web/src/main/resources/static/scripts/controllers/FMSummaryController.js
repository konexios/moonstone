controllers.controller('FMSummaryController', ['$scope', '$timeout', '$routeParams', '$location', '$uibModal', 'FMSummaryService', 'CommonService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', FMSummaryController]);

function FMSummaryController($scope, $timeout, $routeParams, $location, $uibModal, FMSummaryService, CommonService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    var vm = this; // controller context (vm) used basically for List operations (on this page)

    $scope.mobileLayot = false;
    $scope.collapsedView = false;
    $scope.mobileMenuShowed = false;

    $scope.tab = $routeParams.tab;
    $scope.tabs = FMSummaryService.tabs();
    $scope.tabsUrls = $scope.tabs.map(function(item) { return item.id; });
    $scope.summary = null;
    $scope.eligibleGroups = [];
    $scope.dateTimeFormat = CommonService.formats.dateTimeFormat;

    $scope.filterOptions = null;
    $scope.filter = {
        jobNumbers: [],
        requestors: [],
        deviceTypes: [],
        startDates: null,
        completedDates: null,
        statuses: []
    };


    function init() {

        // Check if the 'tab' url parameter is exists and correct, if not, set default - first (SCHEDULED)
        if ($scope.tab == undefined || $scope.tab == null || $scope.tabsUrls.indexOf($scope.tab) == -1) {
            $scope.tab = $scope.tabs[0].id;
            $location.search({ 'tab': $scope.tab }).replace(); // note: 'replace' for history (don't write incorrect url in history)
            // $location.path('/fmsummary/SCHEDULED').replace(); // alt: more beauty URL, but path may changed and lead to an error
            return; // break initialization (redirect should occur)
        }

        getSummary();
        getFilterOptions($scope.tab);
        vm.find();
    }


    function getSummary() {
        SpinnerService.wrap(FMSummaryService.summary).then(function(response) {
            $scope.summary = response.data;
        });
    }

    function getFilterOptions(tab) {
        SpinnerService.wrap(FMSummaryService.filterOptions, tab).then(function(response) {
            $scope.filterOptions = response.data;
        });
    }

    function getJobs(tab) {
        SpinnerService.wrap(FMSummaryService.filterOptions, tab).then(function(response) {
            $scope.filterOptions = response.data;
        });
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
            property: 'started',
            direction: 'DESC'
        }
    };

    var columnsWithDisplayOptions = [
        { label: 'Job Name', value: 'name', sortable: false, displayOnTabs: ['SCHEDULED', 'INPROGRESS', 'COMPLETE'] },
        { label: 'Requester', value: 'requestor', renderFunc: function(val) { return val == null ? '---': val; }, sortable: false, displayOnTabs: ['SCHEDULED', 'INPROGRESS', 'COMPLETE'] },
        { label: 'Assets', value: 'devices', sortable: false, displayOnTabs: ['SCHEDULED', 'INPROGRESS', 'COMPLETE'] },
        { label: 'Asset Type', value: 'deviceType', sortable: false, displayOnTabs: ['SCHEDULED', 'INPROGRESS', 'COMPLETE'] },
        { label: 'Hardware Version', value: 'hwVersion', sortable: false, displayOnTabs: ['SCHEDULED', 'INPROGRESS', 'COMPLETE'] },
        { label: 'New Firmware Version', value: 'newSwVersion', sortable: false, displayOnTabs: ['SCHEDULED', 'INPROGRESS', 'COMPLETE'] },
        { label: 'Schedule', value: 'start', sortable: false, renderFunc: CommonService.getFormatredDate, displayOnTabs: ['SCHEDULED', 'INPROGRESS', 'COMPLETE'] },
        { label: 'Started', value: 'started', sortable: true, renderFunc: function(date) { return CommonService.getFormatredDate(date, '---'); }, displayOnTabs: ['INPROGRESS', 'COMPLETE'] },
        { label: '% Complete', value: 'progressMetrics.endOfLife.percent', sortable: false, renderFunc: function(value) { return (Math.round(parseFloat(value)) + "%"); }, displayOnTabs: ['INPROGRESS'] },
        { label: 'Completed', value: 'completed', sortable: false, renderFunc: function(date) { return CommonService.getFormatredDate(date, '---'); }, displayOnTabs: ['COMPLETE'] },
        { label: 'Status', value: 'status', sortable: false, displayOnTabs: ['COMPLETE'] }
    ];
    
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
        columnHeaders: columnsWithDisplayOptions.filter(function(column) { return column.displayOnTabs.indexOf($scope.tab) !== -1; }),
        tfootTopic: 'jobs',
        canAdd: false,
        canEdit: $scope.tab != 'CANCELLED',
        openFilter: null,
        openDetails: function(job) {
            if($scope.tab != 'CANCELLED') {
                $location.url('/fmaudit/'+job.id);
            }
        }
    };
    
    $scope.schedule = function(assetTypeId, firmwareVersionId) {
    	console.log("assetTypeId: " + assetTypeId + " firmwareVersionId: " + firmwareVersionId);
    	$location.url('/fmschedule/?action=create&assetTypeId=' + assetTypeId + "&firmwareVersionId=" + firmwareVersionId).replace();
    }

    ListController.call(vm, paginationConfig, listConfig);

    vm.find = function() {
    	
    	if ($scope.tab === "ELIGIBLE") {
            SpinnerService.wrap(FMSummaryService.eligible)
            .then(function(response) {
            	$scope.eligibleGroups = response.data;
            })
            .catch(ErrorService.handleHttpError);    
    	} else {
            SpinnerService.wrap(FMSummaryService.find, $scope.tab, vm.pagination, $scope.filter)
            .then(function(response) {
                if ('result' in response.data) {
                    vm.update(response.data.result);
                } else {
                    vm.update({
                        content: [],
                        first: true,
                        last: true,
                        number: 0,
                        numberOfElements: 0,
                        size: 10,
                        sort: [{
                            ascending: false,
                            descending: true,
                            direction: "DESC",
                            ignoreCase: false,
                            nullHandling: "NATIVE",
                            property: "started"
                        }],
                        totalElements: 0,
                        totalPages: 1
                    });
                }
            })
            .catch(ErrorService.handleHttpError);    		
    	}
    };

    vm.filterUpdated = function() {
        vm.pagination.pageIndex = 0;
        vm.find();
    };


    init();

}
FMSummaryController.prototype = Object.create(ListController.prototype);