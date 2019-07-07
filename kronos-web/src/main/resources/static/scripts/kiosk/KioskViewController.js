controllers.controller('KioskViewController',
    [ '$rootScope', '$scope', '$location', '$q', '$timeout', '$routeParams', 'ErrorService', 'SpinnerService', 'KioskService',
    function($rootScope, $scope, $location, $q, $timeout, $routeParams, ErrorService, SpinnerService, KioskService) {
    	var vm = this;
    	
    	vm.mode = "input";
    	vm.processing = false;
    	vm.resendEmail = false;
    	
        vm.registrationModel = {
        	referralCode: $routeParams.rcode,
        	eventCode:  $routeParams.ecode
        };

        vm.walkawayTimeout = null;
    	vm.walkawayCounter = 0;
    	
    	vm.signup = function(model) {
            vm.registrationForm.$setSubmitted();
            vm.errorgMessage = null;
    		
            if (vm.registrationForm.$valid) {
            	vm.processing = true;
            	
                SpinnerService.wrap(KioskService.signup, model)
                .then(function(response) {
        			vm.mode = "output";
        			vm.walkawayTimeout = $timeout(vm.onTimeout,1000)
                })
                .catch(function(response) {
                    // display error message
                    if (typeof response.data === 'string') {
                        vm.errorgMessage = response.data;
                    } else if (response.status === 400 && response.data.message) {
                    	if (response.data.message.indexOf('email already used') != -1) {
                    		vm.resendEmail = true;
                    	} else
                    		vm.errorgMessage = response.data.message;
                    } else {
                        ErrorService.handleHttpError(response);
                    }
                })
                .finally(function() {
                	vm.processing = false;
                });
            }
    	}
    	
    	vm.resend = function(model) {
            vm.registrationForm.$setSubmitted();
            vm.errorgMessage = null;
    		
            if (vm.registrationForm.$valid) {
            	vm.processing = true;
            	
                SpinnerService.wrap(KioskService.resend, model)
                .then(function(response) {
        			vm.mode = "output";
        			vm.walkawayTimeout = $timeout(vm.onTimeout,1000)
                })
                .catch(function(response) {
                    // display error message
                    if (typeof response.data === 'string') {
                        vm.errorgMessage = response.data;
                    } else if (response.status === 400 && response.data.message) {
                   		vm.errorgMessage = response.data.message;
                    } else {
                        ErrorService.handleHttpError(response);
                    }
                })
                .finally(function() {
                	vm.processing = false;
                	vm.resendEmail = false;
                });
            }
    	}
    	
    	vm.ok = function() {
    		vm.reset();
    	}
    	
    	vm.onTimeout = function(){
            vm.walkawayCounter++;
            vm.walkawayTimeout = $timeout(vm.onTimeout,1000);
            
            if (vm.walkawayCounter >= 31)
            	vm.reset();
        }

    	vm.stop = function(){
            $timeout.cancel(vm.walkawayTimeout);
            vm.walkawayCounter = 0;
            vm.walkawayTimeout = null;
        }

    	vm.resetform = function() {
    		vm.registrationModel.email = null;
    		
    		try {
    			vm.registrationForm.$setPristine();
    			vm.registrationForm.$setUntouched();    			
    		} catch (e) {
    			
    		}
    	}
    	
    	vm.reset = function() {
        	vm.stop();
    		vm.resetform();
    		vm.mode = "input";
    		vm.resendEmail = false;
    	}
    }
]);
