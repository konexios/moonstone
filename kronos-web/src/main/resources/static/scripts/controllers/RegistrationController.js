controllers.controller('RegistrationController', ['$rootScope', '$scope', '$q', '$location', '$routeParams', 'SecurityService', 'UserService', 'KronosService', 'ErrorService', 'SpinnerService', 'vcRecaptchaService', '$uibModal',
    function($rootScope, $scope, $q, $location, $routeParams, SecurityService, UserService, KronosService, ErrorService, SpinnerService, vcRecaptchaService, $uibModal) {
        var vm = this;

        vm.tthid = $routeParams.tthid;
        vm.preRegLock = false;

        vm.registrationModel = {
            referralCode: null,
            eventCode: null
        };
        vm.signupBtnDisabled = false;
        vm.pageSettings = null;
        vm.captchaSettings = {
            key: '6LfVOxcUAAAAADLugPKyN6l5LchJa3DKZ60eHZyc',
            widgetId: null
        };

        vm.signup = signup;


        init();


        function init() {
            // Check is user already registered and authenticated, to prohibit a new account registration:
            if(SecurityService.isLoggedIn()) {
                $uibModal.open({
                    backdrop: 'static',
                    keyboard: false,
                    templateUrl: 'partials/alert-popup.html',
                    controller: 'AlertPopupController',
                    resolve: {
                        options: {
                            title: 'Already registered',
                            message: 'You already have an account and logged in, please sign out if you want to create a new account.'
                        }
                    }
                }).result.finally(function () {
                    $location.url('/home'); // redirect to home when modal is closed
                });
            }

            KronosService.getPageSettings().then(function(response) {
                vm.pageSettings = response.data;
            });
            
            if (vm.tthid != undefined && vm.tthid != null && vm.tthid != '') {
            	try {
	            	UserService.preRegistration(vm.tthid)
	            	.then(function(response) {
	                    if (response.data) {
	                    	vm.preRegLock = true;
	                    	vm.registrationModel.email = response.data.email;
	                    	vm.registrationModel.referralCode = response.data.referralCode;
	                    	vm.registrationModel.eventCode = response.data.eventCode;
	                    }
	                });  
            	} catch(e) {}
            }
        }


        function signup(model) {
            vm.registrationForm.$setSubmitted();
            vm.errorgMessage = null;


            if (vm.registrationForm.$valid) {
                vm.signupBtnDisabled = true;

                UserService.register(model)
                    .then(function(response) {
                        var modalInstance = $uibModal.open({ templateUrl: 'registrationCompletedModal.html', backdrop: 'static', keyboard: false });
                        modalInstance.result.then(function() {
                            $location.url('/signin');
                        });
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

                        // reset the recaptcha
                        vcRecaptchaService.reload(vm.captchaSettings.widgetId);
                    })
                    .finally(function() {
                        vm.signupBtnDisabled = false;
                    });
            }
        }

    }
]);