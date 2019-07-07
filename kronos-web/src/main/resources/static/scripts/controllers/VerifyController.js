(function () {
	'use strict';

controllers.controller('VerifyController', VerifyController);

VerifyController.$inject = [ '$rootScope', '$scope', '$q', '$location', '$routeParams',
	'UserService', 'ErrorService', 'SpinnerService'];

    function VerifyController($rootScope, $scope, $q, $location, $routeParams, 
    		UserService, ErrorService, SpinnerService) {
    	var vm = this;
    	vm.processing = false;
    	vm.resendEmail = false;
    	vm.emailSent = false;
    	vm.unableToSendEmail = false;
    	
    	vm.ok = function() {
    		console.log("ok...");
    	}
    	
    	vm.resend = function() {
    		vm.resendEmail = false;
    		vm.processing = true;
    		
        	UserService.resendVerifyEmail($routeParams.tthid)
	            .then(function(response) {
	            	vm.emailSent = true;
	            })
	            .catch(function(response) {
	            	console.log(response);
	            	vm.unableToSendEmail = true;
	            })
	        	.finally(function() {vm.processing = false;});
    	}
    	
        vm.verify = function(token) {
        	UserService.verifyAccount(token)
	            .then(function(response) {
	            	$location.url('/changepassword?reason=passwordreset&usr=' + encodeURIComponent(response.data.login) + '&pwd=' + encodeURIComponent(response.data.tempPassword));
	            })
	            .catch(function(response) {
	            	// display error message
                    if (response.status === 400 && response.data.message) {
                    	if (response.data.message.indexOf('token has expired') != -1) {
                    		vm.resendEmail = true;
                    	} else
                    		ErrorService.handleHttpError(response);
                    } else {
                        ErrorService.handleHttpError(response);
                    }
	            });
        };

        function init() {
        	if ($routeParams.tthid != null && $routeParams.tthid != '') {
        		vm.verify($routeParams.tthid);
        	}
        }
        
        init();
    }

})();