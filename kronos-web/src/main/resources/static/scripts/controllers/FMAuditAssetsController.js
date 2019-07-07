controllers.controller('FMAuditAssetsController', ['$routeParams', '$scope', '$uibModal', 'FMJobWizardService', 'SecurityService', 'ErrorService', 'ToastrService', 'SpinnerService', FMAuditAssetsController]);

function FMAuditAssetsController($routeParams, $scope, $uibModal, FMJobWizardService, SecurityService, ErrorService, ToastrService, SpinnerService) {
    var vm = this;

    vm.selectedStatus = 'ALL';
    vm.bulkEdit = false;
    vm.bulkCancel = false;
    vm.bulkRetry = false;
    vm.bulkMoveToError = false;
    vm.bulkRetryOnError = false;
    vm.thisPageSelected = false;
    
    $scope.init = function(job) {
      vm.job = job;

      $scope.$watch("job", function (newValue, oldValue, scope) {
    	  if (hasValueChanged(oldValue.progressMetrics.cancelled.count, newValue.progressMetrics.cancelled.count) 
    			  || hasValueChanged(oldValue.progressMetrics.complete.count, newValue.progressMetrics.complete.count)
    			  || hasValueChanged(oldValue.progressMetrics.expired.count, newValue.progressMetrics.expired.count)
    			  || hasValueChanged(oldValue.progressMetrics.failed.count, newValue.progressMetrics.failed.count)
    			  || hasValueChanged(oldValue.progressMetrics.inprogress.count, newValue.progressMetrics.inprogress.count)
    			  || hasValueChanged(oldValue.progressMetrics.pending.count, newValue.progressMetrics.pending.count)
    			  || hasValueChanged(oldValue.progressMetrics.received.count, newValue.progressMetrics.received.count)) {
        	  vm.job = newValue;
        	  ToastrService.popupInfo("The status of 1 or more assests has changed. Please reload to see changes.");    		  
    	  }
      }, false);
      
      vm.find();
    };

    function hasValueChanged(newValue, oldValue) {
    	return newValue !== oldValue;
    }
    
    function renderOptionalValue(value) {
        return value || '---';
    }

    var columnHeaders = [];
    
    function deriveColumnHeaders() {
    	var canSort = true;
    	if (vm.job.status === "SCHEDULED" || vm.job.status === "CANCELLED")
    		canSort = false;
    	
    	columnHeaders = [];
	    columnHeaders = columnHeaders.concat([
	          { label: 'Asset', value: 'name', sortable: false },
	          { label: 'UID', value: 'uid', sortable: true },
	          { label: 'Status', value: 'status', sortable: canSort },
	          { label: 'Started', value: 'started', sortable: canSort },
	          { label: 'Minutes Remaining', value: 'remainingMinutes', sortable: false },
	          { label: 'Retries', value: 'retryCount', sortable: canSort }
	      ]);
    }
    
    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'name'
        }
    }, {
        resultTitle: 'Assets',
        columnHeaders: columnHeaders,
        tfootTopic: 'assets'
    });

    vm.changeSelectedStatus = function(status) {
    	if (vm.selectedStatus != status) {
            vm.selectedStatus = status;
            // reset pageIndex for new tab
            vm.pagination.pageIndex = 0;
    		vm.find();
    	}
    };
    
    vm.toggleSelectThisPage = function() {
    	if (!vm.bulkMoveToError && !vm.bulkRetryOnError && !vm.bulkRetry && !vm.bulkCancel)
    		return;
    	
    	vm.thisPageSelected = !vm.thisPageSelected;
    	
    	if (!vm.thisPageSelected)
    		vm.clearSelection();
    	else
    		vm.selectThisPage();
    }
    
    vm.find = function() {
    	deriveColumnHeaders();
    	
        SpinnerService.wrap(FMJobWizardService.findJobAssets, $routeParams.id, vm.pagination, vm.selectedStatus)
            .then(function(response) {
            	vm.setColumnHeaders(columnHeaders);

            	vm.bulkEdit = vm.job.scheduleStatus !== 'COMPLETE' && vm.job.scheduleStatus !== 'CANCELLED' && (vm.selectedStatus === 'INPROGRESS' || vm.selectedStatus === 'EXPIRED' || vm.selectedStatus === 'ERROR' || vm.selectedStatus === 'RECEIVED');
            	vm.bulkCancel = vm.bulkEdit && (vm.selectedStatus === 'INPROGRESS' || vm.selectedStatus === 'EXPIRED' || vm.selectedStatus === 'ERROR') && SecurityService.canCancelSoftwareReleaseTransaction();
            	vm.bulkRetry = vm.bulkEdit && (vm.selectedStatus === 'EXPIRED' || vm.selectedStatus === 'ERROR') && SecurityService.canRetrySoftwareReleaseTransaction();
            	vm.bulkMoveToError = vm.bulkEdit && (vm.selectedStatus === 'RECEIVED') && SecurityService.canMoveSoftwareReleaseTransactionToError();
            	vm.bulkRetryOnError = vm.bulkEdit && (vm.selectedStatus === 'INPROGRESS') && SecurityService.canRetrySoftwareReleaseTransactionOnError(); // AC-864

            	vm.thisPageSelected = false;
                vm.clearSelection();
                
            	vm.update(response.data.result);
            })
            .catch(ErrorService.handleHttpError);
    };
    
    vm.moveTransactionsToError = function() {
    	if (vm.selectedItemIds.length > 0) {
        	$uibModal.open({
                animation: false,
                templateUrl: 'partials/confirm-popup.html',
                controller: 'ConfirmPopupController',
                size: 'lg',
                resolve: {
                    options: {
                        title: 'Confirm Mark as Failed',
                        message: 'Are you sure you want to mark the selected job assets to as failed?',
                        ok: 'Yes',
                        cancel: 'No'
                    }
                }
            }).result.then(function() {
            	//alert('calling FMJobWizardService.moveSoftwareReleaseTransactionsToError');
    	        SpinnerService.wrap(FMJobWizardService.moveSoftwareReleaseTransactionsToError, vm.job.id, vm.selectedItemIds)
    	        .then(function(response) {
    	        	vm.job = response.data;
    	        	vm.find();
    	        	
                    ToastrService.popupSuccess("Job assets have been marked as failed");
    	        })
    	        .catch(ErrorService.handleHttpError);
            });
    	} else {
    		ToastrService.popupInfo("Select one or more job assets that you would like to mark as failed.");
    	}
    }    
    
    vm.cancelTransactions = function() {
    	if (vm.selectedItemIds.length > 0) {
        	$uibModal.open({
                animation: false,
                templateUrl: 'partials/confirm-popup.html',
                controller: 'ConfirmPopupController',
                size: 'lg',
                resolve: {
                    options: {
                        title: 'Confirm Cancel',
                        message: 'Are you sure you want to cancel the selected job assets?',
                        ok: 'Yes',
                        cancel: 'No'
                    }
                }
            }).result.then(function() {
    	        SpinnerService.wrap(FMJobWizardService.cancelSoftwareReleaseTransactions, vm.job.id, vm.selectedItemIds)
    	        .then(function(response) {
    	        	vm.job = response.data;
    	        	vm.find();
    	        	
                    ToastrService.popupSuccess("Job assets have been cancelled");
    	        })
    	        .catch(ErrorService.handleHttpError);
            });
    	} else {
    		ToastrService.popupInfo("Select one or more job assets that you would like to cancel.");
    	}
    }
    
    vm.retryTransactions = function() {
    	if (vm.selectedItemIds.length > 0) {
        	$uibModal.open({
                animation: false,
                templateUrl: 'partials/confirm-popup.html',
                controller: 'ConfirmPopupController',
                size: 'lg',
                resolve: {
                    options: {
                        title: 'Confirm Retry',
                        message: 'Are you sure you want to retry the selected job assets?',
                        ok: 'Yes',
                        cancel: 'No'
                    }
                }
            }).result.then(function() {
            	if (vm.selectedStatus === 'INPROGRESS') {
        	        SpinnerService.wrap(FMJobWizardService.retrySoftwareReleaseTransactionsOnError, vm.job.id, vm.selectedItemIds)
        	        .then(function(response) {
        	        	vm.job = response.data;
        	        	vm.find();
        	        	
                        ToastrService.popupSuccess("Job assets have been restarted");
        	        })
        	        .catch(ErrorService.handleHttpError);            		
            	} else {
        	        SpinnerService.wrap(FMJobWizardService.retrySoftwareReleaseTransactions, vm.job.id, vm.selectedItemIds)
        	        .then(function(response) {
        	        	vm.job = response.data;
        	        	vm.find();
        	        	
                        ToastrService.popupSuccess("Job assets have been restarted");
        	        })
        	        .catch(ErrorService.handleHttpError);
            	}
            });
    	} else {
    		ToastrService.popupInfo("Select one or more job assets that you would like to retry.");
    	}
    }
    
    // AC-864
