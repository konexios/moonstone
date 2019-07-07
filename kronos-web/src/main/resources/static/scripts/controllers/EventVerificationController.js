controllers.controller('EventVerificationController', ['$location', 'ToastrService', 'ErrorService', 'EventRegistrationService', 'KronosExceptions', EventVerificationController]);

function EventVerificationController($location, ToastrService, ErrorService, EventRegistrationService, KronosExceptions) {
    var vm = this;

    vm.verificationModel = { code: '' };
    vm.errorMessage = null;

    vm.verify = Verify;

    function Verify(model) {
        vm.verificationForm.$setSubmitted();

        if (vm.verificationForm.$valid) {
            vm.errorMessage = null;
            vm.verificationLoading = true;

            EventRegistrationService.verification(model)
                .then(function(response) {
                    ToastrService.popupSuccess('Verification has been passed successfully.');
                    $location.path('/event/registration-complete');
                })
                .catch(function(response) {
                    if (response.status == 400 && response.data.exceptionClassName === KronosExceptions.InvalidInputException) {
                        vm.errorMessage = JSON.parse(response.data.message);
                    } else {
                        ErrorService.handleHttpError(response);
                    }

                })
                .finally(function() {
                    vm.verificationLoading = false;
                });
        }
    }

}