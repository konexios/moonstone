services.factory('EventRegistrationService', ['$http',
    function($http) {

        // nth: move to constants
        var baseUrl = '/api/kronos/social-event/';
        var apiUrls = {
            getEvents: baseUrl + 'current-events',
            postEvent: baseUrl + 'registration',
            postResendCode: baseUrl + 'resend',
            postVerification: baseUrl + 'verification',
        };

        function eventsList() {
            return $http.get(apiUrls.getEvents);
        }

        function registration(model) {
            return $http.post(apiUrls.postEvent, model);
        }

        function resendCode(model) {
            return $http.post(apiUrls.postResendCode, model);
        }

        function verification(model) {
            return $http.post(apiUrls.postVerification, model);
        }

        return {
            apiUrls: apiUrls,
            eventsList: eventsList,
            registration: registration,
            resendCode: resendCode,
            verification: verification
        };
    }
]);