//    vm.retryTransactionsOnError = function() {
//    	if (vm.selectedItemIds.length > 0) {
//        	$uibModal.open({
//                animation: false,
//                templateUrl: 'partials/confirm-popup.html',
//                controller: 'ConfirmPopupController',
//                size: 'lg',
//                resolve: {
//                    options: {
//                        title: 'Confirm Mark as Error',
//                        message: 'Are you sure you want to mark the selected job assets to as error?',
//                        ok: 'Yes',
//                        cancel: 'No'
//                    }
//                }
//            }).result.then(function() {
//            	//alert('calling FMJobWizardService.retrySoftwareReleaseTransactionsOnError()');
//    	        SpinnerService.wrap(FMJobWizardService.retrySoftwareReleaseTransactionsOnError, vm.job.id, vm.selectedItemIds)
//    	        .then(function(response) {
//    	        	vm.job = response.data;
//    	        	vm.find();
//    	        	
//                    ToastrService.popupSuccess("Job assets have been marked as error");
//    	        })
//    	        .catch(ErrorService.handleHttpError);
//            });
//    	} else {
//    		ToastrService.popupInfo("Select one or more job assets that you would like to mark as error.");
//    	}
//    }
}
FMAuditAssetsController.prototype = Object.create(ListController.prototype);
