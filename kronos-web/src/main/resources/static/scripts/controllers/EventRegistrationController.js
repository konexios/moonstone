controllers.controller('EventRegistrationController', ['$location', 'ToastrService', 'ErrorService', 'EventRegistrationService', 'KronosExceptions', EventRegistrationController]);
controllers.controller('ResendCodeController', ['$location', 'ToastrService', 'ErrorService', 'EventRegistrationService', 'KronosExceptions', ResendCodeController]);

function EventRegistrationController($location, ToastrService, ErrorService, EventRegistrationService, KronosExceptions) {
    var vm = this;

    vm.eventRegistration = {};
    vm.errorMessage = null;
    vm.events = null;
    vm.apiCalls = EventRegistrationService.apiUrls;

    vm.registration = Registration;

    init();

    function init() {
        EventRegistrationService.eventsList()
            .then(function(response) {
                vm.events = response.data;
            })
            .catch(ErrorService.handleHttpError);
    }

    function Registration(model) {
        vm.registrationForm.$setSubmitted();

        if (vm.registrationForm.$valid) {
            vm.errorMessage = null;
            vm.registrationLoading = true;

            EventRegistrationService.registration(model)
                .then(function(response) {
                    ToastrService.popupSuccess('Registration has been passed successfully. Please check your email.');
                    $location.path('/event/verification');
                })
                .catch(function(response) {
                    if (response.status == 400 && response.data.exceptionClassName === KronosExceptions.AccountExistsException) {
                        $location.path('/event/account-exist');
                    } else
                    if (response.status == 400 && response.data.exceptionClassName === KronosExceptions.InvalidInputException) {
                        vm.errorMessage = JSON.parse(response.data.message);
                    } else {
                        ErrorService.handleHttpError(response);
                    }

                })
                .finally(function() {
                    vm.registrationLoading = false;
                });
        }
    }

}

// reusable partial view with "Resend code" form
directives.directive('resendEventCode', ResendCodeDirective);

function ResendCodeDirective() {

    var directive = {
        restrict: 'E',
        scope: {},
        templateUrl: '/partials/events/resendCodeForm.html',
        controller: 'ResendCodeController',
        controllerAs: 'vm'
    };

    return directive;

}

function ResendCodeController($location, ToastrService, ErrorService, EventRegistrationService, KronosExceptions) {
    var vm = this;

    vm.resendCodeModel = {};
    vm.resendErrorMessage = null;

    vm.resendCode = ResendCode;

    function ResendCode(model) {
        vm.resendCodeForm.$setSubmitted();

        if (vm.resendCodeForm.$valid) {
            vm.resendErrorMessage = null;
            vm.resendCodeLoading = true;

            EventRegistrationService.resendCode(model)
                .then(function(response) {
                    ToastrService.popupSuccess('Code has been successfully sent. Please check your email.');
                    $location.path('/event/verification');
                })
                .catch(function(response) {
                    if (response.status == 400 && response.data.exceptionClassName === KronosExceptions.InvalidInputException) {
                        vm.resendErrorMessage = JSON.parse(response.data.message);
                    } else {
                        ErrorService.handleHttpError(response);
                    }
                })
                .finally(function() {
                    vm.resendCodeLoading = false;
                });
        }
    }
}