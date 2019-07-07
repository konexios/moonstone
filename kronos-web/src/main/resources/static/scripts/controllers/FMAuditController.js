controllers.controller('FMAuditController', ['$scope', "$timeout", '$uibModal', '$routeParams', '$timeout', '$location', 'FMJobWizardService', 'FMSummaryService', 'CommonService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', FMAuditController]);

function FMAuditController($scope, $timeout, $uibModal, $routeParams, $timeout, $location, FMJobWizardService, FMSummaryService, CommonService, ErrorService, ToastrService, SecurityService, SpinnerService) {
    $scope.job = {};
    $scope.activeTab = 0;
    $scope.refreshRate = (5 * 1000);
    $scope.tabsNames = ['Details', 'Assets', 'Audit Logs'];

    $scope.canStartOnDemand = SecurityService.canStartSoftwareReleaseSchedule();
    $scope.canEdit = SecurityService.canEditSoftwareReleaseSchedule();
    $scope.canCancel = SecurityService.canCancelSoftwareReleaseSchedule();

    $scope.jobSummaryStatuses = FMSummaryService.statuses();

    $scope.startOnDemand = function() {
        SpinnerService.wrap(FMJobWizardService.start, $scope.job.id)
        .then(function() {
            ToastrService.popupSuccess("The job has been started");
            $scope.refreshJob();
        })
        .catch(ErrorService.handleHttpError);
    };

    $scope.edit = function() {
        $location.url('/fmschedule/'+$routeParams.id);
    };

    $scope.cancel = function() {
        $uibModal.open({
            animation: false,
            templateUrl: 'partials/confirm-popup.html',
            controller: 'ConfirmPopupController',
            size: 'lg',
            resolve: {
                options: {
                    title: 'Confirm Cancel',
                    message: 'Are you sure you want to cancel this job?',
                    ok: 'Yes',
                    cancel: 'No'
                }
            }
        }).result.then(function() {
            SpinnerService.wrap(FMJobWizardService.cancel, $scope.job.id)
            .then(function() {
                ToastrService.popupSuccess("The job has been cancelled");
                $scope.refreshJob();
            })
            .catch(ErrorService.handleHttpError);
        });
    };

    $scope.getDuration = function(job) {
        return CommonService.getDuration(new Date(job.started), new Date(job.completed));
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

    $scope.$on('$destroy', function () {
    	destroyTimer();
    });
    
    function destroyTimer() {
        // cancel all current XHR requests
        if ($scope.timer) {
            $timeout.cancel($scope.timer);
            $scope.timer = null;
        }    	
    }
    
    $scope.refreshJob = function() {
    	destroyTimer();
    	
    	FMJobWizardService.audit($routeParams.id)
    		.then(function(response) {
    			$scope.job = response.data;
    			$scope.showButtons = $scope.job == 'SCHEDULED';
            
    			$scope.timer = $timeout($scope.refreshJob, $scope.refreshRate);
    		});
    };
}